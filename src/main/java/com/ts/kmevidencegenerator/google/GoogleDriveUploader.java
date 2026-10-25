package com.ts.kmevidencegenerator.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleDriveUploader {

    private static final String APPLICATION_NAME = "Google Drive PDF Uploader";
    private static final String CREDENTIALS_FILE_PATH = "/home/tomek/workspace/kmevidencegenerator/src/main/resources/kmgenerator-474315-4e957a2eb86d.json"; // your service account JSON file
//    private static final String YOUR_FOLDER_ID = "1Sk6irn-mcn5Q1QeL3s9a0sn53gY_FF1e";
    private static final String YOUR_FOLDER_ID = "14mOcwegYHa3qj-j03PQN47hjHEfPgw8S";

    @SneakyThrows
    private static Drive getDriveService() {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        var credentials = ServiceAccountCredentials
                .fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(httpTransport, GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void uploadPdf(String pdfFilePath) throws IOException {
        Drive driveService = getDriveService();





        java.io.File filePath = new java.io.File(pdfFilePath);

        File fileMetadata = new File();
        fileMetadata.setName(filePath.getName()); // name on Drive
        fileMetadata.setParents(Collections.singletonList(YOUR_FOLDER_ID));
        FileContent mediaContent = new FileContent("application/pdf", filePath);

        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, name, parents ,webViewLink")
                .execute();

        System.out.println("✅ Uploaded file:");
        System.out.println("Name: " + uploadedFile.getName());
        System.out.println("ID: " + uploadedFile.getId());
        System.out.println("Link: " + uploadedFile.getWebViewLink());
    }
}
