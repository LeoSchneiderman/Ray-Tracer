package render;
import bodies.*;

import main.Ray;
import myMath.Vector;

public class RectangleRender {

    // Calculates the Z value where the ray intersects the rectangle (flat on the Z plane)
	// Calculates the Z value where the ray intersects the rectangle
	public static float getZ(Rectangle rectangle, Ray ray) {
	    // Extract direction and origin of the ray
	    float[] direction = ray.direction;
	    float dx = direction[0];
	    float dy = direction[1];
	    float dz = direction[2];

	    // The ray origin is the camera at (0, 0, 0)
	    float[] origin = ray.origin;
	    float ox = origin[0], oy = origin[1], oz = origin[2];

	    // Rectangle's normal and the equation of the plane
	    float[] normal = rectangle.normal; // Normal from the Rectangle class
	    float[] center = rectangle.centerOfMass; // Center of the rectangle
	    float width = rectangle.width; // Width of the rectangle
	    float height = rectangle.height; // Height of the rectangle

	    // Calculate the denominator for the intersection formula (dot product of normal and ray direction)
	    float denominator = normal[0] * dx + normal[1] * dy + normal[2] * dz;

	    // If the ray is parallel to the plane, there's no intersection
	    if (Math.abs(denominator) < 1e-6) {
	        return Float.NaN; // No intersection
	    }

	    // Calculate the vector from the ray origin to the rectangle's center
	    float[] centerToOrigin = new float[3];
	    centerToOrigin[0] = center[0] - ox;
	    centerToOrigin[1] = center[1] - oy;
	    centerToOrigin[2] = center[2] - oz;

	    // Calculate the t value for the intersection point (distance along the ray)
	    float t = (centerToOrigin[0] * normal[0] + centerToOrigin[1] * normal[1] + centerToOrigin[2] * normal[2]) / denominator;

	    // If t is negative, the intersection is behind the ray's origin
	    if (t < 0) {
	        return Float.NaN; // No intersection
	    }

	    // Calculate the intersection point
	    float ix = ox + t * dx;
	    float iy = oy + t * dy;
	    float iz = oz + t * dz;

	    // Derive the rectangle's local axes (x and y)
	    // Choose any vector not parallel to the normal to create the x-axis
	    float[] arbitraryVector = new float[] {1.0f, 0.0f, 0.0f};
	    if (Math.abs(normal[0]) > 0.9) {
	        arbitraryVector = new float[] {0.0f, 1.0f, 0.0f}; // If normal is close to (1, 0, 0), choose (0, 1, 0)
	    }

	    // Compute the rectangle's x-axis using the cross product of the normal and the arbitrary vector
	    float[] xAxis = crossProduct(normal, arbitraryVector);
	    // Recompute the y-axis as the cross product of the normal and xAxis
	    float[] yAxis = crossProduct(normal, xAxis);

	    // Normalize the axes
	    normalize(xAxis);
	    normalize(yAxis);

	    // Check if the intersection point lies within the bounds of the rectangle
	    float halfWidth = width / 2;
	    float halfHeight = height / 2;

	    // Vector from rectangle's center to the intersection point
	    float[] toIntersection = new float[3];
	    toIntersection[0] = ix - center[0];
	    toIntersection[1] = iy - center[1];
	    toIntersection[2] = iz - center[2];

	    // Project the point onto the rectangle's local space (using the rectangle's axes)
	    float dotX = toIntersection[0] * xAxis[0] + toIntersection[1] * xAxis[1] + toIntersection[2] * xAxis[2];
	    float dotY = toIntersection[0] * yAxis[0] + toIntersection[1] * yAxis[1] + toIntersection[2] * yAxis[2];

	    // Check if the point lies within the rectangle's bounds
	    if (Math.abs(dotX) <= halfWidth && Math.abs(dotY) <= halfHeight) {
	        return iz; // Return the z-coordinate of the intersection point
	    }

	    return Float.NaN; // No intersection within bounds
	}

	// Helper method for the cross product
	private static float[] crossProduct(float[] v1, float[] v2) {
	    return new float[] {
	        v1[1] * v2[2] - v1[2] * v2[1],
	        v1[2] * v2[0] - v1[0] * v2[2],
	        v1[0] * v2[1] - v1[1] * v2[0]
	    };
	}

	// Helper method to normalize a vector
	private static void normalize(float[] v) {
	    float length = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
	    v[0] /= length;
	    v[1] /= length;
	    v[2] /= length;
	}




    // Calculates the reflection direction when the ray hits the rectangle
    public static float[] getReflectionDirection(Rectangle rectangle, Ray ray, float[] normal, float z) {
        // Calculate reflection direction using the formula
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
