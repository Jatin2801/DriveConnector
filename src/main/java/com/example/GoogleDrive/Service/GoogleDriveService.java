package com.example.GoogleDrive.Service;

import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.api.services.drive.Drive;
import org.springframework.core.io.ClassPathResource;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.services.drive.DriveScopes;
import java.io.InputStream;
import java.util.*;

@Service

public class GoogleDriveService {
private final Drive drive;
private final String sharedFolderId = "https://drive.google.com/drive/u/2/folders/1lqti0l96iKf7yIwXmYCFe2IAlXIbKhBh";


    public GoogleDriveService() throws Exception {
        InputStream keyStream = new ClassPathResource("driveconnector-463715-6bdc8746d33c.json").getInputStream();

        ServiceAccountCredentials credentials = (ServiceAccountCredentials) ServiceAccountCredentials.fromStream(keyStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        this.drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Spring Drive API")
                .build();
    }
}

