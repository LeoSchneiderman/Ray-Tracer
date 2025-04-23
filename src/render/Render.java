package render;

import main.Ray;

public class Render {
	public static float[] getPoint(Ray ray, float z) {
	    return new float[] {
	        ray.origin[0] + z * ray.direction[0],
	        ray.origin[1] + z * ray.direction[1],
	        ray.origin[2] + z * ray.direction[2]
	    };
	}
}
