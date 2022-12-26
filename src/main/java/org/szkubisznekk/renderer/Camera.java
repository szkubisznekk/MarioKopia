package org.szkubisznekk.renderer;

import org.joml.*;

/**
 * Stores camera data,
 *
 * @param Position The world space position of the camera.
 * @param Size     The size of the camera in number of tiles on the screen vertically.
 */
public record Camera(Vector2f Position, float Size) {}
