package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Keyboard extends InputDevice
{
	private static Keyboard s_instance;

	public ArrayList<Consumer<Integer>> OnKeyPress = new ArrayList<>();
	public ArrayList<Consumer<Integer>> OnKeyRelease = new ArrayList<>();

	public static Keyboard get()
	{
		return s_instance;
	}

	@Override
	public void init(Window window)
	{
		super.init(window);
		s_instance = this;

		glfwSetKeyCallback(m_window.getHandle(), (long handle, int key, int scancode, int action, int mods) ->
		{
			switch(action)
			{
				case InputDeviceManager.Actions.Press ->
				{
					for(var callback : OnKeyPress)
					{
						callback.accept(key);
					}
				}
				case InputDeviceManager.Actions.Release ->
				{
					for(var callback : OnKeyRelease)
					{
						callback.accept(key);
					}
				}
			}
		});
	}

	@Override
	public void update()
	{
		super.update();
	}

	public boolean isKey(int key, int action)
	{
		return glfwGetKey(m_window.getHandle(), key) == action;
	}
}
