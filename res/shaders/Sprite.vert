#version 460 core

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_uv;

layout (std140, binding = 0) uniform ProjectionParamsUBO
{
    mat4 ViewMatrix;
    mat4 ProjectionMatrix;
} ProjectionParams;

layout (std430, binding = 1) buffer InstanceTransformsSSBO
{
    vec2 Position[];
} InstanceTransforms;

out Varyings
{
    vec2 Position;
    vec2 UV;
} OUT;

uniform mat4 ViewMatrix;
uniform mat4 ProjectionMatrix;

void main()
{
    OUT.Position = a_position + InstanceTransforms.Position[gl_InstanceID];
    OUT.UV = a_uv;
    gl_Position = ProjectionParams.ProjectionMatrix * ProjectionParams.ViewMatrix * vec4(OUT.Position, 0, 1.0);
}
