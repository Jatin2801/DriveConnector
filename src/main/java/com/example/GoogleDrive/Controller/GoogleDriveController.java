package com.example.GoogleDrive.Controller;

import com.example.GoogleDrive.Service.GoogleDriveService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;


@RestController
@RequestMapping("/api/drive")
public class GoogleDriveController {

private final GoogleDriveService googleDriveSer;

    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveSer = googleDriveService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = googleDriveSer.uploadFile(file);
            return ResponseEntity.ok(" File uploaded successfully. File ID: " + fileId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(" Upload failed: " + e.getMessage());
        }
    }

}
