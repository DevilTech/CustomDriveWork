package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class CustomRobotDrive {
    //Standard 2 and 4 Wheel Config Jags
    CANJaguar left;
    CANJaguar right;
    CANJaguar leftR;
    CANJaguar rightR;
    
    /**Swerve/Crab/4PointRot Drive Jags :Not in use until rotationDrive code is filled out
    CANJaguar leftRot;
    CANJaguar rightRot;
    CANJaguar leftRRot;
    CANJaguar rightRRot;
    */
    
    boolean isTwoMotors;
    byte sg;
    
    public CustomRobotDrive(CANJaguar rightWheel, CANJaguar leftWheel) {
        this.sg = 2;
        left = leftWheel;
        right = rightWheel;
        isTwoMotors = true;
    }
    
    public CustomRobotDrive(CANJaguar rightFWheel, CANJaguar leftFWheel, CANJaguar rightRWheel, CANJaguar leftRWheel) {
        this.sg = 2;
        left = leftFWheel;
        right = rightFWheel;
        leftR = leftRWheel;
        rightR = rightRWheel;
        isTwoMotors = false;
    }
    
    //ArcadeDrive without input squaring
    public void arcadeDrive(Joystick stick) {
        double x = stick.getAxis(Joystick.AxisType.kX);
        double y = stick.getAxis(Joystick.AxisType.kY);
        double left = x+y;
        double right = x-y;
        drive(left, right);
    }
    
    //ArcadeDrive with input squaring
    public void arcadeDriveSq(Joystick stick) {
        double x = stick.getAxis(Joystick.AxisType.kX);
        double y = stick.getAxis(Joystick.AxisType.kY);
        double leftS = x+y;
        double rightS = x-y;
        leftS = leftS*Math.abs(leftS);
        rightS = rightS*Math.abs(rightS);
        drive(leftS, rightS);
    }
    
    /*Base for 4pointRot drive code.
    *Look at: http://www.chiefdelphi.com/media/papers/download/3027 and
    *http://wiki.team1640.com/images/6/60/Programming_a_Pivot_Drive_Robot.pdf
    *for math examples for implementation
    */
    public void rotationDrive(Joystick stick) {
        double x = stick.getAxis(Joystick.AxisType.kX);
        double y = stick.getAxis(Joystick.AxisType.kY);
        double z = stick.getAxis(Joystick.AxisType.kZ);
        double leftS = 0;
        double rightS = 0;
        double theta = 0;
        double rotS = 0;
        rotPointDrive(leftS, rightS, theta, rotS);
    }
    
    //Flattens values passed to jaguars to -1 to 1
    private double normalize(double speed) {
        if(speed<-1.0) {speed = -1.0;}
        else if(speed>1.0) {speed = 1.0;}
        return speed;
    }
    
    //SyncByte drive processing code for both 2 and 4 motor drives. 
    //Usable for autonomous drive as well
    public void drive(double left_speed, double right_speed){
        if(isTwoMotors){
            try {
                left.setX(normalize(left_speed), sg);
                right.setX(normalize(right_speed), sg);
                left.updateSyncGroup(sg);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        else {
            try {
                left.setX(normalize(left_speed), sg);
                leftR.setX(normalize(left_speed), sg);
                right.setX(normalize(right_speed), sg);
                rightR.setX(normalize(right_speed), sg);
                left.updateSyncGroup(sg);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //Blank Header for later implementation of rotational drive output code
    public void rotPointDrive(double left_speed, double right_speed, double rotation_angle, double rotation_speed) {
        
    }
    
    //Stop: Only really useful for autonomous code
    public void stop() {
        drive(0,0);
    }
}
