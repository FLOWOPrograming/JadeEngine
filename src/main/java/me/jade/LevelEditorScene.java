package me.jade;

import me.jade.ecs.GameObject;
import me.jade.ecs.components.SpriteRenderer;
import me.jade.renderer.Renderer;
import me.jade.renderer.Shader;
import me.jade.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    private static void loadResources() {
        AssetPool.getShader("C:\\Users\\marij\\IdeaProjects\\JadeEngine\\src\\main\\java\\me\\jade\\util\\shaders\\default.glsl");
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0.0f, 0.0f));
        renderer = new Renderer(AssetPool.getShader("src\\main\\java\\me\\jade\\util\\shaders\\default.glsl"));

        GameObject obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("src/main/java/me/jade/util/images/mario.png")));

        GameObject obj2 = new GameObject("Obj 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("src/main/java/me/jade/util/images/goomba.png")));

        GameObject obj3 = new GameObject("Obj 3", new Transform(new Vector2f(700, 100), new Vector2f(256, 256)));
        obj3.addComponent(new SpriteRenderer(AssetPool.getTexture("src/main/java/me/jade/util/images/mario.png")));

        loadResources();

        this.addGameObject(obj1);
        this.addGameObject(obj2);
        this.addGameObject(obj3);
    }

    float fpsSum = 0;
    int frameCount = 0;

    @Override
    public void update(float dt) {
        //System.out.println("FPS: " + 1/dt);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
