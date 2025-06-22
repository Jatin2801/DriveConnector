package com.example.GoogleDrive.Service;

import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.core.io.ClassPathResource;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import java.util.*;
@Service
public class GoogleDriveService {
private final Drive drive;
private final String sharedFolderId = "https://drive.google.com/drive/u/2/folders/1lqti0l96iKf7yIwXmYCFe2IAlXIbKhBh";
    public GoogleDriveService() throws Exception {
        GoogleCredential credential = GoogleCredential.fromStream(
                        new ClassPathResource("drive-key.json").getInputStream())
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        this.drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("Spring Drive API")
                .build();
    }
}
}
