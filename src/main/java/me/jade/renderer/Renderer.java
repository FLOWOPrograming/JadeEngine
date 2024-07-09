package me.jade.renderer;

import me.jade.ecs.GameObject;
import me.jade.ecs.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 10000;
    private List<RenderBatch> batches;

    private Shader shader;

    public Renderer(Shader shader) {
        this.batches = new ArrayList<>();
        this.shader = shader;
    }

    public void add(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    private void add(SpriteRenderer sprite) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom()) {
                if (!(sprite.getTexture() == null || (batch.hasTexture(sprite.getTexture()) || batch.hasTextureRoom()))) continue; // Check if there is enough textures

                batch.addSprite(sprite);
                added = true;
                break;
            }
        }

        if (!added) {
            RenderBatch renderBatch = new RenderBatch(MAX_BATCH_SIZE, 8, shader);
            renderBatch.start();
            batches.add(renderBatch);
            renderBatch.addSprite(sprite);
        }
    }

    public void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }
}
