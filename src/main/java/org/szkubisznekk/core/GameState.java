package org.szkubisznekk.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class GameState
{
	public static int Forints = 0;
	public static int LastFinishedMap = 0;
	public static byte PlayerSkin = 0;

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
