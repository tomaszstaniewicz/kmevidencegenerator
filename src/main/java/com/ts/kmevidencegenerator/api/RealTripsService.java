package com.ts.kmevidencegenerator.api;

import com.ts.kmevidencegenerator.model.Trip;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RealTripsService {

    public static List<Trip> getRealTrips(){
        List<Trip> realTrips = new ArrayList<>();

        Trip realTrip1 = Trip.builder()
                .startDate(LocalDate.of(2025, 8, 12))
                .endDate(LocalDate.of(2025, 8, 13))
                .tripGoal("Wizyta u klienta")
                .tripLengthKm(772)
                .tripDescription("Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Grzybowska\u00A05A\u00A0Warszawa")
                .build();

        Trip realTrip2 = Trip.builder()
                .startDate(LocalDate.of(2025, 8, 19))
                .endDate(LocalDate.of(2025, 8, 20))
                .tripGoal("Wizyta u klienta")
                .tripLengthKm(772)
                .tripDescription("Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Grzybowska\u00A05A\u00A0Warszawa")
                .build();

        Trip realTrip4 = Trip.builder()
                .startDate(LocalDate.of(2025, 8, 19))
                .endDate(LocalDate.of(2025, 8, 19))
                .tripGoal("Wymiana opony")
                .tripLengthKm(6)
                .tripDescription("Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Kościuszki\u00A0135\u00A0Wrocław")
                .build();

        Trip realTrip5 = Trip.builder()
                .startDate(LocalDate.of(2025, 8, 22))
                .endDate(LocalDate.of(2025, 8, 22))
                .tripGoal("Obsługa okresowa pojazdu")
                .tripLengthKm(31)
                .tripDescription("Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Paprotna\u00A08\u00A0Wrocław")
                .build();

        realTrips.add(realTrip1);
        realTrips.add(realTrip2);
        // realTrips.add(realTrip3);
        realTrips.add(realTrip4);
        realTrips.add(realTrip5);

        return realTrips;
    }
}
