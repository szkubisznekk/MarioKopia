package org.szkubisznekk.menu;

public class Button extends MenuOption
{
	public Button(String text)
	{
		super(text);
	}

	@Override
	public float getTextureOffset()
	{
		return 0f;
	}
}
