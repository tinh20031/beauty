package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.BeautyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//repo cho bang service
public interface Service_StudioRepositories extends JpaRepository<BeautyService, Integer> {
    BeautyService findByServiceName(String serviceName);
}
