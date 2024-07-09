package me.jade.ecs.components;

import me.jade.renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    private Texture texture;
    private List<Sprite> sprites;

    private int spriteWidth;
    private int spriteHeight;
    private int numSprites;
    private int spacing;

    public Spritesheet (Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numSprites = numSprites;
        this.spacing = spacing;

        generateSprites();
    }

    public void generateSprites() {
        //get bottom left coord
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for (int i = 0; i < numSprites; i++) {
            float bottomY = currentY / (float) texture.getHeight();
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float leftX = currentX / (float) texture.getWidth();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();

            Vector2f[] texCoords = new Vector2f[4];
            texCoords[0] = new Vector2f(leftX, bottomY);
            texCoords[1] = new Vector2f(rightX, bottomY);
            texCoords[2] = new Vector2f(leftX, topY);
            texCoords[3] = new Vector2f(rightX, topY);

            sprites.add(new Sprite(this.texture, texCoords));

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }
}
