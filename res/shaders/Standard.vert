#version 460 core

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_uv;

out Varyings {
    vec2 Position;
    vec2 UV;
} OUT;

void main()
{
    gl_Position = vec4(a_position, 0.0, 1.0);
    OUT.Position = a_position;
    OUT.UV = a_uv;
}
