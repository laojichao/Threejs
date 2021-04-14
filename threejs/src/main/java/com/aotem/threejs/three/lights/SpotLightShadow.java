package com.aotem.threejs.three.lights;

import com.aotem.threejs.three.cameras.PerspectiveCamera;
import com.aotem.threejs.three.math.MathTool;

public class SpotLightShadow extends LightShadow {
    public SpotLightShadow() {
        super(new PerspectiveCamera(50, 1, 0.5f, 500));

    }

    public void update(SpotLight light) {
        double fov = MathTool.RAD2DEG * 2 * light.angle;
        double aspect = mapSize.x / mapSize.y;
        double far = light.distance;
        if (far == 0) {
            far = camera.far;
        }
        PerspectiveCamera camera1 = (PerspectiveCamera) super.camera;
        if (fov != camera1.fov || aspect != camera1.aspect || far != camera1.far) {
            camera1.fov = fov;
            camera1.aspect = aspect;
            camera.far = far;
            camera.updateProjectionMatrix();
        }
    }
}
