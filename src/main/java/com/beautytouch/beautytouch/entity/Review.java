package com.beautytouch.beautytouch.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Integer id;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Lob
    @Column(name = "comment")
    private String comment;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private com.beautytouch.beautytouch.entity.User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "studio_id", nullable = false)
    private com.beautytouch.beautytouch.entity.Studio studio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private com.beautytouch.beautytouch.entity.StudioService service;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public com.beautytouch.beautytouch.entity.User getUser() {
        return user;
    }

    public void setUser(com.beautytouch.beautytouch.entity.User user) {
        this.user = user;
    }

    public com.beautytouch.beautytouch.entity.Studio getStudio() {
        return studio;
    }

    public void setStudio(com.beautytouch.beautytouch.entity.Studio studio) {
        this.studio = studio;
    }

    public com.beautytouch.beautytouch.entity.StudioService getService() {
        return service;
    }

    public void setService(com.beautytouch.beautytouch.entity.StudioService service) {
        this.service = service;
    }

}