#version 460 core

in Varyings
{
    vec2 Position;
    vec2 UV;
    int TextureID;
} IN;

layout (location = 0) out vec4 Color;

layout (binding = 1) uniform sampler2D u_textAtlas;

void main()
{
    Color = texture(u_textAtlas, IN.UV);
}
