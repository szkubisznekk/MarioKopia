#version 460 core

in Varyings
{
    vec2 Position;
    vec2 UV;
} IN;

layout (location = 0) out vec4 Color;

layout (binding = 0) uniform sampler2D u_texture0;

void main()
{
    Color = texture(u_texture0, IN.UV);
}
