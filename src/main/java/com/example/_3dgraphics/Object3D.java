package com.example._3dgraphics;

import com.example._3dgraphics.math.Matrix4x4;
import com.example._3dgraphics.math.Vec4d;

public class Object3D {
    protected Matrix4x4 model = Matrix4x4.getIdentity();
    protected Vec4d position = new Vec4d(0, 0, 0);

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

    public Matrix4x4 getModelTranslationMatrix() {
        return model.multiply(Matrix4x4.getTranslation(position.getX(), position.getY(), position.getZ()));
    }

    public Matrix4x4 getModel() {
        return model;
    }

    public void setModel(Matrix4x4 model) {
        this.model = model;
    }

    public Vec4d getPosition() {
        return position;
    }

    public void setPosition(Vec4d position) {
        this.position = position;
    }
}
