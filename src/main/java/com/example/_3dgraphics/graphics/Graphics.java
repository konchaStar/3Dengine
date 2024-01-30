package com.example._3dgraphics.graphics;

import com.example._3dgraphics.ZBuffer;
import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Graphics {
    private BufferedImage buffer;
    private int width;
    private int height;
    private ZBuffer zBuffer;

    public Graphics(BufferedImage buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        this.zBuffer = new ZBuffer(width, height);
    }

    public void clear(int color) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                buffer.setRGB(i, j, color);

            }
        }
    }

    public void lineZBuf(Vec4d vec1, Vec4d vec2, int color) {
        if(vec1.getX() == vec2.getX() && vec1.getY() == vec2.getY()){
            if (Math.round(vec1.getX()) >= 0 && Math.round(vec1.getX()) < width - 1 && Math.round(vec1.getY()) >= 0 && Math.round(vec1.getY()) < height - 1){
                buffer.setRGB((int)Math.round(vec1.getX()), (int)Math.round(vec1.getY()), color);
            }
            return;
        }
        double l = Math.max(Math.abs(vec1.getX() - vec2.getX()), Math.abs(vec1.getY() - vec2.getY()));
        double x = vec1.getX();
        double y = vec1.getY();
        double z = vec1.getZ();
        double dx = (vec2.getX() - vec1.getX()) / l;
        double dy = (vec2.getY() - vec1.getY()) / l;
        double dz = (vec2.getZ() - vec1.getZ()) / l;
        do {
            if (Math.round(x) >= 0 && Math.round(x) < width - 1 && Math.round(y) >= 0 && Math.round(y) < height - 1 && z < zBuffer.getZ((int)Math.round(x),(int)Math.round(y))) {
                buffer.setRGB((int) Math.round(x), (int) Math.round(y), color);
                zBuffer.setZ((int)Math.round(x),(int)Math.round(y), z);
            }
            x += dx;
            y += dy;
            z += dz;
        } while ((x != vec2.getX() || vec1.getX() == vec2.getX()) && (y != vec2.getY() || vec1.getY() == vec2.getY()));


    }

    public void line(int x1, int y1, int x2, int y2, int color) {
        if (x1 == x2 && y1 == y2) {
            if (Math.round(x1) >= 0 && Math.round(x1) < width - 1 && Math.round(y1) >= 0 && Math.round(y1) < height - 1) {
                buffer.setRGB(x1, y1, color);
            }
            return;
        }
        double l = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));

        double x = x1;
        double y = y1;
        double dx = (x2 - x1) / l;
        double dy = (y2 - y1) / l;
        do {
            if (Math.round(x) >= 0 && Math.round(x) < width - 1 && Math.round(y) >= 0 && Math.round(y) < height - 1) {
                buffer.setRGB((int) Math.round(x), (int) Math.round(y), color);
            }
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
        double y = points[0].getY();
        while (y <= points[1].getY()) {
            double x1 = findX(points[0], points[2], y);
            double x2 = findX(points[0], points[1], y);
            //line((int) x1, (int) y, (int) x2, (int) y, color);
            lineZBuf(new Vec4d(x1, y, findZ(points[0], points[2], y)), new Vec4d(x2, y, findZ(points[0], points[1], y)), color);
            y++;
        }
        while (y < points[2].getY()) {
            double x1 = findX(points[0], points[2], y);
            double x2 = findX(points[1], points[2], y);
            lineZBuf(new Vec4d(x1, y, findZ(points[0], points[2], y)), new Vec4d(x2, y, findZ(points[0], points[1], y)), color);
            //line((int) x1, (int) y, (int) x2, (int) y, color);
            y++;
        }
    }

    private double findX(Vec4d v1, Vec4d v2, double y) {
        return v2.getX() - (v2.getX() - v1.getX()) * (v2.getY() - y) / (v2.getY() - v1.getY());
    }

    private double findZ(Vec4d v1, Vec4d v2, double y) {
        return v2.getZ() - (v2.getZ() - v1.getZ()) * (v2.getY() - y) / (v2.getY() - v1.getY());
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
