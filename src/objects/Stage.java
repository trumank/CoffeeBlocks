package objects;

import java.util.ArrayList;
import java.util.Arrays;

import geometry.Transformation;

public class Stage extends Scriptable {
	private IDrawable[] children;
	
	public Stage() {
		
	}

	@Override
	public void initFromFields(Object[] fields) {
		super.initFromFields(fields);
		ArrayList<IDrawable> c = new ArrayList<IDrawable>();
		for (Object object : Arrays.asList((Object[]) fields[2])) {
			if (object instanceof IDrawable) {
				c.add((IDrawable) object);
			}
		}
		children = c.toArray(new IDrawable[0]);
	}

	public void step() {
		super.step();
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
}
