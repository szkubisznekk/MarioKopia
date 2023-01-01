#version 460 core

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_uv;

layout (std430, binding = 2) buffer TextInstanceTransformsSSBO
{
    vec4 Position[];
} InstanceTransforms;

out Varyings
{
    vec2 UV;
} OUT;

uniform mat4 u_projectionMatrix;

void main()
{
    OUT.UV = a_uv - vec2(0.0, InstanceTransforms.Position[gl_InstanceID].y);
    gl_Position = u_projectionMatrix * vec4(a_position + vec2(0.0, InstanceTransforms.Position[gl_InstanceID].x), 0.0, 1.0);
}
