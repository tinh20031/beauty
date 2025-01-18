package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.repositories.StudioRepositories;
import com.beautytouch.beautytouch.security.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.UUID;

@Service
public class Studio_Service {
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/studios/";
    @Autowired
    private StudioRepositories studioRepositories;

    @Autowired
    private CloudinaryService cloudinaryService;

    //list studio
    public List<Studio> GetAllStudio() {
        return studioRepositories.findAll();
    }

    public Studio GetStudioById(Integer id) {
        return studioRepositories.findById(id).orElse(null);
    }

    public Studio getStudioByName(String studioName) {
        return studioRepositories.findByStudioName(studioName);
    }


    public List<Studio> GetallStudioByUserId(Integer userId) {
        return studioRepositories.findStudioByUserId(userId);
    }


    public Studio saveStudio(Studio studio) {
        // Lưu studio vào database
        return studioRepositories.save(studio);
    }
    public void deleteStudio(Integer id) {
        studioRepositories.deleteById(id); // Gọi phương thức xóa từ repository
    }
    public List<Studio> getStudiosByUserId(Integer userId) {
        return studioRepositories.findByUserId(userId); // Lấy danh sách studio theo userId
    }
}