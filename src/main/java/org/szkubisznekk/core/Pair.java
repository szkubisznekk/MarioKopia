package org.szkubisznekk.core;

/**
 * Stores two variables.
 *
 * @param <T> First variable's type.
 * @param <U> Second variable's type.
 */
public class Pair<T, U>
{
	private final T m_first;
	private final U m_second;

	/**
	 * Sets the two variables.
	 *
	 * @param first
	 * @param second
	 */
	public Pair(T first, U second)
	{
		m_first = first;
		m_second = second;
	}

	/**
	 * Returns the first variable.
	 *
	 * @return The first variable.
	 */
	public T getFirst()
	{
		return m_first;
	}

	/**
	 * Returns the second variable.
	 *
	 * @return The second variable.
	 */
	public U getSecond()
	{
		return m_second;
	}
}
