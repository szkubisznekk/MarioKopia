package org.szkubisznekk.core;

import org.szkubisznekk.input.*;
import org.szkubisznekk.renderer.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Path;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Application
{
	float px = 0.0f;
	float py = 0.0f;
	float dx = 0.0f;
	float dy = 0.0f;

	private boolean m_running = true;
	private final Window m_window;
	private final Renderer m_renderer;

	public Application() throws IOException
	{
		m_window = new Window();
		m_window.OnClose.add(() ->
		{
			m_running = false;
		});

		Input.init(m_window);
		Input.AddInputDevice(Keyboard.class);
		Input.AddInputDevice(Mouse.class);
		Input.AddInputDevice(Gamepad.class);

		m_renderer = new Renderer(m_window);

		Keyboard.get().OnKeyPress.add((Integer key) ->
		{
			switch (key)
			{
				case Input.Keys.Escape -> m_running = false;
				case Input.Keys.A, Input.Keys.Left -> dx -= 5.0f;
				case Input.Keys.D, Input.Keys.Right -> dx += 5.0f;
				case Input.Keys.W, Input.Keys.Up -> dy = (py <= 0.0001f) ? 7.0f : dy;
			}
		});

		Keyboard.get().OnKeyRelease.add((Integer key) ->
		{
			switch (key)
			{
				case Input.Keys.A, Input.Keys.Left -> dx += 5.0f;
				case Input.Keys.D, Input.Keys.Right -> dx -= 5.0f;
			}
		});

		Gamepad.get().OnAxis.add((Integer axis, Float value) ->
		{
			switch (axis)
			{
				case Input.GamepadAxes.LeftStickX -> dx = value * 5.0f;
			}
		});
	}

	public void destruct()
	{
		m_renderer.destruct();
		m_window.destruct();
	}

	public void run()
	{
		Time.initialize();

		m_renderer.Camera = new Camera(new Vector2f(0.0f, 0.0f), 15.0f);

		Texture texture = new Texture(Path.of("res/textures/test.png"));
		texture.bind(0);

		while (m_running)
		{
			Input.update();
			Time.update();

			px += dx * Time.getDeltaTime();
			dy -= 19.81f * Time.getDeltaTime();
			py += dy * Time.getDeltaTime();
			if (py < 0.0f)
			{
				py = 0.0f;
				dy = 0.0f;
			}

			m_renderer.beginFrame();

			m_renderer.submit(new Vector2f(px, py));

			m_renderer.endFrame();
		}

		texture.destruct();
	}
}
