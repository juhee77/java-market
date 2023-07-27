package com.lahee.market.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "user")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; //loginId
    private String password;
    private String nickname;


    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Negotiation> negotiations = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesItem> salesItems = new ArrayList<>();


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
