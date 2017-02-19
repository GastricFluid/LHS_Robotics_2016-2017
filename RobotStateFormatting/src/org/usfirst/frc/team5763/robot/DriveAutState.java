package org.usfirst.frc.team5763.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAutState implements RobotInterface {
	
	private Robot robot;
	
	public DriveAutState(Robot currentState){
		robot = currentState;
		
	}
	
	public void StateProcess() {
		//this process will include the code that moves the robot towards a certain vector/destination
		//Switches to Manual State at 15 second (when automated phase ends)
		//Switches to CameraAndAdjust when a signal is given that it is near the reflective tape
		switch(robot.getAutonomousOption()) {
			case 'C': 
				Center();
				break;
			case 'L': 
				Left();
				break;
			case 'R': 
				Right();
				break;
			default: 
				Center();
				break;
		}
	}
	
	public void ToDriveAutState(){
		robot.currentState = robot.driveState;
	}
	
	public void ToManualState(){
		robot.enc.reset();
		robot.currentState = robot.manualState;
	}
	
	public void ToStopState(){
		robot.currentState = robot.stopState;
	}
	
	public void ToCameraAndAdjustState(){
		robot.currentState = robot.cameraState;
	}
	
	public void TestStuffThatIDoNotWantToCommentOut(){
		if (robot.timer.get() < 2.0) {
			robot.myRobot.drive(-0.5, 0.0); // drive forwards half speed
		} else {
			robot.myRobot.drive(0.0, 0.0); // stop robot
			robot.teleopInit();
		}
	}

	public String GetState() {
		return "AUTONOMOUS";
	}
	
	private void Center() {
		robot.display(0, "INITIAL DISTANCE", robot.range());
		//SmartDashboard.putString("DB/String 0", " " + robot.range());
		robot.mySolenoid.set(DoubleSolenoid.Value.kReverse);
		robot.wait(250);
		while (robot.range() > 45){
			robot.drive(.25, 0.0);
			robot.display(1, "MOVING DISTANCE", robot.range());
			//SmartDashboard.putString("DB/String 1", " " + robot.range());
		}
		robot.mySolenoid.set(DoubleSolenoid.Value.kForward);
		while (robot.range() > 25){
			robot.drive(.15, 0.0);
			robot.display(2, "FINAL DISTANCE", robot.range());
			//SmartDashboard.putString("DB/String 2", " " + robot.range());
		}
		
		robot.drive(0.0, 0.0);
		robot.enc.reset();
		while (robot.enc.get() < 10 || robot.timer.get() < 15){
			robot.drive(.15, 0.0);
			robot.display(4, "ENCODER", robot.enc.get());
			//SmartDashboard.putString("DB/String 4", " " + robot.enc.get());
		}
		robot.drive(0.0, 0.0);
	}
	
	private void Left() {
		robot.display(0, "INITIAL DISTANCE", robot.range());
		//SmartDashboard.putString("DB/String 0", " " + robot.range());
		robot.mySolenoid.set(DoubleSolenoid.Value.kReverse);
		robot.wait(250);
		while (robot.range() > 40){
			robot.drive(.25, 0.0);
			robot.display(1, "MOVING DISTANCE", robot.range());
			//SmartDashboard.putString("DB/String 1", " " + robot.range());
		}
		while (robot.range() > 25){
			robot.mySolenoid.set(DoubleSolenoid.Value.kForward);
			robot.drive(.15, 0.0);
			robot.display(2, "FINAL DISTANCE", robot.range());
			//SmartDashboard.putString("DB/String 2", " " + robot.range());
		}
		
		robot.drive(0.0, 0.0);
		robot.enc.reset();
		while (robot.enc.get() < 10 || robot.timer.get() < 15){
			robot.drive(.15, 0.0);
			robot.display(4, "ENCODER", robot.enc.get());
			//SmartDashboard.putString("DB/String 4", " " + robot.enc.get());
		}
		robot.drive(0.0, 0.0);
	}
	
	private void Right() {robot.display(0, "INITIAL DISTANCE", robot.range());
	//SmartDashboard.putString("DB/String 0", " " + robot.range());
	robot.mySolenoid.set(DoubleSolenoid.Value.kReverse);
	robot.wait(250);
	while (robot.range() > 40){
		robot.drive(.25, 0.0);
		robot.display(1, "MOVING DISTANCE", robot.range());
		//SmartDashboard.putString("DB/String 1", " " + robot.range());
	}
	while (robot.range() > 25){
		robot.mySolenoid.set(DoubleSolenoid.Value.kForward);
		robot.drive(.15, 0.0);
		robot.display(2, "FINAL DISTANCE", robot.range());
		//SmartDashboard.putString("DB/String 2", " " + robot.range());
	}
	
	robot.drive(0.0, 0.0);
	robot.enc.reset();
	while (robot.enc.get() < 10 || robot.timer.get() < 15){
		robot.drive(.15, 0.0);
		robot.display(4, "ENCODER", robot.enc.get());
		//SmartDashboard.putString("DB/String 4", " " + robot.enc.get());
	}
	robot.drive(0.0, 0.0);}
	
}
