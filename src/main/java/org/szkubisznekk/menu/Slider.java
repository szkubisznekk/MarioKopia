package org.szkubisznekk.menu;

/**
 * A slider in the menu.
 */
public class Slider extends MenuOption
{
	private float m_value;
	private final float m_tickValue;

	/**
	 * Sets the text of the button.
	 *
	 * @param text     The text.
	 * @param value    The start value of the slider.
	 * @param numTicks The number of ticks in the slider.
	 */
	public Slider(String text, float value, int numTicks)
	{
		super(text);
		clamp();
		m_tickValue = 1f / (numTicks - 1);
	}

	/**
	 * Returns the current value.
	 *
	 * @return The current value.
	 */
	public float getValue()
	{
		return m_value;
	}

	/**
	 * Increment the value with one tick.
	 */
	public void increment()
	{
		m_value += m_tickValue;
		clamp();
	}

	/**
	 * Decrement the value with one tick.
	 */
	public void decrement()
	{
		m_value -= m_tickValue;
		clamp();
	}

	/**
	 * Clamps the value to [0, 1].
	 */
	private void clamp()
	{
		m_value = Math.min(Math.max(m_value, 0f), 1f);
	}

	@Override
	public float getTextureOffset()
	{
		return 0.25f;
	}
}
