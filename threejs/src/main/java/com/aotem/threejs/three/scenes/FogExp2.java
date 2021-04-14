package com.aotem.threejs.three.scenes;

import com.aotem.threejs.three.math.Color;

public class FogExp2 extends Fog {
    public float density = 0.00025f;

    public FogExp2(Color color, float density) {
        this.color = color;
        this.density = density;
    }

    public FogExp2 clone() {
        return new FogExp2(color, density);
    }
}
