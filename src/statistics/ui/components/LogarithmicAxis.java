package statistics.ui.components;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.ValueAxis;
import javafx.util.StringConverter;

/**
 * A ValueAxis implementation that displays data on a logarithmic scale.
 * Used in HistogramChartPanel to display histogram data.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class LogarithmicAxis extends ValueAxis<Number> {
    /**
     * Constructor with lower and upper bounds.
     * @param lowerBound The lower bound of the axis (must be positive for log scale).
     * @param upperBound The upper bound of the axis.
     */
    public LogarithmicAxis(double lowerBound, double upperBound) {
        super(lowerBound, upperBound);
        
        // Set a custom string converter to format tick labels appropriately.
        setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                // For small numbers, use decimal form; for large numbers, use scientific notation
                if (object.doubleValue() < 0.1 || object.doubleValue() >= 10000) {
                    return String.format("%.1e", object.doubleValue());
                } else {
                    if (object.doubleValue() < 1) {
                        return String.format("%.2f", object.doubleValue());
                    } else {
                        return String.format("%.0f", object.doubleValue());
                    }
                }
            }

            @Override
            public Number fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (Exception e) {
                    return 0.0;
                }
            }
        });
    }
    
    @Override
    protected List<Number> calculateTickValues(double length, Object range) {
        List<Number> tickValues = new ArrayList<>();

        if (range == null) return tickValues;

        double lb = ((double[]) range)[0];
        double ub = ((double[]) range)[1];
        double lbLog = Math.log10(lb);
        double ubLog = Math.log10(ub);
        
        for (double i = lbLog; i <= ubLog; i++) {
            tickValues.add(Math.pow(10, i));
        }

        if (!tickValues.isEmpty() && tickValues.getLast().doubleValue() != ub) {
            tickValues.add(ub);
        }
        
        return tickValues;
    }

    @Override
    protected List<Number> calculateMinorTickMarks() {
        List<Number> minorTickMarks = new ArrayList<>();
        
        // Calculate logarithmic minor tick values:
        double logLower = Math.log10(getLowerBound());
        double logUpper = Math.log10(getUpperBound());
        
        // Add minor ticks between powers of 10:
        for (double i = logLower; i <= logUpper; i++) {
            double base = Math.pow(10, i);
            for (int j = 2; j < 10; j++) {
                double minorTick = j * base / 10;
                if (minorTick >= getLowerBound() && minorTick <= getUpperBound()) {
                    minorTickMarks.add(minorTick);
                }
            }
        }
        
        return minorTickMarks;
    }

    @Override
    protected double[] getRange() {
        return new double[] { getLowerBound(), getUpperBound() };
    }

    @Override
    protected String getTickMarkLabel(Number value) {
        // Use our custom string converter defined in the constructor.
        return getTickLabelFormatter().toString(value);
    }

    @Override
    protected void setRange(Object range, boolean animate) {
        if (range == null) return;

        double lb = ((double[]) range)[0];
        double ub = ((double[]) range)[1];

        setLowerBound(lb);
        setUpperBound(ub);
    }

    @Override
    public Number getValueForDisplay(double displayPosition) {
        double logRange = Math.log10(getUpperBound()) - Math.log10(getLowerBound());
        
        double normalisedPosition = invertPosition(displayPosition / getHeight());
        
        // Convert display position to logarithmic value:
        double logValue = Math.log10(getLowerBound()) + normalisedPosition * logRange;
        return Math.pow(10, logValue);
    }

    @Override
    public double getDisplayPosition(Number value) {
        double logValue = Math.log10(value.doubleValue());
        double logLower = Math.log10(getLowerBound());
        double logUpper = Math.log10(getUpperBound());
        
        // Calculate normalised position (0 - 1)
        double normalisedPosition = (logValue - logLower) / (logUpper - logLower);
        
        return invertPosition(normalisedPosition) * getHeight();
    }

    /**
     * Invert the position value. Used to ensure smaller values are at the bottom,
     * and larger values are at the top.
     * @param position The position to invert.
     * @return The inverted position.
     */
    private double invertPosition(double position) {
        return 1.0 - position;
    }
}
