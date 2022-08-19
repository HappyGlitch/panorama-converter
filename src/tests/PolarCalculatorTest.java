package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import projection.PolarCalculator;

import java.awt.geom.Point2D;

class PolarCalculatorTest {
    double maxError = 1e-10;

    private Point2D.Double createPoint(double x, double y) {
        return new Point2D.Double(x, y);
    }

    private void assertPointEquals(Point2D.Double p1, Point2D.Double p2) {
        assertEquals(p2.x, p1.x, maxError);
        assertEquals(p2.y, p1.y, maxError);
    }

    @Test
    void polarToCartesian() {
        assertPointEquals(PolarCalculator.polarToCartesian(createPoint(3, Math.PI/2)), createPoint(0, 3));
        assertPointEquals(PolarCalculator.polarToCartesian(createPoint(-2, 0)), createPoint(-2, 0));
        assertPointEquals(PolarCalculator.polarToCartesian(createPoint(3 * Math.sqrt(2), Math.PI/4)), createPoint(3, 3));
    }

    @Test
    void cartesianToPolar() {
        assertPointEquals(PolarCalculator.cartesianToPolar(createPoint(0, 3)), createPoint(3, Math.PI/2));
        assertPointEquals(PolarCalculator.cartesianToPolar(createPoint(-2, 0)), createPoint(2, Math.PI));
        assertPointEquals(PolarCalculator.cartesianToPolar(createPoint(3, 3)), createPoint(3 * Math.sqrt(2), Math.PI/4));
    }

    @Test
    void cartesianToPolarPositive() {
        assertPointEquals(PolarCalculator.cartesianToPolarPositive(createPoint(0, 3)), createPoint(3, Math.PI/2));
        assertPointEquals(PolarCalculator.cartesianToPolarPositive(createPoint(-2, 0)), createPoint(2, Math.PI));
        assertPointEquals(PolarCalculator.cartesianToPolarPositive(createPoint(3, 3)), createPoint(3 * Math.sqrt(2), Math.PI/4));
    }

    @Test
    void mapToNegative() {
        assertPointEquals(PolarCalculator.mapToNegative(createPoint(0, 1)), createPoint(-1, -1));
        assertPointEquals(PolarCalculator.mapToNegative(createPoint(0.75, 0.25)), createPoint(0.5, 0.5));
        assertPointEquals(PolarCalculator.mapToNegative(createPoint(0.25, 0.75)), createPoint(-0.5, -0.5));
        assertPointEquals(PolarCalculator.mapToNegative(createPoint(0.5, 0.5)), createPoint(0, 0));
        assertPointEquals(PolarCalculator.mapToNegative(createPoint(1, 1)), createPoint(1, -1));
    }

    @Test
    void mapToPositive() {
        assertPointEquals(PolarCalculator.mapToPositive(createPoint(-1, 1)), createPoint(0, 1));
        assertPointEquals(PolarCalculator.mapToPositive(createPoint(0.5, 0.5)), createPoint(0.75, 0.75));
        assertPointEquals(PolarCalculator.mapToPositive(createPoint(-0.5, -0.5)), createPoint(0.25, 0.25));
        assertPointEquals(PolarCalculator.mapToPositive(createPoint(0, 0)), createPoint(0.5, 0.5));
        assertPointEquals(PolarCalculator.mapToPositive(createPoint(1, 1)), createPoint(1, 1));
    }
}