package com.aotem.threejs.three.renderers.gl;

import java.util.ArrayList;
import java.util.Arrays;

import com.aotem.threejs.three.buffergeometries.BoxBufferGeometry;
import com.aotem.threejs.three.buffergeometries.PlaneBufferGeometry;
import com.aotem.threejs.three.cameras.Camera;
import com.aotem.threejs.three.constant.Constants;
import com.aotem.threejs.three.core.ICallback;
import com.aotem.threejs.three.geometries.param.BoxParam;
import com.aotem.threejs.three.geometries.param.PlaneParam;
import com.aotem.threejs.three.materials.Material;
import com.aotem.threejs.three.materials.ShaderMaterial;
import com.aotem.threejs.three.math.Color;
import com.aotem.threejs.three.objects.Mesh;
import com.aotem.threejs.three.renderers.GLRenderTargetCube;
import com.aotem.threejs.three.renderers.GLRenderer;
import com.aotem.threejs.three.renderers.shaders.ShaderLib;
import com.aotem.threejs.three.scenes.Scene;
import com.aotem.threejs.three.textures.CubeTexture;
import com.aotem.threejs.three.textures.Texture;

public class GLBackground {
    Color clearColor = new Color(0x000000);
    float clearAlpha = 0;
    Mesh planeMesh = null;
    Mesh boxMesh = null;

    // Store the current background texture and its `version`
    // so we can recompile the material accordingly.
    Object currentBackground = null; // THREE.Color Texture
    int currentBackgroundVersion = 0;

    GLRenderer renderer;
    GLState state;
    GLObjects objects;
    boolean premultipliedAlpha;

    public GLBackground(GLRenderer renderer, GLState state, GLObjects objects, boolean premultipliedAlpha) {
        this.renderer = renderer;
        this.state = state;
        this.objects = objects;
        this.premultipliedAlpha = premultipliedAlpha;
    }


    public void render(GLRenderLists.List renderList, Scene scene, Camera camera, boolean forceClear) {
        Object background = scene.background;
        if (background == null) {
            setClear(clearColor, clearAlpha);
            currentBackground = null;
            currentBackgroundVersion = 0;
        }
        if (background instanceof Color) {
            forceClear = true;
            currentBackground = null;
            currentBackgroundVersion = 0;
        }
        if (renderer.autoClear || forceClear) {
            renderer.clear(renderer.autoClearColor, renderer.autoClearDepth, renderer.autoClearStencil);
        }
        if (background != null && (background instanceof CubeTexture || background instanceof GLRenderTargetCube) ) {
            if (boxMesh == null) {
                ShaderMaterial shaderMaterial = createShaderMaterial(ShaderLib.cube(),"BackgroundCubeMaterial", Constants.BackSide);
                boxMesh = new Mesh(
                        new BoxBufferGeometry(new BoxParam(1, 1, 1)),
                        new ArrayList<Material>(Arrays.asList(shaderMaterial))
                );

                boxMesh.geometry.normal = null;
                boxMesh.geometry.uv = null;

                boxMesh.onBeforeRender = new ICallback() {
                    @Override
                    public void call(GLRenderer renderer, Scene scene, Camera camera) {
                        boxMesh.setWorldMatrix(camera.getWorldMatrix());
                    }
                };
                objects.update(boxMesh);
            }
            Texture texture;
            boolean isGLRenderTargetCube = false;
            if (background instanceof  GLRenderTargetCube) {
                texture = ((GLRenderTargetCube) background).texture;
                isGLRenderTargetCube = true;
            } else {
                texture = (Texture) background;
            }
            boxMesh.material.get(0).uniforms.put("tCube", texture);
            boxMesh.material.get(0).uniforms.put("tFlip", isGLRenderTargetCube ? 1 : -1);

            if (currentBackground != background || currentBackgroundVersion != texture.version) {
                boxMesh.material.get(0).needsUpdate = true;
                currentBackground = background;
                currentBackgroundVersion = texture.version;
            }
            // push to the pre-sorted opaque render list
            renderList.unshift(boxMesh, boxMesh.geometry, boxMesh.material.get(0), 0, 0, null);
        } else if (background != null && (background instanceof  Texture) ) {
            Texture texture = (Texture) background;
            if (planeMesh == null) {
                ShaderMaterial shaderMaterial = createShaderMaterial(ShaderLib.background(),"BackgroundMaterial", Constants.FrontSide);
                planeMesh = new Mesh(
                        new PlaneBufferGeometry(new PlaneParam(2, 2)),
                        new ArrayList<Material>(Arrays.asList(shaderMaterial))
                );
                planeMesh.geometry.normal = null;

                objects.update(planeMesh);
            }
            planeMesh.material.get(0).uniforms.put("t2D", texture);
            if (texture.matrixAutoUpdate) {
                texture.updateMatrix();
            }
            planeMesh.material.get(0).uniforms.put("uvTransform", texture.getMatrix().clone());

            if (currentBackground != background ||currentBackgroundVersion != texture.version) {
                planeMesh.material.get(0).needsUpdate = true;
                currentBackground = background;
                currentBackgroundVersion = texture.version;
            }

            // push to the pre-sorted opaque render list
            renderList.unshift( planeMesh, planeMesh.geometry, planeMesh.material.get(0), 0, 0, null );
        }
    }

    private ShaderMaterial createShaderMaterial(ShaderLib shader, String type, int side) {
        ShaderMaterial shaderMaterial = new ShaderMaterial();
        shaderMaterial.type = "BackgroundCubeMaterial";
        shaderMaterial.uniforms = shader.uniforms;
        shaderMaterial.vertexShader = shader.vertexShader;
        shaderMaterial.fragmentShader = shader.fragmentShader;
        shaderMaterial.side = side;
        shaderMaterial.depthTest = false;
        shaderMaterial.depthWrite = false;
        shaderMaterial.fog = false;
        return shaderMaterial;
    }

    public void setClear(Color color, float alpha) {
        state.colorBuffer.setClear(color.r, color.g, color.b, alpha, premultipliedAlpha);
    }

    public Color getClearColor() {
        return clearColor;
    }

    public void setClearColor(Color color, float alpha) {
        clearColor = color;
        clearAlpha = alpha >= 0 ? alpha : 1;
        setClear(clearColor, alpha);
    }

    public float getClearAlpha() {
        return clearAlpha;
    }

    public void setClearAlpha(float alpha) {
        clearAlpha = alpha;
        setClear(clearColor, clearAlpha);
    }
}
