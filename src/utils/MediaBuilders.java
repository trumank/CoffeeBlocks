package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class MediaBuilders {
	static final byte[] squeakColors = {-1, -1, -1, 0, 0, 0, -1, -1, -1, -128, -128, -128, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, -1, -1, -1, -1, 0, -1, 0, -1, 32, 32, 32, 64, 64, 64, 96, 96, 96, -97, -97, -97, -65, -65, -65, -33, -33, -33, 8, 8, 8, 16, 16, 16, 24, 24, 24, 40, 40, 40, 48, 48, 48, 56, 56, 56, 72, 72, 72, 80, 80, 80, 88, 88, 88, 104, 104, 104, 112, 112, 112, 120, 120, 120, -121, -121, -121, -113, -113, -113, -105, -105, -105, -89, -89, -89, -81, -81, -81, -73, -73, -73, -57, -57, -57, -49, -49, -49, -41, -41, -41, -25, -25, -25, -17, -17, -17, -9, -9, -9, 0, 0, 0, 0, 51, 0, 0, 102, 0, 0, -103, 0, 0, -52, 0, 0, -1, 0, 0, 0, 51, 0, 51, 51, 0, 102, 51, 0, -103, 51, 0, -52, 51, 0, -1, 51, 0, 0, 102, 0, 51, 102, 0, 102, 102, 0, -103, 102, 0, -52, 102, 0, -1, 102, 0, 0, -103, 0, 51, -103, 0, 102, -103, 0, -103, -103, 0, -52, -103, 0, -1, -103, 0, 0, -52, 0, 51, -52, 0, 102, -52, 0, -103, -52, 0, -52, -52, 0, -1, -52, 0, 0, -1, 0, 51, -1, 0, 102, -1, 0, -103, -1, 0, -52, -1, 0, -1, -1, 51, 0, 0, 51, 51, 0, 51, 102, 0, 51, -103, 0, 51, -52, 0, 51, -1, 0, 51, 0, 51, 51, 51, 51, 51, 102, 51, 51, -103, 51, 51, -52, 51, 51, -1, 51, 51, 0, 102, 51, 51, 102, 51, 102, 102, 51, -103, 102, 51, -52, 102, 51, -1, 102, 51, 0, -103, 51, 51, -103, 51, 102, -103, 51, -103, -103, 51, -52, -103, 51, -1, -103, 51, 0, -52, 51, 51, -52, 51, 102, -52, 51, -103, -52, 51, -52, -52, 51, -1, -52, 51, 0, -1, 51, 51, -1, 51, 102, -1, 51, -103, -1, 51, -52, -1, 51, -1, -1, 102, 0, 0, 102, 51, 0, 102, 102, 0, 102, -103, 0, 102, -52, 0, 102, -1, 0, 102, 0, 51, 102, 51, 51, 102, 102, 51, 102, -103, 51, 102, -52, 51, 102, -1, 51, 102, 0, 102, 102, 51, 102, 102, 102, 102, 102, -103, 102, 102, -52, 102, 102, -1, 102, 102, 0, -103, 102, 51, -103, 102, 102, -103, 102, -103, -103, 102, -52, -103, 102, -1, -103, 102, 0, -52, 102, 51, -52, 102, 102, -52, 102, -103, -52, 102, -52, -52, 102, -1, -52, 102, 0, -1, 102, 51, -1, 102, 102, -1, 102, -103, -1, 102, -52, -1, 102, -1, -1, -103, 0, 0, -103, 51, 0, -103, 102, 0, -103, -103, 0, -103, -52, 0, -103, -1, 0, -103, 0, 51, -103, 51, 51, -103, 102, 51, -103, -103, 51, -103, -52, 51, -103, -1, 51, -103, 0, 102, -103, 51, 102, -103, 102, 102, -103, -103, 102, -103, -52, 102, -103, -1, 102, -103, 0, -103, -103, 51, -103, -103, 102, -103, -103, -103, -103, -103, -52, -103, -103, -1, -103, -103, 0, -52, -103, 51, -52, -103, 102, -52, -103, -103, -52, -103, -52, -52, -103, -1, -52, -103, 0, -1, -103, 51, -1, -103, 102, -1, -103, -103, -1, -103, -52, -1, -103, -1, -1, -52, 0, 0, -52, 51, 0, -52, 102, 0, -52, -103, 0, -52, -52, 0, -52, -1, 0, -52, 0, 51, -52, 51, 51, -52, 102, 51, -52, -103, 51, -52, -52, 51, -52, -1, 51, -52, 0, 102, -52, 51, 102, -52, 102, 102, -52, -103, 102, -52, -52, 102, -52, -1, 102, -52, 0, -103, -52, 51, -103, -52, 102, -103, -52, -103, -103, -52, -52, -103, -52, -1, -103, -52, 0, -52, -52, 51, -52, -52, 102, -52, -52, -103, -52, -52, -52, -52, -52, -1, -52, -52, 0, -1, -52, 51, -1, -52, 102, -1, -52, -103, -1, -52, -52, -1, -52, -1, -1, -1, 0, 0, -1, 51, 0, -1, 102, 0, -1, -103, 0, -1, -52, 0, -1, -1, 0, -1, 0, 51, -1, 51, 51, -1, 102, 51, -1, -103, 51, -1, -52, 51, -1, -1, 51, -1, 0, 102, -1, 51, 102, -1, 102, 102, -1, -103, 102, -1, -52, 102, -1, -1, 102, -1, 0, -103, -1, 51, -103, -1, 102, -103, -1, -103, -103, -1, -52, -103, -1, -1, -103, -1, 0, -52, -1, 51, -52, -1, 102, -52, -1, -103, -52, -1, -52, -52, -1, -1, -52, -1, 0, -1, -1, 51, -1, -1, 102, -1, -1, -103, -1, -1, -52, -1, -1, -1, -1};
	
	public static BufferedImage buildCostume(Object[] form) {
		int width = ((Number)form[0]).intValue();
		int height = ((Number)form[1]).intValue();
		int depth = ((Number)form[2]).intValue();
		
		int[] bitmap;
		
		try {
			bitmap = decodePixels((byte[]) form[4]);
		} catch (IOException e) {
			System.err.println("Failed to build image.");
			return null;
		}
		MemoryImageSource localMemoryImageSource = null;

		Object[] colors;
		ColorModel colorMap;
		if (depth <= 8)
		{
			if (form.length == 6) {
				colors = (Object[]) form[5];
				colorMap = customColorMap(depth, Arrays.asList(colors).toArray(new Color[0]));
			}
			else {
				colorMap = squeakColorMap(depth);
			}
			localMemoryImageSource = new MemoryImageSource(width, height, colorMap, rasterToByteRaster(bitmap, width, height, depth), 0, width);
		} else if (depth == 16) {
			localMemoryImageSource = new MemoryImageSource(width, height, raster16to32(bitmap, width, height), 0, width);
		} else if (depth == 32) {
			localMemoryImageSource = new MemoryImageSource(width, height, rasterAddAlphaTo32(bitmap), 0, width);
		}
		if (localMemoryImageSource != null) {
			int[] pixels = new int[width * height];
			PixelGrabber grabber = new PixelGrabber(localMemoryImageSource, 0, 0, width, height, pixels, 0, width);
			try {
				grabber.grabPixels();
			}
			catch (InterruptedException localInterruptedException) {
				System.out.println("???");
			}
			BufferedImage localBufferedImage = new BufferedImage(width, height, 2);
			localBufferedImage.getRaster().setDataElements(0, 0, width, height, pixels);
			return localBufferedImage;
		}
		System.err.println("Unknown image depth: " + depth);
		return null;
	}
	
	private static int[] decodePixels(Object paramObject) throws IOException
	{
		if ((paramObject instanceof int[])) {
			return (int[])paramObject;
		}

		DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
		int i = decodeInt(localDataInputStream);
		int[] arrayOfInt = new int[i];
		int j = 0;
		int m;
		int i2;
		int i3;
		while (localDataInputStream.available() > 0 && j < i)
		{
			int k = decodeInt(localDataInputStream);
			m = k >> 2;
			int n = k & 3;

			switch (n)
			{
			case 0:
				j++;
				break;
			case 1:
				int i1 = localDataInputStream.readUnsignedByte();
				i2 = i1 << 24 | i1 << 16 | i1 << 8 | i1;
				for (i3 = 0; i3 < m; i3++) {
					arrayOfInt[j++] = i2;
				}
				break;
			case 2:
				i2 = localDataInputStream.readInt();
				for (i3 = 0; i3 < m; i3++) {
					arrayOfInt[j++] = i2;
				}
				break;
			case 3:
				for (i3 = 0; i3 < m; i3++) {
					i2 = localDataInputStream.readInt();
					arrayOfInt[j++] = i2;
				}
				break;
			}
		}
		return arrayOfInt;
	}

	private static int decodeInt(DataInputStream paramDataInputStream) throws IOException
	{
		int i = paramDataInputStream.readUnsignedByte();
		if (i <= 223)
			return i;
		if (i <= 254)
			return (i - 224) * 256 + paramDataInputStream.readUnsignedByte();
		return paramDataInputStream.readInt();
	}

	private static int[] rasterAddAlphaTo32(int[] paramArrayOfInt)
	{
		for (int i = 0; i < paramArrayOfInt.length; i++)
		{
			int j = paramArrayOfInt[i];
			if (j != 0)
				paramArrayOfInt[i] = (0xFF000000 | j);
		}
		return paramArrayOfInt;
	}

	private static int[] raster16to32(int[] paramArrayOfInt, int paramInt1, int paramInt2)
	{
		int[] arrayOfInt = new int[paramInt1 * paramInt2];
		int i2 = (paramInt1 + 1) / 2;
		for (int i3 = 0; i3 < paramInt2; i3++)
		{
			int i = 16;
			for (int i4 = 0; i4 < paramInt1; i4++)
			{
				int j = paramArrayOfInt[(i3 * i2 + i4 / 2)] >> i & 0xFFFF;
				int k = (j >> 10 & 0x1F) << 3;
				int m = (j >> 5 & 0x1F) << 3;
				int n = (j & 0x1F) << 3;
				int i1 = k + m + n == 0 ? 0 : 0xFF000000 | k << 16 | m << 8 | n;
				arrayOfInt[(i3 * paramInt1 + i4)] = i1;
				i = i == 16 ? 0 : 16;
			}
		}
		return arrayOfInt;
	}

	private static byte[] rasterToByteRaster(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
	{
		byte[] arrayOfByte = new byte[paramInt1 * paramInt2];
		int i = paramArrayOfInt.length / paramInt2;
		int j = (1 << paramInt3) - 1;
		int k = 32 / paramInt3;

		for (int m = 0; m < paramInt2; m++)
		{
			for (int n = 0; n < paramInt1; n++)
			{
				int i1 = paramArrayOfInt[(m * i + n / k)];
				int i2 = paramInt3 * (k - n % k - 1);
				arrayOfByte[(m * paramInt1 + n)] = (byte)(i1 >> i2 & j);
			}
		}
		return arrayOfByte;
	}

	private static IndexColorModel squeakColorMap(int paramInt)
	{
		int i = paramInt == 1 ? -1 : 0;
		return new IndexColorModel(paramInt, 256, squeakColors, 0, false, i);
	}

	private static IndexColorModel customColorMap(int bits, Color[] colors)
	{
		byte[] map = new byte[4 * colors.length];
		int i = 0;
		for (int j = 0; j < colors.length; j++)
		{
			Color color = colors[j];
			map[(i++)] = (byte)color.getRed();
			map[(i++)] = (byte)color.getGreen();
			map[(i++)] = (byte)color.getBlue();
			map[(i++)] = (byte)color.getAlpha();
		}
		return new IndexColorModel(bits, colors.length, map, 0, true, 0);
	}
}
