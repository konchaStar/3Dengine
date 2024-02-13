package com.example._3dgraphics;

import com.example._3dgraphics.graphics.Graphics;
import com.example._3dgraphics.math.Vec4d;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App extends JComponent implements ActionListener, KeyListener, MouseWheelListener {
    private static JFrame frame;
    private static final int HEADER_HEIGHT = 32;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private Timer timer;
    private long prev;
    private Graphics graphics;
    private Mesh cube;
    private double angle = 0;
    private Camera camera;
    private Robot input;
    private Vec4d light = new Vec4d(1, 1, 0);
    private BufferedImage dm;
    private BufferedImage nm;
    private BufferedImage rm;

    public static void main(String[] args) throws IOException, AWTException {
        BufferedImage buffer = new BufferedImage(WINDOW_WIDTH + 1, WINDOW_HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
        App app = new App();
        app.init(buffer);
        frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT + HEADER_HEIGHT);
        frame.add(app);
        frame.addKeyListener(app);
        frame.addMouseWheelListener(app);
    }

    private void init(BufferedImage buffer) throws IOException, AWTException {
        input = new Robot();
        graphics = new Graphics(buffer, WINDOW_WIDTH, WINDOW_HEIGHT);
        prev = System.currentTimeMillis();
        camera = new Camera(WINDOW_WIDTH, WINDOW_HEIGHT, 90, 0.1, 1000, graphics);
        cube = new Mesh();
        cube.loadMesh("objects/head.obj");
        cube.rotateY(180);
        cube.translate(new Vec4d(0, 0, 2));
        dm = ImageIO.read(new File("maps/african_head_diffuse.png"));
        rm = ImageIO.read(new File("maps/african_head_spec.png"));
        nm = ImageIO.read(new File("maps/african_head_nm.png"));
        flipHor(dm, false);
        flipVert(dm, false);
        flipHor(rm, false);
        flipVert(rm, false);
        flipHor(nm, true);
        flipVert(nm, true);
        input.mouseMove(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
        timer = new Timer(5, this);
        timer.start();
    }

    private void flipHor(BufferedImage image, boolean nm) {
        for (int i = 0; i < image.getHeight() / 2; i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color1 = new Color(image.getRGB(j, i));
                Color color2 = new Color(image.getRGB(j, image.getHeight() - 1 - i));
//                if (nm) {
//                    color1 = new Color(color1.getRed(), 255 - color1.getGreen(), color1.getBlue());
//                    color2 = new Color(color2.getRed(), 255- color2.getGreen(), color2.getBlue());
//                }
                image.setRGB(j, i, color2.getRGB());
                image.setRGB(j, image.getHeight() - 1 - i, color1.getRGB());
            }
        }
    }

    private void flipVert(BufferedImage image, boolean nm) {
        for (int i = 0; i < image.getWidth() / 2; i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color1 = new Color(image.getRGB(i, j));
                Color color2 = new Color(image.getRGB(image.getWidth() - 1 - i, j));
                if (nm) {
                    color1 = new Color(255 - color1.getRed(), color1.getGreen(), color1.getBlue());
                    color2 = new Color(255 - color2.getRed(), color2.getGreen(), color2.getBlue());
                }
                image.setRGB(i, j, color2.getRGB());
                image.setRGB(image.getWidth() - 1 - i, j, color1.getRGB());
            }
        }
    }

    @Override
    public void paint(java.awt.Graphics g) {
//        angle += (System.currentTimeMillis() - prev) / 1000.0 * 90;
        //frame.setTitle(String.format("Press esc to exit %d fps", (int) (1000 / (System.currentTimeMillis() - prev))));
        frame.setTitle(String.format("light %.3f %.3f %.3f position %.3f %.3f %.3f", light.getX(), light.getY(), light.getZ(),
                camera.getPosition().getX(), camera.getPosition().getY(), camera.getPosition().getZ()));
        prev = System.currentTimeMillis();
//        if (angle > 360) {
//            angle -= 360;
//        }
        graphics.clear(new Color(255, 255, 255));
//        camera.draw(cube, new Color(255, 153, 153), light, true);
//        camera.phongShading(cube, light, new Color(255, 153, 153));
//        camera.phongLighting(cube, light, new Color(128, 70, 70), new Color(255, 153, 153), new Color(255, 200, 200));
        camera.drawText(cube, light, dm, nm, rm);
        g.drawImage(graphics.getBuffer(), 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Point position = MouseInfo.getPointerInfo().getLocation();
        int dx = position.x - WINDOW_WIDTH / 2;
        int dy = position.y - WINDOW_HEIGHT / 2;
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
            Vec4d direction = camera.lookAt().multiply(delta * 5);
            camera.translate(direction);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            Vec4d direction = camera.lookAt().multiply(-delta * 5);
            camera.translate(direction);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            Vec4d direction = camera.right().multiply(-delta * 5);
            camera.translate(direction);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            Vec4d direction = camera.right().multiply(delta * 5);
            camera.translate(direction);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            camera.translate(new Vec4d(0, 1, 0).multiply(delta * 5));
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            camera.translate(new Vec4d(0, 1, 0).multiply(-delta * 5));
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