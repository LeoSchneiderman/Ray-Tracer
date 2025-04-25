package myMath;

public class Vector {

    public static float dot(float[] a, float[] b) {
        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }

    public static float[] normalize(float[] v) {
        float mag = magnitude(v);
        return mag == 0 ? new float[]{0, 0, 0} : new float[]{v[0]/mag, v[1]/mag, v[2]/mag};
    }

    public static float magnitude(float[] v) {
        return (float) Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }
    
    public static float[] subtract(float[] a, float[] b) {
    	return new float[] {a[0] - b[0], a[1] - b[1], a[2] - b[2]};
    }
    
    public static float[] getPointAtZ(float[] vector, float z) {
    	float scale = z / vector[2];
    	float a = vector[0] * scale;
    	float b = vector[1] * scale;
    	float c = vector[2] * scale;
    	return new float[] {a, b, c};
    }

    public static float[] cross(float[] a, float[] b) {
        return new float[] {
            a[1] * b[2] - a[2] * b[1],
            a[2] * b[0] - a[0] * b[2],
            a[0] * b[1] - a[1] * b[0]
        };
    }

}
