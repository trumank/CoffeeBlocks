package gui;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import main.Main;

import org.lwjgl.BufferUtils;

import geometry.Rectangle;
import geometry.Transformation;
import objects.IDrawable;

public class Component implements IDrawable {
	protected Rectangle bounds;
	protected FloatBuffer border;
	protected Component[] children;
	
	public Component(Rectangle bounds) {
		this.bounds = bounds;
		border = BufferUtils.createFloatBuffer(6 * 2);
		border.put(new float[] {
			0.0f, 0.0f,
			bounds.w, 0.0f,
			bounds.w, bounds.h,
			0.0f, bounds.h
		});
	}
	
	public void draw() {
		Transformation.pushMatix();
		
		Transformation.translate(bounds.x, bounds.y);
		
		glUseProgram(Main.shaderProgram);
		
		Transformation.applyUniforms();

		glUniform1i(Main.shaderType, 0);
		
		glEnableClientState(GL_VERTEX_ARRAY);
        border.rewind();
        glVertexPointer(2, 0, border);
        glDrawArrays(GL_TRIANGLES, 0, 6);
		glEnableClientState(GL_VERTEX_ARRAY);
		glUseProgram(0);
		
		Transformation.popMatix();
	}
}
