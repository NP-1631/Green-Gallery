package com.greengallery.greengallery.controller;

import com.greengallery.greengallery.model.Plant;
import com.greengallery.greengallery.service.PlantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "*")
public class PlantController {

    @Autowired
    private PlantService plantService;

    // Upload plant
    @PostMapping("/upload")
    public Plant uploadPlant(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("scientificName") String scientificName,
            @RequestParam("notes") String notes,
            @RequestParam("userId") Long userId,
            @RequestParam("image") MultipartFile image
    ) throws IOException {

        return plantService.uploadPlant(name, category, scientificName, notes, userId, image);
    }

    // Get plants by user
    @GetMapping("/user/{userId}")
    public List<Plant> getUserPlants(@PathVariable Long userId) {
        return plantService.getUserPlants(userId);
    }

    // Delete plant
    @DeleteMapping("/{id}")
    public String deletePlant(@PathVariable Long id){
        plantService.deletePlant(id);
        return "Plant deleted successfully";
    }

    @GetMapping
    public ResponseEntity<List<Plant>> getAllPlants() {
        List<Plant> plants = plantService.getAllPlants();
        return ResponseEntity.ok(plants);
    }
}