package com.ts.kmevidencegenerator;

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
import com.ts.kmevidencegenerator.model.Aggregation;
import com.ts.kmevidencegenerator.model.DataModel;
import com.ts.kmevidencegenerator.model.Trip;

import java.io.IOException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import static com.itextpdf.layout.properties.TextAlignment.RIGHT;

public class KmEvidenceGeneratorApplication {

    public static void main(String[] args) throws IOException {
        // DataModel dataModel = new DataModel(166514, 168402); // august
        double zPrzeniesieniaKm = 728.0;
        double zPrzeniesieniaWartosc = 837.20;
        DataModel dataModel = new DataModel(166514, 168402, zPrzeniesieniaKm, zPrzeniesieniaWartosc); // july

        String path  = "km_august.pdf";
        PdfWriter pdfWriter =  new PdfWriter(path);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);

        String fontPath = "/Users/tomaszstaniewicz/workspace_private/kmevidencegenerator/kmevidencegenerator/src/main/resources/DejaVuSans.ttf";
        FontProgram fontProgram = FontProgramFactory.createFont(fontPath);
        PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);
        document.setFont(font);

        document.setMargins(10, 10, 10, 10);

        float twocol = 285f;
        float twocol150 = 285f+150f;
        float twocolumnWidth[] = {twocol150, twocol};

        Table headerTable = new Table(twocolumnWidth);

        Table taxPayerTable = getTaxPayerTable();
        Table vehicleTable = getVehicleTable();

        headerTable.addCell(new Cell().setBorder(Border.NO_BORDER).add(taxPayerTable));
        headerTable.addCell(new Cell().setBorder(Border.NO_BORDER).add(vehicleTable));

        Table mainTableTitleTable = getMainTableTitleTable(dataModel);
        Table mainTable = getMainTable(dataModel);
        mainTable.setFontSize(8);

        document.add(headerTable);
        document.add(mainTableTitleTable);
        document.add(mainTable);

        document.close();
    }

    private static Table getMainTable(DataModel dataModel) {
        float mainTableColumns[] = {20f,20f,20f,20f,20f,20f,20f,20f,20f};

        Table mainTable = new Table(mainTableColumns);
        addHeaderRows(mainTable);
        addTripRows(mainTable, dataModel);
        addAggregationRows(mainTable, dataModel.aggregate());

        return mainTable;
    }

    private static Table getVehicleTable() {
        Table vehicleTable = new Table(2);

        Cell cell3 = getCell().add(new Paragraph("Numer rejestracyjny pojazdu:").setFontSize(8)).setVerticalAlignment(VerticalAlignment.BOTTOM);
        Cell cell4 = getCell().add(new Paragraph("DLE 37216")).setVerticalAlignment(VerticalAlignment.BOTTOM).setPaddingBottom(0);
        Cell cell5 = getCell().add(new Paragraph("Pojemność silnika:").setFontSize(8)).setVerticalAlignment(VerticalAlignment.BOTTOM);
        Cell cell6 = getCell().add(new Paragraph("1600 [cm3]")).setVerticalAlignment(VerticalAlignment.BOTTOM).setPaddingBottom(0);

        vehicleTable.addCell(cell3);
        vehicleTable.addCell(cell4);
        vehicleTable.addCell(cell5);
        vehicleTable.addCell(cell6);
        return vehicleTable;
    }

    private static Table getMainTableTitleTable(DataModel dataModel) {
        Table tableTitleTable = new Table(1);
        for (int i = 0; i < 10; i++) {
            tableTitleTable.addCell(getCell());
        }
        tableTitleTable.addCell(getCell().add(new Paragraph("EWIDENCJA PRZEBIEGU POJAZDU").simulateBold()));
        tableTitleTable.addCell(getCell().add(new Paragraph("za miesiąc " +
                dataModel.getYearMonth().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pl", "PL")) + " " +
                dataModel.getYearMonth().getYear()
        )));
        tableTitleTable.setTextAlignment(TextAlignment.CENTER);
        tableTitleTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        tableTitleTable.setMarginBottom(20f);
        return tableTitleTable;
    }

    private static void addHeaderRows(Table mainTable) {
        mainTable.addCell(getCell("Nr kolejny wpisu").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
        mainTable.addCell(getCell("Data wyjazdu").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
        mainTable.addCell(getCell("Opis trasy\nwyjazdu\n(skąd\u2011dokąd)").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
        mainTable.addCell(getCell("Cel\nWyjazdu").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
        mainTable.addCell(getCell("Liczba\nfaktycznie\nprzejechanych\nkilometrów").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
        mainTable.addCell(getCell("Stawka za 1 km przebiegu\nzł gr").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
        mainTable.addCell(getCell("Wartość (5)x(6)\nzł gr").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
        mainTable.addCell(getCell("Podpis podatnika").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
        mainTable.addCell(getCell("Uwagi").simulateBold().setHorizontalAlignment(HorizontalAlignment.CENTER));

        mainTable.addCell(getCell("1").simulateBold());
        mainTable.addCell(getCell("2").simulateBold());
        mainTable.addCell(getCell("3").simulateBold());
        mainTable.addCell(getCell("4").simulateBold());
        mainTable.addCell(getCell("5").simulateBold());
        mainTable.addCell(getCell("6").simulateBold());
        mainTable.addCell(getCell("7").simulateBold());
        mainTable.addCell(getCell("8").simulateBold());
        mainTable.addCell(getCell("9").simulateBold());
    }

    private static void addTripRows(Table mainTable, DataModel dataModel) {
        List<Trip> trips = dataModel.getTrips();

        for (int i = 0; i < trips.size(); i++) {
            var trip = trips.get(i);
            mainTable.addCell(getCell(i + 1 + ""));
            mainTable.addCell(getCell(trip.getDateCell()));
            mainTable.addCell(getCell(trip.getTripGoal()));
            mainTable.addCell(getCell(trip.getTripDescription()));
            mainTable.addCell(getCell(trip.getTripLengthKmCell()));
            mainTable.addCell(getCell(trip.getPricePerKm()));
            mainTable.addCell(getCell(trip.getTotalCostCell()));
            mainTable.addCell(getCell("Tomasz Staniewicz"));
            mainTable.addCell(getCell(trip.getRemarks()));
        }
    }

    private static void addAggregationRows(Table mainTable, Aggregation aggregate) {
        mainTable.addCell(new Cell(1, 4).setTextAlignment(RIGHT).add(new Paragraph("Podsumowanie strony")).simulateBold());
        mainTable.addCell(getCell(aggregate.getKmTotalCell()).simulateBold());
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));
        mainTable.addCell(getCell(aggregate.getCostTotalCell()).simulateBold());
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));

        mainTable.addCell(new Cell(1, 4).setTextAlignment(RIGHT).add(new Paragraph("Z przeniesienia")).simulateBold());
        mainTable.addCell(getCell(aggregate.getZPrzeniesieniaKm()).simulateBold());
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));
        mainTable.addCell(getCell(aggregate.getZPrzeniesieniaWartosc()).simulateBold());
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));

        mainTable.addCell(new Cell(1, 4).setTextAlignment(RIGHT).add(new Paragraph("Razem")).simulateBold());
        mainTable.addCell(getCell(aggregate.getRazemKm()).simulateBold());
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));
        mainTable.addCell(getCell(aggregate.getRazemWartosc()).simulateBold());
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));
        mainTable.addCell(new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY));
    }

    private static Cell getCell() {
        return new Cell().setBorder(Border.NO_BORDER);
    }

    private static Cell getCell(String text) {
        return new Cell().add(new Paragraph(text)).setTextAlignment(TextAlignment.CENTER);
    }

    private static Table getTaxPayerTable() {
        Cell nameCell1 = getCell().add(new Paragraph("Tomasz Staniewicz")).setVerticalAlignment(VerticalAlignment.BOTTOM).setMarginBottom(0);
        Cell nameCell = getCell().add(new Paragraph("Tomasz Staniewicz")).setVerticalAlignment(VerticalAlignment.BOTTOM).setMarginBottom(0);
        Cell addressCell = getCell().add(new Paragraph("Brzoskwiniowa 4C/3, 50-539 Wrocław")).setVerticalAlignment(VerticalAlignment.BOTTOM).setMarginBottom(0);
        Cell taxPayerLabelCell = getCell().add(new Paragraph("Dane: Podatnika:").setFontSize(8)).setVerticalAlignment(VerticalAlignment.BOTTOM);
        Cell driverLabelCell1 = getCell().add(new Paragraph("Dane: osoby używającej pojazd:").setFontSize(8)).setVerticalAlignment(VerticalAlignment.BOTTOM);
        Cell driverLabelCell2 = getCell().add(new Paragraph("(nazwisko, imie, adres zamieszkania)").setFontSize(8)).setVerticalAlignment(VerticalAlignment.BOTTOM);

        Table taxPayerTable = new Table(1);
        taxPayerTable.addCell(nameCell1);
        taxPayerTable.addCell(taxPayerLabelCell);
        taxPayerTable.addCell(getCell());
        taxPayerTable.addCell(getCell());
        taxPayerTable.addCell(getCell());
        taxPayerTable.addCell(nameCell);
        taxPayerTable.addCell(addressCell);
        taxPayerTable.addCell(driverLabelCell1);
        taxPayerTable.addCell(driverLabelCell2);
        return taxPayerTable;
    }
}
