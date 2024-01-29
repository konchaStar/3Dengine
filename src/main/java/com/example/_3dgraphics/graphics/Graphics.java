package com.example._3dgraphics.graphics;

import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Graphics {
    private BufferedImage buffer;
    private int width;
    private int height;

    public Graphics(BufferedImage buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    public void clear(int color) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                buffer.setRGB(i, j, color);
            }
        }
    }

    public void line(int x1, int y1, int x2, int y2, int color) {
        if (x1 == x2 && y1 == y2) {
            buffer.setRGB(x1, y1, color);
            return;
        }
        double l = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));

        double x = x1;
        double y = y1;
        double dx = (x2 - x1) / l;
        double dy = (y2 - y1) / l;
        do {
            buffer.setRGB((int) Math.round(x), (int) Math.round(y), color);
            x += dx;
            y += dy;
        } while ((x != x2 || x1 == x2) && (y != y2 || y1 == y2));
    }

    public void drawTriangle(Triangle triangle, int color) {
        Vec4d[] vertices = triangle.getPoints();
        line((int) vertices[0].getX(), (int) vertices[0].getY(), (int) vertices[1].getX(), (int) vertices[1].getY(), color);
        line((int) vertices[1].getX(), (int) vertices[1].getY(), (int) vertices[2].getX(), (int) vertices[2].getY(), color);
        line((int) vertices[0].getX(), (int) vertices[0].getY(), (int) vertices[2].getX(), (int) vertices[2].getY(), color);
    }

    public void rasterTriangle(Triangle triangle, int color) {
        Vec4d[] points = Arrays.stream(triangle.getPoints())
                .sorted((vec1, vec2) -> (int) Math.ceil(vec1.getY() - vec2.getY()))
                .map(vec -> new Vec4d((int) vec.getX(), (int) vec.getY(), (int) vec.getZ()))
                .toArray(Vec4d[]::new);

        int l1 = (int) Math.max(Math.abs(points[0].getX() - points[1].getX()), Math.abs(points[0].getY() - points[1].getY()));
        int l2 = (int) Math.max(Math.abs(points[1].getX() - points[2].getX()), Math.abs(points[1].getY() - points[2].getY()));
        double k = (points[2].getX() - points[0].getX()) / (points[2].getY() - points[0].getY());
        double dx1 = (points[1].getX() - points[0].getX()) / l1;
        double dy1 = (points[1].getY() - points[0].getY()) / l1;
        double dx2 = (points[2].getX() - points[1].getX()) / l2;
        double dy2 = (points[2].getY() - points[1].getY()) / l2;
        double x = points[0].getX();
        double y = points[0].getY();
        int l = 0;
        while (l <= l1) {
            line((int) Math.round(x), (int) Math.round(y), findX(points[2].getX(), points[2].getY(), k, y), (int) Math.round(y), color);
            y += dy1;
            x += dx1;
            l++;
        }
        x = points[1].getX();
        y = points[1].getY();
        l = 0;
        while (l <= l2) {
            line((int) Math.round(x), (int) Math.round(y), findX(points[2].getX(), points[2].getY(), k, y), (int) Math.round(y), color);
            y += dy2;
            x += dx2;
            l++;
        }
    }

    private int findX(double lineX, double lineY, double k, double y) {
        return (int) Math.round(lineX - k * (lineY - y));
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getBuffer() {
        return buffer;
    }

    public void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }
}
