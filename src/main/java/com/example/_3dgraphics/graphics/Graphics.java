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
        if(x1 == x2 && y1 == y2) {
            buffer.setRGB(x1, y1, color);
            return;
        }
        double l = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));

        double x = x1;
        double y = y1;
        double dx = (x2 - x1) / l;
        double dy = (y2 - y1) / l;
        do {
            buffer.setRGB((int)Math.round(x), (int)Math.round(y), color);
            x += dx;
            y += dy;
        } while ((x != x2 || x1 == x2) && (y != y2 || y1 == y2));
    }

    public void drawTriangle(Triangle triangle, int color) {
        Vec4d[] vertices = triangle.getPoints();
        line((int)vertices[0].getX(), (int)vertices[0].getY(), (int)vertices[1].getX(), (int)vertices[1].getY(), color);
        line((int)vertices[1].getX(), (int)vertices[1].getY(), (int)vertices[2].getX(), (int)vertices[2].getY(), color);
        line((int)vertices[0].getX(), (int)vertices[0].getY(), (int)vertices[2].getX(), (int)vertices[2].getY(), color);
    }

    public void rasterTriangle(Triangle triangle, int color) {

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
