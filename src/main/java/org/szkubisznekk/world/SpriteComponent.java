package org.szkubisznekk.world;

public class SpriteComponent
{
	public float Depth = 0.0f;
	public byte TextureID = 0;

	public SpriteComponent() {}

	public SpriteComponent(float depth, byte textureID)
	{
		Depth = depth;
		TextureID = textureID;
	}
}
