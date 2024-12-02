package com.example.news.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @CreationTimestamp
    @Column(name = "create_at")
    private Instant createAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<News> newses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void addNews(News news){
        if(newses == null){
            newses = new ArrayList<>();
        }
        newses.add(news);
    }

    public void removeNews(Long newsId){
        newses = newses.stream()
                .filter(news -> !news.getId().equals(newsId))
                .collect(Collectors.toList());
    }

    public void addComment(Comment comment){
        if(comments == null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    public void removeComment(Long commentId){
        comments = comments.stream()
                .filter(comment -> !comment.getId().equals(commentId))
                .collect(Collectors.toList());
    }

}
