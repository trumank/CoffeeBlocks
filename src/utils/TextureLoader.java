package utils;
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;
 
import org.lwjgl.BufferUtils;
 
import static org.lwjgl.opengl.GL11.*;

public class TextureLoader {
	private static ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {8, 8, 8, 8}, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
	private static ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {8, 8, 8, 0}, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
	
	private static IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);
	
	private static int createTextureID() {
		glGenTextures(textureIDBuffer);
		return textureIDBuffer.get(0);
	}
	 
	
	public static int getTexture(BufferedImage bufferedImage) throws IOException {
		return getTexture(bufferedImage, GL_TEXTURE_2D, GL_RGBA, GL_NEAREST, GL_NEAREST);
	}
	 
	
	public static int getTexture(BufferedImage bufferedImage, int target, int dstPixelFormat, int minFilter, int magFilter) throws IOException {
		int srcPixelFormat;
		
		int textureID = createTextureID();
		 
		
		glBindTexture(target, textureID);
		 
		if (bufferedImage.getColorModel().hasAlpha()) {
			srcPixelFormat = GL_RGBA;
		} else {
			srcPixelFormat = GL_RGB;
		}
		 
		
		ByteBuffer textureBuffer = convertImageData(bufferedImage);
		 
		if (target == GL_TEXTURE_2D) {
			glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
			glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
		}
		 
		
		glTexImage2D(target, 0, dstPixelFormat, get2Fold(bufferedImage.getWidth()), get2Fold(bufferedImage.getHeight()), 0, srcPixelFormat, GL_UNSIGNED_BYTE, textureBuffer);
		 
		return textureID;
	}
	
	private static int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold) {
			ret <<= 1;
		}
		return ret;
	}
	
	private static ByteBuffer convertImageData(BufferedImage bufferedImage) {
		ByteBuffer imageBuffer;
		WritableRaster raster;
		BufferedImage texImage;
		 
		int texWidth = 2;
		int texHeight = 2;
		
		while (texWidth < bufferedImage.getWidth()) {
			texWidth <<= 1;
		}
		while (texHeight < bufferedImage.getHeight()) {
			texHeight <<= 1;
		}
		 
		
		if (bufferedImage.getColorModel().hasAlpha()) {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
			texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
		} else {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
			texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
		}
		 
		
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, texWidth, texHeight);
		g.drawImage(bufferedImage, 0, 0, null);
		 
		
		
		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();
		 
		imageBuffer = ByteBuffer.allocateDirect(data.length);
		imageBuffer.order(ByteOrder.nativeOrder());
		imageBuffer.put(data, 0, data.length);
		imageBuffer.flip();
		 
		return imageBuffer;
	}
}