package main;
import java.util.ArrayList;
import bodies.*;
import myMath.*;
import render.Render;

public class Ray {
    private static final int SKY_COLOR = color(0, 0, 255);
    private static final int AMBIENT_COLOR = color(40, 40, 40);  // soft gray ambient light

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
        Body bodyHit = getClosestBody(bodies);
        if (bodyHit == null) return SKY_COLOR;

        float z = bodyHit.getZ(this);
        float[] hitPoint = Render.getPoint(this, z);
        float[] normal = bodyHit.getNormalAt(this, z);

        if (bodyHit.texture != null && bodyHit.texture.type == 0) {
            float[] reflectionDirection = bodyHit.getReflectionDirection(this, normal, z);
            Ray newRay = new Ray(hitPoint, reflectionDirection);
            return newRay.getColor(bodies, numLeft - 1);
        }

        return getDiffuseColor(hitPoint, normal, bodies, numLeft);
    }

    private int getDiffuseColor(float[] hitPoint, float[] normal, ArrayList<Body> bodies, int numLeft) {
        if (numLeft <= 0) return AMBIENT_COLOR;

        // Generate a random direction biased toward the normal (diffuse bounce)
        float[] rand = new float[3];
        for (int i = 0; i < 3; i++) {
            rand[i] = (float)(Math.random() * 2 - 1); // random between -1 and 1
        }

        float[] bounce = Vector.normalize(new float[] {
            normal[0] + rand[0],
            normal[1] + rand[1],
            normal[2] + rand[2]
        });

        Ray newRay = new Ray(hitPoint, bounce);
        int reflectedColor = newRay.getColor(bodies, numLeft - 1);

        if (reflectedColor == SKY_COLOR) {
            return blendWithLight(hitPoint, normal);
        }

        return blendWithDiffuseAndAmbient(reflectedColor);
    }

    private Body getClosestBody(ArrayList<Body> bodies) {
        Body closestBody = null;
        float closestZ = Float.POSITIVE_INFINITY;

        for (Body body : bodies) {
            float z = body.getZ(this);
            if (z < closestZ && z > 0.001f) {
                closestZ = z;
                closestBody = body;
            }
        }
        return closestBody;
    }

    public int getLineLightColor(float[] point, float[] normal) {
        LightSource sun = Main.instance.sun;
        float[] lineLight = Vector.normalize(Vector.subtract(point, sun.centerOfMass));
        normal = Vector.normalize(normal);

        float cos = Math.max(0, Vector.dot(normal, lineLight));
        int brightness = (int)(255 * cos);
        return color(brightness, brightness, brightness);
    }

    private int blendWithLight(float[] point, float[] normal) {
        int lightColor = getLineLightColor(point, normal);
        return lerpColor(lightColor, AMBIENT_COLOR, 0.5f);
    }

    private int blendWithDiffuseAndAmbient(int color) {
        int darkened = lerpColor(color, color(0, 0, 0), 0.3f);
        return lerpColor(darkened, AMBIENT_COLOR, 0.5f);
    }

    private int lerpColor(int a, int b, float t) {
        return Main.instance.lerpColor(a, b, t);
    }

    private static int color(int r, int g, int b) {
        return Main.instance.color(r, g, b);
    }
}
