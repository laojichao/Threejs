package com.aotem.threejs.three.geometries;

import com.aotem.threejs.three.buffergeometries.BoxBufferGeometry;
import com.aotem.threejs.three.core.Geometry;
import com.aotem.threejs.three.geometries.param.BoxParam;

public class BoxGeometry extends Geometry {
    public BoxGeometry(BoxParam param) {
        fromBufferGeometry(new BoxBufferGeometry(param));
    }
}
