package objects;

import java.util.ArrayList;
import java.util.Arrays;

import geometry.Point;
import geometry.Rectangle;
import geometry.Transformation;

public class Stage extends Scriptable {
	private IDrawable[] children;
	private Sprite[] sprites;
	
	private Rectangle bounds;
	
	protected Point center;
	
	public float width;
	public float height;
	
	public Stage() {
		stage = this;
	}

	@Override
	public void initFromFields(Object[] fields) {
		super.initFromFields(fields);
		
		bounds = (Rectangle) fields[0];
		center = new Point(bounds.w / 2, bounds.h / 2);
		width = bounds.w;
		height = bounds.h;
		
		ArrayList<IDrawable> c = new ArrayList<IDrawable>();
		for (Object object : Arrays.asList((Object[]) fields[2])) {
			if (object instanceof IDrawable) {
				c.add((IDrawable) object);
			}
		}
		children = c.toArray(new IDrawable[0]);
		
		sprites = Arrays.asList((Object[]) fields[16]).toArray(new Sprite[0]);
	}

	public void step() {
		super.step();
		
		for (int i = 0; i < sprites.length; i++) {
			sprites[i].step();
		}
	}
	
	public void draw() {
		Transformation.reset();

		Transformation.pushMatix();
		
		Transformation.translate(240.0f, 180.0f);
		
		drawTexture();
		
		Transformation.popMatix();
		
		for (int i = children.length - 1; i >= 0; i--) {
			children[i].draw();
		}
	}
	
	/*public Object evalCommand(String selector, Object[] args) {
		return super.evalCommand(selector, args);
	}
	
	public Object evalArg(String selector, Object[] args) {
		return super.evalArg(selector, args);
	}*/

	public Point scratchCoodsToReal(Point p) {
		return p.multiply(new Point(1, -1)).add(center);
	}
	
	public Point realCoodsToScratch(Point p) {
		return p.subtract(center).multiply(new Point(1, -1));
	}
	
	public Sprite coerceSpriteArg(Object object) {
		if (object instanceof Scriptable) {
			return (Sprite) object;
		}
		String string = object.toString();
		return getSprite(string);
	}

	public Sprite getSprite(String name) {
		if (name == null) {
			return null;
		}
		for (int i = 0; i < sprites.length; i++) {
			if (sprites[i].name.equals(name)) {
				return sprites[i];
			}
		}
		return null;
	}
}
