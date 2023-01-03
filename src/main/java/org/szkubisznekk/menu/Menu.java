package org.szkubisznekk.menu;

import org.szkubisznekk.core.Pair;
import org.szkubisznekk.core.Action;
import org.szkubisznekk.input.Controls;

import java.util.ArrayList;

/**
 * The main menu.
 */
public class Menu
{
	private static final ArrayList<MenuOption> s_options = new ArrayList<>();
	private static int s_selectedIndex = 0;
	private static boolean s_isShown = true;

	private Menu() {}

	/**
	 * Sets callbacks.
	 */
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
				Action.callAction(slider.OnInteract);
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
				Action.callAction(slider.OnInteract);
			}
		});
		Controls.OnMenuInteract.add(() ->
		{
			if(!s_isShown)
			{
				return;
			}

			MenuOption current = s_options.get(s_selectedIndex);
			Action.callAction(current.OnInteract);
		});
	}

	/**
	 * Returns the number of menu options.
	 *
	 * @return The number of menu options.
	 */
	public static int getNumberOfOptions()
	{
		return s_options.size();
	}

	/**
	 * Returns a menu option with the given index.
	 *
	 * @param index The index.
	 * @return A menu option.
	 */
	public static Pair<MenuOption, Boolean> getOption(int index)
	{
		return new Pair<>(s_options.get(index), index == s_selectedIndex);
	}

	/**
	 * Returns whether the menu is shown.
	 *
	 * @return Whether the menu is shown.
	 */
	public static boolean isShown()
	{
		return s_isShown;
	}

	/**
	 * Shows/hides the menu.
	 *
	 * @param isShown The new visibility of the menu.
	 */
	public static void setShown(boolean isShown)
	{
		s_isShown = isShown;
	}

	/**
	 * Adds a new menu option.
	 *
	 * @param option The new menu option.
	 */
	public static void addOption(MenuOption option)
	{
		s_options.add(option);
	}
}
