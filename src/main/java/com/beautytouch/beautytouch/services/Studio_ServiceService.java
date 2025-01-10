package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.entity.StudioService;
import com.beautytouch.beautytouch.entity.BeautyService;  // Đảm bảo import BeautyService
import com.beautytouch.beautytouch.repositories.ServiceRepositories;

import com.beautytouch.beautytouch.repositories.Service_StudioRepositories;
import com.beautytouch.beautytouch.repositories.StudioRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Studio_ServiceService {

    private final StudioRepositories studioRepositories;
    private final ServiceRepositories serviceRepositories;

    @Autowired
    public Studio_ServiceService(StudioRepositories studioRepositories, ServiceRepositories serviceRepositories) {
        this.studioRepositories = studioRepositories;
        this.serviceRepositories = serviceRepositories;
    }
@Autowired
    Service_StudioRepositories service_studioRepositories;
    public Studio GetStudioById(Integer id) {
        return studioRepositories.findById(id).orElse(null);
    }

    public List<StudioService> getServicesByStudioId(Integer studioId) {
        return serviceRepositories.findByStudioId(studioId);
    }

    public StudioService getServiceByStudioIdAndServiceId(Integer studioId, Integer serviceId) {
        return serviceRepositories.findByStudioIdAndServiceId(studioId, serviceId);
    }

    // TIM NAME THEO SERVICE
    public BeautyService getServiceByName(String serviceName) {
        return service_studioRepositories.findByServiceName(serviceName);  // Trả về BeautyService
    }
    public StudioService getServiceById(Integer serviceId ) {
       return serviceRepositories.getStudioServiceById(serviceId);
    }
}
