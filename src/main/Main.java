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
    
    void display() {
    	loadPixels();
    	for(int x = 0; x < width; x++) {
    		for(int y = 0; y < height; y++) {
    			Ray ray = new Ray(x, y);
    			ray.render(bodies);
    		}
    	}
    	updatePixels();
    }
    
    void makeObjects() {
    	bodies = new ArrayList<Body>();
    	sun = new LightSource(0, -100000, 0, 1000000);
    	bodies.add(new Sphere(0, 0, 30, 5, new Texture((float)0.5)));
    	bodies.add(new Sphere(0, -10, 30, 3, new Texture((float)0.5)));
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
