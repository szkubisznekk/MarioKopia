package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Provides callbacks and functions for a keyboard.
 */
public class Keyboard extends InputDevice
{
	private static Keyboard s_instance;

	/**
	 * Called when any key is pressed.
	 */
	public ArrayList<Consumer<Integer>> OnKeyPress = new ArrayList<>();

	/**
	 * Called when any key is pressed.
	 */
	public ArrayList<Consumer<Integer>> OnKeyRelease = new ArrayList<>();

	/**
	 * Returns the only instance of keyboard.
	 * @return The only instance of keyboard.
	 */
	public static Keyboard get()
	{
		return s_instance;
	}

	/**
	 * Sets up callbacks.
	 * @param window The window used to handle inputs.
	 */
	@Override
	void init(Window window)
	{
		super.init(window);
		s_instance = this;

		glfwSetKeyCallback(m_window.getHandle(), (long handle, int key, int scancode, int action, int mods) ->
		{
			switch(action)
			{
				case InputCodes.Actions.Press ->
				{
					for(var callback : OnKeyPress)
					{
						callback.accept(key);
					}
				}
				case InputCodes.Actions.Release ->
				{
					for(var callback : OnKeyRelease)
					{
						callback.accept(key);
					}
				}
			}
		});
	}

	/**
	 * Does nothing.
	 */
	@Override
	void update()
	{
		super.update();
	}

	/**
	 * Returns whether a key is performing a specific action.
	 * @param key The key.
	 * @param action The action.
	 * @return Whether a key is performing a specific action.
	 */
	public boolean isKey(int key, int action)
	{
		return glfwGetKey(m_window.getHandle(), key) == action;
	}
}
