package org.szkubisznekk.core;

import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		Application app = new Application();
		app.run();
		app.destruct();
	}
}