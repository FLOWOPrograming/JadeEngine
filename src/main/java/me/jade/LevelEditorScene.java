package me.jade;

import me.jade.ecs.GameObject;
import me.jade.ecs.components.Sprite;
import me.jade.ecs.components.SpriteRenderer;
import me.jade.ecs.components.Spritesheet;
import me.jade.renderer.Renderer;
import me.jade.renderer.Texture;
import me.jade.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0.0f, 0.0f));
        renderer = new Renderer(AssetPool.getShader("src\\main\\java\\me\\jade\\util\\shaders\\default.glsl"));
        AssetPool.addSpriteSheet("src\\main\\java\\me\\jade\\util\\images\\spritesheet.png", new Spritesheet(AssetPool.getTexture("src\\main\\java\\me\\jade\\util\\images\\spritesheet.png"), 16, 16, 26, 0));

        Spritesheet spritesheet = AssetPool.getSpriteSheet("src\\main\\java\\me\\jade\\util\\images\\spritesheet.png");

        GameObject obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(spritesheet.getSprite(0)));

        GameObject obj2 = new GameObject("Obj 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(spritesheet.getSprite(1)));

        GameObject obj3 = new GameObject("Obj 3", new Transform(new Vector2f(700, 100), new Vector2f(256, 256)));
        obj3.addComponent(new SpriteRenderer(new Vector4f(1)));

        this.addGameObject(obj1);
        this.addGameObject(obj2);
        this.addGameObject(obj3);
    }

    float fpsSum = 0;
    int frameCount = 0;

    @Override
    public void update(float dt) {
        //System.out.println("FPS: " + 1/dt);

        this.gameObjects.get(2).getComponent(SpriteRenderer.class).getColor().sub(new Vector4f(dt * 0.1f, dt * 0.1f, dt * 0.1f, 0));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
