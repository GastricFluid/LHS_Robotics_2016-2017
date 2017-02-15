package org.usfirst.frc.team5763.robot;
import edu.wpi.first.wpilibj.Joystick;

public class ManualState implements RobotInterface {
	
	public void StateProcess(){
		
		robotDrive.mecanumDrive_Cartesian(stick.getX(), stick.getY(), stick.getZ(), 0);

		if (stick.getRawButton(2)){
			testRelay.set(Relay.Value.kOn);
		}
		else{
			testRelay.set(Relay.Value.kOff);
		}
		
	}
	
	public void ToForwardDriveAutState(){}
	
	public void ToManualState(){}
	
	public void ToStopState(){}
	
	public void ToCameraAndAdjustState(){}
	
	public void ToFindAndClimbAutState(){}
}
