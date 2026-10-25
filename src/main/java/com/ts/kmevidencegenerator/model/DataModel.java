package com.ts.kmevidencegenerator.model;

import com.ts.kmevidencegenerator.api.RealTripsService;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class DataModel {

    private final List<Trip> trips;
    private double kmPerTrip = 20.8;
    private double pricePerKm = 1.15;
    private final YearMonth yearMonth;
    private double zPrzeniesieniaKm;
    private double zPrzeniesieniaWartosc;

    private final int kmStart;
    private final int kmEnd;

    public DataModel(int kmStart, int kmEnd, double zPrzeniesieniaKm, double zPrzeniesieniaWartosc) {
        this.kmStart = kmStart;
        this.kmEnd = kmEnd;
        this.zPrzeniesieniaWartosc = zPrzeniesieniaWartosc;
        this.yearMonth = LocalDate.now().getDayOfMonth() > 27 ? YearMonth.now() : YearMonth.now().minusMonths(1);
        //this.yearMonth = YearMonth.of(2026, 7);
        this.trips = calculateTrips();
        this.zPrzeniesieniaKm = zPrzeniesieniaKm;
    }

    private List<Trip> calculateTrips() {
        List<Trip> realTrips = RealTripsService.getRealTrips();

        int numberOfTrips = calculateNumberOfTrips(realTrips);
        List<LocalDate> tripDays = selectWorkingDays(this.yearMonth, numberOfTrips, realTrips);

        List<Trip> trips = new ArrayList<>();
        for (int i = 1; i <= numberOfTrips; i++) {
            trips.add(new Trip(tripDays.get(i-1)));
        }
        trips.addAll(realTrips);
        trips.sort(Comparator.comparing(Trip::getStartDate).thenComparing(Trip::getEndDate));

        return trips;
    }

    public Aggregation aggregate() {
        double kmTotal = 0;
        double costTotal = 0;

        for (Trip trip : trips) {
            kmTotal+=trip.getTripLengthKm();
            costTotal+=trip.getTotalCost();
        }

        return new Aggregation(kmTotal, costTotal, this.zPrzeniesieniaKm, this.zPrzeniesieniaWartosc);
    }

    private int calculateNumberOfTrips(List<Trip> realTrips) {
        double realTripsDistanceTotal = realTrips.stream()
                .map(Trip::getTripLengthKm).mapToDouble(Double::doubleValue).sum();

        int fakeDistanceToCover = kmEnd - kmStart - ((int) realTripsDistanceTotal);

        return (int) (fakeDistanceToCover / kmPerTrip);
    }

    private static List<LocalDate> selectWorkingDays(YearMonth month,
                                                     int totalDaysToSelect,
                                                     List<Trip> realTrips) {
        Map<Integer, List<LocalDate>> weekToWorkingDays = new LinkedHashMap<>();

        LocalDate firstDay = month.atDay(1);
        LocalDate lastDay = month.atEndOfMonth();

        Set<LocalDate> realTripsDates = realTrips.stream()
                .flatMap(trip -> Stream.of(trip.getStartDate(), trip.getEndDate()))
                .collect(Collectors.toSet());

        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            DayOfWeek dow = date.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY && !realTripsDates.contains(date)) {
                int week = date.get(WeekFields.ISO.weekOfMonth());
                weekToWorkingDays
                        .computeIfAbsent(week, k -> new ArrayList<>())
                        .add(date);
            }
        }

        // Step 2: Distribute totalDaysToSelect proportionally across weeks
        List<LocalDate> result = new ArrayList<>();
        int totalWorkingDays = weekToWorkingDays.values().stream().mapToInt(List::size).sum();

        if (totalDaysToSelect > totalWorkingDays) {
            throw new IllegalArgumentException("Requested more working days than available in the month.");
        }

        int remaining = totalDaysToSelect;

        Map<Integer, Integer> weekSelections = new LinkedHashMap<>();
        for (var entry : weekToWorkingDays.entrySet()) {
            int weekWorkingDays = entry.getValue().size();
            int proportion = (int) Math.floor(((double) weekWorkingDays / totalWorkingDays) * totalDaysToSelect);
            weekSelections.put(entry.getKey(), proportion);
            remaining -= proportion;
        }

        // Distribute remaining days (rounding compensation)
        while (remaining > 0) {
            for (Integer week : weekToWorkingDays.keySet()) {
                if (remaining == 0) break;
                if (weekSelections.get(week) < weekToWorkingDays.get(week).size()) {
                    weekSelections.put(week, weekSelections.get(week) + 1);
                    remaining--;
                }
            }
        }

        // Step 3: Select actual dates
        for (Map.Entry<Integer, List<LocalDate>> entry : weekToWorkingDays.entrySet()) {
            int toSelect = weekSelections.get(entry.getKey());
            result.addAll(entry.getValue().subList(0, toSelect));
        }

        // Optional: Sort the result
        result.sort(Comparator.naturalOrder());

        return result;
    }
}
