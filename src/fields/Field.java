package fields;

public class Field {
	public Object[] fields;
	public int id;
	
	public Field(int i, Object[] f) {
		id = i;
		fields = f;
	}

	public void fixReferences(Object[] newTable) {
		for (int i = 0; i < fields.length; i++) {
			Object f = fields[i];
			if (f instanceof Reference) {
				fields[i] = newTable[((Reference) f).ref];
			}
		}
	}
	
	public void init() {
		
	}
}
