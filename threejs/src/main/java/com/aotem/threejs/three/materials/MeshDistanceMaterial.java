package com.aotem.threejs.three.materials;

import com.aotem.threejs.three.math.Vector3;
import com.aotem.threejs.three.textures.Texture;

public class MeshDistanceMaterial extends Material {
    //skinning
    public Vector3 referencePosition = new Vector3();
    public double nearDistance = 1f;
    public double farDistance = 1000f;

    //morphTargets

//    public Texture map = null;

//    public Texture alphaMap = null;

    //displacementMap
    public double displacementScale = 1;
    public double displacementBias = 0;

//    public boolean fog = false;
//    public boolean lights = false;

    public MeshDistanceMaterial() {
        fog = false;
        lights = false;
    }
}
