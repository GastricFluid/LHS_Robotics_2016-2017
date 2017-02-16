package org.usfirst.frc.team5763.robot;

public class DriveAutState implements RobotInterface {
	
	private Robot robot;
	
	public void StateProcess(){
		//this process will include the code that moves the robot towards a certain vector/destination
		//Switches to Manual State at 15 second (when automated phase ends)
		//Switches to CameraAndAdjust when a signal is given that it is near the reflective tape
		if (robot.timer.get() < 2.0) {
			robot.myRobot.drive(-0.5, 0.0); // drive forwards half speed
		} else {
			robot.myRobot.drive(0.0, 0.0); // stop robot
			robot.teleopInit();
		}
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
