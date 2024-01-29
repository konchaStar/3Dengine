package com.example._3dgraphics.math;

public class Triangle {
    private Vec4d points[] = new Vec4d[3];
    private Vec4d normal;

    public Triangle(Vec4d p1, Vec4d p2, Vec4d p3) {
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
    }

    public Triangle multiply(Matrix4x4 matrix4x4) {
        Vec4d p1 = points[0].multiply(matrix4x4);
        Vec4d p2 = points[1].multiply(matrix4x4);
        Vec4d p3 = points[2].multiply(matrix4x4);
        return new Triangle(p1, p2, p3);
    }

    public Vec4d[] getPoints() {
        return points;
    }

    public void setPoints(Vec4d[] points) {
        this.points = points;
    }

    private void calculateNormal() {
        normal = points[0].cross(points[1]).normalize();
    }
}