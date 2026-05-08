package com.shopflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Images", description = "Gestion des uploads d'images")
public class ImageUploadController {

    @Value("${upload.path:uploads/images}")
    private String uploadPath;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Upload une ou plusieurs images")
    public ResponseEntity<Map<String, Object>> uploadImages(
            @RequestParam("files") MultipartFile[] files) {
        
        List<String> imageUrls = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // Créer le dossier d'upload s'il n'existe pas
        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    errors.add("Fichier vide ignoré");
                    continue;
                }

                // Vérifier le type de fichier
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    errors.add(file.getOriginalFilename() + " n'est pas une image");
                    continue;
                }

                // Vérifier la taille (max 5MB)
                if (file.getSize() > 5 * 1024 * 1024) {
                    errors.add(file.getOriginalFilename() + " est trop volumineux (max 5MB)");
                    continue;
                }

                // Générer un nom de fichier unique
                String originalFilename = file.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String filename = UUID.randomUUID().toString() + extension;

                // Sauvegarder le fichier
                Path filePath = uploadDir.resolve(filename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Ajouter l'URL de l'image
                String imageUrl = "/uploads/images/" + filename;
                imageUrls.add(imageUrl);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("imageUrls", imageUrls);
            if (!errors.isEmpty()) {
                response.put("errors", errors);
            }
            response.put("message", imageUrls.size() + " image(s) uploadée(s) avec succès");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erreur lors de l'upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{filename}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Supprimer une image")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(filename);
            Files.deleteIfExists(filePath);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Image supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erreur lors de la suppression: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
