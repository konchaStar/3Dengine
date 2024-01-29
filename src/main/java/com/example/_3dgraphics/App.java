package com.example._3dgraphics;

import com.example._3dgraphics.graphics.Graphics;
import com.example._3dgraphics.math.Matrix4x4;
import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;
import com.sun.prism.paint.Color;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends JComponent implements ActionListener, KeyListener, MouseWheelListener {
    private static JFrame frame;
    private static final int HEADER_HEIGHT = 32;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private Timer timer = new Timer(5, this);
    private long prev;
    private Graphics graphics;
    private Mesh cube;
    private List<Triangle> modelTriangles = new ArrayList<>();
    private double angle = 0;
    private Matrix4x4 cameraModel = Matrix4x4.getIdentity();

    public static void main(String[] args) throws IOException {
        BufferedImage buffer = new BufferedImage(WINDOW_WIDTH + 1, WINDOW_HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        App app = new App();
        app.graphics = new Graphics(buffer, WINDOW_WIDTH, WINDOW_HEIGHT);
        app.prev = System.currentTimeMillis();
        frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT + HEADER_HEIGHT);
        frame.add(app);
        frame.addKeyListener(app);
        frame.addMouseWheelListener(app);
        app.cube = new Mesh();
        app.cube.loadMesh("objects/cube.obj");
        app.timer.start();
    }

    @Override
    public void paint(java.awt.Graphics g) {
        angle += (System.currentTimeMillis() - prev) / 1000.0 * 90;
        frame.setTitle(Integer.toString((int) (1000 / (System.currentTimeMillis() - prev))));
        prev = System.currentTimeMillis();
        if (angle > 360) {
            angle -= 360;
        }
        graphics.clear(Color.WHITE.getIntArgbPre());
        modelTriangles.clear();
        Matrix4x4 model = Matrix4x4.getRotationYMatrix(angle)
                .multiply(Matrix4x4.getTranslation(0, 0, 4))
                .multiply(Matrix4x4.getCameraMatrix(cameraModel))
                .multiply(Matrix4x4.getProjectionMatrix(90, (double) WINDOW_HEIGHT / WINDOW_WIDTH, 0.1, 10))
                .multiply(Matrix4x4.getScreenMatrix(WINDOW_WIDTH, WINDOW_HEIGHT));
        for (Triangle tri : cube.getTris()) {
            graphics.rasterTriangle(tri.multiply(model), Color.GREEN.getIntArgbPre());
        }
//        graphics.rasterTriangle(new Triangle(new Vec4d(300, 200, 0), new Vec4d(200, 100, 0), new Vec4d(500, 100, 0)),
//                Color.GREEN.getIntArgbPre());
        g.drawImage(graphics.getBuffer(), 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}