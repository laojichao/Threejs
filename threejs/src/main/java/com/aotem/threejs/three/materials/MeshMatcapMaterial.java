package com.aotem.threejs.three.materials;

import com.aotem.threejs.three.math.Color;
import com.aotem.threejs.three.math.Vector2;
import com.aotem.threejs.three.math.Vector4;
import com.aotem.threejs.three.textures.Texture;

import static com.aotem.threejs.three.constant.Constants.TangentSpaceNormalMap;

public class MeshMatcapMaterial extends Material {
    public MeshMatcapMaterial() {
        normalMapType = TangentSpaceNormalMap;
        color = new Color().setHex(0xffffff);// diffuse
        lights = false;
    }
    //matcap`
//    public Texture map = null;

    //bumpMap
    public float bumpScale = 1;

    //normalMap
    public Vector2 normalScale = new Vector2(1, 1);

    //displacementMap
    public float displacementScale = 1;
    public float displacementBias = 0;

//    public Texture alphaMap = null;

    //skinning
    //morphTargets
    //morphNormals

//    public boolean lights = false;
}
