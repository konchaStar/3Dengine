package com.example._3dgraphics.math;

public class Vec3d {
    private double u;
    private double v;
    private double w = 1;

    public Vec3d(double u, double v) {
        this.u = u;
        this.v = v;
    }

    public Vec3d add(Vec3d vec) {
        return new Vec3d(u + vec.u, v + vec.v);
    }

    public Vec3d sub(Vec3d vec) {
        return new Vec3d(u - vec.u, v - vec.v);
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }
}
