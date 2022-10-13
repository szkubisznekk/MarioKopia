package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;

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
			switch (action)
			{
				case Input.Actions.Press:
					for (var callback : OnKeyPress)
					{
						callback.accept(key);
					}
					break;
				case Input.Actions.Release:
					for (var callback : OnKeyRelease)
					{
						callback.accept(key);
					}
					break;
			}
		});
	}

	@Override
	public void update()
	{
		super.update();
	}
}
