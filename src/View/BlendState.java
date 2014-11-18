package View;

/**
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 *       View.BlendState: fade effects for cards.
 */

public class BlendState {
	private float alpha;
	private Direction direction;
	private boolean stopped;

	public BlendState() {
		this.alpha = 1.0f;
		this.stopped = true;
		this.direction = Direction.Saturate;
	}

	// blends the image in direction specified by parameter, in/out.
	public void animate(Direction direction) {
		this.direction = direction;
		this.stopped = false;

		if (direction == Direction.Fade)
			this.alpha = 1.0f;
		else
			this.alpha = 0.1f;
	}

	// returns the blend direction.
	public Direction getDirection() {
		return direction;
	}

	// returns blend alpha level.
	public float getAlpha() {
		return this.alpha;
	}

	//updates the values.
	public void process() {
		if (!stopped) {
			if (direction == Direction.Fade) {
				alpha *= 0.97f;

				if (alpha < 0.05f) {
					stopped = true;
					alpha = 0f;
				}
			}

			if (direction == Direction.Saturate) {
				alpha *= 1.01;

				if (alpha >= 1.0f) {
					alpha = 1.0f;
					stopped = true;
				}
			}
		}
	}

	public void setAlpha(float f) {
		this.alpha = f;
	}

	public void setDirection(Direction fade) {
		this.direction = fade;
	}
	
	public boolean stopped() {
		return stopped;
	}

	public static enum Direction {
		Fade, Saturate
	}
}
