package com.aotem.threejs.three.extras.lib.lines;

import com.aotem.threejs.three.core.BufferAttribute;
import com.aotem.threejs.three.core.BufferGeometry;
import com.aotem.threejs.three.core.InstancedInterleavedBuffer;
import com.aotem.threejs.three.core.InterleavedBufferAttribute;
import com.aotem.threejs.three.materials.Material;
import com.aotem.threejs.three.math.Vector3;
import com.aotem.threejs.three.objects.Mesh;

public class LineSegments2 extends Mesh {

  Vector3 start = new Vector3();
  Vector3 end = new Vector3();
  public LineSegments2(BufferGeometry geometry, Material material) {
    super(geometry, material);
  }

  public LineSegments2 computeLineDistances() {
    BufferAttribute instanceStart = geometry.getAttribute("instanceStart");
    BufferAttribute instanceEnd = geometry.getAttribute("instanceEnd");
    float[] lineDistances = new float[2 * instanceStart.getCount()];
    for (int i = 0, j = 0, l = instanceStart.getCount(); i < l; i++, j+=2) {
      start.fromBufferAttribute(instanceStart, i);
      end.fromBufferAttribute(instanceEnd, i);
      lineDistances[j] = j == 0 ? 0 : lineDistances[j - 1];
      lineDistances[j + 1] = lineDistances[j] + (float)start.distanceTo(end);
    }
    InstancedInterleavedBuffer instanceDistanceBuffer = new InstancedInterleavedBuffer(lineDistances, 2, 1);
    geometry.addAttribute("instanceDistanceStart", new InterleavedBufferAttribute(instanceDistanceBuffer, 1, 0));
    geometry.addAttribute("instanceDistanceEnd", new InterleavedBufferAttribute(instanceDistanceBuffer, 1, 1));
    return this;
  }
}
