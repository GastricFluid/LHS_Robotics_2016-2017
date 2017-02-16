package org.usfirst.frc.team5763.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private RobotDrive myRobot = new RobotDrive(0,1,2,3);
	private Spark rope1 = new Spark(8);
	private Spark rope2 = new Spark(9);
	private Joystick stick = new Joystick(0);
	private Timer timer = new Timer();
	private Relay myRelay = new Relay(0);
	private PowerDistributionPanel myPDP = new PowerDistributionPanel();
	private AnalogInput rangesensor = new AnalogInput(0);
	private DoubleSolenoid mySolenoid = new DoubleSolenoid(2, 3);
	private SerialPort myPort = new SerialPort(9600, SerialPort.Port.kOnboard);
	private ADXRS450_Gyro myGyro = new ADXRS450_Gyro();
	private Accelerometer myAccel = new BuiltInAccelerometer();
	
	
	// axis offset vars
	double yoffset = 0.14; // offset added to correct left joystick y-axis
	double xoffset = 0.14;
	
	// range sensor vars
	double rangedist = 0; // value to hold calculated range distance in inches
	public static final double rangescaleval = 0.000977; // value to scale voltage from range sensor
	
	// amperage  vars
	double maxobservedamps = 0;
	double maxallowedamps = 15;
	
	// position vars
	double x = 0;
	double y = 0;
	double phi = 0;
	
	
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	
	public RobotInterface currentState;
	public ManualState manualState;
	public DriveAutState driveState;
	public StopState stopState;
	public CameraAndAdjustState cameraState;

	public double range() {
		return (rangesensor.getVoltage() / rangescaleval) / 25.4;
	}
	
	public double getaxis(int axis){
		double val = 0;
		double accel = 0;
		val = stick.getRawAxis(axis);
//		if (axis == 1){
//			val += yoffset;
//		}

		if (val < 0.2 && val > -0.2){
			return 0;
		}
		else
			return val / 3;
	}	
	
	public void processButton(int b){
		switch(b){
		case 1:
			break;
		case 2:
			break;
		}
		
	}
	
	
	
	
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Boolean.TRUE);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, Boolean.TRUE);
		myGyro.calibrate();
		
		//may or may not be necicarry to set the states up like this (aka I did it in C# but doesn't seem needed in java)
		//manualState = new ManualState(this);
		//driveState = new DriveState(this);
		//ect.
	}


	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		
		//you want to set the robot's state to the automated driving at autonomous init
		currentState = driveState;
		timer.reset();
		timer.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		while (timer.get() < 15){
			switch (autoSelected) {
			case customAuto:
				// Put custom auto code here
				
				
				
				//I think that the state process should go here
				currentState.StateProcess();
				break;
			case defaultAuto:
			default:
				// Put default auto code here
				break;
			}
		}
		teleopInit();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		//I think that the state process should go here
		currentState.StateProcess();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		//I think that the state process should go here
		currentState = manualState;
		currentState.StateProcess();
	}
}

