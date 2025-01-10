package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.appointments;
import com.beautytouch.beautytouch.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    public void saveAppointment(appointments Appointments ) {
        appointmentRepository.save(Appointments);
    }
}
