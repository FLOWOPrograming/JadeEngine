package me.jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;

    private boolean[] keyPressed = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
        }

        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key >= get().keyPressed.length) return;

        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyDown(int keyCode) {
        if (keyCode >= get().keyPressed.length || keyCode < 0) {
            throw new ArrayIndexOutOfBoundsException("You tried to access a key that is out of bounds of the array. Key range: 0 to " + get().keyPressed.length + ", You passed in: " + keyCode + ".");
        }

        return get().keyPressed[keyCode];
    }
}
