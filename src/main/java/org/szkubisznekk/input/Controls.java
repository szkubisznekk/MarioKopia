package org.szkubisznekk.input;

import org.szkubisznekk.core.Action;

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

	/**
	 * Called when the interact button is pressed on the menu.
	 */
	public static ArrayList<Runnable> OnMenuInteract = new ArrayList<>();

	/**
	 * Called when the up button is pressed on the menu.
	 */
	public static ArrayList<Runnable> OnMenuUp = new ArrayList<>();

	/**
	 * Called when the down button is pressed on the menu.
	 */
	public static ArrayList<Runnable> OnMenuDown = new ArrayList<>();

	/**
	 * Called when the right button is pressed on the menu.
	 */
	public static ArrayList<Runnable> OnMenuRight = new ArrayList<>();

	/**
	 * Called when the left button is pressed on the menu.
	 */
	public static ArrayList<Runnable> OnMenuLeft = new ArrayList<>();

	private static float s_moveAD = 0.0f;
	private static float s_moveArrows = 0.0f;
	private static float s_moveGamepad = 0.0f;

	private Controls() {}

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
					Action.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.D ->
				{
					s_moveAD += 1.0f;
					Action.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.Left ->
				{
					s_moveArrows -= 1.0f;
					Action.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.Right ->
				{
					s_moveArrows += 1.0f;
					Action.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.W, InputCodes.Keys.Up -> Action.callAction(OnJump);
			}
		});

		Keyboard.get().OnKeyRelease.add((Integer key) ->
		{
			switch(key)
			{
				case InputCodes.Keys.A ->
				{
					s_moveAD += 1.0f;
					Action.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.D ->
				{
					s_moveAD -= 1.0f;
					Action.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.Left ->
				{
					s_moveArrows += 1.0f;
					Action.callAction(OnMove, getMove());
				}
				case InputCodes.Keys.Right ->
				{
					s_moveArrows -= 1.0f;
					Action.callAction(OnMove, getMove());
				}
			}
		});

		Gamepad.get().OnButtonPress.add((Integer button) ->
		{
			switch(button)
			{
				case InputCodes.GamepadButtons.South -> Action.callAction(OnJump);
				case InputCodes.GamepadButtons.Start -> Action.callAction(OnMenuToggle);
			}
		});

		Gamepad.get().OnAxis.add((Integer axis, Float value) ->
		{
			if(axis == InputCodes.GamepadAxes.LeftStickX)
			{
				s_moveGamepad = value;
				Action.callAction(OnMove, getMove());
			}
		});

		// Menu
		Keyboard.get().OnKeyPress.add((Integer key) ->
		{
			switch(key)
			{
				case InputCodes.Keys.Escape -> Action.callAction(OnMenuToggle);
				case InputCodes.Keys.Enter -> Action.callAction(OnMenuInteract);
				case InputCodes.Keys.W, InputCodes.Keys.Up -> Action.callAction(OnMenuUp);
				case InputCodes.Keys.S, InputCodes.Keys.Down -> Action.callAction(OnMenuDown);
				case InputCodes.Keys.D, InputCodes.Keys.Right -> Action.callAction(OnMenuRight);
				case InputCodes.Keys.A, InputCodes.Keys.Left -> Action.callAction(OnMenuLeft);
			}
		});

		Gamepad.get().OnButtonPress.add((Integer button) ->
		{
			switch(button)
			{
				case InputCodes.GamepadButtons.Start -> Action.callAction(OnMenuToggle);
				case InputCodes.GamepadButtons.South -> Action.callAction(OnMenuInteract);
				case InputCodes.GamepadButtons.DPadUp -> Action.callAction(OnMenuUp);
				case InputCodes.GamepadButtons.DPadDown -> Action.callAction(OnMenuDown);
				case InputCodes.GamepadButtons.DPadRight -> Action.callAction(OnMenuRight);
				case InputCodes.GamepadButtons.DPadLeft -> Action.callAction(OnMenuLeft);
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
