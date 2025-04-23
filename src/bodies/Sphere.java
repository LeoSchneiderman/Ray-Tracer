package bodies;
import textures.*;
import main.*;
import render.*;

public class Sphere extends Body{
	//fields
	public float r;//radius in meters
	
	public Sphere(float x, float y, float z, float r, Texture texture){
		//x is left right meters. y is  down meters. z is forward meters.
		//higher x is right. Higher y is up.
		float[] centerOfMass = {x, y, z};
		this.centerOfMass = centerOfMass;
		this.r = r;//in meters
		this.texture = texture;
	}
	
	public float getZ(Ray ray) {
		return SphereRender.getZ(this, ray);
	}
	
	public float[] getReflectionDirection(Ray ray, float[] normal, float z) {
		return SphereRender.getReflectionDirection(this,  ray,  normal, z);
	}
	
	public float[] getNormalAt(Ray ray, float z) {
		return SphereRender.getNormalAt(this, ray, z);
	}


}
