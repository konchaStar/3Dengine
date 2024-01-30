package com.example._3dgraphics;

import java.util.Arrays;

public class ZBuffer {
    private double[][] buffer;

    public ZBuffer(int width, int height) {
        double[][] tempBuff = new double[width][height];
        Arrays.stream(tempBuff).forEach(row -> Arrays.fill(row, 2));
        this.buffer = tempBuff;

    }

    public double[][] getBuffer() {
        return buffer;
    }

    public void setBuffer(double[][] buffer) {
        this.buffer = buffer;
    }

    public double getZ(int i, int j) {
        return buffer[i][j];
    }

    public void setZ(int i, int j, double Z) {
        buffer[i][j] = Z;
    }
}
