package bodies;

public class LightSource {
	public LightSource(float x, float y, float z, float brightness){
		this.x = x;
		this.y = y;
		this.z = z;
		this.brightness = brightness;
	}
	public float brightness;
	public float x, y, z;
}
