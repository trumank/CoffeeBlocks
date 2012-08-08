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
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import objects.Stage;

public class Main {
	public static int shaderProgram;
	public static int translationMatrix;
	
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
		
		loadShaders();
		
		InputStream i = null;
		ObjectReader reader = new ObjectReader();
		
		try {
			reader.readProject(i = new FileInputStream(new File(args[0])));
		} catch (Throwable e) {
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
		
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);
			stage.step();
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}
	
	private static void loadShaders() {
		shaderProgram = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(vertexShader, loadWholeFile("src/shaders/vertex.shader"));
		glCompileShader(vertexShader);
		
		if (glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Vertex shader failed to compile.");
		}
		
		
		glShaderSource(fragmentShader, loadWholeFile("src/shaders/fragment.shader"));
		glCompileShader(fragmentShader);
		
		if (glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Fragment shader failed to compile.");
		}
		
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
		
		translationMatrix = glGetUniformLocation(shaderProgram, "transformation");
		//glUniform1i(glGetUniformLocation(shaderProgram, "texture"), 0);
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
