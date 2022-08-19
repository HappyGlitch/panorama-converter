package projection;

import java.awt.*;
import java.awt.geom.Point2D;

import static projection.PolarCalculator.*;
public class Projector {
    public ProjectionMapping mapping = new CubeToSphere();

    public Point2D.Double project(double x, double y, double sourceWidth, double sourceHeight, double endWidth, double endHeight) {
        Point2D.Double input = mapToNegative(valueToProportions(createPoint(x, y), createPoint(endWidth-1, endHeight-1)));
        Point2D.Double output = proportionsToValue(mapToPositive(explodePoint(mapping.map(input))), createPoint(sourceWidth-1, sourceHeight-1));
        return output;
    }

    private Point2D.Double explodePoint(Point2D.Double p) {
        if(Math.max(Math.abs(p.x), Math.abs(p.y)) > 1) {
            //return createPoint(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        return p;
    }
}
