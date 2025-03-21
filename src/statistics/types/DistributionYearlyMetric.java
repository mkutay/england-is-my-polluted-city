package statistics.types;

/**
 * Yearly distribution metrics data record for use with DataTablePanel.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public record DistributionYearlyMetric(int year, Double median, Double q1, Double q3, Double skewness, Double kurtosis) {
    /**
     * Get the interquartile range (IQR) for this year metric.
     * @return The IQR, or null if q1 or q3 is null.
     */
    public Double getIQR() {
        if (q3 == null || q1 == null) return null;
        return q3 - q1;
    }

    /**
     * Get the skewness meaning for this year metric.
     * @return A string describing the skewness.
     */
    public String getSkewnessMeaning() {
        if (skewness == null) return "N/A";
        if (skewness < -1) return "Moderately negatively skewed";
        if (skewness > 1) return "Moderately positively skewed";
        if (skewness < -1.5) return "Strongly negatively skewed";
        if (skewness > 1.5) return "Strongly positively skewed";
        return "Symmetric";
    }

    /**
     * Get the kurtosis meaning for this year metric.
     * @return A string describing the kurtosis.
     */
    public String getKurtosisMeaning() {
        if (kurtosis == null) return "N/A";
        if (kurtosis < -1) return "Platykurtic";
        if (kurtosis > 1) return "Leptokurtic";
        return "Mesokurtic";
    }
}