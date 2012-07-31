import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import fields.Field;
import fields.Reference;
import fields.UserField;

public class ObjectReader {
	DataInputStream in;
	DataOutputStream out;
	
	byte[] ba;

	public Field info;
	public Field stage;
	
	protected HashMap<Field, Integer> objLocs = new HashMap<Field, Integer>();
	
	public ObjectReader() {
		
	}
	
	public void readProject(InputStream i) throws IOException {
		in = new DataInputStream(i);
		boolean isProject = readHeader();
		if (!isProject) {
			throw new IOException("Bad header.");
		}
		in.readInt();
		info = readObject();
		stage = readObject();
	}
	
	protected Field readObject() throws IOException {
		Field[] objTable = readObjectTable();
		
		fixReferences(objTable);
		
		return objTable[0];
	}
	
	protected void fixReferences(Field[] objTable) {
		for (Field f : objTable) {
			f.fixReferences(objTable);
		}
	}
	
	protected Field[] readObjectTable() throws IOException {
		in.read(ba = new byte[10]);
		if (!new String(ba).equals("ObjS\1Stch\1")) {
			throw new IOException("Not an object");
		}
		Field[] objTable = new Field[in.readInt()];
		for (int i = 0; i < objTable.length; i++) {
			objTable[i] = readField();
		}
		return objTable;
	}
	
	protected boolean readHeader() throws IOException {
		in.read(ba = new byte[10]);
		return new String(ba).equals("ScratchV02");
	}
	
	protected Field readField() throws IOException {
		int id = in.readUnsignedByte();
		
		Object o[];
		int i, j, k;
		if (id >= 100) {
			int v = in.readUnsignedByte();
			i = in.readUnsignedByte();
			
			o = new Object[i];
			for (j = 0; j < i; j++) {
				o[j] = readInlineObject();
			}
			return new UserField(id, v, o);
		}
		
		Object[] params;
		
		switch (id) {
		case 9:
		case 10:
		case 14:
			i = in.readInt();
			in.read(ba = new byte[i]);
			params = new Object[] {new String(ba)};
			break;
		case 11:
			i = in.readInt();
			in.read(ba = new byte[i]);
			params = new Object[] {ba};
			break;
		case 12:
			i = in.readInt();
			in.read(ba = new byte[2 * i]);
			params = new Object[] {ba};
			break;
		case 13:
			i = in.readInt();
			int[] bitmap = new int[i];
			for (j = 0; j < i; i++) {
				bitmap[j] = in.readInt();
			}
			params = new Object[] {bitmap};
			break;
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
			i = in.readInt();
			params = new Object[id >= 23 ? i * 2 : i];
			for (j = 0; j < params.length; j++) {
				params[j] = readInlineObject();
			}
			break;
		case 30:
			i = in.readInt();
			params = new Object[] {i >> 22 & 0xFF, i >> 12 & 0xFF, i >> 2 & 0xFF};
			break;
		case 31:
			i = in.readInt();
			params = new Object[] {i >> 22 & 0xFF, i >> 12 & 0xFF, i >> 2 & 0xFF, in.readUnsignedByte()};
			break;
		case 32:
		case 33:
		case 34:
		case 35:
			int fieldNum = (new int[] {2, 4, 5, 6})[id - 32];
			params = new Object[fieldNum];
			for (i = 0; i < fieldNum; i++) {
				params[i] = readInlineObject();
			}
			break;
		default:
			throw new IOException("Unknown fixed-format class: " + id);
		}
		return new Field(id, params);
	}
	
	protected Object readInlineObject() throws IOException {
		int id = in.readUnsignedByte();
		switch (id) {
		case 1:
			return null;
		case 2:
			return true;
		case 3:
			return false;
		case 4:
			return in.readInt();
		case 5:
			return in.readShort();
		case 6:
		case 7:
			double d1 = 0.0;
			double d2 = 1.0;
			int i, j, k;
			i = in.readShort();
			for (j = 0; j < i; j++) {
				k = in.readUnsignedByte();
				d1 += d2 * k;
				d2 *= 256.0D;
			}
			return id == 7 ? d1 : -d1;
		case 8:
			return in.readDouble();
		case 99:
			return new Reference((in.readUnsignedByte() << 16) + (in.readUnsignedByte() << 8) + in.readUnsignedByte() - 1);
		}
		throw new IOException("Unknown inline class: " + id);
	}
}