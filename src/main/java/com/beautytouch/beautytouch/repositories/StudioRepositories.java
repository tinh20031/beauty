package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioRepositories extends JpaRepository<Studio, Integer> {

    List<Studio> id(Integer id);

    Studio findByStudioName(String studioName);
    List<Studio> findStudioByUserId(Integer UserId);
    List<Studio> findByUserId(Integer userId);
}


