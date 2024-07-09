package me.jade.ecs.components;

import me.jade.ecs.Component;
import me.jade.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Vector2f[] texCoords;
    private Texture texture;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.texture = null;
    }

    public SpriteRenderer(Texture texture) {
        this.color = new Vector4f(1);
        this.texture = texture;
    }

    public SpriteRenderer(Vector4f color, Texture texture) {
        this.color = color;
        this.texture = texture;
        this.texCoords = new Vector2f[4];
    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getUVCoords() {
        if (texCoords == null) {
            texCoords = new Vector2f[]{
                    new Vector2f(0, 0),
                    new Vector2f(1, 0),
                    new Vector2f(0, 1),
                    new Vector2f(1, 1)
            };
        }

        return texCoords;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }
}
