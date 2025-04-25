package main;
import processing.core.PApplet;
import bodies.*;
import textures.*;
import java.util.ArrayList;

public class Main extends PApplet {

    public void settings() {
        size(500, 500);
        instance = this;
        Width = width;
        Height = height;
        FOV = (float)Math.PI / 2;
    }

    public void setup() {
    	cameraOrigin = new float[]{0, 0, 0};
        makeObjects();
        display();
    }

    public void draw() {
    }

    void display() {
        loadPixels();
        float aspectRatio = (float) width / height;
        float scale = (float) Math.tan(FOV / 2);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float r = 0, g = 0, b = 0;
                int numRays = 100;

                for (int i = 0; i < numRays; i++) {
                    // Jitter the pixel center slightly within the pixel
                    float u = (x + random(1)) / width;
                    float v = (y + random(1)) / height;

                    float dx = (2 * u - 1) * aspectRatio * scale;
                    float dy = (1 - 2 * v) * scale;
                    float[] direction = new float[] {dx, dy, 1};

                    Ray ray = new Ray(cameraOrigin, direction);
                    float[] energy = ray.getEnergy(bodies, 3);
                    r += energy[0];
                    g += energy[1];
                    b += energy[2];
                }

                // Average the result
                r /= numRays;
                g /= numRays;
                b /= numRays;

                // Clamp to [0, 255]
                int cr = (int) Math.min(255, Math.max(0, r));
                int cg = (int) Math.min(255, Math.max(0, g));
                int cb = (int) Math.min(255, Math.max(0, b));

                pixels[x + y * width] = color(cr, cg, cb);
            }
        }
        updatePixels();
    }

    void makeObjects() {
        bodies = new ArrayList<Body>();

        // Define textures
        Texture gray = new Texture(0.8f, 0.8f);
        Texture rough = new Texture(0.6f, 1.0f);
        Texture mirror = new Texture(0.9f, 0.0f);
        Texture sunTexture = new Texture(1, 1);
        sunTexture.luminocity = new float[] {255, 255, 255};

        // Sun (above the scene)
        sun = new Sphere(0, 1000, 0, 100, sunTexture);
        bodies.add(sun);

        // Floor (large enough to hold all objects)
        Rectangle floor = new Rectangle(0, -15, 0, 0, 1, 0, 1000, 1000, gray);
        bodies.add(floor);

        // Mirror spheres
        Sphere mirror1 = new Sphere(-30, -5, 50, 10, mirror);
        Sphere mirror2 = new Sphere(30, -5, 50, 10, mirror);
        bodies.add(mirror1);
        bodies.add(mirror2);

        // Rough and gray spheres
        Sphere graySphere = new Sphere(0, -5, 30, 10, gray);
        Sphere roughSphere = new Sphere(0, -5, 70, 10, rough);
        bodies.add(graySphere);
        bodies.add(roughSphere);
    }


    // Fields
    public static int Width;
    public static int Height;
    public static float FOV;
    public static Main instance;
    public static float[] cameraOrigin;

    // Objects
    Body sun;
    ArrayList<Body> bodies;

    public static void main(String[] args) {
        PApplet.main("main.Main");
    }
}
