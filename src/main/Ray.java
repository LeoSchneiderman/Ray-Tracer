package main;
import java.util.ArrayList;
import bodies.*;
import myMath.*;
import render.Render;

public class Ray {
	private int color(int r, int g, int b) {
		return Main.instance.color(r, g, b);
	}
	private int lerpColor(int a, int b, float t) {
		return Main.instance.lerpColor(a, b, t);
	}

	public float[] direction;
	public float[] origin;

	public Ray(float[] direction) {
		this.direction = Vector.normalize(direction);
		this.origin = new float[] {0, 0, 0};
	}

	public Ray(float[] origin, float[] direction) {
		this(Vector.normalize(direction));
		this.origin = origin;
	}

	public int getColor(ArrayList<Body> bodies, int numLeft) {
		int skyColor = color(0, 0, 255);
		int ambientColor = color(40, 40, 40);  // soft gray ambient light

		Body bodyHit = null;
		float closestZ = Float.POSITIVE_INFINITY;

		for (Body body : bodies) {
			float z = body.getZ(this);
			if (z < closestZ && z > 0.001f) {
				closestZ = z;
				bodyHit = body;
			}
		}

		if (bodyHit == null) return skyColor;

		float[] normal = bodyHit.getNormalAt(this, closestZ);
		float[] reflectionDirection = bodyHit.getReflectionDirection(this, normal, closestZ);
		float[] newOrigin = Render.getPoint(this, closestZ);
		Ray newRay = new Ray(newOrigin, reflectionDirection);

		if (numLeft <= 0) return ambientColor;

		int newColor = newRay.getColor(bodies, numLeft - 1);
		if (newColor == skyColor) {
			int lightColor = getLineLightColor(newOrigin, normal);
			// Blend ambient + directional light
			return lerpColor(lightColor, ambientColor, 0.5f);
		}

		// Mix reflected color with black, and also blend in ambient light
		int darkened = lerpColor(newColor, color(0, 0, 0), 0.5f);
		return lerpColor(darkened, ambientColor, 0.3f);
	}

	public int getLineLightColor(float[] point, float[] normal) {
		LightSource sun = Main.instance.sun;
		float[] lineLight = new float[] {
			point[0] - sun.centerOfMass[0],
			point[1] - sun.centerOfMass[1],
			point[2] - sun.centerOfMass[2]
		};
		lineLight = Vector.normalize(lineLight);
		normal = Vector.normalize(normal);
		float cos = Math.max(0, Vector.dot(normal, lineLight));
		int brightness = (int)(255 * cos);
		return color(brightness, brightness, brightness);
	}
}
