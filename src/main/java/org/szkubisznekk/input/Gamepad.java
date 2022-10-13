package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import org.lwjgl.glfw.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

import static org.lwjgl.glfw.GLFW.*;

public class Gamepad extends InputDevice
{
	private static Gamepad s_instance;

	public ArrayList<Consumer<Integer>> OnButtonPress = new ArrayList<>();
	public ArrayList<Consumer<Integer>> OnButtonRelease = new ArrayList<>();
	public ArrayList<BiConsumer<Integer, Float>> OnAxis = new ArrayList<>();

	private boolean m_connected;
	private final GLFWGamepadState m_state = GLFWGamepadState.calloc();
	private final GLFWGamepadState m_lastState = GLFWGamepadState.calloc();

	public static Gamepad get()
	{
		return s_instance;
	}

	@Override
	public void init(Window window)
	{
		super.init(window);
		s_instance = this;

		m_connected = glfwJoystickPresent(GLFW_JOYSTICK_1);

		glfwSetJoystickCallback((int id, int event) ->
		{
			switch (event)
			{
				case GLFW_CONNECTED ->
				{
					glfwGetGamepadState(GLFW_JOYSTICK_1, m_lastState);
					m_lastState.set(m_state);
					m_connected = true;
				}
				case GLFW_DISCONNECTED -> m_connected = false;
			}
		});
	}

	@Override
	public void update()
	{
		super.update();
		if (!m_connected)
		{
			return;
		}

		m_lastState.set(m_state);
		glfwGetGamepadState(GLFW_JOYSTICK_1, m_state);

		for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++)
		{
			int state = m_state.buttons(i);
			if (state != m_lastState.buttons(i))
			{
				switch (state)
				{
					case Input.Actions.Press:
						for (var callback : OnButtonPress)
						{
							callback.accept(i);
						}
						break;
					case Input.Actions.Release:
						for (var callback : OnButtonRelease)
						{
							callback.accept(i);
						}
						break;
				}
			}
		}

		for (int i = 0; i < GLFW_GAMEPAD_AXIS_LAST; i++)
		{
			float state = m_state.axes(i);
			if (state != m_lastState.axes(i))
			{
				for (var callback : OnAxis)
				{
					callback.accept(i, state);
				}
			}
		}
	}

	public boolean isButton(int button, int action)
	{
		return m_state.buttons(button) == action;
	}

	public float getAxis(int axis)
	{
		return m_state.axes(axis);
	}
}
