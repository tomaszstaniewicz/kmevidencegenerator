package com.ts.kmevidencegenerator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Builder
@Getter
public class Trip {

    private LocalDate startDate;
    private LocalDate endDate;
    private String tripDescription = "Brzoskwiniowa\u00A04c/3\u00A0Wrocław\n- Niedźwiedzia\u00A09\u00A0Wrocław";
    private String tripGoal = "Wizyta u klienta";
    private double tripLengthKm = 20.8;
    private double pricePerKm = 1.15;
    private String remarks;

    public Trip(LocalDate startDate) {
        this.startDate = startDate;
        this.endDate = startDate;
    }

    public Trip(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDateCell() {
        return startDate.isEqual(endDate)
            ? startDate.toString()
            : startDate + "-" + endDate;
    }

    public String getTripGoal() {
        return tripGoal;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public double getTripLengthKm() {
        return tripLengthKm;
    }

    public String getTripLengthKmCell() {
        return String.valueOf(tripLengthKm);
    }

    public String getPricePerKm() {
        return String.valueOf(pricePerKm);
    }

    public String getRemarks() {
        return remarks != null ? remarks : "";
    }

    public double getTotalCost() {
        return tripLengthKm * pricePerKm;
    }

    public String getTotalCostCell() {
        return String.format("%.2f", tripLengthKm * pricePerKm);
    }

}