package com.aotem.threejs.three.lights;

import com.aotem.threejs.three.math.SphericalHarmonics3;

public class LightProbe extends Light {
    public SphericalHarmonics3 sh = new SphericalHarmonics3();

    public LightProbe() {}
    public LightProbe(int color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }
    public LightProbe copy(LightProbe source) {
        super.copy(source);
        sh.copy(source.sh);
        return this;
    }
}
