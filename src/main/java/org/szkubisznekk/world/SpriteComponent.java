package org.szkubisznekk.world;

/**
 * Stores data used by an object to be submitted to the renderer.
 */
public class SpriteComponent
{
	/**
	 * The z coordinate of the object.
	 */
	public float Depth;

	/**
	 * The id of the texture on the texture atlas.
	 */
	public byte TextureID;

	/**
	 * Sets the depth and texture id.
	 *
	 * @param depth     The depth.
	 * @param textureID The texture id.
	 */
	public SpriteComponent(float depth, byte textureID)
	{
		Depth = depth;
		TextureID = textureID;
	}
}
