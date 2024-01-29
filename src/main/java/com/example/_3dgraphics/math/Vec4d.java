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
        if(w != 0) {
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
