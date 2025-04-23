package bodies;

import textures.*;
import main.*;
import render.*;

public class Rectangle extends Body {
    public float width;  // width in meters
    public float height; // height in meters
    
    public Rectangle(float x, float y, float z, float width, float height, Texture texture) {
        float[] centerOfMass = {x, y, z};
        this.centerOfMass = centerOfMass;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }
    
    public float getZ(Ray ray) {
        return RectangleRender.getZ(this, ray);
    }
    
    public float[] getReflectionDirection(Ray ray, float[] normal, float z) {
        return RectangleRender.getReflectionDirection(this, ray, normal, z);
    }

    // The normal to the rectangle is always along the Y-axis (vertical surface)
    public float[] getNormalAt(Ray ray, float z) {
        return new float[] {0, 1, 0};  // Normal pointing along the Y-axis (rectangle lies flat on the Z plane)
    }
}
