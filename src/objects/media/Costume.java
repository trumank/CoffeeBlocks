package objects.media;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;

import utils.MediaBuilders;
import utils.TextureLoader;
import fields.Field;
import fields.ISerializable;
import geometry.Point;

public class Costume implements ISerializable {
	private String name;
	private BufferedImage image;
	private int textureId;
	
	public int width;
	public int height;
	
	public Point rotationCenter;
	
	private FloatBuffer vertices;
	private FloatBuffer texCoords;
	
	public Costume() {
		
	}

	@Override
	public void initFromFields(Object[] fields) {
		name = (String) fields[0];
		image = MediaBuilders.buildCostume(((Field) fields[1]).fields);

		width = ((Number) ((Field) fields[1]).fields[0]).intValue();
		height = ((Number) ((Field) fields[1]).fields[1]).intValue();
		
		rotationCenter = (Point) fields[2];
		
		float widthFold = get2Fold(width);
		float heightFold = get2Fold(height);
		
		float rcx = rotationCenter.x;
		float rcy = rotationCenter.y;
		
		vertices = BufferUtils.createFloatBuffer(4 * 2);
		vertices.put(new float[] {
			-rcx, -rcy,
			widthFold - rcx, -rcy,
			widthFold - rcx, heightFold - rcy,
			-rcx, heightFold - rcy,
		});
		texCoords = BufferUtils.createFloatBuffer(4 * 2);
		texCoords.put(new float[] {
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
		});
		
		try {
			textureId = TextureLoader.getTexture(image);
		} catch (IOException e) {
			System.err.println("Could not get texture ID.");
		}
	}

	@Override
	public void init() {
		
	}
	
	private int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold) {
			ret <<= 1;
		}
		return ret;
	}
	
	public void draw() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		
        vertices.rewind();
		texCoords.rewind();
		
        glVertexPointer(2, 0, vertices);
        glTexCoordPointer(2, 0, texCoords);
        
        
        glDrawArrays(GL_QUADS, 0, 4);
        
		glDisable(GL_BLEND);

		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
	}
}
