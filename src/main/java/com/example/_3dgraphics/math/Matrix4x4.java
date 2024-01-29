package com.example._3dgraphics.math;

public class Matrix4x4 {
    private double[][] matrix = new double[4][4];

    public Matrix4x4(double[][] matrix) {
        this.matrix = matrix;
    }

    public static Matrix4x4 getIdentity() {
        return new Matrix4x4(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        });
    }

    public static Matrix4x4 getTranslation(double x, double y, double z) {
        return new Matrix4x4(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {x, y, z, 1},
        });
    }

    public static Matrix4x4 getRotationXMatrix(double angle) {
        double rad = angle / 180 * Math.PI;
        return new Matrix4x4(new double[][]{
                {1, 0, 0, 0},
                {0, Math.cos(rad), Math.sin(rad), 0},
                {0, -Math.sin(rad), Math.cos(rad), 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 getRotationYMatrix(double angle) {
        double rad = angle / 180 * Math.PI;
        return new Matrix4x4(new double[][]{
                {Math.cos(rad), 0, -Math.sin(rad), 0},
                {0, 1, 0, 0},
                {Math.sin(rad), 0, Math.cos(rad), 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 getRotationZMatrix(double angle) {
        double rad = angle / 180 * Math.PI;
        return new Matrix4x4(new double[][]{
                {Math.cos(rad), Math.sin(rad), 0, 0},
                {-Math.sin(rad), Math.cos(rad), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 getProjectionMatrix(double fovDeg, double aspectRatio, double zNear, double zFar) {
        double fovRad = 1.0 / Math.tan(fovDeg * 0.5 / 180 * Math.PI);
        return new Matrix4x4(new double[][]{
                {aspectRatio * fovRad, 0, 0, 0},
                {0, fovRad, 0, 0},
                {0, 0, zFar / (zFar - zNear), 1},
                {0, 0, (-zFar * zNear) / (zFar - zNear), 0}
        });
    }

    public static Matrix4x4 getScreenMatrix(double width, double height) {
        return new Matrix4x4(new double[][]{
                {0.5 * width, 0, 0, 0},
                {0, -0.5 * height, 0, 0},
                {0, 0, 1, 0},
                {0.5 * width, 0.5 * height, 0, 1}
        });
    }

    public static Matrix4x4 getScaleMatrix(double x, double y, double z) {
        return new Matrix4x4(new double[][]{
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 getCameraMatrix(Matrix4x4 cameraModel) {
        double[][] model = cameraModel.getMatrix();
        Vec4d a = new Vec4d(model[0][0], model[0][1], model[0][2]);
        Vec4d b = new Vec4d(model[1][0], model[1][1], model[1][2]);
        Vec4d c = new Vec4d(model[2][0], model[2][1], model[2][2]);
        Vec4d t = new Vec4d(model[3][0], model[3][1], model[3][2]);
        double aSqr = a.sqr();
        double bSqr = b.sqr();
        double cSqr = c.sqr();
        return new Matrix4x4(new double[][]{
                {a.getX() / aSqr, b.getX() / bSqr, c.getX() / cSqr, 0},
                {a.getY() / aSqr, b.getY() / bSqr, c.getY() / cSqr, 0},
                {a.getZ() / aSqr, b.getZ() / bSqr, c.getZ() / cSqr, 0},
                {-t.dot(a) / aSqr, -t.dot(b) / bSqr, -t.dot(c) / cSqr, 1}
        });
    }

    public Matrix4x4 multiply(Matrix4x4 matrix4x4) {
        double[][] matrix = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = this.matrix[i][0] * matrix4x4.matrix[0][j] + this.matrix[i][1] * matrix4x4.matrix[1][j]
                        + this.matrix[i][2] * matrix4x4.matrix[2][j] + this.matrix[i][3] * matrix4x4.matrix[3][j];
            }
        }
        return new Matrix4x4(matrix);
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }
}
