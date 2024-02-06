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

    public void line(Vec4d vec1, Vec4d vec2, Vec4d l1, Vec4d l2, Vec4d n1, Vec4d n2, Color color) {
        vec1 = new Vec4d((int) vec1.getX(), (int) vec1.getY(), vec1.getZ());
        vec2 = new Vec4d((int) vec2.getX(), (int) vec2.getY(), vec2.getZ());
        if (vec1.getX() == vec2.getX() && vec1.getY() == vec2.getY()) {
            int posX = (int) Math.round(vec1.getX());
            int posY = (int) Math.round(vec1.getY());
            if (posX >= 0 && posX < width - 1 && posY >= 0 && posY < height - 1) {
                double z = Math.min(vec1.getZ(), vec2.getZ());
                if (z < zBuffer[posX][posY]) {
                    double intensity = Math.max(0, l1.normalize().dot(n1.normalize()));
                    Color illuminated = new Color(
                            (int) (color.getRed() * intensity),
                            (int) (color.getGreen() * intensity),
                            (int) (color.getBlue() * intensity)
                    );
                    buffer.setRGB(posX, posY, illuminated.getRGB());
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
        Vec4d dl = l2.sub(l1).divide(l);
        Vec4d dn = n2.sub(n1).divide(l);
        for (int i = 0; i <= l; i++) {
            if (Math.round(x) >= 0 && Math.round(x) < width - 1 && Math.round(y) >= 0 && Math.round(y) < height - 1) {
                int posX = (int) Math.round(x);
                int posY = (int) Math.round(y);
                if (z < zBuffer[posX][posY]) {
                    Vec4d light = l1.add(dl.multiply(i)).normalize();
                    Vec4d normal = n1.add(dn.multiply(i)).normalize();
                    double intensity = Math.max(0, light.dot(normal));
                    Color illuminated = new Color(
                            (int) (color.getRed() * intensity),
                            (int) (color.getGreen() * intensity),
                            (int) (color.getBlue() * intensity)
                    );
                    buffer.setRGB(posX, posY, illuminated.getRGB());
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
                .sorted((vec1, vec2) -> (int) (Math.ceil(vec1.getY() - vec2.getY())))
                .toArray(Vec4d[]::new);

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

    public void phongShading(Vec4d[] vertices, Vec4d[] lights, Vec4d[] normals, Color color) {
        List<Vec4d> points1 = getDrawableVectors(vertices[0], vertices[1]);
        List<Vec4d> points2 = getDrawableVectors(vertices[1], vertices[2]);
        List<Vec4d> points3 = getDrawableVectors(vertices[0], vertices[2]);
        Vec4d dl1 = lights[1].sub(lights[0]).divide(points1.size());
        Vec4d dl2 = lights[2].sub(lights[1]).divide(points2.size());
        Vec4d dl3 = lights[2].sub(lights[0]).divide(points3.size());
        Vec4d dn1 = normals[1].sub(normals[0]).divide(points1.size());
        Vec4d dn2 = normals[2].sub(normals[1]).divide(points2.size());
        Vec4d dn3 = normals[2].sub(normals[0]).divide(points3.size());
        int index = 0;
        for (Vec4d vec : points1) {
            Vec4d l1 = lights[0].add(dl1.multiply(index));
            Vec4d l2 = lights[0].add(dl3.multiply(index));
            Vec4d n1 = normals[0].add(dn1.multiply(index));
            Vec4d n2 = normals[0].add(dn3.multiply(index));
            line(vec, points3.get(index), l1, l2, n1, n2, color);
            index++;
        }
        index--;
        int index2 = 0;
        for (Vec4d vec : points2) {
            Vec4d l1 = lights[1].add(dl2.multiply(index2));
            Vec4d l2 = lights[0].add(dl3.multiply(index));
            Vec4d n1 = normals[1].add(dn2.multiply(index2));
            Vec4d n2 = normals[0].add(dn3.multiply(index));
            line(vec, points3.get(index), l1, l2, n1, n2, color);
            index2++;
            index++;
        }
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
