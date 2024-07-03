package me.jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window window = null;
    private long glfwWindow;

    int width, height;
    String title;

    public float brightness = 0;

    private static Scene currentScene = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static Window get() {
        if (window == null) {
            window = new Window();
        }

        return window;
    }

    public static void changeScene(int newScene) {
        System.out.println(newScene);
        if (newScene == 0) {
            currentScene = new LevelEditorScene();
            System.out.println("Switching to level editor scene");
        } else if (newScene == 1) {
            currentScene = new LevelScene();
            System.out.println("Switching to level scene");
        } else {
            System.exit(-1);
        }

        currentScene.init();
    }

    public void run() throws IllegalAccessException {
        System.out.println("Hello LWJGL " + Version.getVersion()+"!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() throws IllegalAccessException {
        //Error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if (!glfwInit()) {
            throw new IllegalAccessException("Unable to initialize GLFW.");
        }

        //Config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window!");
        }

        // Create callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make GLFW context current
        glfwMakeContextCurrent(glfwWindow);

        //enable v-sync
        glfwSwapInterval(1);

        //make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities(); //VERY IMPORTANT

        Window.changeScene(0);
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            glClearColor(brightness, brightness, brightness, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            currentScene.update(Time.getDT());

            glfwSwapBuffers(glfwWindow);

            MouseListener.endFrame();
            Time.endFrame();
        }
    }
}