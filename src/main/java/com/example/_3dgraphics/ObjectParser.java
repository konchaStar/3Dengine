package com.example._3dgraphics;

import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectParser {
    private List<Vec4d> vertices = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>();

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
        }
    }

    private void parseVertex(String[] data) {
        Double[] coordinates = Arrays.stream(data)
                .map(coord -> Double.parseDouble(coord))
                .toArray(Double[]::new);
        vertices.add(new Vec4d(coordinates[0], coordinates[1], coordinates[2]));
    }

    private void parseFaces(String[] data) {
        Integer[] vectors = Arrays.stream(data)
                .map(vertex -> Integer.parseInt(vertex.split("/")[0]) - 1)
                .toArray(Integer[]::new);
        for (int i = 1; i < vectors.length - 1; i++) {
            triangles.add(new Triangle(vertices.get(vectors[0]), vertices.get(vectors[i]),
                    vertices.get(vectors[i + 1])));
        }
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
