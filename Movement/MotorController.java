package org.usfirst.frc.team5763.robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class MotorController{
	
	private SpeedController motor = new Talon(0);
	
	private Joystick jStick = new Joystick(0);
	
	private final double UpdatePeriod = 0.005;
}
edit
