package com.aotem.threejs.three.materials;

import com.aotem.threejs.three.math.Color;

public class LineBasicMaterial extends Material {
    public String linecap = "round";
    public String linejoin = "round";

    public LineBasicMaterial() {
        color = new Color(0xffffff);
        linewidth = 1;
        lights = false;
    }
}
