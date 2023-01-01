#version 460 core

in Varyings
{
    vec2 UV;
} IN;

layout (location = 0) out vec4 Color;

layout (binding = 2) uniform sampler2D u_menuAtlas;

void main()
{
    Color = texture(u_menuAtlas, IN.UV);
}
