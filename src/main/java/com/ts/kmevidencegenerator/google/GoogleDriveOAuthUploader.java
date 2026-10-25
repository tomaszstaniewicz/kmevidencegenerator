package com.ts.kmevidencegenerator.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleDriveOAuthUploader {

    private static final String APPLICATION_NAME = "Google Drive PDF Uploader (OAuth)";
    private static final java.io.File TOKENS_DIR = new java.io.File("tokens");
    private static final String CREDENTIALS_FILE_PATH = "/home/tomek/workspace/kmevidencegenerator/src/main/resources/client_secret_992660704083-udbj8k1p2s7bf3gji8l98lvuro1tne89.apps.googleusercontent.com.json";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);

    @SneakyThrows
    private static Credential getCredentials() throws IOException, GeneralSecurityException {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, GsonFactory.getDefaultInstance(), clientSecrets, SCOPES)
                .setDataStoreFactory(new com.google.api.client.util.store.FileDataStoreFactory(TOKENS_DIR))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @SneakyThrows
    private static Drive getDriveService() {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials();
        return new Drive.Builder(httpTransport, GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String uploadPdf(java.io.File fileToUpload) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        String parentFolderId = "1Sk6irn-mcn5Q1QeL3s9a0sn53gY_FF1e";

        File fileMetadata = new File();
        fileMetadata.setName(fileToUpload.getName());
        fileMetadata.setParents(Collections.singletonList(parentFolderId));

        FileContent mediaContent = new FileContent("application/pdf", fileToUpload);

        File uploadedFile = driveService.files()
                .create(fileMetadata, mediaContent)
                .setFields("id, name, webViewLink")
                .execute();

        System.out.println("✅ Uploaded file:");
        System.out.println("Name: " + uploadedFile.getName());
        System.out.println("ID: " + uploadedFile.getId());
        System.out.println("Link: " + uploadedFile.getWebViewLink());

        return uploadedFile.getWebViewLink();
    }
}
