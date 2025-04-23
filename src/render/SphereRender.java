package render;
import bodies.*;

import main.Ray;
import myMath.Vector;

public class SphereRender{
	public static float getZ(Sphere sphere, Ray ray) {
	    float[] direction = ray.direction;
	    float dx = direction[0];
	    float dy = direction[1];
	    float dz = direction[2];

	    // Ray origin is camera at (0, 0, 0)
	    float[] origin = ray.origin;
	    float ox = origin[0], oy = origin[1], oz = origin[2];
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
	    float z1 = (-b - sqrtDisc) / (2*a);
	    float z2 = (-b + sqrtDisc) / (2*a);

	    float z = (z1 > 0) ? z1 : ((z2 > 0) ? z2 : Float.POSITIVE_INFINITY);
	    return z;
	}
	
    public static float[] getNormalAt(Sphere sphere, Ray ray, float z) {
        // 1. Compute intersection point
        float[] hitPoint = Render.getPoint(ray, z);

        // 2. Compute vector from center of sphere to hit point
        float[] normal = new float[] {
            hitPoint[0] - sphere.centerOfMass[0],
            hitPoint[1] - sphere.centerOfMass[1],
            hitPoint[2] - sphere.centerOfMass[2]
        };

        // 3. Normalize the normal vector
        return Vector.normalize(normal);
    }
    
    public static float[] getReflectionDirection(Sphere sphere, Ray ray, float[] normal, float z) {

        // 3. Calculate the reflection direction using the formula
        float dotProduct = Vector.dot(ray.direction, normal);
        
        // Reflected direction: incident - 2 * (incident . normal) * normal
        float[] reflection = new float[] {
            ray.direction[0] - 2 * dotProduct * normal[0],
            ray.direction[1] - 2 * dotProduct * normal[1],
            ray.direction[2] - 2 * dotProduct * normal[2]
        };

        // Return the reflected direction
        return Vector.normalize(reflection);
    }
    
    
}
