package render;
import bodies.*;

import main.Main;
import main.Ray;

public class SphereRender{
	public static float getZ(Sphere sphere, Ray ray) {
	    float fov = Main.FOV;
	    int width = Main.Width;
	    int height = Main.Height;

	    // Convert screen (x, y) to direction vector in view space
	    float aspectRatio = (float) width / height;
	    float px = (float) ray.x / width * 2 * (float)Math.tan(fov / 2) * aspectRatio;
	    float py = (float) ray.y / height * 2 * (float)Math.tan(fov / 2);
	    float pz = 1;

	    // Normalize direction
	    float len = (float)Math.sqrt(px*px + py*py + pz*pz);
	    float dx = px / len;
	    float dy = py / len;
	    float dz = pz / len;

	    // Ray origin is camera at (0, 0, 0)
	    float ox = 0, oy = 0, oz = 0;
	    float cx = sphere.centerOfMass[0], cy = sphere.centerOfMass[1], cz = sphere.centerOfMass[2];

	    float ocx = ox - cx;
	    float ocy = oy - cy;
	    float ocz = oz - cz;

	    float a = dx*dx + dy*dy + dz*dz;
	    float b = 2 * (dx*ocx + dy*ocy + dz*ocz);
	    float c_term = ocx*ocx + ocy*ocy + ocz*ocz - sphere.r*sphere.r;

	    float discriminant = b*b - 4*a*c_term;
	    if (discriminant < 0) return Float.POSITIVE_INFINITY;

	    float sqrtDisc = (float)Math.sqrt(discriminant);
	    float t1 = (-b - sqrtDisc) / (2*a);
	    float t2 = (-b + sqrtDisc) / (2*a);

	    float t = (t1 > 0) ? t1 : ((t2 > 0) ? t2 : Float.POSITIVE_INFINITY);
	    return t;
	}
	
	public static float[] getReflectionDirection(Sphere sphere, Ray ray) {
	    float fov = Main.FOV;
	    int width = Main.Width;
	    int height = Main.Height;

	    float aspectRatio = (float) width / height;
	    float px = (float) ray.x / width * 2 * (float)Math.tan(fov / 2) * aspectRatio;
	    float py = (float) ray.y / height * 2 * (float)Math.tan(fov / 2);
	    float pz = 1;

	    float len = (float)Math.sqrt(px*px + py*py + pz*pz);
	    float dx = px / len;
	    float dy = py / len;
	    float dz = pz / len;

	    // Find intersection
	    float[] c = sphere.centerOfMass;
	    float ocx = -c[0];
	    float ocy = -c[1];
	    float ocz = -c[2];

	    float a = dx*dx + dy*dy + dz*dz;
	    float b = 2 * (dx*ocx + dy*ocy + dz*ocz);
	    float c_term = ocx*ocx + ocy*ocy + ocz*ocz - sphere.r*sphere.r;

	    float discriminant = b*b - 4*a*c_term;
	    if (discriminant < 0) return null;

	    float sqrtDisc = (float)Math.sqrt(discriminant);
	    float t1 = (-b - sqrtDisc) / (2*a);
	    float t2 = (-b + sqrtDisc) / (2*a);
	    float t = (t1 > 0) ? t1 : ((t2 > 0) ? t2 : -1);
	    if (t < 0) return null;

	    // Intersection point
	    float ix = dx * t;
	    float iy = dy * t;
	    float iz = dz * t;

	    // Surface normal at intersection (point - center)
	    float nx = ix - c[0];
	    float ny = iy - c[1];
	    float nz = iz - c[2];
	    float nLen = (float)Math.sqrt(nx*nx + ny*ny + nz*nz);
	    nx /= nLen; ny /= nLen; nz /= nLen;

	    // Reflect D around N: R = D - 2*(D·N)*N
	    float dot = dx*nx + dy*ny + dz*nz;
	    float rx = dx - 2 * dot * nx;
	    float ry = dy - 2 * dot * ny;
	    float rz = dz - 2 * dot * nz;

	    return new float[] { rx, ry, rz };
	}
	
	public static float[] getNormalAt(Sphere sphere, Ray ray, float t) {
	    float fov = Main.FOV;
	    int width = Main.Width;
	    int height = Main.Height;

	    float aspectRatio = (float) width / height;
	    float px = (float) ray.x / width * 2 * (float)Math.tan(fov / 2) * aspectRatio;
	    float py = (float) ray.y / height * 2 * (float)Math.tan(fov / 2);
	    float pz = 1;

	    float len = (float)Math.sqrt(px*px + py*py + pz*pz);
	    float dx = px / len;
	    float dy = py / len;
	    float dz = pz / len;

	    // Intersection point
	    float ix = dx * t;
	    float iy = dy * t;
	    float iz = dz * t;

	    // Normal = (intersection - center)
	    float nx = ix - sphere.centerOfMass[0];
	    float ny = iy - sphere.centerOfMass[1];
	    float nz = iz - sphere.centerOfMass[2];

	    float nLen = (float)Math.sqrt(nx*nx + ny*ny + nz*nz);
	    return new float[] { nx / nLen, ny / nLen, nz / nLen };
	}

	
	public static float[] getReflectionLocation(Ray ray, float z) {
		float[] reflectedLocation = {ray.x * z, ray.y * z, z};
		return reflectedLocation;
	}
}
