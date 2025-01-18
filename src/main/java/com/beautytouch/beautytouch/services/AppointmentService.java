package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.appointments;
import com.beautytouch.beautytouch.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {


    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void saveAppointment(appointments Appointments ) {
        appointmentRepository.save(Appointments);
    }

    public List<appointments> getAppointmentsByUserId(Integer userId) {
        return appointmentRepository.findByUserId(userId);
    }
    public void deleteById(Integer id) {
        appointmentRepository.deleteById(id);
    }
    public List<appointments> getAppointmentsByStudioIds(List<Integer> studioIds) {
        return appointmentRepository.findAllByStudio_IdIn(studioIds);
    }

    public appointments getAppointmentById(Integer id) {
        return appointmentRepository.findById(id).orElse(null);
    }
}
