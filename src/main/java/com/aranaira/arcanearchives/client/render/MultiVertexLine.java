package com.aranaira.arcanearchives.client.render;

import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;

public class MultiVertexLine {
	private ArrayList<Vec3d> vertices = new ArrayList<Vec3d>();
	private Color color;
	private float durationRemaining;
	private float fadeTime;

	public MultiVertexLine (Vec3d startPos, Color color) {
		this.color = color;
		addVertex(startPos);
	}

	public void addVertex (Vec3d vert) {
		vertices.add(vert);
	}

	public void addVertex (double x, double y, double z) {
		Vec3d vert = new Vec3d(x, y, z);
		vertices.add(vert);
	}
}
