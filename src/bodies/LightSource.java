package bodies;

public class LightSource extends Body{
	public LightSource(float x, float y, float z, float brightness){
		this.brightness = brightness;
		centerOfMass = new float[]{x, y, z};
	}
	float  brightness;
}
