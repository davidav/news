package com.example.news.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "create_at")
    private Instant createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "comment")
    private String comment;

    @ManyToOne()
    @JoinColumn(name = "news_id")
    @ToString.Exclude
    @JsonIgnore
    private News news;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonIgnore
    private User user;


}
