package org.szkubisznekk.input;

import org.szkubisznekk.core.*;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

public class Input
{
	public static class Keys
	{
		public static final int Space = 32;
		public static final int Apostrophe = 39;
		public static final int Comma = 44;
		public static final int Minus = 45;
		public static final int Period = 46;
		public static final int Slash = 47;
		public static final int Alpha0 = 48;
		public static final int Alpha1 = 49;
		public static final int Alpha2 = 50;
		public static final int Alpha3 = 51;
		public static final int Alpha4 = 52;
		public static final int Alpha5 = 53;
		public static final int Alpha6 = 54;
		public static final int Alpha7 = 55;
		public static final int Alpha8 = 56;
		public static final int Alpha9 = 57;
		public static final int Semicolon = 59;
		public static final int Equal = 61;
		public static final int A = 65;
		public static final int B = 66;
		public static final int C = 67;
		public static final int D = 68;
		public static final int E = 69;
		public static final int F = 70;
		public static final int G = 71;
		public static final int H = 72;
		public static final int I = 73;
		public static final int J = 74;
		public static final int K = 75;
		public static final int L = 76;
		public static final int M = 77;
		public static final int N = 78;
		public static final int O = 79;
		public static final int P = 80;
		public static final int Q = 81;
		public static final int R = 82;
		public static final int S = 83;
		public static final int T = 84;
		public static final int U = 85;
		public static final int V = 86;
		public static final int W = 87;
		public static final int X = 88;
		public static final int Y = 89;
		public static final int Z = 90;
		public static final int LeftBracket = 91;
		public static final int Backslash = 92;
		public static final int RightBracket = 93;
		public static final int GRAVEAccent = 96;
		public static final int World1 = 161;
		public static final int World2 = 162;
		public static final int Escape = 256;
		public static final int Enter = 257;
		public static final int Tab = 258;
		public static final int Backspace = 259;
		public static final int Insert = 260;
		public static final int Delete = 261;
		public static final int Right = 262;
		public static final int Left = 263;
		public static final int Down = 264;
		public static final int Up = 265;
		public static final int PageUp = 266;
		public static final int PageDown = 267;
		public static final int Home = 268;
		public static final int End = 269;
		public static final int CapsLock = 280;
		public static final int ScrollLock = 281;
		public static final int NumLock = 282;
		public static final int PrintScreen = 283;
		public static final int Pause = 284;
		public static final int F1 = 290;
		public static final int F2 = 291;
		public static final int F3 = 292;
		public static final int F4 = 293;
		public static final int F5 = 294;
		public static final int F6 = 295;
		public static final int F7 = 296;
		public static final int F8 = 297;
		public static final int F9 = 298;
		public static final int F10 = 299;
		public static final int F11 = 300;
		public static final int F12 = 301;
		public static final int F13 = 302;
		public static final int F14 = 303;
		public static final int F15 = 304;
		public static final int F16 = 305;
		public static final int F17 = 306;
		public static final int F18 = 307;
		public static final int F19 = 308;
		public static final int F20 = 309;
		public static final int F21 = 310;
		public static final int F22 = 311;
		public static final int F23 = 312;
		public static final int F24 = 313;
		public static final int F25 = 314;
		public static final int Numpad0 = 320;
		public static final int Numpad1 = 321;
		public static final int Numpad2 = 322;
		public static final int Numpad3 = 323;
		public static final int Numpad4 = 324;
		public static final int Numpad5 = 325;
		public static final int Numpad6 = 326;
		public static final int Numpad7 = 327;
		public static final int Numpad8 = 328;
		public static final int Numpad9 = 329;
		public static final int NumpadDecimal = 330;
		public static final int NumpadDivide = 331;
		public static final int NumpadMultiply = 332;
		public static final int NumpadSubtract = 333;
		public static final int NumpadAdd = 334;
		public static final int NumpadEnter = 335;
		public static final int NumpadEqual = 336;
		public static final int LeftShift = 340;
		public static final int LeftControl = 341;
		public static final int LeftAlt = 342;
		public static final int LeftSuper = 343;
		public static final int RightShift = 344;
		public static final int RightControl = 345;
		public static final int RightAlt = 346;
		public static final int RightSuper = 347;
		public static final int Menu = 348;
	}

	public static class MouseButtons
	{
		public static final int Button1 = 0;
		public static final int Button2 = 1;
		public static final int Button3 = 2;
		public static final int Button4 = 3;
		public static final int Button5 = 4;
		public static final int Button6 = 5;
		public static final int Button7 = 6;
		public static final int Button8 = 7;
	}

	public static class GamepadButtons
	{
		public static final int South = 0;
		public static final int East = 1;
		public static final int West = 2;
		public static final int North = 3;
		public static final int LeftBumper = 4;
		public static final int RightBumper = 5;
		public static final int Back = 6;
		public static final int Start = 7;
		public static final int Guide = 8;
		public static final int LeftThumb = 9;
		public static final int RightThumb = 10;
		public static final int DPadUp = 11;
		public static final int DPadRight = 12;
		public static final int DPadDown = 13;
		public static final int DPadLeft = 14;

		public static final int A = South;
		public static final int B = East;
		public static final int X = West;
		public static final int Y = North;
		public static final int Cross = South;
		public static final int Circle = East;
		public static final int Square = West;
		public static final int Triangle = North;
	}

	public static class GamepadAxes
	{
		public static final int LeftStickX = 0;
		public static final int LeftStickY = 1;
		public static final int RightStickX = 2;
		public static final int RightStickY = 3;
		public static final int LeftTrigger = 4;
		public static final int RightTrigger = 5;
	}

	public static class Actions
	{
		public static final int Release = 0;
		public static final int Press = 1;
		public static final int Repeat = 2;
	}

	private static Window s_window;
	private static final ArrayList<InputDevice> s_inputDevices = new ArrayList<>();

	private Input() {}

	public static void init(Window window)
	{
		s_window = window;
	}

	public static void update()
	{
		glfwPollEvents();

		for(var inputDevice : s_inputDevices)
		{
			inputDevice.update();
		}
	}

	public static <T extends InputDevice> void AddInputDevice(Class<T> type)
	{
		try
		{
			InputDevice inputDevice = type.getConstructor().newInstance();
			inputDevice.init(s_window);
			s_inputDevices.add(inputDevice);
		}
		catch(Exception ignored) {}
	}

	public static <T extends InputDevice> void RemoveInputDevice(Class<T> type)
	{
		int n = s_inputDevices.size();
		int i = 0;
		while(i < n && s_inputDevices.get(i).getClass() != type) {i++;}
		if(i < n)
		{
			s_inputDevices.remove(i);
		}
	}
}
