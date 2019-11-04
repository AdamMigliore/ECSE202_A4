
/**
 * @author adamd
 * Assignment 2 for McGill Fall19 ECSE202 class
 */
public final class gUtil {

	/**
	 * @param scale : the scale of the simulation in pixels/meter
	 * @param meter : the size in meters to convert
	 * @return conversion from meters to pixels
	 */
	public static int meterToPixels(double scale, double meter) {
		return (int) (meter * scale);
	}

	/**
	 * @param scale : the scale of the simulation in pixels/meter
	 * @param pixel : the size in pixels to convert
	 * @return conversion from pixels to meters
	 */
	public static int pixelsToMeter(double scale, double pixel) {
		return (int) (pixel / scale);
	}
}
