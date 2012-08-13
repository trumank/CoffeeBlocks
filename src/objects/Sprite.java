package objects;

import org.lwjgl.input.Mouse;

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
		
		stage = (Stage) fields[1];
		
		position = ((Rectangle) fields[0]).origin().add(costumes[costumeIndex].rotationCenter);
		rotation = (((Number) fields[14]).floatValue()) * (float) Math.PI / 180;
		
		rotationStyle = RotationStyle.valueOf((String) fields[15]);
		
		hidden = (((Number) fields[4]).intValue() & 1) == 1;
	}

	@Override
	public void init() {
		super.init();
		position = stage.realCoodsToScratch(position);
	}

	@Override
	public void draw() {
		if (hidden) {
			return;
		}
		
		Transformation.pushMatix();
		
		Point p = stage.scratchCoodsToReal(position);
		
		Transformation.translate(Math.round(p.x), Math.round(p.y));
		
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
	
	public void setRotation(float r) {
		rotation = r;
	}
	
	public void setPosition(Point p) {
		position = p;
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public void setPositionX(float x) {
		position.x = x;
	}
	
	public void setPositionY(float y) {
		position.y = y;
	}
	
	public void evalCommand(String selector, Object[] args) {
		
		// MOTION /////////////////////
		
		if (selector.equals("forward:")) {
			double dist = Util.castDouble(args[0]);
			position.translate(Math.cos(rotation) * dist, -Math.sin(rotation) * dist);
			return;
		} else if (selector.equals("turnRight:")) {
			setRotation(rotation + (float)Math.toRadians(Util.castFloat(args[0])));
			return;
		} else if (selector.equals("turnLeft:")) {
			setRotation(rotation - (float) Math.toRadians(Util.castFloat(args[0])));
			return;
		} else if (selector.equals("heading:")) {
			setRotation((float) Math.toRadians(Util.castFloat(args[0]) - 90));
			return;
		} else if (selector.equals("pointTowards:")) {
			Point other = null;
			if (args[0].equals("mouse")) {
				other = stage.realCoodsToScratch(new Point(Mouse.getX(), stage.height - Mouse.getY()));
			} else {
				Sprite sprite = stage.coerceSpriteArg(args[0]);
				if (sprite != null) {
					other = sprite.position;
				}
			}
			if (other != null) {
				other = other.subtract(position);
				setRotation((float) Math.atan2(other.x, other.y) - (float) Math.PI / 2);
			}
			return;
		} else if (selector.equals("gotoX:y:")) {
			position.set(Util.castFloat(args[0]), Util.castFloat(args[1]));
			return;
		} else if (selector.equals("gotoSpriteOrMouse:")) {
			Point other = null;
			if (args[0].equals("mouse")) {
				other = stage.realCoodsToScratch(new Point(Mouse.getX(), stage.height - Mouse.getY()));
			} else {
				Sprite sprite = stage.coerceSpriteArg(args[0]);
				if (sprite != null) {
					other = sprite.position;
				}
			}
			if (other != null) {
				setPosition(other);
			}
			return;
		} else if (selector.equals("changeXposBy:")) {
			setPositionX(position.x + Util.castFloat(args[0]));
			return;
		} else if (selector.equals("xpos:")) {
			setPositionX(Util.castFloat(args[0]));
			return;
		} else if (selector.equals("changeYposBy:")) {
			setPositionY(position.y + Util.castFloat(args[0]));
			return;
		} else if (selector.equals("ypos:")) {
			setPositionY(Util.castFloat(args[0]));
			return;
		}

		super.evalCommand(selector, args);
	}
	
	public Object evalArg(String selector, Object[] args) {
		
		// MOTION /////////////////////
		
		if (selector.equals("xpos")) {
			return position.x;
		} else if (selector.equals("ypos")) {
			return position.y;
		} else if (selector.equals("heading")) {
			return ((Math.toDegrees(rotation) - 90) % 360) + 180;
		}

		return super.evalArg(selector, args);
	}
}
