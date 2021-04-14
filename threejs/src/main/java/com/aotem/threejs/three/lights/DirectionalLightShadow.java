package com.aotem.threejs.three.lights;

import com.aotem.threejs.three.cameras.OrthographicCamera;

public class DirectionalLightShadow extends LightShadow {
    public DirectionalLightShadow() {
        super(new OrthographicCamera(-5, 5, 5, -5, 0.5f, 500));
    }
}
