package com.aotem.threejs.three.lights;

public class AmbientLight extends Light {
    public boolean castShadow;
    public AmbientLight(int color) {
        this.color = color;
    }
}
