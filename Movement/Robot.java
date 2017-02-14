package org.usfirst.frc.team5763.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	//RobotDrive myRobot = new RobotDrive(0, 1);
	RobotDrive myRobot = new RobotDrive(0,1,2,3);
	Joystick stick = new Joystick(0);
	Timer timer = new Timer();
	private Relay myRelay = new Relay(0); // rope catcher relay
	private PWM rope = new PWM(4);
	private PowerDistributionPanel myPDP = new PowerDistributionPanel();
	private SmartDashboard myDash = new SmartDashboard();
	private AnalogInput rangesensor = new AnalogInput(0);
	private DoubleSolenoid mySolenoid = new DoubleSolenoid(2, 3);
	private SerialPort myPort = new SerialPort(9600, SerialPort.Port.kOnboard);
	double ycorrection = 0.14;
	double range = 0;
	double maxamps = 0;
	
	
	public void robotInit() {
	}

	
	
	public void autonomousInit() {
		timer.reset();
		timer.start();
	}

	
	
	public void autonomousPeriodic() {
		// Drive for 2 seconds
		if (timer.get() < 2.0) {
			myRobot.drive(-0.5, 0.0); // drive forwards half speed
		} else {
			myRobot.drive(0.0, 0.0); // stop robot
		}
	}
	
	
	

	public void teleopInit() {
		myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Boolean.TRUE);
		myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, Boolean.TRUE);
		
	}

	
	
	
	public void teleopPeriodic() {
		//myRobot.arcadeDrive(stick);

		//myPort.writeString("Hello\r\n");
		myRobot.mecanumDrive_Polar(stick.getY()+ycorrection,0,0);
		//rope.setRaw((int) stick.getY());
		range = (rangesensor.getVoltage() / 0.000977) / 25.4;
		double mycurr = myPDP.getCurrent(13);
		if (mycurr > 15){
			myRelay.set(Relay.Value.kOff);
		}
		// 1=A, 2=B, 3=X, 4=Y, 5=LBUMPER, 6 =RBUMPER
		if (stick.getRawButton(1)) {
			myRelay.set(Relay.Value.kForward);							
		}
		if (stick.getRawButton(2)) {
			myRelay.set(Relay.Value.kReverse);	
			}
		if (stick.getRawButton(3)) {
			myRelay.set(Relay.Value.kOn);	
			}
		if (stick.getRawButton(4)) {
			myRelay.set(Relay.Value.kOff);	
			}
		if (stick.getRawButton(5)) {
			mySolenoid.set(DoubleSolenoid.Value.kForward);	
			}
		if (stick.getRawButton(6)) {
			mySolenoid.set(DoubleSolenoid.Value.kReverse);	
			}
		if (mycurr > maxamps){
			maxamps = mycurr;
		}

		SmartDashboard.putString("DB/String 0", "Current: " + Double.toString(mycurr));	
		SmartDashboard.putString("DB/String 1", "Y-Axis: " + Double.toString(stick.getY()+ycorrection));		
		SmartDashboard.putString("DB/String 2", "Range: " + Double.toString(range));
		SmartDashboard.putString("DB/String 3", "Max current: " + Double.toString(maxamps));
	
	}


	
	public void testPeriodic() {
		LiveWindow.run();
	}
}
