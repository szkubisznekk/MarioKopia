#version 460 core

in Varyings
{
    vec2 Position;
    vec2 UV;
    int TextureID;
} IN;

layout (location = 0) out vec4 Color;

layout (binding = 0) uniform sampler2D u_textureAtlas;

void main()
{
    Color = texture(u_textureAtlas, IN.UV);
}
