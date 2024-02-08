package com.example._3dgraphics;

import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mesh extends Object3D {
    private List<Triangle> tris = new ArrayList<>();

    public void loadMesh(String path) throws IOException {
        tris.clear();

        BufferedReader reader = new BufferedReader(new FileReader(path));
        ObjectParser parser = new ObjectParser();

        while (reader.ready()) {
            String line = reader.readLine();
            parser.parse(line);
        }
        tris.addAll(parser.getTriangles());
        if (!parser.hasNormals()) {
            Map<Vec4d, Vec4d> normals = new HashMap<>();
            for (Triangle tri : tris) {
                for (int i = 0; i < 3; i++) {
                    Vec4d normal = tri.getNormal();
                    if (normals.containsKey(tri.getPoints()[i])) {
                        normal = normal.add(normals.get(tri.getPoints()[i]));
                    }
                    normals.put(tri.getPoints()[i], normal);
                }
            }
            for (Triangle tri : tris) {
                for (int i = 0; i < 3; i++) {
                    tri.getNormals()[i] = normals.get(tri.getPoints()[i]).normalize();
                }
            }
        }
    }

    public void addTriangle(Triangle tri) {
        tris.add(tri);
    }

    public List<Triangle> getTris() {
        return tris;
    }

    public void setTris(List<Triangle> tris) {
        this.tris = tris;
    }
}
