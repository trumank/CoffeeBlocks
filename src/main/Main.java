package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import objects.Stage;

public class Main {
	public static int shaderProgram;
	public static int translationMatrix;
	public static int shaderType;
	
	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(480, 360));
			Display.setTitle("CoffeeBlocks");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(0);
		}
		
		shaderProgram = loadShaders("texture");
		translationMatrix = glGetUniformLocation(shaderProgram, "transformation");
		shaderType = glGetUniformLocation(shaderProgram, "type");
		
		InputStream i = null;
		ObjectReader reader = new ObjectReader();
		
		try {
			reader.readProject(i = new FileInputStream(new File(args[0])));
			i.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Stage stage = reader.stage;
		
		System.out.println(stage);
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 480, 360, 0, 0, 1);
		glMatrixMode(GL_MODELVIEW);
		
		boolean turbo = true;
		
		//double count = 0;
		//double initStart = System.currentTimeMillis();
		
		while (!Display.isCloseRequested()) {
			long start = System.currentTimeMillis();
			glClear(GL_COLOR_BUFFER_BIT);
			
			do {
				stage.step();
			} while (turbo && System.currentTimeMillis() - start < 1000 / 30);
			
			stage.draw();
			
			Display.update();
			Display.sync(30);
			
			//System.out.println(++count / ((System.currentTimeMillis() - initStart) / 1000.0));
		}
		Display.destroy();
	}
	
	private static int loadShaders(String name) {
		int shader = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(vertexShader, loadWholeFile("src/shaders/" + name + ".vert.txt"));
		glCompileShader(vertexShader);
		
		if (glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(vertexShader, glGetShader(vertexShader, GL_INFO_LOG_LENGTH)));
		}
		
		
		glShaderSource(fragmentShader, loadWholeFile("src/shaders/" + name + ".frag.txt"));
		glCompileShader(fragmentShader);
		
		if (glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(fragmentShader, glGetShader(fragmentShader, GL_INFO_LOG_LENGTH)));
		}
		
		glAttachShader(shader, vertexShader);
		glAttachShader(shader, fragmentShader);
		glLinkProgram(shader);
		glValidateProgram(shader);
		
		return shader;
	}
	
	private static String loadWholeFile(String path) {
		File file = new File(path);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {        
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}
}
