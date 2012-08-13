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
	protected String name;
	private Thread[] threads;
	protected Stage stage;
	
	public Scriptable() {
		
	}

	@Override
	public void initFromFields(Object[] fields) {
		name = (String) fields[6];
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
		
		Object[] scripts = (Object[]) fields[8];
		
		ArrayList<Object> hats = new ArrayList<Object>();
		hats.add("EventHatMorph");
		hats.add("KeyEventHatMorph");
		hats.add("MouseClickEventHatMorph");
		
		ArrayList<Thread> list = new ArrayList<Thread>();
		
		for (int i = 0; i < scripts.length; i++) {
			Object[] script = (Object[]) ((Object[]) scripts[i])[1];
			if (hats.indexOf(((Object[]) script[0])[0]) != -1) {
				Thread thread = new Thread(this, (Object[]) script);
				list.add(thread);
				//if (script[]) {
				thread.start();
				//}
			}
		}
		
		threads = list.toArray(new Thread[0]);
	}

	@Override
	public void init() {
		
	}
	
	public void step() {
		for (int i = 0; i < threads.length; i++) {
			threads[i].step();
		}
	}
	
	public void draw() {
		drawTexture();
	}
	
	protected void drawTexture() {
		glUseProgram(Main.shaderProgram);
		
		glUniform1i(Main.shaderType, 1);
		
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

	
	public void evalCommand(String selector, Object[] args) {
		if (selector.equals("say:")) {
			System.out.println(args[0]);
			return;
		}
		System.out.println("Unknown command: " + selector);
	}
	
	public Object evalArg(String selector, Object[] args) {
		
		// OPERATORS //////////////////
		
		if (selector.equals("+")) {
			return Util.castDouble(args[0]) + Util.castDouble(args[1]);
		} else if (selector.equals("-")) {
			return Util.castDouble(args[0]) - Util.castDouble(args[1]);
		} else if (selector.equals("*")) {
			return Util.castDouble(args[0]) * Util.castDouble(args[1]);
		} else if (selector.equals("/")) {
			return Util.castDouble(args[0]) / Util.castDouble(args[1]);
		} else if (selector.equals("randomFrom:to:")) {
			double a = Util.castDouble(args[0]);
			double b = Util.castDouble(args[1]);
			
			if (Math.floor(a) == a && Math.floor(b) == b) {
				return Util.random.nextDouble() * (b - a) + a;
			} else {
				return Math.floor(Util.random.nextDouble() * (b + 1 - a)) + a;
			}
		}
		System.out.println("Unknown arg: " + selector);
		return null;
	}
}
