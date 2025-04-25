package textures;

public class Texture {
	public float[] reflectance;
	public float roughness;
	public float[] luminocity;
	public int type;//type 0 means it is just broadly a texture, smooth or rough. No noise. No real texture. Just lighting.
	public Texture(float brightness, float roughness) {
		reflectance = new float[] {brightness, brightness, brightness};
		this.roughness = roughness;
		luminocity = new float[]{0, 0, 0};
	}
}
