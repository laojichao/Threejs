package com.aotem.threejs.three.objects;

import com.aotem.threejs.three.core.Face3;
import com.aotem.threejs.three.core.Object3D;
import com.aotem.threejs.three.math.Triangle;
import com.aotem.threejs.three.math.Vector2;
import com.aotem.threejs.three.math.Vector3;

public class RaycastItem {
    public double distance;
    public Vector3 point;
    public Object3D object;
    public int faceIndex;
    public Face3 face;

    public double distanceToRay;
    public int index;

    public Vector2 uv;

    public Triangle triangle;
}
