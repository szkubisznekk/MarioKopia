package org.szkubisznekk.menu;

public class Slider extends MenuOption
{
	private float m_value;
	private float m_tickValue;

	public Slider(String text, float value, int numTicks)
	{
		super(text);
		setValue(value);
		m_tickValue = 1f / (numTicks - 1);
	}

	public float getValue()
	{
		return m_value;
	}

	public void increment()
	{
		m_value += m_tickValue;
		setValue(m_value);
	}

	public void decrement()
	{
		m_value -= m_tickValue;
		setValue(m_value);
	}

	private void setValue(float value)
	{
		m_value = Math.min(Math.max(value, 0f), 1f);
	}

	@Override
	public float getTextureOffset()
	{
		return 0.25f;
	}
}
