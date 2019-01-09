package testing;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

import org.lwjgl.glfw.GLFWErrorCallback;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Test {

    public static void main(String[] args) {
    	new Test();
    }
    
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private long window;
    
    Test() {
    	errorCallback = GLFWErrorCallback.createPrint(System.err);
    	
    	glfwSetErrorCallback(errorCallback);
    	if (!glfwInit()) {
    	    throw new IllegalStateException("Unable to initialize GLFW");
    	}
    	
    	window = glfwCreateWindow(640, 480, "Simple example", NULL, NULL);
    	if (window == NULL) {
    	    glfwTerminate();
    	    throw new RuntimeException("Failed to create the GLFW window");
    	}
    	
    	keyCallback = new GLFWKeyCallback() {
    	    @Override
    	    public void invoke(long window, int key, int scancode, int action, int mods) {
    	        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
    	            glfwSetWindowShouldClose(window, true);
    	        }
    	    }
    	};
    	
    	glfwSetKeyCallback(window, keyCallback);
    	
    	glfwMakeContextCurrent(window);
    	GL.createCapabilities();
    	
    	loop();
    }
    
    private void loop() {
    	while (!glfwWindowShouldClose(window)) {
    		double time = glfwGetTime();

        	glfwSwapBuffers(window);
        	glfwPollEvents();
    	}
    }
    
    
    private void terminate() {
    	glfwDestroyWindow(window);
    	keyCallback.free();
    }
}