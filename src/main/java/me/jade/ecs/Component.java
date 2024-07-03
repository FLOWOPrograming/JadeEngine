package me.jade.ecs;

public abstract class Component {

    public GameObject parent = null;

    public abstract void start();
    public abstract void update(float dt);
}
