package me.jade;

public class Time {
    public static long timeStarted = System.nanoTime();

    private static long lastFrameTime = System.nanoTime();
    private static float dt = 0;

    public static float getTimeElapsed() {
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }

    public static float getDT() {
        return dt;
    }

    public static void endFrame() {
        dt = (float)((System.nanoTime() - lastFrameTime) * 1E-9);

        lastFrameTime = System.nanoTime();
    }
}