package com.aotem.threejs.three.lights;

import com.aotem.threejs.three.cameras.Camera;
import com.aotem.threejs.three.math.Matrix4;
import com.aotem.threejs.three.math.Vector2;
import com.aotem.threejs.three.math.Vector3;
import com.aotem.threejs.three.renderers.GLRenderTarget;

public class LightShadow {
    public Camera camera;
    public float bias = 0;
    public float radius = 1;
    public GLRenderTarget map;
    public Vector2 mapSize = new Vector2(512, 512);
    public Matrix4 matrix = new Matrix4();

    public LightShadow() {}
    public LightShadow(Camera camera) {
        this.camera = camera;
    }

    public LightShadow copy(LightShadow source) {
        camera = (Camera) source.camera.clone();
        bias = source.bias;
        radius = source.radius;
        mapSize.copy(source.mapSize);
        return this;
    }

    public LightShadow clone() {
        return new LightShadow().copy(this);
    }
}
