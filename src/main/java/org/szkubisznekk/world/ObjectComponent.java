package org.szkubisznekk.world;

/**
 * Stores the type of the object.
 */
public class ObjectComponent
{
	/**
	 * The type of the object.
	 */
	public enum Type
	{
		FinishLine,
		Coin
	}

	private final Type m_type;

	/**
	 * Sets the type.
	 *
	 * @param type The type.
	 */
	public ObjectComponent(Type type)
	{
		m_type = type;
	}

	/**
	 * Returns the type.
	 *
	 * @return The type.
	 */
	public Type getType()
	{
		return m_type;
	}
}
