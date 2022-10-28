package org.szkubisznekk.world;

public class SpriteComponent
{
	public int TextureID = 0;
	public float Depth = 0.0f;

	public SpriteComponent() {}

	public SpriteComponent(int textureID, float depth)
	{
		TextureID = textureID;
		Depth = depth;
	}
}
