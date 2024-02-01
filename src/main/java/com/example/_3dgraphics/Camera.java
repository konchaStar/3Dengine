package com.example._3dgraphics;
import com.example._3dgraphics.math.Vec4d;
import com.example._3dgraphics.math.Matrix4x4;
import com.example._3dgraphics.math.Vec4d;


public class Camera {
    private Matrix4x4 model = Matrix4x4.getIdentity();
    private Vec4d position = new Vec4d(0,0,0);
    private int width;
    private int height;
    private double aspectRatio;
    private double fov;
    private double zNear;
    private double zFar;

    public Camera(int width, int height, double fov, double zNear, double zFar) {
        this.width = width;
        this.height = height;
        this.aspectRatio = (double) height / width;
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    public Vec4d lookAt() {
        double[][] matrix = model.getMatrix();
        return new Vec4d(matrix[2][0], matrix[2][1], matrix[2][2]).normalize();
    }

    public Vec4d up() {
        double[][] matrix = model.getMatrix();
        return new Vec4d(matrix[1][0], matrix[1][1], matrix[1][2]).normalize();
    }

    public Vec4d right() {
        double[][] matrix = model.getMatrix();
        return new Vec4d(matrix[0][0], matrix[0][1], matrix[0][2]).normalize();
    }

    public Matrix4x4 getCameraMatrix() {
        return Matrix4x4.getCameraMatrix(model
                .multiply(Matrix4x4.getTranslation(position.getX(), position.getY(), position.getZ())));
    }

    public Matrix4x4 getProjectionMatrix() {
        return Matrix4x4.getProjectionMatrix(fov, aspectRatio, zNear, zFar);
    }

    public Matrix4x4 getScreenMatrix() {
        return Matrix4x4.getScreenMatrix(width, height);
    }

    public void translate(Vec4d vec) {
        position = position.add(vec);
    }

    public void rotateY(double angle) {
        model = model.multiply(Matrix4x4.getRotationYMatrix(angle));
    }

    public void rotateX(double angle) {
        model = model.multiply(Matrix4x4.getRotationXMatrix(angle));
    }

    public void rotateZ(double angle) {
        model = model.multiply(Matrix4x4.getRotationZMatrix(angle));
    }

    public void rotate(Vec4d vec, double angle) {
        model = model.multiply(Matrix4x4.getRotationMatrix(vec, angle));
    }

    public Matrix4x4 getModel() {
        return model;
    }

    public void setModel(Matrix4x4 model) {
        this.model = model;
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

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    public double getzNear() {
        return zNear;
    }

    public void setzNear(double zNear) {
        this.zNear = zNear;
    }

    public double getzFar() {
        return zFar;
    }

    public void setzFar(double zFar) {
        this.zFar = zFar;
    }

    public Vec4d getPosition() {
        return position;
    }

    public void setPosition(Vec4d position) {
        this.position = position;
    }
}
