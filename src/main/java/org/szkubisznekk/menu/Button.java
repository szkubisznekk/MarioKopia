package org.szkubisznekk.menu;

/**
 * A button in the menu.
 */
public class Button extends MenuOption
{
	/**
	 * Sets the text of the button.
	 *
	 * @param text The text.
	 */
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
