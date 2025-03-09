package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByStudioId(Integer studioId);





}