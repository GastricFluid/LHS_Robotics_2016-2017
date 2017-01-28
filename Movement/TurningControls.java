package org.usfirst.frc.team5763.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

public class TurningControls extends IterativeRobot {
  //calibration for turning, may need to be fine tuned
  private static final double VoltsPerDegreePerSecond = 0.0128;
  
  private static final int FrontLeftMotorPort = 0;
	private static final int FrontRightMotorPort = 1;
	private static final int RearLeftMotorPort = 2;
	private static final int RearRightMotorPort = 3;
	private static final int GyroPort = 0;
	private static final int JoystickPort = 0;
  
  private RobotDrive myRobot;
	private AnalogGyro gyro = new AnalogGyro(GyroPort);
	private Joystick joystick = new Joystick(JoystickPort);
  
  @Override
	public void robotInit() {
		myRobot = new RobotDrive(FrontLeftMotorPort, FrontRightMotorPort,
			RearLeftMotorPort, RearRightMotorPort);
    myRobot.setInvertedMotor(MotorType.FrontLeft, true);
		myRobot.setInvertedMotor(MotorType.RearLeft, true);
    
    gyro.setSensitivity(VoltsPerDegreePerSecond);
  }
  
  @Override
	public void teleopPeriodic() {
		myRobot.mecanumDrive_Cartesian(joystick.getX(), joystick.getY(), joystick.getZ(), gyro.getAngle());
  }
}
