package com.aotem.threejs.three.objects;

import java.util.ArrayList;

import com.aotem.threejs.three.cameras.PerspectiveCamera;
import com.aotem.threejs.three.core.BufferAttribute;
import com.aotem.threejs.three.core.BufferGeometry;
import com.aotem.threejs.three.core.InterleavedBuffer;
import com.aotem.threejs.three.core.InterleavedBufferAttribute;
import com.aotem.threejs.three.core.Object3D;
import com.aotem.threejs.three.core.Raycaster;
import com.aotem.threejs.three.materials.Material;
import com.aotem.threejs.three.materials.SpriteMaterial;
import com.aotem.threejs.three.math.Matrix4;
import com.aotem.threejs.three.math.Triangle;
import com.aotem.threejs.three.math.Vector2;
import com.aotem.threejs.three.math.Vector3;

public class Sprite extends Object3D {
    public SpriteMaterial material1;
    public Vector2 center = new Vector2(0.5f, 0.5f);
    public Vector3 globalPos = null;

    public Sprite(SpriteMaterial material) {
        geometry = new BufferGeometry();
        float[] float32Array = new float[] {
                - 0.5f, - 0.5f, 0, 0, 0,
                0.5f, - 0.5f, 0, 1, 0,
                0.5f, 0.5f, 0, 1, 1,
                - 0.5f, 0.5f, 0, 0, 1
        };
        InterleavedBuffer interleavedBuffer = new InterleavedBuffer(float32Array, 5);
        BufferAttribute index = new BufferAttribute().setArray(new int[] {
                0, 1, 2,	0, 2, 3
        });
        geometry.setIndex(index);
        geometry.position = new InterleavedBufferAttribute(interleavedBuffer, 3, 0, false);
        geometry.uv = new InterleavedBufferAttribute(interleavedBuffer, 2, 3, false);
        if (material == null) {
            this.material.add(new SpriteMaterial());
        } else {
            this.material.add(material);
        }
        material1 = (SpriteMaterial)this.material.get(0);
    }

    public void raycast(Raycaster raycaster, ArrayList<RaycastItem> intersects) {
        Vector3 intersectPoint = new Vector3();
        Vector3 worldScale = new Vector3();
        Vector3 mvPosition = new Vector3();

        Vector2 alignedPosition = new Vector2();
        Vector2 rotatedPosition = new Vector2();
        Matrix4 viewWorldMatrix = new Matrix4();

        Vector3 vA = new Vector3();
        Vector3 vB = new Vector3();
        Vector3 vC = new Vector3();

        Vector2 uvA = new Vector2();
        Vector2 uvB = new Vector2();
        Vector2 uvC = new Vector2();

        worldScale.setFromMatrixScale( matrixWorld );

        viewWorldMatrix.copy( raycaster.getCamera().getWorldMatrix() );
        this.modelViewMatrix.multiplyMatrices( raycaster.getCamera().matrixWorldInverse, matrixWorld );

        mvPosition.setFromMatrixPosition( modelViewMatrix );

        if (( raycaster.getCamera() instanceof PerspectiveCamera) && !material.get(0).sizeAttenuation ) {

            worldScale.multiplyScalar( - mvPosition.z );

        }

        float rotation = material1.rotation;
        Float sin = null, cos = null;
        if ( rotation != 0 ) {
            cos = (float) Math.cos( rotation );
            sin = (float) Math.sin( rotation );

        }

        Vector2 scale = worldScale.to();
        transformVertex( vA.set( - 0.5f, - 0.5f, 0 ), mvPosition, center, scale, sin, cos,
                alignedPosition, rotatedPosition, viewWorldMatrix );
        transformVertex( vB.set( 0.5f, - 0.5f, 0 ), mvPosition, center, scale, sin, cos,
                alignedPosition, rotatedPosition, viewWorldMatrix);
        transformVertex( vC.set( 0.5f, 0.5f, 0 ), mvPosition, center, scale, sin, cos,
                alignedPosition, rotatedPosition, viewWorldMatrix);

        uvA.set( 0, 0 );
        uvB.set( 1, 0 );
        uvC.set( 1, 1 );

        // check first triangle
        Vector3 intersect = raycaster.ray.intersectTriangle( vA, vB, vC, false, intersectPoint );

        if ( intersect == null ) {

            // check second triangle
            transformVertex( vB.set( - 0.5f, 0.5f, 0 ), mvPosition, center, scale, sin, cos,
                    alignedPosition, rotatedPosition, viewWorldMatrix);
            uvB.set( 0, 1 );

            intersect = raycaster.ray.intersectTriangle( vA, vC, vB, false, intersectPoint );
            if ( intersect == null ) {
                return;
            }

        }

        double distance = raycaster.ray.getOrigin().distanceTo( intersectPoint );

        if ( distance < raycaster.near || distance > raycaster.far ) return;

        RaycastItem item = new RaycastItem();
        item.distance = distance;
        item.point = intersectPoint.clone();
        item.uv = Triangle.getUV( intersectPoint, vA, vB, vC, uvA, uvB, uvC, new Vector2() );
        item.face = null;
        item.object = this;
        intersects.add(item);
    }

    private void transformVertex(Vector3 vertexPosition, Vector3 mvPosition, Vector2 center, Vector2 scale, Float sin,
                                 Float cos, Vector2 alignedPosition, Vector2 rotatedPosition, Matrix4 viewWorldMatrix) {
        // compute position in camera space
        Vector2 vPos = vertexPosition.to();
        alignedPosition.subVectors( vPos, center ).addScalar( 0.5f ).multiply( scale );

        // to check if rotation is not zero
        if ( sin != null ) {

            rotatedPosition.x = ( cos * alignedPosition.x ) - ( sin * alignedPosition.y );
            rotatedPosition.y = ( sin * alignedPosition.x ) + ( cos * alignedPosition.y );

        } else {

            rotatedPosition.copy( alignedPosition );

        }


        vertexPosition.copy( mvPosition );
        vertexPosition.x += rotatedPosition.x;
        vertexPosition.y += rotatedPosition.y;

        // transform to world space
        vertexPosition.applyMatrix4( viewWorldMatrix );
    }

    public Sprite copy(Sprite source) {
        super.copy(source);
        if (source.center != null) {
            center.copy(source.center);
        }
        return this;
    }

    public Sprite clone() {
        return new Sprite(material1).copy(this);
    }
}
