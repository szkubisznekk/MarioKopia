package org.szkubisznekk.core;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

/**
 * Action utility functions.
 */
public class Action
{
	/**
	 * Calls every callback in an action.
	 *
	 * @param action The action.
	 */
	public static void callAction(ArrayList<Runnable> action)
	{
		for(var callback : action)
		{
			callback.run();
		}
	}

	/**
	 * Calls every callback in an action.
	 *
	 * @param action The action.
	 * @param arg    Parameter passed to callbacks.
	 * @param <T>    The type of the parameter.
	 */
	public static <T> void callAction(ArrayList<Consumer<T>> action, T arg)
	{
		for(var callback : action)
		{
			callback.accept(arg);
		}
	}

	/**
	 * Calls every callback in an action.
	 *
	 * @param action The action.
	 * @param arg1   First parameter passed to callbacks.
	 * @param arg2   Second parameter passed to callbacks.
	 * @param <T>    The type of the first parameter.
	 * @param <U>    The type of the second parameter.
	 */
	public static <T, U> void callAction(ArrayList<BiConsumer<T, U>> action, T arg1, U arg2)
	{
		for(var callback : action)
		{
			callback.accept(arg1, arg2);
		}
	}
}
