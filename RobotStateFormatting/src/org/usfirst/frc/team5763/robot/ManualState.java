package org.usfirst.frc.team5763.robot;

public class ManualState implements RobotInterface {

	private Robot robot;
	
	public void StateProcess(){
		//Contains all the button commands for general robot control
		//Switches to DriveAutState on Button input
		//Switches to CameraAndAdjustState on Button input
		//Switches to StopState on Button input
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
}
