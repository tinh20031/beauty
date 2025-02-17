package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<appointments,Integer> {
    List<appointments> findByServiceId(Integer serviceId);
    List<appointments> findByUserId(Integer userId);
    List<appointments> findAllByStudio_IdIn(List<Integer> studioIds);






}
