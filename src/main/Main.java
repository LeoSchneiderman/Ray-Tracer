package main;
import processing.core.PApplet;
import bodies.*;
import textures.*;
import java.util.ArrayList;

public class Main extends PApplet {

    // Setup the canvas size and other settings
    public void settings() {
        size(500, 500);
        instance = this;
        Width = width;
        Height = height;
        FOV = (float)Math.PI / 2;
    }
    
    public void setup() {
    	makeObjects();
    	display();
    }
    
    public void draw() {
    	bodies.get(1).centerOfMass[0] += 0.1;
    	display();
    }
    
    void display() {
    	loadPixels();
		float aspectRatio = (float) width / height;
		float scale = (float) Math.tan(FOV / 2);
    	for(int x = 0; x < width; x++) {
    		for(int y = 0; y < height; y++) {
    			float dx = (2 * ((x + 0.5f) / width) - 1) * aspectRatio * scale;
    			float dy = (1 - 2 * ((y + 0.5f) / height)) * scale;
    			float[] vector = new float[] {dx, dy, 1};
    			Ray ray = new Ray(vector);
    			pixels[x + y * width] = ray.getColor(bodies, 1);
    		}
    	}
    updatePixels();
    }
    
    void makeObjects() {
    	bodies = new ArrayList<Body>();
    	sun = new LightSource(0, -100000, 100000, 1000000);
    	bodies.add(new Sphere(0, 0, 60, 10, new Texture((float)0.5)));
    	bodies.add(new Sphere(-30, 0, 30, 10, new Texture((float)0.5)));
    }
    
    
    //fields
    public static int Width;
    public static int Height;
    public static float FOV;
    public static Main instance;
    
    //objects
    LightSource sun;
    ArrayList<Body> bodies;
    

    // Main method to start the sketch
    public static void main(String[] args) {
        PApplet.main("main.Main");  // Starts the PApplet sketch
    }
}
