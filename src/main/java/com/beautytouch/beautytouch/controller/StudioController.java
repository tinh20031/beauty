package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.entity.User;

import com.beautytouch.beautytouch.security.CloudinaryService;

import com.beautytouch.beautytouch.security.CustomUserDetails;
import com.beautytouch.beautytouch.services.Studio_Service;
import com.beautytouch.beautytouch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;

import java.util.List;

@Controller
public class StudioController {
    @Autowired
    private Studio_Service studioService;
    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @GetMapping("/shop")
    public String ShowStudios (Model model){
        List<Studio> studioList = studioService.GetAllStudio();
        model.addAttribute("Studio",studioList );
        return "shop";
    }

    @GetMapping("/")
    public String viewStudiosOnIndexPage(Model model) {
        List<Studio> studios = studioService.GetAllStudio(); // Fetch all studios
        model.addAttribute("studios", studios); // Add the studios to the model
        return "index"; // Return the index page
    }

    //role studio


@GetMapping("/list-studio")
public String viewAppointments(Model model) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    User currentUser = userService.getUser(username);

    //danh sách studio theo user
    List<Studio> studioList = studioService.GetallStudioByUserId(currentUser.getId());
    // Thêm dữ liệu vào model để hiển thị trong view
    model.addAttribute("studioList", studioList);

    return "studio_index";
}
    @GetMapping("/add-studio")
    public String showAddStudioForm(Model model) {
        // Tạo một đối tượng Studio mới
        Studio studio = new Studio();
        model.addAttribute("studio", studio); // Thêm đối tượng vào model
        return "create_form_studio"; // Trả về view
    }


    @GetMapping("/edit-studio/{id}")
    public String showEditStudioForm(@PathVariable Integer id, Model model) {
        Studio studio = studioService.GetStudioById(id);
        if (studio == null) {
            model.addAttribute("errorMessage", "Studio not found");
            return "redirect:list-studio"; // Chuyển hướng về danh sách studio nếu không tìm thấy
        }
        model.addAttribute("studio", studio);
        return "create_form_studio";
    }
    @PostMapping("/addStudio")
    public String saveStudio(
            @RequestParam(value = "id", required = false) Integer id, // Tham số id để xác định xem có phải là cập nhật không
            @RequestParam("studioName") String studioName,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("kinhNghiM") String kinhNghiM,
            @RequestParam(value = "image", required = false) MultipartFile imageFile, // Hình ảnh có thể không cần thiết khi cập nhật
            Model model
    ) {
        try {
            Studio studio;
            if (id != null) {

                studio = studioService.GetStudioById(id);
                if (studio == null) {
                    model.addAttribute("errorMessage", "Studio not found");
                    return "redirect:list-studio"; // Nếu không tìm thấy studio, chuyển hướng về danh sách
                }
                studio.setStudioName(studioName);
                studio.setAddress(address);
                studio.setEmail(email);
                studio.setPhone(phone);
                studio.setKinhNghiem(kinhNghiM);

                // Nếu có hình ảnh mới, upload và cập nhật URL
                if (imageFile != null && !imageFile.isEmpty()) {
                    String publicId = cloudinaryService.uploadImage(imageFile);
                    String imageUrl = cloudinaryService.generateImageUrl(publicId, 300, 300); // Tùy chỉnh kích thước
                    studio.setImage(imageUrl);
                }
            } else {
                // Nếu không có ID, tạo studio mới
                studio = new Studio();
                studio.setStudioName(studioName);
                studio.setAddress(address);
                studio.setEmail(email);
                studio.setPhone(phone);
                studio.setKinhNghiem(kinhNghiM);

                // Lấy thông tin người dùng hiện tại

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                Integer userId = userDetails.getUserId();
                com.beautytouch.beautytouch.entity.User user = new com.beautytouch.beautytouch.entity.User();
                user.setId(userId);  // Thiết lập userId cho đối tượng User
                studio.setUser (user);

                // Upload hình ảnh và lưu URL
                if (imageFile != null && !imageFile.isEmpty()) {
                    String publicId = cloudinaryService.uploadImage(imageFile);
                    String imageUrl = cloudinaryService.generateImageUrl(publicId, 300, 300); // Tùy chỉnh kích thước
                    studio.setImage(imageUrl);
                }
            }

            // Lưu studio (cả tạo mới và cập nhật)
            studioService.saveStudio(studio); // Gọi phương thức lưu studio
            model.addAttribute("successMessage", "Studio saved successfully with image URL: " + studio.getImage());
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Failed to upload studio image: " + e.getMessage());
        }
        return "redirect:list-studio";
    }
    @PostMapping("/delete-studio/{id}")
    public String deleteStudio(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            studioService.deleteStudio(id); // Gọi phương thức xóa studio từ dịch vụ
            redirectAttributes.addFlashAttribute("successMessage", "Studio deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete studio: " + e.getMessage());
        }
        return "redirect:/list-studio"; // Chuyển hướng đến danh sách studio
    }
}
