package com.beautytouch.beautytouch.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "studios")
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studio_id", nullable = false)
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "studio_name", nullable = false)
    private String studioName;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private com.beautytouch.beautytouch.entity.User user;

    @OneToMany(mappedBy = "studio")
    private Set<appointments> appointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "studio")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "studio")
    private Set<StudioService> studioServices = new LinkedHashSet<>();

    @Column(name = "image")
    private String image;

    @Column(name = "`kinh nghiệm`", nullable = false, length = 45)
    private String kinhNghiM;

    public String getKinhNghiM() {
        return kinhNghiM;
    }

    public void setKinhNghiM(String kinhNghiM) {
        this.kinhNghiM = kinhNghiM;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
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

    public Set<com.beautytouch.beautytouch.entity.appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<com.beautytouch.beautytouch.entity.appointments> appointments) {
        this.appointments = appointments;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<StudioService> getStudioServices() {
        return studioServices;
    }

    public void setStudioServices(Set<StudioService> studioServices) {
        this.studioServices = studioServices;
    }

}