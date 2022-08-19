package projection;

import java.awt.geom.Point2D;

public abstract class ProjectionMapping {
    public abstract Point2D.Double map(Point2D.Double sourcePoint);
    public abstract Point2D.Double getSize(double tileSize);
}
