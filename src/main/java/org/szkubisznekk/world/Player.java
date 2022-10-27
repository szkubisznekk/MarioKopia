package org.szkubisznekk.world;

import org.szkubisznekk.core.*;
import org.szkubisznekk.input.*;
import org.szkubisznekk.renderer.*;

import org.joml.*;

public class Player
{
	private float px = 0.0f;
	private float py = 0.0f;
	private float dx = 0.0f;
	private float dy = 0.0f;

	public Player()
	{
		Controls.OnMove.add((Float value) ->
		{
			dx = value * 5.0f;
		});

		Controls.OnJump.add(() ->
		{
			dy = (py <= 0.0001f) ? 7.0f : dy;
		});
	}

	public void update()
	{
		px += dx * Time.getDeltaTime();
		dy -= 19.81f * Time.getDeltaTime();
		py += dy * Time.getDeltaTime();
		if (py < 0.0f)
		{
			py = 0.0f;
			dy = 0.0f;
		}

		Renderer.get().Camera.Position().x = px;

		submit();
	}

	private void submit()
	{
		Renderer.get().submit(new Vector2f(px, py), 0.0f, 1);
	}
}
