package com.ts.kmevidencegenerator.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleSheetsAcessor {

    private static final String APPLICATION_NAME = "Google Sheets Java Reader";
    private static final String CREDENTIALS_FILE_PATH = "/home/tomek/workspace/kmevidencegenerator/src/main/resources/kmgenerator-474315-4e957a2eb86d.json"; // your service account JSON file
    private static final String SPREADSHEET_ID = "1I7OjaTAQnx_dHF2Y2hLYdz3dLW7Btt4TTaZo_6pcqmY";

    public List<List<Object>> read(String range) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();

        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();

        return response.getValues();
    }

    public void write(String range, String bodyValue) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();

        ValueRange body = new ValueRange().setValues(List.of(List.of(bodyValue)));

        service.spreadsheets().values()
                .update(SPREADSHEET_ID, range, body)
                .setValueInputOption("RAW")
                .execute();
        System.out.println("stored link");
    }

    private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        var credentials = ServiceAccountCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(SheetsScopes.SPREADSHEETS);

        return new Sheets.Builder(httpTransport, GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
