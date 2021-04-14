package com.aotem.threejs.three.lights;

import com.aotem.threejs.three.math.Color;

public class AmbientLightProbe extends LightProbe {

    public AmbientLightProbe(int color, float intensity) {
        super(color, intensity);
        Color color1 = new Color().setHex(color);
        sh.elements()[0].set(color1.r, color1.g, color1.b)
                .multiplyScalar(2 * (float) Math.sqrt(Math.PI));
    }
}
