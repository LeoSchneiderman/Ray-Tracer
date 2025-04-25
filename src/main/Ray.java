package main;

import java.util.ArrayList;
import bodies.*;
import myMath.*;
import textures.*;
import render.Render;

public class Ray {
    private static final float[] SKY_HORIZON = {255, 255, 255};  // Brighter horizon
    private static final float[] SKY_ZENITH = {100, 150, 255};  // Brighter zenith
    private static final float[] AMBIANT = {40, 40, 40};

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

    public float[] getEnergy(ArrayList<Body> bodies, int numLeft) {
        Body sun = Main.instance.sun;
        if (numLeft == 0) return AMBIANT;

        float[] energy = {0, 0, 0};
        Body body = getClosestBody(bodies);
        if (body == null) {
        	if(sun != null) {
        		float[] fromSun = new float[] {sun.centerOfMass[0] - origin[0],sun.centerOfMass[1] - origin[1],sun.centerOfMass[2] - origin[2]};
        		float sunCos = Vector.dot(Vector.normalize(direction), Vector.normalize(fromSun));
        		float t = 0.5f * (sunCos + 1.0f); // remap from [-1,1] to [0,1]

        		for (int i = 0; i < 3; i++) {
        			energy[i] += SKY_HORIZON[i] * (1 - t) + SKY_ZENITH[i] * t;
        		}
        	}
        } else {
            Texture texture = body.texture;
            for (int i = 0; i < 3; i++) {
                energy[i] += texture.luminocity[i];
            }

            float z = body.getZ(this);
            float[] hitPoint = Render.getPoint(this, z);
            float[] normal = body.getNormalAt(this, z);
            float[] perfectReflection = body.getReflectionDirection(this, normal, z);

            float roughness = texture.roughness;
            float[] reflection;
            if (roughness == 0) {
                reflection = perfectReflection;
            } else {
                reflection = sampleHemisphere(perfectReflection, roughness);
            }

            Ray newRay = new Ray(hitPoint, reflection);
            float[] newRayEnergy = newRay.getEnergy(bodies, numLeft - 1);

            for (int i = 0; i < 3; i++) {
                energy[i] += newRayEnergy[i] * texture.reflectance[i];
            }
        }

        return energy;
    }

    private Body getClosestBody(ArrayList<Body> bodies) {
        Body closest = null;
        float closestZ = Float.POSITIVE_INFINITY;

        for (Body b : bodies) {
            float z = b.getZ(this);
            if (z > 0.1f && z < closestZ) {
                closest = b;
                closestZ = z;
            }
        }
        return closest;
    }

    private float[] sampleHemisphere(float[] direction, float roughness) {
        float u1 = (float)Math.random();
        float u2 = (float)Math.random();

        float r = (float)Math.sqrt(1.0 - u1 * u1);
        float phi = 2.0f * (float)Math.PI * u2;

        float x = r * (float)Math.cos(phi);
        float y = r * (float)Math.sin(phi);
        float z = u1;

        float[] w = Vector.normalize(direction);
        float[] u = Vector.normalize(Vector.cross(w, new float[]{0.0072f, 1.0f, 0.0034f}));
        float[] v = Vector.cross(w, u);

        float[] sample = new float[3];
        for (int i = 0; i < 3; i++) {
            sample[i] = u[i] * x + v[i] * y + w[i] * z;
        }

        for (int i = 0; i < 3; i++) {
            sample[i] = direction[i] * (1 - roughness) + sample[i] * roughness;
        }

        return Vector.normalize(sample);
    }
}
