package org.usfirst.frc.team5763.robot;

public class ManualState implements RobotInterface {

	private Robot robot;
	private double x_acc, y_acc;
	
	public ManualState(Robot currentState){
		robot = currentState;
	}
	
	public void StateProcess() {
		//Contains all the button commands for general robot control
		//Switches to DriveAutState on Button input
		//Switches to CameraAndAdjustState on Button input
		//Switches to StopState on Button input
		//robot.myRobot.mecanumDrive_Cartesian(robot.getaxis(0),robot.getaxis(1),-1*robot.getaxis(5) + robot.getaxis(4),0);
		
//		x_acc = robot.myAccel.getX();
//		y_acc = robot.myAccel.getY();
//		
//		robot.display(7, "x acc ", x_acc);
//		robot.display(8, "y acc", y_acc);
		
		robot.myRobot.mecanumDrive_Cartesian(robot.getaxis(0),robot.getaxis(1),robot.getaxis(4),robot.myGyro.getAngle());
//		robot.myRobot.mecanumDrive_Cartesian(0,0,0,0);
//		if (robot.getaxis(0) == 0 && robot.getaxis(1) == 0 && robot.getaxis(4) == 0 && (Math.abs(x_acc) > 0.01 || Math.abs(y_acc) > 0.01))
//		{
//			robot.myRobot.mecanumDrive_Cartesian(0,0,0,0);
//		}
		
		robot.processButtons();		
	}
	
	public void ToDriveAutState(){
		robot.currentState = robot.driveState;
	}
	
	public void ToManualState(){
		robot.currentState = robot.manualState;
	}
	
	public void ToStopState(){
		robot.currentState = robot.stopState;
	}
	
	public void ToCameraAndAdjustState(){
		robot.currentState = robot.cameraState;
	}

	public String GetState() {
		return "MANUAL";
	}
}
