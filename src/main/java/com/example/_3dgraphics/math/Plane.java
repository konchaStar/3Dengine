package com.example._3dgraphics.math;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Plane {
    private Vec4d normal;
    private Vec4d point;

    public Plane(Vec4d normal, Vec4d point) {
        this.normal = normal;
        this.point = point;
    }

    private double distance(Vec4d point) {
        return point.dot(normal) - this.point.dot(normal);
    }

    public Pair<Vec4d, Double> intersection(Vec4d start, Vec4d end) {
        double s_dot_n = start.dot(normal);
        double k = (s_dot_n - point.dot(normal)) / (s_dot_n - end.dot(normal));
        Vec4d res = start.add(end.sub(start).multiply(k));
        return new Pair<>(res, k);
    }

    public List<Triangle> clip(Triangle tri) {
        List<Triangle> result = new ArrayList<>();
        List<Vec4d> insidePoints = new ArrayList<>();
        List<Vec4d> outsidePoints = new ArrayList<>();

        double[] distances = new double[]{
                distance(tri.getPoints()[0]),
                distance(tri.getPoints()[1]),
                distance(tri.getPoints()[2])
        };

        for (int i = 0; i < 3; i++) {
            if (distances[i] >= 0) {
                insidePoints.add(tri.getPoints()[i]);
            } else {
                outsidePoints.add(tri.getPoints()[i]);
            }
        }
        if (insidePoints.size() == 1) {
            Vec4d intersect1 = intersection(insidePoints.get(0), outsidePoints.get(0)).getKey();
            Vec4d intersect2 = intersection(insidePoints.get(0), outsidePoints.get(1)).getKey();
            result.add(new Triangle(insidePoints.get(0), intersect2, intersect1));
        }
        if (insidePoints.size() == 2) {
            Vec4d intersect1 = intersection(outsidePoints.get(0), insidePoints.get(0)).getKey();
            Vec4d intersect2 = intersection(outsidePoints.get(0), insidePoints.get(1)).getKey();
            result.add(new Triangle(insidePoints.get(0), intersect1, insidePoints.get(1)));
            result.add(new Triangle(intersect1, intersect2, insidePoints.get(1)));
        }
        if (insidePoints.size() == 3) {
            result.add(tri);
        }
        return result;
    }

    public Vec4d getNormal() {
        return normal;
    }

    public void setNormal(Vec4d normal) {
        this.normal = normal;
    }

    public Vec4d getPoint() {
        return point;
    }

    public void setPoint(Vec4d point) {
        this.point = point;
    }
}