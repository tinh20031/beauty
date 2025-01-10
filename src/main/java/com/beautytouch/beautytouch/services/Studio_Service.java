package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.repositories.StudioRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Studio_Service {
    @Autowired
    private StudioRepositories studioRepositories;
    //list studio
    public List<Studio> GetAllStudio(){
return studioRepositories.findAll();
    }
    public Studio GetStudioById(Integer id){
        return studioRepositories.findById(id).orElse(null);
    }
    public Studio getStudioByName(String studioName) {
        return studioRepositories.findByStudioName(studioName);
    }
}
