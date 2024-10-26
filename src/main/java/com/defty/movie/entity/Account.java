package com.defty.movie.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Account extends BaseEntity implements UserDetails {

    @NotBlank(message = "Username cannot be blank")
    @Column(nullable = false, length = 50, unique = true)
    String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    @Column(nullable = false, length = 255)
    String password;

    @Column(length = 50)
    String fullName;

    @Email(message = "Email should be valid")
    @Column(length = 50, unique = true)
    String email;

    @Pattern(regexp = "^\\+?\\d{1,15}$", message = "Phone must be a valid phone number")
    @Column(length = 50)
    String phone;

    @Column(length = 50)
    String gender;

    @Column(length = 255)
    String address;

    @Column(length = 255)
    String avatar;

    Integer status;

    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    Set<RefreshToken> tokens;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    Set<AccountMovie> accountMovies;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    Set<EpisodeAccount> episodeAccounts;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    Set<AccountUser> accountUsers;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    Set<Article> articles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName().toUpperCase()));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
