package com.defty.movie.service.impl;

import com.defty.movie.dto.response.MovieRedisDTO;
import com.defty.movie.entity.Movie;
import com.defty.movie.repository.IMovieRepository;
import com.defty.movie.service.IMovieUserRedisService;
import com.defty.movie.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationContext;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.*;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieUserRedisServiceImpl implements IMovieUserRedisService {

    ApplicationContext applicationContext;
    JedisPooled jedis;


    private IMovieRepository getMovieRepository() { //TODO: Thay vì inject MovieRepository trực tiếp vào thì ta sẽ lấy nó thông qua ApplicationContext để tránh bị đệ quy
        return applicationContext.getBean(IMovieRepository.class);
    }

    @Override
    public List<MovieRedisDTO> getAllMovie(String title) throws JsonProcessingException {
        if (!hasMovieData()) {
            List<Movie> movies = getMovieRepository().findAllByStatus(1);
            saveAllMovie(movies); // Lưu data vào cache
        }

        StringBuilder queryBuilder = new StringBuilder();
        if (title != null && !title.isEmpty()) { // TODO: Làm sạch String (Loại bỏ ký tự đặc biệt khỏi chuỗi tìm kiếm để tránh lỗi cú pháp Redis Search )
            String sanitizedTitle = title.trim().replaceAll("[^a-zA-Z0-9\\p{L}\\s]", "");
            String sanitizedTitleNoAccent = StringUtil.removeAccents(sanitizedTitle);
            if (sanitizedTitle.isEmpty()) {
                return Collections.emptyList();
            }
            // Tìm kiếm trên cả name và nameNoAccent với toán tử OR
            queryBuilder.append("@nameNoAccent:*").append(sanitizedTitleNoAccent).append("* | @name:*").append(sanitizedTitle).append("*");
        }

        String queryCriteria = queryBuilder.toString();
        Query query = queryCriteria.isEmpty() ? new Query() : new Query(queryCriteria).setVerbatim();
        query.limit(0, 9);
        SearchResult searchResult = jedis.ftSearch("movie-idx", query);

        List<MovieRedisDTO> movieList = searchResult.getDocuments().stream()
                .map(this::convertDocToMovieResponse)
                .collect(Collectors.toList());

        // Sắp xếp
        if (title != null && !title.isEmpty()) {
            String searchTerm = StringUtil.removeAccents(title.trim()).toLowerCase();
            movieList.sort((a, b) -> {
                int posA = a.getNameNoAccent().toLowerCase().indexOf(searchTerm);
                int posB = b.getNameNoAccent().toLowerCase().indexOf(searchTerm);
                // Nếu không tìm thấy từ khóa, đẩy xuống cuối
                if (posA == -1) posA = Integer.MAX_VALUE;
                if (posB == -1) posB = Integer.MAX_VALUE;
                return Integer.compare(posA, posB);
            });
        }

        return movieList;
    }

    private MovieRedisDTO convertDocToMovieResponse(Document doc) {
        Gson gson = new Gson();
        String jsonDoc=doc
                .getProperties()
                .iterator()
                .next()
                .getValue()
                .toString();
        return gson.fromJson(jsonDoc, MovieRedisDTO.class);
    }

    @Override
    public void saveAllMovie(List<Movie> movies) throws JsonProcessingException {
        if(movies.size()>0 && movies!=null) {
            createIndex();
            Gson gson = new Gson();
            for (Movie movie : movies) {
                MovieRedisDTO movieRedisDTO = new MovieRedisDTO();
                movieRedisDTO.setName(movie.getTitle());
                movieRedisDTO.setSlug(movie.getSlug());
                movieRedisDTO.setNameNoAccent(StringUtil.removeAccents(movie.getTitle()));
                String key = "movie:" + movie.getSlug();
                jedis.jsonSet(key, gson.toJson(movieRedisDTO));
            }
        }
    }

    private void createIndex() {
        Schema schema = new Schema()
                .addField(new Schema.Field(FieldName.of("$.name").as("name"), Schema.FieldType.TEXT, true, false))
                .addField(new Schema.Field(FieldName.of("$.nameNoAccent").as("nameNoAccent"), Schema.FieldType.TEXT, true, false))
                .addField(new Schema.Field(FieldName.of("$.slug").as("slug"), Schema.FieldType.TEXT, true, false));

        IndexDefinition rule = new IndexDefinition(IndexDefinition.Type.JSON)
                .setPrefixes(new String[] {"movie:"});

        IndexOptions options = IndexOptions.defaultOptions()
                .setNoStopwords()
                .setDefinition(rule);
        jedis.ftConfigSet("MINPREFIX", "1");
        jedis.ftCreate("movie-idx", options, schema);
    }

    @Override
    public void clearCache() {
        Set<String> keys = jedis.keys("movie:*");
        if (keys != null && !keys.isEmpty()) {
            jedis.del(keys.toArray(new String[0]));
        }
        try {
            jedis.ftDropIndexDD("movie-idx");
        } catch (JedisDataException e) {
            if (e.getMessage() == null || !e.getMessage().toLowerCase().contains("unknown index")) {
                log.error("Redis Search: Error dropping index 'movie-idx': {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("Redis Search: Unexpected error while trying to drop index 'movie-idx': {}", e.getMessage(), e);
        }
    }


    public boolean hasMovieData() {
        try {
            Set<String> movieKeys = jedis.keys("movie:*");
            boolean hasData = !movieKeys.isEmpty();

            // Nếu không có dữ liệu, kiểm tra và xóa index nếu tồn tại
            if (!hasData) {
                try {
                    jedis.ftDropIndexDD("movie-idx");
                    log.info("Redis Search: Dropped index 'movie-idx' as no movie data was found");
                } catch (JedisDataException e) {
                    if (e.getMessage() == null || !e.getMessage().toLowerCase().contains("unknown index")) {
                        log.error("Redis Search: Error dropping index 'movie-idx': {}", e.getMessage(), e);
                    }
                } catch (Exception e) {
                    log.error("Redis Search: Unexpected error while trying to drop index 'movie-idx': {}", e.getMessage(), e);
                }
            }

            return hasData;
        } catch (Exception e) {
            log.error("Error checking movie data in Redis: {}", e.getMessage());
            return false; // Trả về false nếu có lỗi
        }
    }


}
