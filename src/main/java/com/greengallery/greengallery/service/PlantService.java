package com.greengallery.greengallery.service;

import com.greengallery.greengallery.model.Plant;
import com.greengallery.greengallery.repository.PlantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    private final String uploadDir = "uploads/";

    // Get all plants
    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    // Get plants by user
    public List<Plant> getUserPlants(Long userId) {
        return plantRepository.findByUserId(userId);
    }

    // Upload plant
    public Plant uploadPlant(String name, String category, String scientificName,
                             String notes, Long userId, MultipartFile image) throws IOException {

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Plant plant = new Plant();
        plant.setName(name);
        plant.setCategory(category);
        plant.setScientificName(scientificName);
        plant.setNotes(notes);
        plant.setUserId(userId);
        plant.setUploadDate(LocalDate.now());
        plant.setImagePath("uploads/" + fileName);

        return plantRepository.save(plant);
    }

    // Delete plant
    public void deletePlant(Long id) {
        plantRepository.deleteById(id);
    }
}