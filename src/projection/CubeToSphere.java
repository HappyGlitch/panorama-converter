package projection;

import java.awt.*;
import java.awt.geom.Point2D;

import static projection.PolarCalculator.*;

public class CubeToSphere extends ProjectionMapping {
    private static final Point2D.Double[] TILES = {
            createPoint(-0.5, 1), createPoint(-0.5, -1.0/3),
            createPoint(-1, 1.0/3), createPoint(-0.5, 1.0/3), createPoint(0, 1.0/3), createPoint(0.5, 1.0/3)
    };

    private static final Point2D.Double[] TILES_OPTIFINE = {
            createPoint(-1.0/3, 1), createPoint(-1, 1),
            createPoint(-1, 0), createPoint(-1.0/3, 0), createPoint(1.0/3, 0), createPoint(1.0/3, 1)
    };

    private static final double tileSizeX = 2.0/4;
    private static final double tileSizeY = Math.nextUp(2.0/3);
    private static final double tileSizeXOptifine = Math.nextUp(2.0/3);
    private static final double tileSizeYOptifine = 2.0/2.0;
    private static final Point2D.Double INFINITE_POINT = createPoint(Double.POSITIVE_INFINITY, 0);

    public boolean isOptifine = false;
    @Override
    public Point2D.Double map(Point2D.Double sourcePoint) {
        int tileIndex = toTileIndex(sourcePoint);
        if(tileIndex == -1)
            return createPoint(Double.POSITIVE_INFINITY, 0);
        Point2D.Double polarResult = mapTile(tileIndex, mapPointRelativeToTile(sourcePoint, tileIndex));
        return polarResult;
    }

    @Override
    public Point2D.Double getSize(double tileSize) {
        if(isOptifine)
            return createPoint(tileSize * 3, tileSize * 2);
        return createPoint(tileSize * 4, tileSize * 3);
    }

    private int toTileIndex(Point2D.Double point) {
        Point2D.Double[] tiles = getTiles();
        for(int i = 0; i < tiles.length; i++) {
            Point2D.Double tile = tiles[i];
            double dx = point.x - tile.x;
            double dy = tile.y - point.y;
            if(dx <= getTileSizeX() && dx >= 0 && dy <= getTileSizeY() && dy >= 0)
                return i;
        }
        return -1;
    }

    private Point2D.Double[] getTiles() {
        if(isOptifine)
            return TILES_OPTIFINE;
        return TILES;
    }

    private double getTileSizeX() {
        if(isOptifine)
            return tileSizeXOptifine;
        return tileSizeX;
    }
    private double getTileSizeY() {
        if(isOptifine)
            return tileSizeYOptifine;
        return tileSizeY;
    }

    private Point2D.Double mapPointRelativeToTile(Point2D.Double point, int tileIndex) {
        Point2D.Double tile = getTiles()[tileIndex];
        point.x = (point.x - tile.x) / getTileSizeX();
        point.y = (tile.y - point.y) / getTileSizeY();
        point.x = 2 * point.x - 1.0;
        point.y = 2 * point.y - 1.0;
        return point;
    }

    private Point2D.Double mapTile(int tileIndex, Point2D.Double pointRelativeToTile) {
        gnomonicToStereographic(pointRelativeToTile);
        return switch (tileIndex) {
            case 0 -> mapTile0(pointRelativeToTile);
            case 1 -> mapTile1(pointRelativeToTile);
            case 2 -> mapTile2(pointRelativeToTile);
            case 3 -> mapTile3(pointRelativeToTile);
            case 4 -> mapTile4(pointRelativeToTile);
            case 5 -> mapTile5(pointRelativeToTile);
            default -> createPoint(Double.POSITIVE_INFINITY, 0);
        };
    }

    private void gnomonicToStereographic(Point2D.Double p) {
        Point2D.Double polar = PolarCalculator.cartesianToPolar(p);
        double arc = Math.atan2(-1, polar.x);
        polar.x = Math.cos(arc)/(1 - Math.sin(arc));
        p.setLocation(PolarCalculator.polarToCartesian(polar));
    }

    //TOP
    private Point2D.Double mapTile0(Point2D.Double pointRelativeToTile) {
        latitudeOfOrigin = -Math.PI/2;
        pointRelativeToTile.setLocation(mapTopTile(pointRelativeToTile, -1));
        return offset(pointRelativeToTile, 0, 0);
    }

    //BOTTOM
    private Point2D.Double mapTile1(Point2D.Double pointRelativeToTile) {
        latitudeOfOrigin = Math.PI/2;
        pointRelativeToTile.setLocation(pointRelativeToTile.x, -pointRelativeToTile.y);
        pointRelativeToTile.setLocation(mapTopTile(pointRelativeToTile, 1));
        return offset(pointRelativeToTile, 0, 0);
    }


    //SIDES
    private Point2D.Double mapTile2(Point2D.Double pointRelativeToTile) {
        latitudeOfOrigin = 0;
        pointRelativeToTile.setLocation(mapTileCoordinates(pointRelativeToTile));
        return offset(pointRelativeToTile, -0.5, 0);
    }

    private Point2D.Double mapTile3(Point2D.Double pointRelativeToTile) {
        latitudeOfOrigin = 0;
        pointRelativeToTile.setLocation(mapTileCoordinates(pointRelativeToTile));
        return offset(pointRelativeToTile, 0, 0);
    }

    private Point2D.Double mapTile4(Point2D.Double pointRelativeToTile) {
        latitudeOfOrigin = 0;
        pointRelativeToTile.setLocation(mapTileCoordinates(pointRelativeToTile));
        return offset(pointRelativeToTile, 0.5, 0);
    }

    private Point2D.Double mapTile5(Point2D.Double pointRelativeToTile) {
        latitudeOfOrigin = 0;
        pointRelativeToTile.setLocation(mapTileCoordinates(pointRelativeToTile));
        offset(pointRelativeToTile, 1, 0);
        if(pointRelativeToTile.x > 1)
            pointRelativeToTile.setLocation(pointRelativeToTile.x-2, pointRelativeToTile.y);
        return pointRelativeToTile;
    }

    private Point2D.Double offset(Point2D.Double coordinates, double offsetX, double offsetY) {
        coordinates.setLocation(coordinates.x + offsetX, coordinates.y + offsetY);
        return coordinates;
    }

    private Point2D.Double mapTileCoordinates(Point2D.Double coordinates) {
        Point2D.Double result = coordinates;
        double v = 2;
        result.setLocation(result.x*v, result.y*v);
        double r = Math.hypot(result.x, result.y);
        if(r>v)
            return createPoint(Double.POSITIVE_INFINITY, 0);
        double c = 2*Math.atan(r/2);
        result.setLocation(mapLongitude(result.x, result.y, r, c), mapLatitude(result.y, r, c));
        result.setLocation(result.x/Math.PI, result.y/Math.PI*2);
        return result;
    }

    private double latitudeOfOrigin = 0;

    private double mapLongitude(double x, double y, double r, double c) {
        double result = r*Math.cos(latitudeOfOrigin)*Math.cos(c) - y*Math.sin(latitudeOfOrigin)*Math.sin(c);
        result = x*Math.sin(c) / result;
        result = Math.atan(result);
        return result;
    }

    private double mapLatitude(double y, double r, double c) {
        double result = y * Math.sin(c)*Math.cos(latitudeOfOrigin);
        result = Math.cos(c)*Math.sin(latitudeOfOrigin)+result/r;
        result = Math.asin(result);
        return result;
    }

    private Point2D.Double mapTopTile(Point2D.Double point, double scale) {
        Point2D.Double result = point;
        result.setLocation(result.y, result.x);
        result.setLocation(PolarCalculator.cartesianToPolar(result));
        if(result.x > 1)
            return INFINITE_POINT;
        result.setLocation(result.y/Math.PI, (Math.atan(1/result.x)/Math.PI*4 - 1)*scale);
        return result;
    }

}
