package objects;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import main.Main;
import org.lwjgl.BufferUtils;

import objects.media.Costume;
import objects.media.Sound;
import fields.ISerializable;
import geometry.Transformation;

public class Scriptable implements ISerializable, IDrawable {
	protected Costume[] costumes;
	private Sound[] sounds;
	protected int costumeIndex;
	private String name;
	
	public Scriptable() {
		
	}

	@Override
	public void initFromFields(Object[] fields) {
		name = (String) fields[5];
		ArrayList<Costume> c = new ArrayList<Costume>();
		ArrayList<Sound> s = new ArrayList<Sound>();
		Object[] media = (Object[]) fields[10];
		for (int i = 0; i < media.length; i++) {
			if (media[i] instanceof Costume) {
				c.add((Costume) media[i]);
			}
			if (media[i] instanceof Sound) {
				s.add((Sound) media[i]);
			}
		}
		costumes = c.toArray(new Costume[0]);
		sounds = s.toArray(new Sound[0]);
		
		costumeIndex = Arrays.asList(costumes).indexOf(fields[11]);
	}
	
	public void step() {
		draw();
	}
	
	public void draw() {
		drawTexture();
	}
	
	protected void drawTexture() {
		glUseProgram(Main.shaderProgram);
		
		Transformation.applyUniforms();
		
		costumes[costumeIndex].draw();
		
		glUseProgram(0);
	}

	private static FloatBuffer vertices;
	
	static {
		vertices = BufferUtils.createFloatBuffer(2 * 4);
		vertices.put(new float[] {
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 1.0f,
    		1.0f, 0.0f
        });
	}
}
