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
        t = - 3 * (float)Math.PI / 4;
    }

    public void setup() {
        makeObjects();
        bodies.get(1).centerOfMass = new float[] {cos(t) * 30, 0f, sin(t) * 30 + 60};
        display();
    }

    public void draw() {
     //   t += 0.02;
       // bodies.get(1).centerOfMass = new float[] {cos(t) * 30, 0f, sin(t) * 30 + 60};
       // display();
    }

    void display() {
        loadPixels();
        float aspectRatio = (float) width / height;
        float scale = (float) Math.tan(FOV / 2);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float dx = (2 * ((x + 0.5f) / width) - 1) * aspectRatio * scale;
                float dy = (1 - 2 * ((y + 0.5f) / height)) * scale;
                float[] vector = new float[] {dx, dy, 1};

                int r = 0, g = 0, b = 0;
                int numRays = 100;
                for (int i = 0; i < numRays; i++) {
                    Ray ray = new Ray(vector);
                    int c = ray.getColor(bodies, 10);
                    r += (c >> 16) & 0xFF;
                    g += (c >> 8) & 0xFF;
                    b += c & 0xFF;
                }

                r /= numRays;
                g /= numRays;
                b /= numRays;

                int avgColor = color(r, g, b);
                pixels[x + y * width] = avgColor;
            }
        }
        updatePixels();
    }

    void makeObjects() {
        bodies = new ArrayList<Body>();
        sun = new LightSource(0, -100000, 0, 1000000);
        bodies.add(new Sphere(0, 0, 60, 10, new Texture(0)));
        bodies.add(new Sphere(-30, 0, 30, 10, new Texture(1)));
        bodies.add(new Rectangle(0, -15, 100, 100, 100, new Texture(1)));
    }

    // Fields
    public static int Width;
    public static int Height;
    public static float FOV;
    public static Main instance;
    public static float t;

    // Objects
    LightSource sun;
    ArrayList<Body> bodies;

    public static void main(String[] args) {
        PApplet.main("main.Main");
    }
}
