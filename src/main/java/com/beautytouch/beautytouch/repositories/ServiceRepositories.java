package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.StudioService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepositories extends JpaRepository<StudioService, Integer> {
    List<StudioService> findByStudioId(Integer studioId);
    StudioService findByStudioIdAndServiceId(Integer studioId, Integer serviceId);

    List<StudioService> findStudioServicesByid(Integer serviceId);

    StudioService getStudioServiceById(Integer id);

}
