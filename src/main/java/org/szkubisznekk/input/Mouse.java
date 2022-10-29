package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.function.Consumer;

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

		glfwSetMouseButtonCallback(m_window.getHandle(), (long handle, int button, int action, int mods) ->
		{
			switch(action)
			{
				case Input.Actions.Press ->
				{
					for(var callback : OnButtonPress)
					{
						callback.accept(button);
					}
				}
				case Input.Actions.Release ->
				{
					for(var callback : OnButtonRelease)
					{
						callback.accept(button);
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

	public boolean isButton(int button, int action)
	{
		return glfwGetMouseButton(m_window.getHandle(), button) == action;
	}
}
