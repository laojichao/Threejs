package com.aotem.threejsdemo.renderer;

import android.opengl.GLSurfaceView;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.aotem.threejs.three.buffergeometries.BoxBufferGeometry;
import com.aotem.threejs.three.cameras.PerspectiveCamera;
import com.aotem.threejs.three.control.Screen;
import com.aotem.threejs.three.control.TrackballControls;
import com.aotem.threejs.three.core.BufferAttribute;
import com.aotem.threejs.three.core.InstancedBufferGeometry;
import com.aotem.threejs.three.core.InterleavedBuffer;
import com.aotem.threejs.three.core.InterleavedBufferAttribute;
import com.aotem.threejs.three.extras.lib.lines.LineGeometry;
import com.aotem.threejs.three.extras.lib.lines.LineMaterial;
import com.aotem.threejs.three.extras.lib.lines.LineSegments2;
import com.aotem.threejs.three.geometries.param.BoxParam;
import com.aotem.threejs.three.helpers.AxesHelper;
import com.aotem.threejs.three.loaders.TextureLoader;
import com.aotem.threejs.three.materials.MeshBasicMaterial;
import com.aotem.threejs.three.math.Color;
import com.aotem.threejs.three.math.MathTool;
import com.aotem.threejs.three.math.Matrix4;
import com.aotem.threejs.three.math.Quaternion;
import com.aotem.threejs.three.math.Vector2;
import com.aotem.threejs.three.math.Vector3;
import com.aotem.threejs.three.objects.InstancedMesh;
import com.aotem.threejs.three.objects.Mesh;
import com.aotem.threejs.three.renderers.GLRenderer;
import com.aotem.threejs.three.scenes.Scene;

public class InstancingSegments extends BaseRender {
  private String TAG = getClass().getSimpleName();
  private PerspectiveCamera camera;
  private Scene scene = new Scene();

  private static final float Z_NEAR = 1f;
  private static final float Z_FAR = 1000f;

  //
  public InstancingSegments(GLSurfaceView view) {
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
    renderer.setClearColor(0x000000, 1);

    LineMaterial lineMaterial = new LineMaterial();
    lineMaterial.setColor(0x00bb00);
    lineMaterial.setLinewidth(32);
//    lineMaterial.transparent = true;
//    lineMaterial.dashed = false;
//    lineMaterial.depthWrite = lineMaterial.depthTest = false;
    lineMaterial.setResolution(new Vector2(mWidth, mHeight));

    LineGeometry lineGeo = new LineGeometry();
    Vector3[] points = {new Vector3(-1,1,0), new Vector3(1,1,0), new Vector3(-1,-1,0), new Vector3(1,-1,0)};
    lineGeo.setPositions(MathTool.flatten(points));

    LineSegments2 lineSegments = new LineSegments2(lineGeo, lineMaterial);
    lineSegments.computeLineDistances();
    scene.add(lineSegments);

    Mesh mCube = new Mesh(new BoxBufferGeometry(new BoxParam()) );
    int[] colors = new int[] {
        0xcccc00, 0xffffff, 0x0000cc, 0x009900, 0xbb0000, 0xcc6600
    };
    for (int i = 0; i < 6; i++) {
      MeshBasicMaterial materialFace = new MeshBasicMaterial();
      materialFace.color = new Color(colors[i]);
      materialFace.transparent = true;
      materialFace.opacity = 0.5f;
      mCube.material.add(materialFace);
    }
    scene.add(mCube);

    camera = new PerspectiveCamera(50, aspect, Z_NEAR, Z_FAR);
    camera.position = new Vector3(0, 0, 6);
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
}
