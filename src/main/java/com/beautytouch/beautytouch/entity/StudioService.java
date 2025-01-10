package com.beautytouch.beautytouch.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "studio_services")
public class StudioService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studio_service_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "studio_id", nullable = false)
    private com.beautytouch.beautytouch.entity.Studio studio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private BeautyService service;

    @Column(name = "custom_price", precision = 10)
    private BigDecimal customPrice;

    @ColumnDefault("1")
    @Column(name = "available")
    private Boolean available;

    @OneToMany(mappedBy = "service")
    private Set<appointments> appointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "service")
    private Set<Review> reviews = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.beautytouch.beautytouch.entity.Studio getStudio() {
        return studio;
    }

    public void setStudio(com.beautytouch.beautytouch.entity.Studio studio) {
        this.studio = studio;
    }

    public BeautyService getService() {
        return service;
    }

    public void setService(BeautyService service) {
        this.service = service;
    }

    public BigDecimal getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(BigDecimal customPrice) {
        this.customPrice = customPrice;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

}