package org.szkubisznekk.input;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Controls
{
	public static ArrayList<Consumer<Float>> OnMove = new ArrayList<>();
	public static ArrayList<Runnable> OnJump = new ArrayList<>();
	public static ArrayList<Runnable> OnMenu = new ArrayList<>();
	private static float m_moveAD = 0.0f;
	private static float m_moveArrows = 0.0f;
	private static float m_moveGamepad = 0.0f;

	public static void init()
	{
		Keyboard.get().OnKeyPress.add((Integer key) ->
		{
			switch (key)
			{
				case Input.Keys.A ->
				{
					m_moveAD -= 1.0f;
					onMove();
				}
				case Input.Keys.D ->
				{
					m_moveAD += 1.0f;
					onMove();
				}
				case Input.Keys.Left ->
				{
					m_moveArrows -= 1.0f;
					onMove();
				}
				case Input.Keys.Right ->
				{
					m_moveArrows += 1.0f;
					onMove();
				}
				case Input.Keys.W, Input.Keys.Up -> onJump();
				case Input.Keys.Escape -> onMenu();
			}
		});

		Keyboard.get().OnKeyRelease.add((Integer key) ->
		{
			switch (key)
			{
				case Input.Keys.A ->
				{
					m_moveAD += 1.0f;
					onMove();
				}
				case Input.Keys.D ->
				{
					m_moveAD -= 1.0f;
					onMove();
				}
				case Input.Keys.Left ->
				{
					m_moveArrows += 1.0f;
					onMove();
				}
				case Input.Keys.Right ->
				{
					m_moveArrows -= 1.0f;
					onMove();
				}
			}
		});

		Gamepad.get().OnButtonPress.add((Integer button) ->
		{
			switch (button)
			{
				case Input.GamepadButtons.South -> onJump();
				case Input.GamepadButtons.Start -> onMenu();
			}
		});

		Gamepad.get().OnAxis.add((Integer axis, Float value) ->
		{
			switch (axis)
			{
				case Input.GamepadAxes.LeftStickX ->
				{
					m_moveGamepad = value;
					onMove();
				}
			}
		});
	}

	public static float getMove()
	{
		float value = m_moveAD + m_moveArrows + m_moveGamepad;
		return Math.max(-1.0f, Math.min(1.0f, value));
	}

	public static boolean isJump()
	{
		return Keyboard.get().isKey(Input.Keys.W, Input.Actions.Press) || Keyboard.get().isKey(Input.Keys.Up, Input.Actions.Press)
			|| Gamepad.get().isButton(Input.GamepadButtons.South, Input.Actions.Press);
	}

	public static boolean isMenu()
	{
		return Keyboard.get().isKey(Input.Keys.Escape, Input.Actions.Press)
			|| Gamepad.get().isButton(Input.GamepadButtons.Start, Input.Actions.Press);
	}

	private static void onMove()
	{
		for (var callback : OnMove)
		{
			callback.accept(getMove());
		}
	}

	private static void onJump()
	{
		for (var callback : OnJump)
		{
			callback.run();
		}
	}

	private static void onMenu()
	{
		for (var callback : OnMenu)
		{
			callback.run();
		}
	}
}
