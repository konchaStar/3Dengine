package com.example._3dgraphics;

import com.example._3dgraphics.graphics.Graphics;
import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;
import com.example._3dgraphics.math.Matrix4x4;
import com.example._3dgraphics.math.Plane;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Camera {
    private Matrix4x4 model = Matrix4x4.getIdentity();
    private Matrix4x4 projScale;
    private Vec4d position = new Vec4d(0, 0, 0);
    private List<Plane> planes = new ArrayList<>();
    private int width;
    private int height;
    private double aspectRatio;
    private double fov;
    private double zNear;
    private double zFar;
    private Graphics graphics;

    public Camera(int width, int height, double fov, double zNear, double zFar, Graphics graphics) {
        this.width = width;
        this.height = height;
        this.aspectRatio = (double) height / width;
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;
        this.graphics = graphics;
        projScale = getProjectionMatrix().multiply(getScreenMatrix());
        planes.add(new Plane(new Vec4d(0, 0, 1), new Vec4d(0, 0, zNear)));
        planes.add(new Plane(new Vec4d(0, 0, -1), new Vec4d(0, 0, zFar)));
        double angle1 = Math.PI * 0.5 * fov / 180;
        double angle2 = angle1 / aspectRatio;
        planes.add(new Plane(new Vec4d(-Math.cos(angle2), 0, Math.sin(angle2)), new Vec4d(0, 0, 0)));
        planes.add(new Plane(new Vec4d(Math.cos(angle2), 0, Math.sin(angle2)), new Vec4d(0, 0, 0)));
        planes.add(new Plane(new Vec4d(0, -Math.cos(angle1), Math.sin(angle1)), new Vec4d(0, 0, 0)));
        planes.add(new Plane(new Vec4d(0, Math.cos(angle1), Math.sin(angle1)), new Vec4d(0, 0, 0)));
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

    public void draw(Mesh mesh, Matrix4x4 model, Color color, Vec4d light, boolean isPointLight) {
        List<Triangle> buffer = new ArrayList<>();
        List<Triangle> clipped = new ArrayList<>();
        Matrix4x4 view = getCameraMatrix();
        for (Triangle tri : mesh.getTris()) {
            Triangle triangle = tri.multiply(model);
            double dot = triangle.getNormal().dot(triangle.getPoints()[0].sub(getPosition()).normalize());
            if (dot > 0) {
                double intensity = isPointLight ? Math.max(0.3, triangle.getNormal()
                        .dot(triangle.getPoints()[0].sub(light).normalize()))
                        : Math.max(0.3, triangle.getNormal().dot(light));
                triangle = triangle.multiply(view);
                clipped.clear();
                buffer.clear();
                clipped.add(triangle);
                for (Plane plane : planes) {
                    while (!clipped.isEmpty()) {
                        List<Triangle> clippedResult = plane.clip(clipped.get(clipped.size() - 1));
                        clipped.remove(clipped.size() - 1);
                        buffer.addAll(clippedResult);
                    }
                    clipped.addAll(buffer);
                    buffer.clear();
                }
                Color illuminated = new Color(
                        (int) (intensity * color.getRed()),
                        (int) (intensity * color.getGreen()),
                        (int) (intensity * color.getBlue()));
                for(Triangle clippedTri : clipped) {
                    graphics.rasterTriangle(clippedTri.multiply(getProjScale()), illuminated);
                }
            }
        }
    }

    public void draw(Mesh mesh, Matrix4x4 model, Color color) {
        draw(mesh, model, color, getPosition(), true);
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

    public Matrix4x4 getProjScale() {
        return projScale;
    }

    public void setProjScale(Matrix4x4 projScale) {
        this.projScale = projScale;
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