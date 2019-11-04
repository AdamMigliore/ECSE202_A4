
import java.awt.Color;
import acm.graphics.GOval;

/**
 * @author adamd
 * Assignment 3 for McGill Fall19 ECSE202 class
 */

/**
 * @author Adam
 *
 */
public class aBall extends Thread {

	private static final double g = 9.8; // MKS gravitational constant 9.8 m/s^2
	private static final double Pi = 3.141592654; // To convert degrees to radians
	private static final double k = 0.0001;// constant K value
	private static final double ETHR = 0.1;// minimum energy level

	private double theta, // launch angle (degrees)
			bSize, // ball size (meters)
			bLoss; // coefficient of energy loss
	private Color bColor; // ball color
	private GOval myBall; // ball object

	private int WIDTH, HEIGHT; // Width and Height of the window
	private double scale; // scale used (Height/100) (pixels/meter)

	private double Xi, // initial position in X
			Yi, // initial position in Y
			X, // position in X
			Y, // position in Y
			XLast, // last X position
			YLast; // last Y position

	private double Vt, // velocity with air friction
			Vox, // initial velocity in X
			Voy, // initial velocity in Y
			Vo, // initial velocity
			Vx, // velocity in X
			Vy; // velocity in Y

	private double KEx = ETHR, // kinetic energy in X
			KEy = ETHR, // kinetic energy in Y
			ELast = 0;

	private double time;// time in s
	private boolean isRunning = true;// continue simulation if true; stop simulation if false
	private final double TICK = 0.1;// delta t
	private int ScrX, // screen coordinates in X
			ScrY;// screen coordinates in Y

	/**
	 * @param Xi     : initial position X (meters)
	 * @param Yi     : initial position Y (meters)
	 * @param Vo     : initial velocity (meters/s)
	 * @param theta  : launch angle (degrees)
	 * @param bSize  : ball size (meters)
	 * @param bColor : ball color
	 * @param bLoss  : energy loss
	 * @param scale  : scale of simulation (pixels/meters)
	 * @param WIDTH  : width of window (pixels)
	 * @param HEIGHT : height of window (pixels)
	 */
	public aBall(double Xi, double Yi, double Vo, double theta, double bSize, Color bColor, double bLoss, double scale,
			int WIDTH, int HEIGHT) {
		this.Xi = Xi;
		this.Yi = Yi;
		this.Vo = Vo;
		this.theta = theta;
		this.bSize = bSize;
		this.bColor = bColor;
		this.bLoss = bLoss;
		this.HEIGHT = HEIGHT;
		this.WIDTH = WIDTH;
		this.scale = scale;

		createBall();
		initializeParameters();
	}

	/**
	 * create the ball at it's initial with its initial size and color
	 */
	private void createBall() {
		myBall = new GOval(gUtil.meterToPixels(scale, Xi), HEIGHT - gUtil.meterToPixels(scale, Yi * 2),
				gUtil.meterToPixels(scale, bSize * 2), gUtil.meterToPixels(scale, bSize * 2));
		myBall.setFilled(true);
		myBall.setFillColor(this.bColor);
	}

	/**
	 * initialize the parameters necessary for the simulation
	 */
	private void initializeParameters() {
		// Initial parameters
		Vt = g / (4 * Pi * this.bSize * this.bSize * k); // Terminal velocity
		Vox = Vo * Math.cos(this.theta * Pi / 180);// Initial velocity in X
		Voy = Vo * Math.sin(this.theta * Pi / 180);// Initial velocity in Y
		ELast = 0.5 * Vo * Vo;// initial energy
	}

	/**
	 * calculate the simulation variables
	 */
	private void calculateVariables() {

		X = Xi + Vox * Vt / g * (1 - Math.exp(-g * time / Vt));// calculate X
		Y = Yi + Vt / g * (Voy + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time;// Calculate Y
		Vx = (X - XLast) / TICK;// Calculate horizontal velocity
		Vy = (Y - YLast) / TICK;// calculate vertical velocity

		if (Vy < 0 && Y <= bSize) {
			KEx = 0.5 * Vx * Vx * (1 - bLoss); // Kinetic energy in X direction after collision
			KEy = 0.5 * Vy * Vy * (1 - bLoss); // Kinetic energy in Y direction after collision
			Vox = theta < 90 ? Math.sqrt(2 * KEx) : -Math.sqrt(2 * KEx); // Resulting horizontal velocity; if launch
																			// angle is bigger than 90 degrees the ball
																			// is going towards the left so velocity is
																			// negative
			Voy = Math.sqrt(2 * KEy); // Resulting vertical velocity
			Xi = XLast;// the offset will be equal to the last coordinate minus the beginning of the
						// simulation
			time = 0;// reset the time
			Y = bSize;// the position of the ball is at its lowest position
			XLast = 0;// the beginning of the simulation starts at 0 meters

			if ((KEx + KEy < ETHR || KEx + KEy >= ELast)) {// if energy is under a threshold the simulation
				// stops
				isRunning = !isRunning;
			} else {
				ELast = KEx + KEy;
			}

		}

		ScrX = gUtil.meterToPixels(scale, X);// convert to screen units
		ScrY = (HEIGHT - gUtil.meterToPixels(scale, (Y + bSize)));// convert to screen units
		XLast = X;// save last X
		YLast = Y;// save last Y

		myBall.setLocation(ScrX, ScrY);// set the ball location

	}

	/**
	 * the simulation loop which continues until canLoop is false
	 */
	public void run() {
		while (isRunning) {
			calculateVariables();
			time += TICK;
			try { // pause for 50 milliseconds
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @return myBall : the simulation ball
	 */
	public GOval getBall() {
		return myBall;
	}

	/**
	 * @return isRunning : the predicate to know if the ball is still in motion
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @return bSize : the size of the ball
	 */
	public double getBSize() {
		return bSize;
	}

	/**
	 * @param double x : x position in pixels
	 * @param double y : y position in pixels
	 */
	public void moveTo(double x, double y) {
		myBall.setLocation(x, y);
	}
}
