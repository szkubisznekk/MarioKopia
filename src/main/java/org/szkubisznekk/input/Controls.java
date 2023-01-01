package org.szkubisznekk.input;

import org.szkubisznekk.core.Utility;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Provides callbacks and functions for binds used by the application.
 */
public class Controls
{
	/**
	 * Called when the character move action is performed.
	 */
	public static ArrayList<Consumer<Float>> OnMove = new ArrayList<>();

	/**
	 * Called when the character jump button is pressed.
	 */
	public static ArrayList<Runnable> OnJump = new ArrayList<>();

	/**
	 * Called when the menu button is pressed.
	 */
	public static ArrayList<Runnable> OnMenuToggle = new ArrayList<>();
	public static ArrayList<Runnable> OnMenuInteract = new ArrayList<>();
	public static ArrayList<Runnable> OnMenuUp = new ArrayList<>();
	public static ArrayList<Runnable> OnMenuDown = new ArrayList<>();
	public static ArrayList<Runnable> OnMenuRight = new ArrayList<>();
	public static ArrayList<Runnable> OnMenuLeft = new ArrayList<>();

	private static float s_moveAD = 0.0f;
	private static float s_moveArrows = 0.0f;
	private static float s_moveGamepad = 0.0f;

	/**
	 * Sets callback.
	 */
	public static void init()
	{
		// Movement
		Keyboard.get().OnKeyPress.add((Integer key) ->
		{
			switch(key)
			{
				case InputCodes.Keys.A ->
				{
					s_moveAD -= 1.0f;
					Utility.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.D ->
				{
					s_moveAD += 1.0f;
					Utility.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.Left ->
				{
					s_moveArrows -= 1.0f;
					Utility.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.Right ->
				{
					s_moveArrows += 1.0f;
					Utility.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.W, InputCodes.Keys.Up -> Utility.callAction(OnJump);
			}
		});

		Keyboard.get().OnKeyRelease.add((Integer key) ->
		{
			switch(key)
			{
				case InputCodes.Keys.A ->
				{
					s_moveAD += 1.0f;
					Utility.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.D ->
				{
					s_moveAD -= 1.0f;
					Utility.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.Left ->
				{
					s_moveArrows += 1.0f;
					Utility.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.Right ->
				{
					s_moveArrows -= 1.0f;
					Utility.callAction(OnMove, getMove());
				}
			}
		});

		Gamepad.get().OnButtonPress.add((Integer button) ->
		{
			switch(button)
			{
				case InputCodes.GamepadButtons.South -> Utility.callAction(OnJump);
				case InputCodes.GamepadButtons.Start -> Utility.callAction(OnMenuToggle);
			}
		});

		Gamepad.get().OnAxis.add((Integer axis, Float value) ->
		{
			if(axis == InputCodes.GamepadAxes.LeftStickX)
			{
				s_moveGamepad = value;
				Utility.callAction(OnMove, getMove());
			}
		});

		// Menu
		Keyboard.get().OnKeyPress.add((Integer key) ->
		{
			switch(key)
			{
				case InputCodes.Keys.Escape -> Utility.callAction(OnMenuToggle);
				case InputCodes.Keys.Enter -> Utility.callAction(OnMenuInteract);
				case InputCodes.Keys.W, InputCodes.Keys.Up -> Utility.callAction(OnMenuUp);
				case InputCodes.Keys.S, InputCodes.Keys.Down -> Utility.callAction(OnMenuDown);
				case InputCodes.Keys.D, InputCodes.Keys.Right -> Utility.callAction(OnMenuRight);
				case InputCodes.Keys.A, InputCodes.Keys.Left -> Utility.callAction(OnMenuLeft);
			}
		});

		Gamepad.get().OnButtonPress.add((Integer button) ->
		{
			switch(button)
			{
				case InputCodes.GamepadButtons.Start -> Utility.callAction(OnMenuToggle);
				case InputCodes.GamepadButtons.South -> Utility.callAction(OnMenuInteract);
				case InputCodes.GamepadButtons.DPadUp -> Utility.callAction(OnMenuUp);
				case InputCodes.GamepadButtons.DPadDown -> Utility.callAction(OnMenuDown);
				case InputCodes.GamepadButtons.DPadRight -> Utility.callAction(OnMenuRight);
				case InputCodes.GamepadButtons.DPadLeft -> Utility.callAction(OnMenuLeft);
			}
		});
	}

	/**
	 * Returns the current value of the character move action.
	 *
	 * @return The current value of the character move action.
	 */
	public static float getMove()
	{
		float value = s_moveAD + s_moveArrows + s_moveGamepad;
		return Math.max(-1.0f, Math.min(1.0f, value));
	}

	/**
	 * Returns whether the character jump button is pressed.
	 *
	 * @return Whether the character jump button is pressed.
	 */
	public static boolean isJump()
	{
		return Keyboard.get().isKey(InputCodes.Keys.W, InputCodes.Actions.Press) || Keyboard.get().isKey(InputCodes.Keys.Up, InputCodes.Actions.Press)
			|| Gamepad.get().isButton(InputCodes.GamepadButtons.South, InputCodes.Actions.Press);
	}

	/**
	 * Returns whether the menu button is pressed.
	 *
	 * @return Whether the menu button is pressed.
	 */
	public static boolean isMenu()
	{
		return Keyboard.get().isKey(InputCodes.Keys.Escape, InputCodes.Actions.Press)
			|| Gamepad.get().isButton(InputCodes.GamepadButtons.Start, InputCodes.Actions.Press);
	}
}
