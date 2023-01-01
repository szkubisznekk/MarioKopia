package org.szkubisznekk.menu;

public class Slider extends MenuOption
{
	private float m_value;

	public Slider(String text, float value)
	{
		super(text);
		setValue(value);
	}

	public float getValue()
	{
		return m_value;
	}

	public void setValue(float value)
	{
		m_value = Math.min(Math.max(value, 0f), 1f);
	}

	@Override
	public float getTextureOffset()
	{
		return 0.25f;
	}
}
