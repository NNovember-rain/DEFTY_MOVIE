package com.defty.movie.specication;

import com.defty.movie.entity.Category;
import com.defty.movie.entity.Movie;
import com.defty.movie.entity.MovieCategory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecification {
    public static Specification<Movie> filterByCategoryAndOthers(
            String slug, String nation, String releaseDate) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("status"), 1));

            if (slug != null) {
                Join<Movie, MovieCategory> movieCategoryJoin = root.join("movieCategories");
                Join<MovieCategory, Category> categoryJoin = movieCategoryJoin.join("category");
                predicates.add((Predicate) cb.equal(categoryJoin.get("slug"), slug));
            }

            if (nation != null) {
                predicates.add((Predicate) cb.equal(root.get("nation"), nation));
            }

            if (releaseDate != null) {
                predicates.add((Predicate) cb.like(root.get("releaseDate").as(String.class), "%" + releaseDate + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Movie> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null) {
                return criteriaBuilder.conjunction(); // Điều kiện luôn đúng khi title là null
            }
            return criteriaBuilder.equal(root.get("title"), title);  // Điều kiện tìm theo title
        };
    }

    public static Specification<Movie> hasNation(String nation) {
        return (root, query, criteriaBuilder) -> {
            if (nation == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("nation"), nation);
        };
    }

    public static Specification<Movie> hasReleaseDate(String releaseDate) {
        return (root, query, criteriaBuilder) -> {
            if (releaseDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("releaseDate"), releaseDate);
        };
    }

    public static Specification<Movie> hasRanking(Integer ranking) {
        return (root, query, criteriaBuilder) -> {
            if (ranking == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("ranking"), ranking);
        };
    }

    public static Specification<Movie> hasDirectorId(Integer directorId) {
        return (root, query, criteriaBuilder) -> {
            if (directorId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("directorId"), directorId);
        };
    }

    public static Specification<Movie> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Movie> buildSpecification(String title, String nation, String releaseDate, Integer ranking, Integer directorId, Integer status) {
        Specification<Movie> spec = Specification.where(null);
        if (title != null) {
            spec = spec.and(hasTitle(title));
        }
        if (nation != null) {
            spec = spec.and(hasNation(nation));
        }
        if (releaseDate != null) {
            spec = spec.and(hasReleaseDate(releaseDate));
        }
        if (ranking != null) {
            spec = spec.and(hasRanking(ranking));
        }
        if (directorId != null) {
            spec = spec.and(hasDirectorId(directorId));
        }
        if (status != null) {
            spec = spec.and(hasStatus(status));
        }
        return spec;
    }
}
