package org.usfirst.frc.team5763.robot;

public class CameraAndAdjustState implements RobotInterface {

	private Robot robot;

	public CameraAndAdjustState(Robot currentState){
		robot = currentState;
	}
	
	public void StateProcess(){
		//This Process includes the code to recive the angle of the tape and move accordingly, and then flip gear when in range
		//Switches to Manual on Button interruption
		//Switches to Manual on Completion of gear placement
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
