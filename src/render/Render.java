package render;

import main.Ray;

public class Render {
	public static float[] getReflectionLocation(Ray ray, float z) {
		float[] reflectedLocation = {ray.x * z, ray.y * z, z};
		return reflectedLocation;
	}
}
