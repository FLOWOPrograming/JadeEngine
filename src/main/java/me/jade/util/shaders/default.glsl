#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec2 aUV;
layout(location = 3) in float aTexID;

uniform mat4 uProjection;
uniform mat4 uView;


out vec4 fColor;
out vec2 fUV;
out float fTexID;

void main() {
    fColor = aColor;
    fUV = aUV;
    fTexID = aTexID;

    gl_Position = uProjection * uView * vec4(aPos, 1.0, 1.0);
}

#type frag
#version 330 core

in vec4 fColor;
in vec2 fUV;
in float fTexID;

uniform sampler2D uTexture[8];

out vec4 color;

void main() {
    if (fTexID > 0) {
        int id = int(fTexID);
        color = fColor * texture(uTexture[id], fUV);
    } else {
        color = fColor;
    }

}

