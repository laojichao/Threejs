package com.aotem.threejs.three.core;

import com.aotem.threejs.three.cameras.Camera;
import com.aotem.threejs.three.renderers.GLRenderer;
import com.aotem.threejs.three.scenes.Scene;

public interface ICallback {
    void call(GLRenderer renderer, Scene scene, Camera camera);
}
