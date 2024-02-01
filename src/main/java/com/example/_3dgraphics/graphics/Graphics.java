package com.example._3dgraphics.graphics;

import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Graphics {
    private BufferedImage buffer;
    private int width;
    private int height;
    private double[][] zBuffer;

    public Graphics(BufferedImage buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        this.zBuffer = new double[width][height];
    }

    public void clear(Color color) {
        int rgb = color.getRGB();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                buffer.setRGB(i, j, rgb);
                zBuffer[i][j] = 100;
            }
        }
    }

    public void line(Vec4d vec1, Vec4d vec2, Color color) {
        int rgb = color.getRGB();
        vec1 = new Vec4d((int) vec1.getX(), (int) vec1.getY(), vec1.getZ());
        vec2 = new Vec4d((int) vec2.getX(), (int) vec2.getY(), vec2.getZ());
        if (vec1.getX() == vec2.getX() && vec1.getY() == vec2.getY()) {
            int posX = (int) Math.round(vec1.getX());
            int posY = (int) Math.round(vec1.getY());
            if (posX >= 0 && posX < width - 1 && posY >= 0 && posY < height - 1) {
                double z = Math.min(vec1.getZ(), vec2.getZ());
                if (z < zBuffer[posX][posY]) {
                    buffer.setRGB(posX, posY, rgb);
                    zBuffer[posX][posY] = z;
                }
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
        for (int i = 0; i <= l; i++) {
            if (Math.round(x) >= 0 && Math.round(x) < width - 1 && Math.round(y) >= 0 && Math.round(y) < height - 1) {
                int posX = (int) Math.round(x);
                int posY = (int) Math.round(y);
                if (z < zBuffer[posX][posY]) {
                    buffer.setRGB(posX, posY, rgb);
                    zBuffer[posX][posY] = z;
                }
            }
            x += dx;
            y += dy;
            z += dz;
        }
    }

    public void line(int x1, int y1, int x2, int y2, Color color) {
        int rgb = color.getRGB();
        if (x1 == x2 && y1 == y2) {
            if (x1 >= 0 && x1 < width - 1 && y1 >= 0 && y1 < height - 1) {
                buffer.setRGB(x1, y1, rgb);
            }
            return;
        }
        double l = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));

        double x = x1;
        double y = y1;
        double dx = (x2 - x1) / l;
        double dy = (y2 - y1) / l;
        for (int i = 0; i <= l; i++) {
            if (Math.round(x) >= 0 && Math.round(x) < width - 1 && Math.round(y) >= 0 && Math.round(y) < height - 1) {
                buffer.setRGB((int) Math.round(x), (int) Math.round(y), rgb);
            }
            x += dx;
            y += dy;
        }
    }

    public void drawTriangle(Triangle triangle, Color color) {
        Vec4d[] vertices = triangle.getPoints();
        line(vertices[0], vertices[1], color);
        line(vertices[1], vertices[2], color);
        line(vertices[0], vertices[2], color);
    }

    private List<Vec4d> getDrawableVectors(Vec4d v1, Vec4d v2) {
        int l = (int) v2.getY() - (int) v1.getY();
        Vec4d dv = new Vec4d(((int) v2.getX() - (int) v1.getX()) / (double) l,
                1, (v2.getZ() - v1.getZ()) / l);
        Vec4d vec = new Vec4d(v1.getX(), v1.getY(), v1.getZ());
        List<Vec4d> vectors = new ArrayList<>();
        for (int i = 0; i <= l; i++) {
            vectors.add(new Vec4d((int) Math.round(vec.getX()), vec.getY(), vec.getZ()));
            vec = vec.add(dv);
        }
        return vectors;
    }

    public void rasterTriangle(Triangle triangle, Color color) {
        Vec4d[] points = Arrays.stream(triangle.getPoints())
                .map(vec -> new Vec4d((int) vec.getX(), (int) vec.getY(), vec.getZ()))
                .sorted((vec1, vec2) -> (int) Math.signum(Math.ceil(vec1.getY() - vec2.getY())))
                .toArray(Vec4d[]::new);
        //drawTriangle(triangle, color);
        List<Vec4d> points1 = getDrawableVectors(points[0], points[1]);
        List<Vec4d> points2 = getDrawableVectors(points[1], points[2]);
        List<Vec4d> points3 = getDrawableVectors(points[0], points[2]);
        int index = 0;
        for (Vec4d vec : points1) {
            line(vec, points3.get(index), color);
            index++;
        }
        index--;
        for (Vec4d vec : points2) {
            line(vec, points3.get(index), color);
            index++;
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
