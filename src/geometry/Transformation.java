package geometry;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.Stack;

import main.Main;

import org.lwjgl.BufferUtils;

public class Transformation {
	private static float[] matrix;
	private static Stack<float[]> matrixStack;
	private static FloatBuffer buffer;
	
	static {
		reset();
		buffer = BufferUtils.createFloatBuffer(9);
		matrixStack = new Stack<float[]>();
	}
	
	public static void pushMatix() {
		matrixStack.push(matrix);
	}
	
	public static void popMatix() {
		matrix = (float[]) matrixStack.pop();
	}
	
	public static void reset() {
		matrix = new float[] {
			1, 0, 0,
			0, 1, 0,
			0, 0, 1
		};
	}
	
	public static float[] multiply(float[] a, float[] b) {
		return new float[] {
			a[0]*b[0]+a[1]*b[3]+a[2]*b[6], a[0]*b[1]+a[1]*b[4]+a[2]*b[7], a[0]*b[2]+a[1]*b[5]+a[2]*b[8],
			a[3]*b[0]+a[4]*b[3]+a[5]*b[6], a[3]*b[1]+a[4]*b[4]+a[5]*b[7], a[3]*b[2]+a[4]*b[5]+a[5]*b[8],
			a[6]*b[0]+a[7]*b[3]+a[8]*b[6], a[6]*b[1]+a[7]*b[4]+a[8]*b[7], a[6]*b[2]+a[7]*b[5]+a[8]*b[8]
		};
	}
	
	public static void translate(float dx, float dy) {
		matrix = multiply(new float[] {
			1, 0, 0,
			0, 1, 0,
			dx, dy, 1
		}, matrix);
	}
	
	public static void rotate(float angle) {
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		matrix = multiply(new float[] {
		    c, s, 0,
		    -s, c, 0,
		    0, 0, 1
		}, matrix);
	}
	
	public static void scale(float sx, float sy) {
		matrix = multiply(new float[] {
			sx, 0, 0,
			0, sy, 0,
			0, 0, 1
		}, matrix);
	}
	
	public static void applyUniforms() {
		buffer.rewind();
		buffer.clear();
		buffer.put(matrix);
		buffer.rewind();
		glUniformMatrix3(Main.translationMatrix, false, buffer);
	}
}
