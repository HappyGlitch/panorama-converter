package gui;

import projection.PolarCalculator;
import projection.Projector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DroppedFilesProcessor {
    List<File> files;
    BufferedImage sourceImage;
    BufferedImage endImage;

    Projector projector = new Projector();

    public DroppedFilesProcessor(List<File> files) {
        this.files = files;
    }

    public void process() throws IOException {
        if(files.size() > 1) {
            showWarning("Too many files. Only one file supported.");
            return;
        }
        File file = files.get(0);
        if(!file.getName().endsWith(".png")) {
            showWarning("Invalid file. Only .png files are supported");
            return;
        }
        sourceImage = ImageIO.read(file);
        fillEndImage();
        File endFile = new File(file.getAbsolutePath()+"@edited.png");
        endFile.createNewFile();
        ImageIO.write(endImage, "png", endFile);
        showInfo("File edited successfully");
    }

    private void fillEndImage() {
        createEndImage();
        for(int y = 0; y < endImage.getHeight(); y++) {
            for(int x = 0; x < endImage.getWidth(); x++) {
                Point2D.Double pos = projector.project(x, y, sourceImage.getWidth(), sourceImage.getHeight(), endImage.getWidth(), endImage.getHeight());
                if(pos.x >= sourceImage.getWidth() || pos.y >= sourceImage.getHeight()) {
                    endImage.setRGB(x, y, Color.GREEN.getRGB());
                    continue;
                }
                if(Math.min(pos.x, pos.y) < 0) {
                    endImage.setRGB(x, y, Color.MAGENTA.getRGB());
                    continue;
                }
                endImage.setRGB(x, y, interpolatePixel(pos));
            }
        }
    }

    private void createEndImage() {
        int size = sourceImage.getHeight()/2;
        Point2D.Double imageSize = projector.mapping.getSize(size);
        endImage = new BufferedImage((int)imageSize.x, (int)imageSize.y, BufferedImage.TYPE_INT_ARGB);
    }

    private int interpolatePixel(Point2D.Double pixel) {
        Color xy = new Color(sourceImage.getRGB((int)pixel.x, (int)pixel.y));
        Color Xy = new Color(sourceImage.getRGB((int)pixel.x+1, (int)pixel.y));
        Color xY = new Color(sourceImage.getRGB((int)pixel.x, (int)pixel.y+1));
        Color XY = new Color(sourceImage.getRGB((int)pixel.x+1, (int)pixel.y+1));

        pixel.setLocation(pixel.x-Math.floor(pixel.x), pixel.y-Math.floor(pixel.y));
        double xyWeight = (1-pixel.x)*(1-pixel.y);
        double XyWeight = (pixel.x)*(1-pixel.y);
        double xYWeight = (1-pixel.x)*(pixel.y);
        double XYWeight = (pixel.x)*(pixel.y);

        double interpolatedRed = xy.getRed() * xyWeight + Xy.getRed() * XyWeight + xY.getRed() * xYWeight + XY.getRed() * XYWeight;
        double interpolatedGreen = xy.getGreen() * xyWeight + Xy.getGreen() * XyWeight + xY.getGreen() * xYWeight + XY.getGreen() * XYWeight;
        double interpolatedBlue = xy.getBlue() * xyWeight + Xy.getBlue() * XyWeight + xY.getBlue() * xYWeight + XY.getBlue() * XYWeight;

        return new Color((int)interpolatedRed, (int)interpolatedGreen, (int)interpolatedBlue).getRGB();
    }

    private void showWarning(String warning) {
        showMessage(warning, JOptionPane.WARNING_MESSAGE, "Error");
    }

    private void showInfo(String info) {
        showMessage(info, JOptionPane.PLAIN_MESSAGE, "Info");
    }

    private void showMessage(String message, int type, String title) {
        JOptionPane.showMessageDialog(null, message, title, type);
    }
}
