package com.beautytouch.beautytouch.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalTime;

@Entity
public class appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự động tạo giá trị cho id
    @Column(name = "appointment_id", nullable = false)
    private Integer id;

    @Column(name = "appointment_date", columnDefinition = "VARCHAR(255)")
    private String  appointmentDate;

    @Column(name = "status", nullable = false)
    @Lob
    @ColumnDefault("'pending'")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private StudioService service;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "studio_id", nullable = false)
    private Studio studio;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "created_at", updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant updatedAt;

    @Column(name = "time", columnDefinition = "VARCHAR(255)")
    private String  time;

    @Column(name = "price_appoint", nullable = false, length = 45)
    private String priceAppoint;

    // Getter and Setter methods

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String  getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String  appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StudioService getService() {
        return service;
    }

    public void setService(StudioService service) {
        this.service = service;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }   

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String  getTime() {
        return time;
    }

    public void setTime(String  time) {
        this.time = time;
    }

    public String getPriceAppoint() {
        return priceAppoint;
    }

    public void setPriceAppoint(String priceAppoint) {
        this.priceAppoint = priceAppoint;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();  // Set createdAt when entity is first persisted
        }
        if (this.updatedAt == null) {
            this.updatedAt = Instant.now();  // Set updatedAt when entity is first persisted
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();  // Update updatedAt when entity is updated
    }
}
