package com.aotem.threejsdemo.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.aotem.threejs.three.buffergeometries.PlaneBufferGeometry;
import com.aotem.threejs.three.buffergeometries.TubeBufferGeometry;
import com.aotem.threejs.three.cameras.PerspectiveCamera;
import com.aotem.threejs.three.control.Screen;
import com.aotem.threejs.three.control.TrackballControls;
import com.aotem.threejs.three.extras.core.Curve;
import com.aotem.threejs.three.extras.core.Path3;
import com.aotem.threejs.three.geometries.param.PlaneParam;
import com.aotem.threejs.three.helpers.AxesHelper;
import com.aotem.threejs.three.materials.MeshBasicMaterial;
import com.aotem.threejs.three.materials.SpriteMaterial;
import com.aotem.threejs.three.math.MathTool;
import com.aotem.threejs.three.math.Vector3;
import com.aotem.threejs.three.objects.Mesh;
import com.aotem.threejs.three.objects.Sprite;
import com.aotem.threejs.three.renderers.GLRenderer;
import com.aotem.threejs.three.scenes.Scene;
import com.aotem.threejs.three.textures.Texture;

public class TestTubeGeometry extends BaseRender {
  private PerspectiveCamera camera;
  private Scene scene = new Scene();
  API api = new API();

  private static final float Z_NEAR = 0.1f;
  private static final float Z_FAR = 1000f;


  //
  public TestTubeGeometry(GLSurfaceView view) {
    super(view);
  }


  ///
  // Initialize the shader and program object
  //
  public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
    mWidth = mView.getWidth();
    mHeight = mView.getHeight();
    aspect = (float) mWidth / mHeight;

    scene = new Scene();
    GLRenderer.Param param = new GLRenderer.Param();
    param.antialias = true;
    renderer = new GLRenderer(param, mWidth, mHeight);
    renderer.setClearColor(0xf4f4f4, 1);

    Path3 path = new Path3(MathTool.toArrayList(new Vector3[] {
        new Vector3(-5,0,0), new Vector3(-5,5,0),
        new Vector3(0,5,0), new Vector3(0,0,0),
        new Vector3(5,0,0), new Vector3(5,5,0)
    }));
    TubeBufferGeometry tubeGeo = new TubeBufferGeometry(path, api.segments, api.radius, api.radialSegments,false);
    MeshBasicMaterial tubeMat = new MeshBasicMaterial(0xff00bb00);
    tubeMat.wireframe = true;
    Mesh tubeMesh = new Mesh(tubeGeo, tubeMat);
    scene.add(tubeMesh);

    PlaneBufferGeometry planeGeo = new PlaneBufferGeometry(new PlaneParam(5, 5, 2,2));
    MeshBasicMaterial basicMaterial = new MeshBasicMaterial(0xffbb5cdd);
    basicMaterial.wireframe = true;
    Mesh planeMesh = new Mesh(planeGeo, basicMaterial);
    planeMesh.rotateX(-MathTool.PI/2);
    planeMesh.position.y = -2;
    scene.add(planeMesh);

    camera = new PerspectiveCamera(45, aspect, Z_NEAR, Z_FAR);
    camera.position = new Vector3(0, 0, 30);
    camera.lookAt(scene.position);
    controls = new TrackballControls(new Screen(0, 0, mWidth, mHeight), camera);

    AxesHelper axesHelper = new AxesHelper(20);
    scene.add(axesHelper);
  }

  // /
  // Draw a triangle using the shader pair created in onSurfaceCreated()
  //
  public void onDrawFrame(GL10 glUnused) {
    renderer.render(scene, camera);
    ((TrackballControls) controls).update();
  }

  public void onSurfaceChanged(GL10 glUnused, int width, int height) {
    mWidth = width;
    mHeight = height;
    aspect = (float) mWidth / mHeight;
    camera.aspect = aspect;
    camera.updateProjectionMatrix();
    renderer.setSize(mWidth, mHeight);
  }

  class SinCurve extends Curve {
    float scale = 1;

    public SinCurve(float scale) {
      this.scale = scale;
    }

    public Vector3 getPoint(double t) {
      double tx = t * 3 - 1.5;
      double ty = Math.sin(2*Math.PI*t);
      return new Vector3(tx, ty, 0).multiplyScalar(scale);
    }
  }

  class API {
    int segments = 20;
    int radialSegments = 8;
    int repeatX = 9;
    int repeatY = 2;
    float radius = 0.15f;
    float rotation = 0;
    float centerX = 0.5f;
    float centerY = 0.5f;
  }
}
