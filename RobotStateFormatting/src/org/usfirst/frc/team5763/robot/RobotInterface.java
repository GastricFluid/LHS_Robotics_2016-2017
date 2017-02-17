package org.usfirst.frc.team5763.robot;

public interface RobotInterface {
	
	public void StateProcess();
	
	public void ToDriveAutState();
	
	public void ToManualState();
	
	public void ToStopState();
	
	public void ToCameraAndAdjustState();
}