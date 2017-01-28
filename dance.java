package org.usfirst.frc.team5763.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Dance extends CommandGroup{
	public Dance(){
		addSequential(new DriveToDistance(1));
		addSequential(new RotateToAngle(90));
		addSequential(new DriveToDistance(1));
		addSequential(new RotateToAngle(90));
		addSequential(new DriveToDistance(1));
		addSequential(new RotateToAngle(90));
		addSequential(new DriveToDistance(1));
		addSequential(new RotateToAngle(90));
	}
}
