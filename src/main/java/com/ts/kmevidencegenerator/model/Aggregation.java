package com.ts.kmevidencegenerator.model;

import com.itextpdf.layout.element.Cell;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Aggregation {
    double kmTotal;
    double costTotal;
    double zPrzeniesieniaKm;
    double zPrzeniesieniaWartosc;

    public String getKmTotalCell() {
        return String.format("%.2f", kmTotal);
    }

    public String getCostTotalCell() {
        return String.format("%.2f", costTotal);
    }

    public String getZPrzeniesieniaKm()  {
        return String.format("%.2f", zPrzeniesieniaKm);
    }

    public String getRazemKm() {
        return String.format("%.2f", kmTotal + zPrzeniesieniaKm);
    }

    public String getZPrzeniesieniaWartosc()  {
        return String.format("%.2f", zPrzeniesieniaWartosc);
    }

    public String getRazemWartosc() {
        return String.format("%.2f", costTotal + zPrzeniesieniaWartosc);
    }
}
