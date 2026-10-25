package com.ts.kmevidencegenerator;

import com.itextpdf.commons.utils.Pair;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.ts.kmevidencegenerator.KmEvidenceGenerator;
import com.ts.kmevidencegenerator.api.RealTripsService;
import com.ts.kmevidencegenerator.google.GoogleDriveOAuthUploader;
import com.ts.kmevidencegenerator.google.GoogleSheetsAcessor;
import com.ts.kmevidencegenerator.model.Aggregation;
import com.ts.kmevidencegenerator.model.DataModel;
import com.ts.kmevidencegenerator.model.Trip;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import static com.itextpdf.layout.properties.TextAlignment.RIGHT;

public class KmEvidenceGeneratorApplication {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        RealTripsService realTripsService = new RealTripsService();
        GoogleDriveOAuthUploader googleDriveUploader = new GoogleDriveOAuthUploader();
        KmEvidenceGenerator generator = new KmEvidenceGenerator();
        GoogleSheetsAcessor googleSheetsAcessor = new GoogleSheetsAcessor();

        YearMonth yearMonth = getYearMonth();
        Pair<Integer, Integer> monthDistance = realTripsService.getMonthDistance(yearMonth.getMonth());
        // DataModel dataModel = new DataModel(166514, 168402); // august
        double zPrzeniesieniaKm = 9662.20; double zPrzeniesieniaWartosc = 9293.61;
        // sierpnień
        //DataModel dataModel = new DataModel(166514, 168402, zPrzeniesieniaKm, zPrzeniesieniaWartosc); // july
        // wrzesień
        DataModel dataModel = new DataModel(monthDistance.getKey(), monthDistance.getValue(), zPrzeniesieniaKm, zPrzeniesieniaWartosc); // september
        java.io.File fileToUpload = generator.generate(dataModel);
        String uploadedFile = googleDriveUploader.uploadPdf(fileToUpload);
        googleSheetsAcessor.write("2026!E7", uploadedFile);
        System.out.println("-----Finished-----");
    }

    private static YearMonth getYearMonth() {
        return LocalDate.now().getDayOfMonth() > 27 ? YearMonth.now() : YearMonth.now().minusMonths(1);
    }
}
