package main;
import java.util.ArrayList;
import bodies.*;

public class Ray {
	public int x, y;// (0, 0) is the center of the screen
	public int pixelX, pixelY;

	private static final int MIN_BRIGHTNESS = 20;
	private static final float MAX_RENDER_DEPTH = 1000f;

	public Ray(int x, int y) {
		this.x = x - Main.Width / 2;
		this.y = y - Main.Height / 2;
		this.pixelX = x;
		this.pixelY = y;
	}
	

	public int getColor(ArrayList<Body> bodies) {
		float z = Float.POSITIVE_INFINITY;
		Body bodyHit = null;

		// Find closest body
		for (Body body : bodies) {
			float tempZ = body.getZ(this);
			if (tempZ < z) {
				z = tempZ;
				bodyHit = body;
			}
		}

		if (bodyHit == null) return 0;

		float[] reflectionDir = normalize(bodyHit.getReflectionDirection(this));
		float[] normal = bodyHit.getNormalAt(this, z);
		float[] reflectionLoc = bodyHit.getReflectionLocation(this, z);
		float[] toSun = {
			Main.instance.sun.x - reflectionLoc[0],
			Main.instance.sun.y - reflectionLoc[1],
			Main.instance.sun.z - reflectionLoc[2]
		};
		float  distToSun = magnitude(toSun);
		toSun = normalize(toSun);
		float cos = dot(normal, toSun);
		cos = Math.max(0, cos); // Prevent negative brightness

		int brightness = Math.max(MIN_BRIGHTNESS, (int)(255 * cos));
		return Main.instance.color(brightness);
	}

	private float magnitude(float[] v) {
		return (float) Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
	}

	private float[] normalize(float[] v) {
		float mag = magnitude(v);
		return mag == 0 ? new float[]{0, 0, 0} : new float[]{v[0]/mag, v[1]/mag, v[2]/mag};
	}

	private float dot(float[] a, float[] b) {
		return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
	}
}
