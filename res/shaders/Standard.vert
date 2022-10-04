#version 460 core

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_uv;

uniform float u_time;
uniform vec2 u_position;

out Varyings
{
    vec2 Position;
    vec2 UV;
} OUT;

void main()
{
    OUT.Position = a_position + u_position;
    OUT.UV = a_uv;
    gl_Position = vec4(OUT.Position, 0.0, 1.0);
}
