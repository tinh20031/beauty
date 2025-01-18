package com.beautytouch.beautytouch.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "services")
public class BeautyService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Integer id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "service")
    private Set<com.beautytouch.beautytouch.entity.StudioService> studioServices = new LinkedHashSet<>();
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<com.beautytouch.beautytouch.entity.StudioService> getStudioServices() {
        return studioServices;
    }

    public void setStudioServices(Set<com.beautytouch.beautytouch.entity.StudioService> studioServices) {
        this.studioServices = studioServices;
    }

}