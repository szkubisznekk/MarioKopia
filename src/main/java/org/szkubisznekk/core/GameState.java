package org.szkubisznekk.core;

import java.io.*;
import java.util.Properties;

/**
 * Stores global game states.
 */
public class GameState
{
	/**
	 * Number of forints collected through-out the game,
	 */
	public static int Forints = 0;

	/**
	 * Index of the last finished map.
	 */
	public static int LastFinishedMap = 0;

	/**
	 * Player skin index.
	 */
	public static byte PlayerSkin = 0;

	/**
	 * Loads game state from file.
	 */
	public static void load()
	{
		try
		{
			FileInputStream propsInput = new FileInputStream("gamestate.properties");
			Properties prop = new Properties();
			prop.load(propsInput);

			propsInput.close();

			Forints = Integer.parseInt(prop.getProperty("Forints"));
			LastFinishedMap = Integer.parseInt(prop.getProperty("LastFinishedMap"));
			PlayerSkin = Byte.parseByte(prop.getProperty("PlayerSkin"));
		}
		catch(Exception ignored) {}
	}

	/**
	 * Saves the state to file.
	 */
	public static void save()
	{
		try
		{
			Properties prop = new Properties();

			prop.setProperty("Forints", String.valueOf(Forints));
			prop.setProperty("LastFinishedMap", String.valueOf(LastFinishedMap));
			prop.setProperty("PlayerSkin", String.valueOf(PlayerSkin));

			FileOutputStream propsOutput = new FileOutputStream("gamestate.properties");
			prop.store(propsOutput, null);

			propsOutput.close();
		}
		catch(Exception ignored) {}
	}
}
