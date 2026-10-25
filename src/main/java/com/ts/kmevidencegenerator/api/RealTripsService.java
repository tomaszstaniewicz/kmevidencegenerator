package com.ts.kmevidencegenerator.api;

import com.itextpdf.commons.utils.Pair;
import com.ts.kmevidencegenerator.google.GoogleSheetsAcessor;
import com.ts.kmevidencegenerator.model.Trip;
import com.ts.kmevidencegenerator.utils.Utils;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class RealTripsService {

    private final GoogleSheetsAcessor googleSheetReader = new GoogleSheetsAcessor();

    private static final String RANGE = "2026!A1:D100"; // adjust to your range

    @SneakyThrows
    public Pair<Integer, Integer> getMonthDistance(Month month) {
        List<List<Object>> distances = googleSheetReader.read(RANGE);
        List<Object> monthValues = filterByMonth(distances, month);

        Integer startDistance = Integer.parseInt((String) monthValues.get(1));
        Integer endDistance = Integer.parseInt((String) monthValues.get(2));

        return new Pair(startDistance, endDistance);
    }

    private List<Object> filterByMonth(List<List<Object>> distances, Month month) {
        String monthName = Utils.getMonthName(month);
        return distances.stream()
                .filter(distance -> distance.get(0).equals(monthName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Month distance data not found"));
    }

    public static List<Trip> getRealTrips(){
        List<Trip> realTrips = new ArrayList<>();

        Trip realTrip1 = Trip.builder()
                .startDate(LocalDate.of(2026, 6, 9))
                .endDate(LocalDate.of(2026, 6, 10))
                .tripGoal("Wizyta u klienta")
                .tripLengthKm(772)
                .tripDescription("Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Grzybowska\u00A05A\u00A0Warszawa")
                .pricePerKm(1.15)
                .build();

        Trip realTrip111 = Trip.builder()
                .startDate(LocalDate.of(2026, 6, 16))
                .endDate(LocalDate.of(2026, 6, 17))
                .tripGoal("Wizyta u klienta")
                .tripLengthKm(772)
                .tripDescription("Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Grzybowska\u00A05A\u00A0Warszawa")
                .pricePerKm(1.15)
                .build();


        Trip realTrip2 = Trip.builder()
                .startDate(LocalDate.of(2026, 6, 27))
                .endDate(LocalDate.of(2026, 6, 28))
                .tripGoal("Wizyta u klienta")
                .tripLengthKm(380)
                .tripDescription("Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Wroniecka\u00A016\u00A0Poznań")
                .pricePerKm(1.15)
                .build();

        Trip realTrip3 = Trip.builder()
                .startDate(LocalDate.of(2026, 6, 21))
                .endDate(LocalDate.of(2026, 6, 21))
                .tripGoal("Wizyta u klienta")
                .tripLengthKm(185)
                .tripDescription("Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Wrocławska\u00A0152A\u00A0Opole")
                .pricePerKm(1.15)
                .build();

        realTrips.add(realTrip1);
        realTrips.add(realTrip111);
        //realTrips.add(realTrip3);

        return realTrips;
    }
}
