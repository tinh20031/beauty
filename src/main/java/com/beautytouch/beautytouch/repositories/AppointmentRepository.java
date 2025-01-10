package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<appointments,Integer> {

}
