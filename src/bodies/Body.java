package bodies;
import textures.*;
import main.*;

public class Body {
	Body(){
	}
	public float getZ(Ray ray) {
		return Float.POSITIVE_INFINITY;
	}
	
	public float[] getReflectionDirection(Ray ray, float[] normal, float z) {
		return null;
	}
	
	public float[] getReflectionLocation(Ray ray, float  z) {
		return null;
	}
	
	public float[] getNormalAt(Ray ray, float z) {
		return null;
	}
	//fields
	public float mass;//mass in kg
	public float[] centerOfMass;//center of mass in meters from  the camera. {x, y, z}.
	public Texture texture;	
}
