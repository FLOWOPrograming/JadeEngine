package me.jade;

import me.jade.ecs.GameObject;
import me.jade.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer;
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {

    }

    public void addGameObject(GameObject go) {
        gameObjects.add(go);

        if (isRunning) {
            go.start();
        }

        this.renderer.add(go);
    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
        }

        isRunning = true;
    }

    public Camera camera() {
        return this.camera;
    }

    public abstract void init();
    public abstract void update(float dt);
}
