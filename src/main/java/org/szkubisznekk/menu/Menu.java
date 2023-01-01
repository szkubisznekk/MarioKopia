package org.szkubisznekk.menu;

import org.szkubisznekk.core.Pair;
import org.szkubisznekk.core.Utility;
import org.szkubisznekk.input.Controls;

import java.util.ArrayList;

public class Menu
{
	private final ArrayList<MenuOption> m_options = new ArrayList<>();
	private int m_selectedIndex = 0;
	private boolean m_isShown = true;

	public Menu()
	{
		Controls.OnMenuToggle.add(() ->
		{
			if(!m_isShown)
			{
				m_selectedIndex = 0;
			}
			m_isShown = !m_isShown;
		});
		Controls.OnMenuUp.add(() ->
		{
			if(!m_isShown)
			{
				return;
			}

			m_selectedIndex--;
			if(m_selectedIndex < 0)
			{
				m_selectedIndex += m_options.size();
			}
		});
		Controls.OnMenuDown.add(() ->
		{
			if(!m_isShown)
			{
				return;
			}

			m_selectedIndex++;
			if(m_selectedIndex >= m_options.size())
			{
				m_selectedIndex -= m_options.size();
			}
		});
		Controls.OnMenuRight.add(() ->
		{
			if(!m_isShown)
			{
				return;
			}

			MenuOption current = m_options.get(m_selectedIndex);
			if(current.getClass() == Slider.class)
			{
				Slider slider = (Slider)current;
				slider.setValue(slider.getValue() + 0.1f);
				Utility.callAction(slider.OnInteract);
			}
		});
		Controls.OnMenuLeft.add(() ->
		{
			if(!m_isShown)
			{
				return;
			}

			MenuOption current = m_options.get(m_selectedIndex);
			if(current.getClass() == Slider.class)
			{
				Slider slider = (Slider)current;
				slider.setValue(slider.getValue() - 0.1f);
				Utility.callAction(slider.OnInteract);
			}
		});
		Controls.OnMenuInteract.add(() ->
		{
			if(!m_isShown)
			{
				return;
			}

			MenuOption current = m_options.get(m_selectedIndex);
			Utility.callAction(current.OnInteract);
		});
	}

	public int getNumberOfOptions()
	{
		return m_options.size();
	}

	public Pair<MenuOption, Boolean> getOption(int index)
	{
		return new Pair<>(m_options.get(index), index == m_selectedIndex);
	}

	public boolean isShown()
	{
		return m_isShown;
	}

	public void setShown(boolean isShown)
	{
		m_isShown = isShown;
	}

	public void addOption(MenuOption option)
	{
		m_options.add(option);
	}
}
