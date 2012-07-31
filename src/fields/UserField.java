package fields;

public class UserField extends Field {
	int version;
	
	public UserField(int i, int v, Object[] f) {
		super(i, f);
		version = v;
	}
}
