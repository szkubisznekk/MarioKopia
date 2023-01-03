package org.szkubisznekk.menu;

import org.szkubisznekk.core.Pair;
import org.szkubisznekk.core.Utility;
import org.szkubisznekk.input.Controls;

import java.util.ArrayList;

public class Menu
{
	private static final ArrayList<MenuOption> s_options = new ArrayList<>();
	private static int s_selectedIndex = 0;
	private static boolean s_isShown = true;

	private Menu() {}

	public static void init()
	{
		Controls.OnMenuToggle.add(() ->
		{
			if(!s_isShown)
			{
				s_selectedIndex = 0;
			}
			s_isShown = !s_isShown;
		});
		Controls.OnMenuUp.add(() ->
		{
			if(!s_isShown)
			{
				return;
			}

			s_selectedIndex--;
			if(s_selectedIndex < 0)
			{
				s_selectedIndex += s_options.size();
			}
		});
		Controls.OnMenuDown.add(() ->
		{
			if(!s_isShown)
			{
				return;
			}

			s_selectedIndex++;
			if(s_selectedIndex >= s_options.size())
			{
				s_selectedIndex -= s_options.size();
			}
		});
		Controls.OnMenuRight.add(() ->
		{
			if(!s_isShown)
			{
				return;
			}

			MenuOption current = s_options.get(s_selectedIndex);
			if(current.getClass() == Slider.class)
			{
				Slider slider = (Slider)current;
				slider.increment();
				Utility.callAction(slider.OnInteract);
			}
		});
		Controls.OnMenuLeft.add(() ->
		{
			if(!s_isShown)
			{
				return;
			}

			MenuOption current = s_options.get(s_selectedIndex);
			if(current.getClass() == Slider.class)
			{
				Slider slider = (Slider)current;
				slider.decrement();
				Utility.callAction(slider.OnInteract);
			}
		});
		Controls.OnMenuInteract.add(() ->
		{
			if(!s_isShown)
			{
				return;
			}

			MenuOption current = s_options.get(s_selectedIndex);
			Utility.callAction(current.OnInteract);
		});
	}

	public static int getNumberOfOptions()
	{
		return s_options.size();
	}

	public static Pair<MenuOption, Boolean> getOption(int index)
	{
		return new Pair<>(s_options.get(index), index == s_selectedIndex);
	}

	public static boolean isShown()
	{
		return s_isShown;
	}

	public static void setShown(boolean isShown)
	{
		s_isShown = isShown;
	}

	public static void addOption(MenuOption option)
	{
		s_options.add(option);
	}
}
