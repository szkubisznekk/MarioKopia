package org.szkubisznekk.world;

public class ObjectComponent
{
	public enum Type
	{
		FinishLine,
		Coin
	}

	private final Type m_type;

	public ObjectComponent(Type type)
	{
		m_type = type;
	}

	public Type getType()
	{
		return m_type;
	}
}
