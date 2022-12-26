#version 460 core

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_uv;

layout (std430, binding = 1) buffer TextInstanceTransformsSSBO
{
    vec4 Position[];
} InstanceTransforms;

out Varyings
{
    vec2 Position;
    vec2 UV;
    int TextureID;
} OUT;

uniform mat4 u_textProjectionMatrix;

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
    OUT.UV = getUV(OUT.TextureID);
    gl_Position = u_textProjectionMatrix * vec4(OUT.Position, InstanceTransforms.Position[gl_InstanceID].z, 1.0);
}
