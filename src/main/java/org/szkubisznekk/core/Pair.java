package org.szkubisznekk.core;

public class Pair<T, U>
{
	private final T m_first;
	private final U m_second;

	public Pair(T first, U second)
	{
		m_first = first;
		m_second = second;
	}

	public T getFirst()
	{
		return m_first;
	}

	public U getSecond()
	{
		return m_second;
	}
}
