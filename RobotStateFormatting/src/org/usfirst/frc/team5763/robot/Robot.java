package org.usfirst.frc.team5763.robot;

import java.util.Set;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
	public RobotDrive myRobot = new RobotDrive(0,1,2,3);
	public Spark rope1 = new Spark(8);
	public Spark rope2 = new Spark(9);
	public Joystick stick = new Joystick(0);
	public Timer timer = new Timer();
	public Relay myRelay = new Relay(0);
	public PowerDistributionPanel myPDP = new PowerDistributionPanel();
	public AnalogInput rangesensor = new AnalogInput(0);
	public DoubleSolenoid mySolenoid = new DoubleSolenoid(2, 3);
	public SerialPort myPort = new SerialPort(9600, SerialPort.Port.kOnboard);
	public ADXRS450_Gyro myGyro = new ADXRS450_Gyro();
	public Accelerometer myAccel = new BuiltInAccelerometer();
	SmartDashboard dashboard = new SmartDashboard();
	String[] autoModes = {"CENTER", "LEFT", "RIGHT"};
	
	
	
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
	double speedscale = 1;
	
	double friction = 0;
	
	byte[] serstring;
	
	
	String autoSelected;
	
	public RobotInterface currentState;
	public ManualState manualState;
	public DriveAutState driveState;
	public StopState stopState;
	public CameraAndAdjustState cameraState;

	public Encoder enc;
	
	public double range() {
		return (rangesensor.getVoltage() / rangescaleval) / 25.4;
	}

	public char getAutonomousOption() {
		display(6,"SELECTION", autoSelected);
		return (autoSelected != null && autoSelected.length() > 0 ? autoSelected.toUpperCase().charAt(0) : 'C');
	}
	
	public void display(int textBoxNumber, String label, String val){
		SmartDashboard.putString("DB/String " + textBoxNumber, label + ": " + val);
	}
	
	public void display(int textBoxNumber, String label, double val){
		SmartDashboard.putString("DB/String " + textBoxNumber, label + ": " + Double.toString(val));
	}	
	
	public void wait(int milliseconds) {
		try {
			Thread.sleep(250);
		}
		catch(InterruptedException e){}
	}
	
	public void drive(double magnitude, double curve) {
		double safeFriction = friction > 1.0 || friction < 0 ? 0.0 : friction;
		double newMagnitude = magnitude == 0 ? 0.0 : ((1.0 - safeFriction) * magnitude) + friction;
		myRobot.drive(newMagnitude, curve);
	}
	
	@Override
	public void robotInit() {
	     dashboard.putStringArray("Auto List", autoModes);	
		//SmartDashboard.putStringArray("Auto List", new String[]{"Center","Left","Right"});
		//SmartDashboard.putData("Auto Selector", chooser);
		mySolenoid.set(DoubleSolenoid.Value.kReverse);
		
		enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
		
		//myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Boolean.TRUE);
		//myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, Boolean.TRUE);
		myGyro.calibrate();
		
		//may or may not be necessary to set the states up like this (aka I did it in C# but doesn't seem needed in java)
		manualState = new ManualState(this);
		driveState = new DriveAutState(this);
		cameraState = new CameraAndAdjustState(this);
		stopState = new StopState(this);
		//ect.
	}


	@Override
	public void autonomousInit() {
		autoSelected = dashboard.getString("Auto Selector");
		
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		
		enc.setReverseDirection(true);
		enc.reset();
		
		myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Boolean.FALSE);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, Boolean.FALSE);
		
		
		mySolenoid.set(DoubleSolenoid.Value.kReverse);

		//you want to set the robot's state to the automated driving at autonomous init
		currentState = driveState;
		
		timer.reset();
		timer.start();
		currentState.StateProcess();
		display(3, "STATE", currentState.GetState());
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		display(3, "STATE", currentState.GetState());
		//SmartDashboard.putString("DB/String 3", " " + currentState.toString());
		if (timer.get() >= 15){
			enc.reset();
			teleopInit();
			//SmartDashboard.putString("DB/String 3", " " + currentState.toString());
			display(3, "STATE", currentState.GetState());
		}
	}

	@Override
	public void teleopInit(){
		currentState = manualState;
		mySolenoid.set(DoubleSolenoid.Value.kReverse);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Boolean.TRUE);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, Boolean.TRUE);
	}
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		//I think that the state process should go here
		currentState.StateProcess();
		display(3, "STATE", currentState.GetState());
		//SmartDashboard.putString("DB/String 3", " " + currentState.toString());
	}

	@Override
	public void testInit(){
		currentState = manualState;
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		//I think that the state process should go here
		display(3, "STATE", currentState.GetState());
		//SmartDashboard.putString("DB/String 3", " " + currentState.toString());
		currentState.StateProcess();
	}
	
	public void processButtons(){
		// rope climb (A) and descend (B)
		
		while (stick.getRawButton(1)){
			rope1.setSpeed(1);
			rope2.setSpeed(1);
		}
		while (stick.getRawButton(2)){
			rope1.setSpeed(-1);
			rope2.setSpeed(-1);
		}
		while (stick.getRawButton(4)){
			rope1.setSpeed(0);
			rope2.setSpeed(0);
		}
	
		// speed select (X)
		if (stick.getRawButton(3)){
			if (speedscale == 0.5){
				speedscale = 0.75;
			}
			else if (speedscale == 0.75){
				speedscale = 1.0;
			}
			else if (speedscale == 1){
				speedscale = .50;
			}
			display(5, "SPEED_SCALE", speedscale);
			//SmartDashboard.putString("DB/String 5", " " + speedscale);
			wait(250);
		}
						
		
		// gear piston Lbump and Rbump
		if (stick.getRawAxis(2) > .5){
			mySolenoid.set(DoubleSolenoid.Value.kReverse);				
		}
		if (stick.getRawAxis(3) > .5){
			mySolenoid.set(DoubleSolenoid.Value.kForward);				
		}
		if (stick.getRawButton(7)){
			myGyro.calibrate();
		}
		
	}
	
	public double getaxis(int axis){
		double val = 0;
		val = stick.getRawAxis(axis);
//		if (axis == 1){
//			val += yoffset;
//		}

		if (val < 0.2 && val > -0.2){
			return 0;
		}
		else {
			//double safeFriction = friction > 1.0 || friction < 0 ? 0.0 : friction;
			//double newScale = speedscale == 0 ? 0.0 : ((1.0 - safeFriction) * speedscale);

			return val / (1/speedscale);
		}
	}
	
	public void getSerial(){
		try{
			serstring = myPort.readString().getBytes();
		} catch (IndexOutOfBoundsException e) {
			
		}
		
		//SmartDashboard.putString("DB/String 3", "Serial: " + Byte.toString(serstring[0]));
	}
}

