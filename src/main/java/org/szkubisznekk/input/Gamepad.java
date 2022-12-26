package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

/**
 * Provides callbacks and functions for a gamepad.
 */
public class Gamepad extends InputDevice
{
	private static Gamepad s_instance;

	/**
	 * Called when any button is pressed.
	 */
	public ArrayList<Consumer<Integer>> OnButtonPress = new ArrayList<>();

	/**
	 * Called when any button is released.
	 */
	public ArrayList<Consumer<Integer>> OnButtonRelease = new ArrayList<>();

	/**
	 * Called when any axis' value is changed.
	 */
	public ArrayList<BiConsumer<Integer, Float>> OnAxis = new ArrayList<>();

	/**
	 * Analog values smaller than this value will be ignored.
	 */
	public float Deadzone = 0.05f;

	private boolean m_connected;
	private final GLFWGamepadState m_state = GLFWGamepadState.calloc();
	private final GLFWGamepadState m_lastState = GLFWGamepadState.calloc();

	/**
	 * Returns the only instance of gamepad.
	 *
	 * @return The only instance of gamepad.
	 */
	public static Gamepad get()
	{
		return s_instance;
	}

	/**
	 * Sets up callbacks.
	 *
	 * @param window The window used to handle inputs.
	 */
	@Override
	void init(Window window)
	{
		super.init(window);
		s_instance = this;

		m_connected = glfwJoystickPresent(GLFW_JOYSTICK_1);

		glfwSetJoystickCallback((int id, int event) ->
		{
			switch(event)
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

	/**
	 * Checks whether any value has been changed since last time.
	 */
	@Override
	void update()
	{
		super.update();
		if(!m_connected)
		{
			return;
		}

		m_lastState.set(m_state);
		glfwGetGamepadState(GLFW_JOYSTICK_1, m_state);

		for(int button = 0; button < GLFW_GAMEPAD_BUTTON_LAST; button++)
		{
			int state = m_state.buttons(button);
			if(state != m_lastState.buttons(button))
			{
				switch(state)
				{
					case InputCodes.Actions.Press -> Utility.callAction(OnButtonPress, button);
					case InputCodes.Actions.Release -> Utility.callAction(OnButtonRelease, button);
				}
			}
		}

		for(int axis = 0; axis < GLFW_GAMEPAD_AXIS_LAST; axis++)
		{
			float state = m_state.axes(axis);
			if(state != m_lastState.axes(axis))
			{
				float value = m_state.axes(axis);
				Utility.callAction(OnAxis, axis, (axis < InputCodes.GamepadAxes.LeftTrigger) ? processStick(value) : processTrigger(value));
			}
		}
	}

	/**
	 * Returns whether a button is performing a specific action.
	 *
	 * @param button The button.
	 * @param action The action.
	 * @return Whether a button is performing a specific action.
	 */
	public boolean isButton(int button, int action)
	{
		return m_state.buttons(button) == action;
	}

	/**
	 * Returns an axis' current value.
	 *
	 * @param axis The axis.
	 * @return An axis' current value.
	 */
	public float getAxis(int axis)
	{
		float value = m_state.axes(axis);
		System.out.println(value);
		return (axis < InputCodes.GamepadAxes.LeftTrigger) ? processStick(value) : processTrigger(value);
	}

	/**
	 * Applies deadzone to an analog value.
	 *
	 * @param value The analog value.
	 * @return The modified value.
	 */
	private float processStick(float value)
	{
		float maxValue = 1.0f / (1.0f - Deadzone);
		float abs = Math.abs(value);
		float sign = Math.signum(value);

		if(abs < Deadzone)
		{
			return 0.0f;
		}

		abs -= Deadzone;
		abs = Math.max(abs, 0.0f);

		return abs * maxValue * sign;
	}

	/**
	 * Applies deadzone to an analog value and transform it from [-1.0, 1.0] to [0.0, 1.0].
	 *
	 * @param value The analog value.
	 * @return The modified value.
	 */
	private float processTrigger(float value)
	{
		value = (value + 1.0f) * 0.5f;

		float maxValue = 1.0f / (1.0f - Deadzone);
		float abs = Math.abs(value);

		if(abs < Deadzone)
		{
			return 0.0f;
		}

		abs -= Deadzone;
		abs = Math.max(abs, 0.0f);

		return abs * maxValue;
	}
}
