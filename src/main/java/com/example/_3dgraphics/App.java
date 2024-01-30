package com.example._3dgraphics;

import com.example._3dgraphics.graphics.Graphics;
import com.example._3dgraphics.math.Matrix4x4;
import com.example._3dgraphics.math.Triangle;
import com.example._3dgraphics.math.Vec4d;
import com.sun.prism.paint.Color;

import javax.swing.*;
import java.awt.*;
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
    private Camera camera;
    private Robot input;

    public static void main(String[] args) throws IOException, AWTException {
        BufferedImage buffer = new BufferedImage(WINDOW_WIDTH + 1, WINDOW_HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        App app = new App();
        frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT + HEADER_HEIGHT);
        frame.add(app);
        frame.addKeyListener(app);
        frame.addMouseWheelListener(app);
        app.init(buffer);
    }

    private void init(BufferedImage buffer) throws IOException, AWTException {
        input = new Robot();
        graphics = new Graphics(buffer, WINDOW_WIDTH, WINDOW_HEIGHT);
        prev = System.currentTimeMillis();
        camera = new Camera(WINDOW_WIDTH, WINDOW_HEIGHT, 90, 0.1, 10);
        cube = new Mesh();
        cube.loadMesh("objects/girl.obj");
        input.mouseMove(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
        timer.start();
    }

    @Override
    public void paint(java.awt.Graphics g) {
        angle += (System.currentTimeMillis() - prev) / 1000.0 * 90;
        frame.setTitle(String.format("Press esc to exit %d fps", (int) (1000 / (System.currentTimeMillis() - prev))));
        prev = System.currentTimeMillis();
        if (angle > 360) {
            angle -= 360;
        }
        graphics.clear(Color.WHITE.getIntArgbPre());
        modelTriangles.clear();
        Matrix4x4 model = Matrix4x4.getRotationYMatrix(180)
                        .multiply(Matrix4x4.getTranslation(0,-50,50));
        Matrix4x4 projection = camera.getCameraMatrix()
                .multiply(Matrix4x4.getProjectionMatrix(90, (double) WINDOW_HEIGHT / WINDOW_WIDTH, 0.1, 10))
                .multiply(Matrix4x4.getScreenMatrix(WINDOW_WIDTH, WINDOW_HEIGHT));
        for (Triangle tri : cube.getTris()) {
            Triangle triangle = tri.multiply(model);
            double dot = triangle.getNormal().dot(camera.getPosition().sub(triangle.getPoints()[0]).normalize());
            if(dot < 0) {
                graphics.drawTriangle(tri.multiply(model.multiply(projection)), 0);
            }
        }

        g.drawImage(graphics.getBuffer(), 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Point position = MouseInfo.getPointerInfo().getLocation();
        int dx = position.x - WINDOW_WIDTH / 2;
        int dy = position.y - WINDOW_HEIGHT / 2;
        double delta = (System.currentTimeMillis() - prev) / 1000.0;
        camera.rotateY(0.2 * dx);
        camera.rotate(camera.right(), 0.2 * dy);
        input.mouseMove(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        double delta = (System.currentTimeMillis() - prev) / 1000.0;
        if (e.getKeyCode() == KeyEvent.VK_W) {
            Vec4d direction = camera.lookAt().multiply(delta * 10);
            camera.translate(direction);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            Vec4d direction = camera.lookAt().multiply(-delta * 10);
            camera.translate(direction);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            Vec4d direction = camera.right().multiply(-delta * 10);
            camera.translate(direction);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            Vec4d direction = camera.right().multiply(delta * 10);
            camera.translate(direction);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            camera.translate(new Vec4d(0, 1, 0).multiply(delta * 10));
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            camera.translate(new Vec4d(0, 1, 0).multiply(-delta * 10));
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}