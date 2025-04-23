package textures;

public class Texture {
	int type;//type 0 means it is just broadly a texture, smooth or rough. No noise. No real texture. Just lighting.
	public Texture(float albedo) {
		this.albedo = albedo;
		type = 0;
	}
	float albedo;
}
