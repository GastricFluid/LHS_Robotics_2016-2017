import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.IterativeRobot;

public class MecanumDrive extends IterativeRobot{
RobotDrive robodriver = new RobotDrive(1, 2, 3, 4);
     //Define joystick being used at USB port 1 on the Driver Station
     Joystick driveStick = new Joystick(1);

     public void teleopPeriodic() {
          robodriver.mecanumDrive_Cartesian((drivestick.getX()/2), (drivestick.getY()/2), drivestick.getTwist(),0);
     }
}
