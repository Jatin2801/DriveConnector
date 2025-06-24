package com.example.GoogleDrive.Controller;

import com.example.GoogleDrive.Service.GoogleDriveService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;

import java.io.InputStream;
import java.util.*;


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

    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        try {
            InputStream inputStream = googleDriveSer.downloadFile(fileId);
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileId)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> listFiles() {
        try {
            List<Map<String, Object>> files = googleDriveSer.listFiles();
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        try {
            googleDriveSer.deleteFile(fileId);
            return ResponseEntity.ok(" File deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(" Delete failed: " + e.getMessage());
        }
    }

    @GetMapping("/storage")
    public ResponseEntity<String> getStorageInfo() {
        try {
            String storageInfo = googleDriveSer.getStorageInfo();
            return ResponseEntity.ok(storageInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(" Failed to get storage info: " + e.getMessage());
        }
    }
}
