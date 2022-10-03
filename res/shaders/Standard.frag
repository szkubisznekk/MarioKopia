#version 460 core

in Varyings {
    vec2 Position;
    vec2 UV;
} IN;

layout (location = 0) out vec4 Color;

void main()
{
    Color = vec4(IN.UV, 0.0, 1.0);
}
