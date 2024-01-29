package com.example._3dgraphics;

import com.example._3dgraphics.math.Triangle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
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
