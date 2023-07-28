package com.lahee.market.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "user")
@Builder
@SQLDelete(sql = "UPDATE sales_item SET deleted_at = datetime('now') WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; //loginId
    private String password;
    private String nickname;

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Negotiation> negotiations = new ArrayList<>();

    @Builder.Default
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


    //연관관계 메서드
    public void addItem(SalesItem salesItem) {
        if (!salesItems.contains(salesItem)) {
            salesItems.add(salesItem);
        }
    }

    public void removeItem(SalesItem item) {
        salesItems.remove(item);
    }

    public void addComment(Comment comment) {
        if (!comments.contains(comment)) {
            comments.add(comment);
        }
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public void addNegotiation(Negotiation negotiation) {
        if (!negotiations.contains(negotiation)) {
            negotiations.add(negotiation);
        }
    }

    public void deleteNegotiation(Negotiation negotiation) {
        negotiations.remove(negotiation);
    }
}
