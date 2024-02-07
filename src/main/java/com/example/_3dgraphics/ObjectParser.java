package com.example._3dgraphics;

import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectParser {
    private List<Vec4d> vertices = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>();
    private List<Vec4d> normals = new ArrayList<>();

    public void parse(String line) {
        String[] data = line.split(" +");
        switch (data[0]) {
            case "v": {
                parseVertex(Arrays.copyOfRange(data, 1, data.length));
                break;
            }
            case "f": {
                parseFaces(Arrays.copyOfRange(data, 1, data.length));
                break;
            }
            case "vn": {
                parseNormals(Arrays.copyOfRange(data, 1, data.length));
            }
        }
    }

    private void parseVertex(String[] data) {
        Double[] coordinates = Arrays.stream(data)
                .map(coord -> Double.parseDouble(coord))
                .toArray(Double[]::new);
        vertices.add(new Vec4d(coordinates[0], coordinates[1], coordinates[2]));
    }

    private void parseFaces(String[] data) {
        Integer[] v = Arrays.stream(data)
                .map(vertex -> Integer.parseInt(vertex.split("/")[0]) - 1)
                .toArray(Integer[]::new);
        Integer[] vt = null;
        Integer[] vn = null;
        if (data[0].split("/").length > 1) {

        }
        if (data[0].split("/").length > 2) {
            vn = Arrays.stream(data)
                    .map(vertex -> Integer.parseInt(vertex.split("/")[2]) - 1)
                    .toArray(Integer[]::new);

        }
        for (int i = 1; i < v.length - 1; i++) {
            Triangle triangle = new Triangle(vertices.get(v[0]), vertices.get(v[i]),
                    vertices.get(v[i + 1]));
            if (vn != null) {
                triangle.setNormals(normals.get(vn[0]), normals.get(vn[i]),
                        normals.get(vn[i + 1]));
            }
            triangles.add(triangle);
        }
    }

    private void parseNormals(String[] data) {
        Double[] coordinates = Arrays.stream(data)
                .map(coord -> Double.parseDouble(coord))
                .toArray(Double[]::new);
        normals.add(new Vec4d(coordinates[0], coordinates[1], coordinates[2]));
    }

    public boolean hasNormals() {
        return !normals.isEmpty();
    }

    public List<Vec4d> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vec4d> vertices) {
        this.vertices = vertices;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }
}
