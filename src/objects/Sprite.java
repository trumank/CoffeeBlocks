package objects;

import geometry.Point;
import geometry.Rectangle;
import geometry.Transformation;

public class Sprite extends Scriptable {
	public enum RotationStyle {
	    normal, leftRight, none 
	}
	
	private Point position;
	private float rotation;
	private RotationStyle rotationStyle;
	
	private boolean hidden;
	
	public Sprite() {
		
	}

	@Override
	public void initFromFields(Object[] fields) {
		super.initFromFields(fields);
		position = ((Rectangle) fields[0]).origin().add(costumes[costumeIndex].rotationCenter);
		rotation = (((Number) fields[14]).floatValue()) * (float) Math.PI / 180;
		
		rotationStyle = RotationStyle.valueOf((String) fields[15]);
		
		hidden = (((Number) fields[4]).intValue() & 1) == 1;
	}

	@Override
	public void draw() {
		if (hidden) {
			return;
		}
		
		Transformation.pushMatix();
		
		Transformation.translate(Math.round(position.x), Math.round(position.y));
		
		switch (rotationStyle) {
		case normal:
			Transformation.rotate(rotation);
			break;
		case leftRight:
			if ((rotation + Math.PI) % (Math.PI * 2) > Math.PI) {
				Transformation.scale(-1, 1);
			}
			break;
		case none:
			break;
		}
		
		
		drawTexture();
		
		Transformation.popMatix();
	}
}
