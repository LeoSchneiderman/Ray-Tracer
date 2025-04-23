package render;
import bodies.*;

import main.Ray;
import myMath.Vector;

public class RectangleRender {

    // Calculates the Z value where the ray intersects the rectangle (flat on the Z plane)
    public static float getZ(Rectangle rectangle, Ray ray) {
        // Extract direction and origin of the ray
        float[] direction = ray.direction;
        float dx = direction[0];
        float dy = direction[1];
        float dz = direction[2];
        
        // The ray origin is the camera at (0, 0, 0)
        float[] origin = ray.origin;
        float ox = origin[0], oy = origin[1], oz = origin[2];

        // Rectangle's plane equation (assuming it's flat on the Z plane at y = rectangle.y)
        // Rectangle's normal points along the Z-axis
        float[] planeNormal = {0, 1, 0};  // Normal pointing along the Y-axis (rectangle lies flat on the Z plane)
        float d = rectangle.centerOfMass[1];  // Y-coordinate of the rectangle's plane

        // Calculate the t value (intersection) using the plane equation
        float t = (d - oy) / dy;
        if (t <= 0) {
            return Float.POSITIVE_INFINITY;  // No intersection, ray doesn't hit the rectangle
        }

        // Calculate the intersection point and check if it lies within the rectangle
        float xHit = ox + t * dx;
        float zHit = oz + t * dz;

        // Assuming the rectangle has width and height and is centered around the origin
        float width = rectangle.width;
        float height = rectangle.height;

        // Check if the intersection point lies within the rectangle bounds
        if (xHit >= -width / 2 && xHit <= width / 2 && zHit >= -height / 2 && zHit <= height / 2) {
            return t;
        } else {
            return Float.POSITIVE_INFINITY;  // Intersection is outside the rectangle
        }
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
