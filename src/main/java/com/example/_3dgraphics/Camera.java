package com.example._3dgraphics;

import com.example._3dgraphics.graphics.Graphics;
import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;
import com.example._3dgraphics.math.Matrix4x4;
import com.example._3dgraphics.math.Plane;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Camera extends Object3D {
    private Matrix4x4 projScale;
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

    public void draw(Mesh mesh, Color color, Vec4d light, boolean isPointLight) {
        List<Triangle> buffer = new ArrayList<>();
        List<Triangle> clipped = new ArrayList<>();
        Matrix4x4 view = getCameraMatrix();
        Matrix4x4 model = mesh.getModelTranslationMatrix();
        for (Triangle tri : mesh.getTris()) {
            Triangle triangle = tri.multiply(model);
            double dot = triangle.getNormal().dot(getPosition().sub(triangle.getPoints()[0]).normalize());
            if (dot > 0) {
                double intensity = isPointLight ? Math.max(0.1, triangle.getNormal()
                        .dot(light.sub(triangle.getPoints()[0]).normalize()))
                        : Math.max(0.1, triangle.getNormal().dot(light));
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
                for (Triangle clippedTri : clipped) {
                    graphics.rasterTriangle(clippedTri.multiply(getProjScale()), illuminated);
                }
            }
        }
    }

    public void phongShading(Mesh mesh, Vec4d light, Color color) {
        Matrix4x4 view = getCameraMatrix();
        Matrix4x4 model = mesh.getModelTranslationMatrix();
        for (Triangle triangle : mesh.getTris()) {
            Triangle modelTriangle = triangle.multiply(model);
            double dot = modelTriangle.getNormal().dot(getPosition().sub(modelTriangle.getPoints()[0]).normalize());
            if (dot > 0) {
                Triangle viewTriangle = modelTriangle.multiply(view);
                Triangle projTriangle = viewTriangle.multiply(getProjScale());
                Integer[] indexes = Arrays.stream(new Integer[]{0, 1, 2})
                        .sorted(Comparator.comparingInt(i -> (int) projTriangle.getPoints()[i].getY()))
                        .toArray(Integer[]::new);
                Vec4d[] lights = new Vec4d[]{light.sub(modelTriangle.getPoints()[indexes[0]]),
                        light.sub(modelTriangle.getPoints()[indexes[1]]),
                        light.sub(modelTriangle.getPoints()[indexes[2]])};
                Vec4d[] normals = new Vec4d[]{modelTriangle.getNormals()[indexes[0]],
                        modelTriangle.getNormals()[indexes[1]],
                        modelTriangle.getNormals()[indexes[2]]};
                Vec4d[] vertices = new Vec4d[]{projTriangle.getPoints()[indexes[0]],
                        projTriangle.getPoints()[indexes[1]],
                        projTriangle.getPoints()[indexes[2]]};
                graphics.phongShading(vertices, lights, normals, color);
            }
        }
    }

    public void phongLighting(Mesh mesh, Vec4d light, Color ambient, Color diffuse, Color reflect) {
        Matrix4x4 view = getCameraMatrix();
        Matrix4x4 model = mesh.getModelTranslationMatrix();
        for (Triangle triangle : mesh.getTris()) {
            Triangle modelTriangle = triangle.multiply(model);
            double dot = modelTriangle.getNormal().dot(getPosition().sub(modelTriangle.getPoints()[0]).normalize());
            if (dot > 0) {
                Triangle viewTriangle = modelTriangle.multiply(view);
                Triangle projTriangle = viewTriangle.multiply(getProjScale());
                Integer[] indexes = Arrays.stream(new Integer[]{0, 1, 2})
                        .sorted(Comparator.comparingInt(i -> (int) projTriangle.getPoints()[i].getY()))
                        .toArray(Integer[]::new);
                Vec4d[] lights = new Vec4d[]{light.sub(modelTriangle.getPoints()[indexes[0]]),
                        light.sub(modelTriangle.getPoints()[indexes[1]]),
                        light.sub(modelTriangle.getPoints()[indexes[2]])};
                Vec4d[] normals = new Vec4d[]{modelTriangle.getNormals()[indexes[0]],
                        modelTriangle.getNormals()[indexes[1]],
                        modelTriangle.getNormals()[indexes[2]]};
                Vec4d[] vertices = new Vec4d[]{projTriangle.getPoints()[indexes[0]],
                        projTriangle.getPoints()[indexes[1]],
                        projTriangle.getPoints()[indexes[2]]};
                graphics.phongLighting(vertices, lights, normals, lookAt(), ambient, diffuse, reflect);
            }
        }
    }

    public void phongShading(Mesh mesh, Color color) {
        phongShading(mesh, getPosition(), color);
    }

    public void draw(Mesh mesh, Color color) {
        draw(mesh, color, getPosition(), true);
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

    public Matrix4x4 getProjScale() {
        return projScale;
    }

    public void setProjScale(Matrix4x4 projScale) {
        this.projScale = projScale;
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

}