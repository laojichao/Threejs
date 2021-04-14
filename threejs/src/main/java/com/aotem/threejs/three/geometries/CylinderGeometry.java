package com.aotem.threejs.three.geometries;

import com.aotem.threejs.three.buffergeometries.CylinderBufferGeometry;
import com.aotem.threejs.three.core.Geometry;
import com.aotem.threejs.three.geometries.param.CylinderParam;

public class CylinderGeometry extends Geometry {

    public CylinderGeometry(CylinderParam param) {
        fromBufferGeometry(new CylinderBufferGeometry(param));
    }
}
