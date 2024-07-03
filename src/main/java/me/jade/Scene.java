package me.jade;

import me.jade.ecs.GameObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

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
    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
        }

        isRunning = true;
    }

    public abstract void init();
    public abstract void update(float dt);
}
