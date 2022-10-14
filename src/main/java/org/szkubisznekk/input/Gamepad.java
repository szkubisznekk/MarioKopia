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
	public float Deadzone = 0.05f;

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

		for (int button = 0; button < GLFW_GAMEPAD_BUTTON_LAST; button++)
		{
			int state = m_state.buttons(button);
			if (state != m_lastState.buttons(button))
			{
				switch (state)
				{
					case Input.Actions.Press ->
					{
						for (var callback : OnButtonPress)
						{
							callback.accept(button);
						}
					}
					case Input.Actions.Release ->
					{
						for (var callback : OnButtonRelease)
						{
							callback.accept(button);
						}
					}
				}
			}
		}

		for (int axis = 0; axis < GLFW_GAMEPAD_AXIS_LAST; axis++)
		{
			float state = m_state.axes(axis);
			if (state != m_lastState.axes(axis))
			{
				for (var callback : OnAxis)
				{
					float value = m_state.axes(axis);
					callback.accept(axis, (axis < Input.GamepadAxes.LeftTrigger) ? processStick(value) : processTrigger(value));
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
		float value = m_state.axes(axis);
		System.out.println(value);
		return (axis < Input.GamepadAxes.LeftTrigger) ? processStick(value) : processTrigger(value);
	}

	private float processStick(float value)
	{
		float maxValue = 1.0f / (1.0f - Deadzone);
		float abs = Math.abs(value);
		float sign = Math.signum(value);

		if (abs < Deadzone)
		{
			return 0.0f;
		}

		abs -= Deadzone;
		abs = Math.max(abs, 0.0f);

		return abs * maxValue * sign;
	}

	private float processTrigger(float value)
	{
		value = (value + 1.0f) * 0.5f;

		float maxValue = 1.0f / (1.0f - Deadzone);
		float abs = Math.abs(value);

		if (abs < Deadzone)
		{
			return 0.0f;
		}

		abs -= Deadzone;
		abs = Math.max(abs, 0.0f);

		return abs * maxValue;
	}
}
