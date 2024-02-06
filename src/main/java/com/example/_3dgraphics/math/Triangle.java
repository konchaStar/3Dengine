package com.example._3dgraphics.math;

public class Triangle {
    private Vec4d points[] = new Vec4d[3];
    private Vec4d normals[] = new Vec4d[3];
    private Vec4d normal;

    public Triangle(Vec4d p1, Vec4d p2, Vec4d p3) {
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        for (int i = 0; i < 3; i++) {
            normals[i] = new Vec4d(0, 0, 0);
        }
        calculateNormal();
    }

    public Triangle multiply(Matrix4x4 matrix4x4) {
        Vec4d p1 = points[0].multiply(matrix4x4);
        Vec4d p2 = points[1].multiply(matrix4x4);
        Vec4d p3 = points[2].multiply(matrix4x4);
        double[][] matrix = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = matrix4x4.getMatrix()[i][j];
            }
        }
        matrix[3][0] = 0;
        matrix[3][1] = 0;
        matrix[3][2] = 0;
        Matrix4x4 model = new Matrix4x4(matrix);
        Vec4d n1 = normals[0].multiply(model).normalize();
        Vec4d n2 = normals[1].multiply(model).normalize();
        Vec4d n3 = normals[2].multiply(model).normalize();
        Triangle triangle = new Triangle(p1, p2, p3);
        triangle.setNormals(n1, n2, n3);
        return triangle;
    }

    public Vec4d[] getPoints() {
        return points;
    }

    public void setPoints(Vec4d[] points) {
        this.points = points;
    }

    private void calculateNormal() {
        Vec4d v1 = points[2].sub(points[0]);
        Vec4d v2 = points[1].sub(points[0]);
        normal = v2.cross(v1).normalize();
    }

    public Vec4d getNormal() {
        return normal;
    }

    public void setNormal(Vec4d normal) {
        this.normal = normal;
    }

    public void setNormals(Vec4d n1, Vec4d n2, Vec4d n3) {
        normals[0] = n1;
        normals[1] = n2;
        normals[2] = n3;
    }

    public Vec4d[] getNormals() {
        return normals;
    }
}