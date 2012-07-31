import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import fields.Field;

public class Main {
	public static void main(String[] args) {
		InputStream i = null;
		ObjectReader reader = new ObjectReader();
		
		try {
			reader.readProject(i = new FileInputStream(new File(args[0])));
		} catch (Throwable e) {
			try {
				System.out.println(i.available());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		Field f = reader.stage;
	}
}
