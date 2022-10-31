#version 460 core

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_uv;

layout (std140, binding = 0) uniform ProjectionParamsUBO
{
    mat4 ViewMatrix;
    mat4 ProjectionMatrix;
} ProjectionParams;

layout (std430, binding = 0) buffer InstanceTransformsSSBO
{
    vec4 Position[];
} InstanceTransforms;

out Varyings
{
    vec2 Position;
    vec2 UV;
    int TextureID;
} OUT;

uniform mat4 ViewMatrix;
uniform mat4 ProjectionMatrix;

vec2 getUV(int textureID)
{
    float size = 1.0 / 8.0;
    int x = textureID % 8;
    int y = 7 - (textureID / 8);
    return (vec2(x, y) + a_uv) * size;
}

void main()
{
    OUT.Position = a_position + InstanceTransforms.Position[gl_InstanceID].xy;
    OUT.TextureID = int(InstanceTransforms.Position[gl_InstanceID].w);
    OUT.UV = getUV(OUT.TextureID - 1);
    gl_Position = ProjectionParams.ProjectionMatrix * ProjectionParams.ViewMatrix * vec4(OUT.Position, InstanceTransforms.Position[gl_InstanceID].z, 1.0);
}
