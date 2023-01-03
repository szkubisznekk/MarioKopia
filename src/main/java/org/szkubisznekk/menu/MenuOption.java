package org.szkubisznekk.menu;

import java.util.ArrayList;

/**
 * A menu option base class.
 */
public abstract class MenuOption
{
	/**
	 * Called when the interact button is pressed on the menu option.
	 */
	public ArrayList<Runnable> OnInteract = new ArrayList<>();

	private final String m_text;

	/**
	 * Sets the text on the button.
	 *
	 * @param text The text.
	 */
	public MenuOption(String text)
	{
		m_text = text;
	}

	/**
	 * Returns the text on the button.
	 *
	 * @return The text on the button.
	 */
	public String getText()
	{
		return m_text;
	}

	/**
	 * Returns the vertical offset of the texture in the texture atlas.
	 *
	 * @return The vertical offset of the texture in the texture atlas.
	 */
	public abstract float getTextureOffset();
}
