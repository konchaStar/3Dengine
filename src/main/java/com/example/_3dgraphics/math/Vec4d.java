package com.example._3dgraphics.math;

public class Vec4d {
    private double x;
    private double y;
    private double z;
    private double w = 1.0;

    public Vec4d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec4d multiply(Matrix4x4 matrix4x4) {
        double[][] matrix = matrix4x4.getMatrix();
        double x = this.x * matrix[0][0] + this.y * matrix[1][0] + this.z * matrix[2][0] + this.w * matrix[3][0];
        double y = this.x * matrix[0][1] + this.y * matrix[1][1] + this.z * matrix[2][1] + this.w * matrix[3][1];
        double z = this.x * matrix[0][2] + this.y * matrix[1][2] + this.z * matrix[2][2] + this.w * matrix[3][2];
        double w = this.x * matrix[0][3] + this.y * matrix[1][3] + this.z * matrix[2][3] + this.w * matrix[3][3];
        if (w != 0) {
            x /= w;
            y /= w;
            z /= w;
        }
        return new Vec4d(x, y, z);
    }

    public double sqr() {
        return x * x + y * y + z * z;
    }

    public double dot(Vec4d vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public Vec4d multiply(double n) {
        return new Vec4d(x * n, y * n, z * n);
    }

    public Vec4d add(Vec4d vec) {
        return new Vec4d(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vec4d cross(Vec4d vec) {
        return new Vec4d(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    public Vec4d normalize() {
        double magnitude = Math.sqrt(sqr());
        return new Vec4d(x / magnitude, y / magnitude, z / magnitude);
    }

    public Vec4d sub(Vec4d vec) {
        return new Vec4d(x - vec.x, y - vec.y, z - vec.z);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
