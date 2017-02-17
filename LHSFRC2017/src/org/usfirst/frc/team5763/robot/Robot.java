package org.usfirst.frc.team5763.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


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
	double mycurr1 = 0;
	double mycurr2 = 0;
	
	// serial buffer
	byte[] serstring;
	
	// position vars
	double x = 0;
	double y = 0;
	double phi = 0;
	double speedscale = 1;
	
	// state 1 vars
	double x_s0 = 0;
	double y_s0 = 0;
	double phi_s0 = 0;
	double x_s1 = 1;
	double y_s1 = 1;
	double phi_s1 = 0;
	
	
	// button mapping vars
	private Button A_button = new JoystickButton(stick,1);
	
	public void robotInit() {
		// we call the left wheels 'correct', meaning the right wheels 
		// are rotated 180 and must be inverted
		myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Boolean.TRUE);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, Boolean.TRUE);
		mySolenoid.set(DoubleSolenoid.Value.kForward);
		myGyro.calibrate();
		x = x_s0;
		y = y_s0;
		phi = phi_s0;
	}

	
	
	public void autonomousInit() {
		timer.reset();
		timer.start();
		phi = myGyro.getAngle();
	}

	
	
	public void autonomousPeriodic() {
		while ((Math.abs(x-x_s1)> 0.1) || (Math.abs(y-y_s1)> 0.1) || (Math.abs(phi-phi_s1)>0)){
			speedscale = 0.25;
			myRobot.mecanumDrive_Cartesian((x-x_s1),(y-y_s1),(phi-phi_s1),0);
		}
		
	}
	
	public double range() {
		return (rangesensor.getVoltage() / rangescaleval) / 25.4;
	}
	

	public void teleopInit() {

		
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
		else
			return val / (1/speedscale);
	}
	
	
	public void getSerial(){
		serstring = myPort.readString().getBytes();
		//SmartDashboard.putString("DB/String 3", "Serial: " + Byte.toString(serstring[0]));
	}
	
	public void processButtons(){
		// rope climb (A) and descend (B)
		if (stick.getRawButton(1)){
			if (rope1.getSpeed() == 0){
				rope1.setSpeed(1);
				rope2.setSpeed(1);
			}
			else {
				rope1.setSpeed(0);
				rope2.setSpeed(0);
			}
		}
		if (stick.getRawButton(2)){
			if (rope1.getSpeed() == 0){
				rope1.setSpeed(-1);
				rope2.setSpeed(-1);
			}
			else{
				rope1.setSpeed(0);
				rope2.setSpeed(0);
			}
		}
		
		// speed select (X)
		if (stick.getRawButton(3)){
			if (speedscale == 0.25){
				speedscale = 0.5;
			}
			if (speedscale == 0.5){
				speedscale = 0.75;
			}
			if (speedscale == 0.75){
				speedscale = 1.0;
			}
		}
		
		// compressor power (Y)
		if (stick.getRawButton(4)){
			if (myRelay.get() == Relay.Value.kOff){
				myRelay.set(Relay.Value.kOn);
			}
			else
				myRelay.set(Relay.Value.kOff);
			
		}
						
		
		// gear piston Rbump
		if (stick.getRawButton(6)){
			if (mySolenoid.get() == DoubleSolenoid.Value.kForward){
				mySolenoid.set(DoubleSolenoid.Value.kReverse);				
			}
			else {
				mySolenoid.set(DoubleSolenoid.Value.kForward);
			}
		}
		
	}
	
	public void teleopPeriodic() {
		myRobot.mecanumDrive_Cartesian(getaxis(4),getaxis(1),-1*getaxis(2) + getaxis(3),0);
		processButtons();

		rangedist = range(); // converts from mm to in
		mycurr1 = myPDP.getCurrent(2);
		mycurr2 = myPDP.getCurrent(3);
		

		
		// print debug values to dashboard
		SmartDashboard.putString("DB/String 0", "CH2_I: " + Double.toString(mycurr1));	
		SmartDashboard.putString("DB/String 1", "CH3_I: " + Double.toString(mycurr2));
		SmartDashboard.putString("DB/String 2", "Range: " + Double.toString(rangedist));
		
		
	}
	
	public void testPeriodic() {
		LiveWindow.run();
	}
}
