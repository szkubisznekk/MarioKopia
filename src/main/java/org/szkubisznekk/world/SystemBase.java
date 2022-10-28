package org.szkubisznekk.world;

import dev.dominion.ecs.api.*;

public class SystemBase
{
	protected Dominion m_registry;

	protected SystemBase(Dominion registry)
	{
		m_registry = registry;
	}

	public void start() {}

	public void update() {}

	public void stop() {}
}
