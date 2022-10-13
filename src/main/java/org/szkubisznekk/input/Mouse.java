package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse extends InputDevice
{
	private static Mouse s_instance;

	public ArrayList<Consumer<Integer>> OnButtonPress = new ArrayList<>();
	public ArrayList<Consumer<Integer>> OnButtonRelease = new ArrayList<>();

	public static Mouse get()
	{
		return s_instance;
	}

	@Override
	public void init(Window window)
	{
		super.init(window);
		s_instance = this;

		glfwSetInputMode(m_window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		glfwSetMouseButtonCallback(m_window.getHandle(), (long handle, int key, int action, int mods) ->
		{
			switch (action)
			{
				case Input.Actions.Press:
					for (var callback : OnButtonPress)
					{
						callback.accept(key);
					}
					break;
				case Input.Actions.Release:
					for (var callback : OnButtonRelease)
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
