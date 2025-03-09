package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.Review;
import com.beautytouch.beautytouch.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        review.setCreatedAt(Instant.now());
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByStudio(Integer studioId) {
        return reviewRepository.findByStudioId(studioId);
    }
    public List<Review> getReviewsByStudioId(Integer studioId) {
        return reviewRepository.findByStudioId(studioId);
    }

}