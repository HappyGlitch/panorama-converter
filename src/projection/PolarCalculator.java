package projection;

import java.awt.*;
import java.awt.geom.Point2D;

public final class PolarCalculator {
    public static Point2D.Double stereoReverse(Point2D.Double polar) {
        if(polar.x == 0)
            return new Point2D.Double(Math.PI, polar.y);
        if(polar.x > 1.0)
            return createPoint(Double.POSITIVE_INFINITY, polar.y);
        return new Point2D.Double(2 * Math.atan(1/polar.x), polar.y);
    }

    public static Point2D.Double polarToCartesian(Point2D.Double polar) {
        Point2D.Double p2D = new Point2D.Double(polar.x * Math.cos(polar.y), polar.x * Math.sin(polar.y));
        return p2D;
    }

    public static Point2D.Double cartesianToPolar(Point2D.Double cartesian) {
        Point2D.Double p2D = new Point2D.Double(Math.sqrt(cartesian.x * cartesian.x + cartesian.y * cartesian.y), Math.atan2(cartesian.y, cartesian.x));
        return p2D;
    }

    public static Point2D.Double cartesianToPolarPositive(Point2D.Double cartesian) {
        Point2D.Double polar = cartesianToPolar(cartesian);
        if(polar.y < 0)
            polar.y += Math.PI * 2;
        return polar;
    }

    public static Point2D.Double mapToNegative(Point2D.Double point) {
        return new Point2D.Double(point.x*2 - 1, -(point.y*2 - 1));
    }

    public static Point2D.Double mapToPositive(Point2D.Double point) {
        return new Point2D.Double(point.x/2 + 0.5, (point.y/2 + 0.5));
    }

    public static Point2D.Double valueToProportions(Point2D.Double value, Point2D.Double reference) {
        return createPoint(value.x/reference.x, value.y/reference.y);
    }

    public static Point2D.Double proportionsToValue(Point2D.Double proportions, Point2D.Double reference) {
        return createPoint(proportions.x*reference.x, proportions.y*reference.y);
    }

    public static Point2D.Double createPoint(double x, double y) {
        return new Point2D.Double(x, y);
    }

    public static void scalePoint(Point2D.Double p, double scale) {
        p.x *= scale;
        p.y *= scale;
    }

    private PolarCalculator() {}
}
