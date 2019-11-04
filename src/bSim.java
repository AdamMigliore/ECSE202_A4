
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import acm.graphics.GCanvas;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

/**
 * @author adamd Assignment 3 for McGill Fall19 ECSE202 class
 */
public class bSim extends GraphicsProgram {

	private static final int WIDTH = 1200; // Width of window
	private static final int HEIGHT = 600; // Height of window
	private static final int OFFSET = 200; // offset for plane
	private static final double SCALE = HEIGHT / 100; // pixels per meter
	private static final int NUMBALLS = 60; // # balls to simulate
	private static final double MINSIZE = 1.0; // Minumum ball radius (meters)
	private static final double MAXSIZE = 25.0; // Maximum ball radius (meters)
	private static final double EMIN = 0.0; // Minimum loss coefficient
	private static final double EMAX = 1.0; // Maximum loss coefficient
	private static final double VoMIN = 1.0; // Minimum velocity (meters/sec)
	private static final double VoMAX = 200.0; // Maximum velocity (meters/sec)
	private static final double ThetaMIN = 1.0; // Minimum launch angle (degrees)
	private static final double ThetaMAX = 180.0; // Maximum launch angle (degrees)
	private static final int GP_HEIGHT = 3; // Maximum launch angle (degrees)
	private RandomGenerator rgen = new RandomGenerator(); // randomGenerator
	private bTree myTree = new bTree();// our bTree
	private GLabel message;// label object for the display

	private static final int MINBALLS = 1;
	private static final int MAXBALLS = 255;

	myInput ballsPNL = new myInput("NUMBALLS: ", MINBALLS, MAXBALLS, NUMBALLS);
	myInput minSizePNL = new myInput("MIN SIZE: ", MINSIZE, MAXSIZE, MINSIZE);
	myInput maxSizePNL = new myInput("MAX SIZE: ", MINSIZE, MAXSIZE, MAXSIZE);
	myInput minLossPNL = new myInput("LOSS MIN:", EMIN, EMAX, EMIN);
	myInput maxLossPNL = new myInput("LOSS MAX:", EMIN, EMAX, EMAX);
	myInput minVelPNL = new myInput("MIN VEL:", VoMIN, VoMAX, VoMIN);
	myInput maxVelPNL = new myInput("MAX VEL:", VoMIN, VoMAX, VoMAX);
	myInput minThetaPNL = new myInput("TH MIN:", ThetaMIN, ThetaMAX, ThetaMIN);
	myInput maxThetaPNL = new myInput("TH MAX:", ThetaMIN, ThetaMAX, ThetaMAX);

	private boolean stopped = false;
	private boolean simEnable = false;

	/**
	 * the main loop of the simulation which will generate the random parameters for
	 * the number of balls in the simulation
	 */
	public void init() {
		rgen.setSeed((long) 424242);// required from prof. to match simulation parameters
		setupDisplay();
		addActionListeners();
	}

	private void setupDisplay() {
		JPanel inputs = new JPanel(new GridLayout(10, 1));
		JPanel options = new JPanel(new GridLayout(1, 6));

		JButton runBTN = new JButton("run");
		options.add(runBTN);
		JButton stopBTN = new JButton("stop");
		options.add(stopBTN);
		JButton clearBTN = new JButton("clear");
		options.add(clearBTN);
		JButton quitBTN = new JButton("quit");
		options.add(quitBTN);
		JButton stackBTN = new JButton("stack");
		options.add(stackBTN);
		JCheckBox traceCBX = new JCheckBox("trace");
		options.add(traceCBX);
		add(options, BorderLayout.SOUTH);

		inputs.add(new JLabel("General Simulation Parameters"));
		inputs.add(ballsPNL);
		inputs.add(minSizePNL);
		inputs.add(maxSizePNL);
		inputs.add(minLossPNL);
		inputs.add(maxLossPNL);
		inputs.add(minVelPNL);
		inputs.add(maxVelPNL);
		inputs.add(minThetaPNL);
		inputs.add(maxThetaPNL);
		add(inputs, BorderLayout.EAST);

		GRect plane = new GRect(0, HEIGHT, WIDTH, GP_HEIGHT);
		plane.setFilled(true);
		plane.setColor(Color.BLACK);
		add(plane);

		this.resize(WIDTH + inputs.getWidth(), HEIGHT + OFFSET);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("run")) {
			doSim();
		} else if (e.getActionCommand().equals("stack")) {
			stopTree();
			doStack();
		} else if (e.getActionCommand().equals("clear")) {
			myTree = new bTree();
			this.removeAll();
		} else if (e.getActionCommand().equals("stop")) {
			if (!stopped) {
				stopTree();
				stopped = true;
			} else {
				resumeTree();
				stopped = false;
			}

		} else if (e.getActionCommand().equals("quit")) {
			System.exit(0);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		JCheckBox source = (JCheckBox) e.getSource();
		if (source.isSelected()) {
			myTree.toggleTracePoint(true);
		} else if (!source.isSelected()) {
			myTree.toggleTracePoint(false);
		}
	}

	/**
	 * @return double scale : scale used in the simulation
	 */
	public double getScale() {
		return SCALE;
	}

	/**
	 * @return int height : height of the window
	 */
	public int getHeight() {
		return HEIGHT;
	}

	private void doSim() {
		for (int i = 0; i < ballsPNL.getTextFieldValue(); i++) {
			double bSize = rgen.nextDouble(minSizePNL.getTextFieldValue(), maxSizePNL.getTextFieldValue()); // randomly
																											// generate
																											// ball size
																											// element
																											// of
			// [MINSIZE,MAXSIZE]
			Color bColor = rgen.nextColor();// randomly generate color
			double bLoss = rgen.nextDouble(minLossPNL.getTextFieldValue(), maxLossPNL.getTextFieldValue());// randomly
																											// generate
																											// energy
																											// loss
																											// element
																											// of
																											// [EMIN,EMAX]
			double Vo = rgen.nextDouble(minVelPNL.getTextFieldValue(), maxVelPNL.getTextFieldValue()); // randomly
																										// generate
																										// initial
																										// velocity
																										// element of
			// [VoMIN,VoMAX]
			double theta = rgen.nextDouble(minThetaPNL.getTextFieldValue(), maxThetaPNL.getTextFieldValue()); // randomly
																												// generate
																												// theta
																												// element
																												// of
			// [ThetaMIN,ThetaMAX]

			aBall ball = new aBall(gUtil.pixelsToMeter(SCALE, WIDTH / 2), bSize, Vo, theta, bSize, bColor, bLoss, SCALE,
					WIDTH, HEIGHT, this);// Generate a new aBall object with it's initial parameters
			add(ball.getBall());// add the ball to the simulation
			myTree.addNode(ball);
			ball.start();// start the balls thread
		}
	}

	private void doStack() {
		myTree.stackBalls(this);
	}

	private void stopTree() {
		myTree.stopTree();
	}

	private void resumeTree() {
		myTree.resumeTree();
	}

}
