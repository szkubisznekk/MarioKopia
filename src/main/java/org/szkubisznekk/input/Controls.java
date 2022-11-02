package org.szkubisznekk.input;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Controls
{
	public static ArrayList<Consumer<Float>> OnMove = new ArrayList<>();
	public static ArrayList<Runnable> OnJump = new ArrayList<>();
	public static ArrayList<Runnable> OnMenu = new ArrayList<>();

	private static float s_moveAD = 0.0f;
	private static float s_moveArrows = 0.0f;
	private static float s_moveGamepad = 0.0f;

	public static void init()
	{
		Keyboard.get().OnKeyPress.add((Integer key) ->
		{
			switch(key)
			{
				case InputDeviceManager.Keys.A ->
				{
					s_moveAD -= 1.0f;
					onMove();
				}
				case InputDeviceManager.Keys.D ->
				{
					s_moveAD += 1.0f;
					onMove();
				}
				case InputDeviceManager.Keys.Left ->
				{
					s_moveArrows -= 1.0f;
					onMove();
				}
				case InputDeviceManager.Keys.Right ->
				{
					s_moveArrows += 1.0f;
					onMove();
				}
				case InputDeviceManager.Keys.W, InputDeviceManager.Keys.Up -> onJump();
				case InputDeviceManager.Keys.Escape -> onMenu();
			}
		});

		Keyboard.get().OnKeyRelease.add((Integer key) ->
		{
			switch(key)
			{
				case InputDeviceManager.Keys.A ->
				{
					s_moveAD += 1.0f;
					onMove();
				}
				case InputDeviceManager.Keys.D ->
				{
					s_moveAD -= 1.0f;
					onMove();
				}
				case InputDeviceManager.Keys.Left ->
				{
					s_moveArrows += 1.0f;
					onMove();
				}
				case InputDeviceManager.Keys.Right ->
				{
					s_moveArrows -= 1.0f;
					onMove();
				}
			}
		});

		Gamepad.get().OnButtonPress.add((Integer button) ->
		{
			switch(button)
			{
				case InputDeviceManager.GamepadButtons.South -> onJump();
				case InputDeviceManager.GamepadButtons.Start -> onMenu();
			}
		});

		Gamepad.get().OnAxis.add((Integer axis, Float value) ->
		{
			if(axis == InputDeviceManager.GamepadAxes.LeftStickX)
			{
				s_moveGamepad = value;
				onMove();
			}
		});
	}

	public static float getMove()
	{
		float value = s_moveAD + s_moveArrows + s_moveGamepad;
		return Math.max(-1.0f, Math.min(1.0f, value));
	}

	public static boolean isJump()
	{
		return Keyboard.get().isKey(InputDeviceManager.Keys.W, InputDeviceManager.Actions.Press) || Keyboard.get().isKey(InputDeviceManager.Keys.Up, InputDeviceManager.Actions.Press)
			|| Gamepad.get().isButton(InputDeviceManager.GamepadButtons.South, InputDeviceManager.Actions.Press);
	}

	public static boolean isMenu()
	{
		return Keyboard.get().isKey(InputDeviceManager.Keys.Escape, InputDeviceManager.Actions.Press)
			|| Gamepad.get().isButton(InputDeviceManager.GamepadButtons.Start, InputDeviceManager.Actions.Press);
	}

	private static void onMove()
	{
		for(var callback : OnMove)
		{
			callback.accept(getMove());
		}
	}

	private static void onJump()
	{
		for(var callback : OnJump)
		{
			callback.run();
		}
	}

	private static void onMenu()
	{
		for(var callback : OnMenu)
		{
			callback.run();
		}
	}
}
