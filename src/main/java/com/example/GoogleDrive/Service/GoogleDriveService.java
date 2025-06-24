package com.example.GoogleDrive.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.model.FileList;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.api.services.drive.Drive;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.About;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service

public class GoogleDriveService {
private final Drive drive;
private final String sharedFolderId = "https://drive.google.com/drive/u/2/folders/1lqti0l96iKf7yIwXmYCFe2IAlXIbKhBh";


    public GoogleDriveService() throws Exception {
        InputStream keyStream = new ClassPathResource("driveconnector-463715-6bdc8746d33c.json").getInputStream();

        ServiceAccountCredentials credentials = (ServiceAccountCredentials) ServiceAccountCredentials.fromStream(keyStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE)); // give full access

        this.drive = new Drive.Builder( // create client
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Spring Drive API")
                .build();
    }

    public String uploadFile(MultipartFile file) throws Exception {
        File metadata = new File(); // yeh drive.model wala hai
        metadata.setName(file.getOriginalFilename());
        metadata.setParents(Collections.singletonList(sharedFolderId)); // to store in my shared folder
        java.io.File tempFile = java.io.File.createTempFile("upload-",file.getOriginalFilename());
        file.transferTo(tempFile); // copy the ori. file in temp
        FileContent content = new FileContent(file.getContentType(),tempFile);
        File uploaded = drive.files().create(metadata,content).setFields("id").execute(); // setFields tell return only id
        return uploaded.getId();
    }

    public InputStream downloadFile(String fileId) throws Exception {
        return drive.files().get(fileId).executeMediaAsInputStream();
    }

   public List<Map<String,Object>>  listFiles() throws Exception{
       FileList result = drive.files().list()
               .setQ("'" + sharedFolderId + "' in parents")
               .setFields("files(id,name,size)")
               .execute();
       List<Map<String,Object>> files = new ArrayList<>();
       for (File file : result.getFiles()){
           Map<String,Object> data = new HashMap<>();
           data.put("id",file.getId());
           data.put("name",file.getName());
           data.put("size",file.size());
           files.add(data);
       }
       return files;
    }

    public void deleteFile(String fileId) throws Exception {
        drive.files().delete(fileId).execute();
    }

    public String getStorageInfo() throws Exception {
        About about = drive.about().get().setFields("storageQuota").execute();
        About.StorageQuota quota = about.getStorageQuota();
        Long used = quota.getUsage();
        Long limit = quota.getLimit();

        StringBuilder sb = new StringBuilder();
        sb.append("Used: ").append(used).append(" bytes\n");
        sb.append("Limit: ").append(limit).append(" bytes\n");
        long remaining = limit - used;
        sb.append("Remaining: ").append(remaining).append(" bytes");
        return sb.toString();
    }
}
