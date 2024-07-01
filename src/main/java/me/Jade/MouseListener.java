package me.Jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseListener {
    private static MouseListener instance = null;

    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }

        return instance;
    }

    private MouseListener() {
        scrollX = 0;
        scrollY = 0;

        xPos = 0;
        yPos = 0;
        lastX = 0;
        lastY = 0;

        isDragging = false;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;

        get().xPos = xPos;
        get().yPos = yPos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= get().mouseButtonPressed.length) return;

        get().mouseButtonPressed[button] = action == GLFW_PRESS;
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;

        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getLastX() {
        return (float)get().lastX;
    }

    public static float getLastY() {
        return (float)get().lastY;
    }

    public static float getDX() {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDY() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean mouseButtonDown(int button) {
        if (button >= get().mouseButtonPressed.length) return false;

        return get().mouseButtonPressed[button];
    }
}