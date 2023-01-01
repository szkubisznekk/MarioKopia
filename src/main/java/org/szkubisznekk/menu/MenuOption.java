package org.szkubisznekk.menu;

import java.util.ArrayList;

public abstract class MenuOption
{
	public ArrayList<Runnable> OnInteract = new ArrayList<>();

	private String m_text;

	public MenuOption(String text)
	{
		m_text = text;
	}

	public String getText()
	{
		return m_text;
	}

	public abstract float getTextureOffset();
}
