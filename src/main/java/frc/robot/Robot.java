// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/* camera */
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.cameraserver.CameraServer;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  CANSparkMax Left_Back_Motor;
  CANSparkMax Left_Front_Motor;
  CANSparkMax Right_Back_Motor;
  CANSparkMax Right_Front_Motor;
  CANSparkMax Conveyer_Motor;
  CANSparkMax Input_Motor;
  XboxController controller;
  Servo shooting_servo;
  Servo shooting_servo_2;
  PneumaticsModuleType ourType = PneumaticsModuleType.CTREPCM;
  Compressor compressor;
  DoubleSolenoid Shooting_Piston;
  DoubleSolenoid Input_Piston;

  boolean driftMode = true;

  boolean enablecompressor = true;

  //Controller Buttons
  int A_Button = 1;
  int B_Button = 2;
  int X_Button = 3;
  int Y_Button = 4;
  int LB_Button = 5;
  int RB_Button = 6;
  int Back_Button = 7;
  int Start_Button = 8;


  //input piston up boolean
  boolean inputPistonUp = false;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    //starts the camera server
    CameraServer.startAutomaticCapture();
    
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    controller = new XboxController(0);
    
    //Change Motor ID HERE
    Left_Back_Motor = new CANSparkMax(1, MotorType.kBrushed);
    Left_Front_Motor = new CANSparkMax(5, MotorType.kBrushed);
    Right_Back_Motor = new CANSparkMax(3, MotorType.kBrushed);
    Right_Front_Motor = new CANSparkMax(4, MotorType.kBrushed);
    Conveyer_Motor = new CANSparkMax(2, MotorType.kBrushed);
    Input_Motor = new CANSparkMax(6, MotorType.kBrushed);


    compressor = new Compressor(0, ourType);
    Shooting_Piston = new DoubleSolenoid(ourType, 0,1);
    Input_Piston = new DoubleSolenoid(ourType, 2,3);

   //leftMotors = new SpeedControllerGroup(Left_Back_Motor, Left_Front_Motor);
   //rightMotors = new SpeedControllerGroup(Right_Back_Motor, Right_Front_Motor);


    Left_Back_Motor.restoreFactoryDefaults();
    Left_Front_Motor.restoreFactoryDefaults();
    Right_Back_Motor.restoreFactoryDefaults();
    Right_Front_Motor.restoreFactoryDefaults();
    Input_Motor.restoreFactoryDefaults();
    Conveyer_Motor.restoreFactoryDefaults();
    shooting_servo = new Servo(0);
    shooting_servo_2 = new Servo(1);


  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  public void driveForward(double speed)  {
    Left_Back_Motor.set(speed);
    Left_Front_Motor.set(speed);
    Right_Back_Motor.set(speed);
    Right_Front_Motor.set(speed);
  }
  public void turnRobot(double speed) {
    if (speed<0) {
      Right_Back_Motor.set(Math.abs(speed));
      Right_Front_Motor.set(Math.abs(speed));
    } else {
      Left_Back_Motor.set(speed);
      Left_Front_Motor.set(speed);
    }
    
    
  }

  @Override
  public void robotPeriodic() {


    CommandScheduler.getInstance().run();
    //driving forward and backward
    var speed = 10;
    if (controller.getLeftY() < Constants.joystickTolerance*-1 || controller.getLeftY() > Constants.joystickTolerance) {
      driveForward(controller.getLeftY()*speed);
    } else if(controller.getRightX() < Constants.joystickTolerance*-1 || controller.getRightX() > Constants.joystickTolerance) {
      turnRobot(controller.getRightX()*speed);
    } else {
      driveForward(0);
    }

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
		NetworkTableEntry tx = table.getEntry("tx");
		NetworkTableEntry ty = table.getEntry("ty");
		NetworkTableEntry ta = table.getEntry("ta");
		
		//read values periodically
		double x = tx.getDouble(0.0);
		double y = ty.getDouble(0.0);
		double area = ta.getDouble(0.0);

    //post to smart dashboard periodically
		SmartDashboard.putNumber("LimelightX", x);
		SmartDashboard.putNumber("LimelightY", y);
		SmartDashboard.putNumber("LimelightArea", area);
  
    if (driftMode) {
      Left_Back_Motor.setIdleMode(IdleMode.kCoast);
      Left_Front_Motor.setIdleMode(IdleMode.kCoast);
      Right_Back_Motor.setIdleMode(IdleMode.kCoast);
      Right_Front_Motor.setIdleMode(IdleMode.kCoast);
    } else {
      Left_Back_Motor.setIdleMode(IdleMode.kBrake);
      Left_Front_Motor.setIdleMode(IdleMode.kBrake);
      Right_Back_Motor.setIdleMode(IdleMode.kBrake);
      Right_Front_Motor.setIdleMode(IdleMode.kBrake);
    }
    
    if(compressor.getPressure()> Constants.pressureMax-1) {
      compressor.disable();
    } else if(compressor.getPressure()< Constants.pressureMin+1 && enablecompressor == true) {
      compressor.enableDigital();
    }
    if (controller.getRawButton(A_Button)) {
      Shooting_Piston.set(DoubleSolenoid.Value.kForward);
    }
    if (controller.getRawButton(B_Button)) {
      Shooting_Piston.set(DoubleSolenoid.Value.kReverse);
    }
    if (controller.getRawButton(X_Button)) {
      compressor.enableDigital();
      enablecompressor=true;
    }
    if (controller.getRawButton(Y_Button)) {
      compressor.disable();
      enablecompressor=false;
    }
    if (controller.getRawButton(LB_Button)) {
      shooting_servo.setAngle(Constants.servo_up_angle);
      shooting_servo_2.setAngle(180-Constants.servo_up_angle);
    }
    if (controller.getRawButton(RB_Button)) {
      shooting_servo.setAngle(Constants.servo_down_angle);
      shooting_servo_2.setAngle(180-Constants.servo_down_angle);
    }
    if (controller.getRawButton(Back_Button)) {
      shooting_servo.setAngle(Constants.servo_shooting_angle);
      shooting_servo_2.setAngle(180-Constants.servo_shooting_angle);
    }
    if (controller.getRawButton(Start_Button)) {
      if (inputPistonUp) {
        Input_Piston.set(DoubleSolenoid.Value.kReverse);
      } else {
        Input_Piston.set(DoubleSolenoid.Value.kForward);
      }
      inputPistonUp = !inputPistonUp;
    }
    if (controller.getLeftTriggerAxis()>0) {
      Input_Motor.set(10);
      Conveyer_Motor.set(15);
    } else {
      Input_Motor.set(0);
      Conveyer_Motor.set(0);
    }
      
    }
    /*if (controller.getRawButton(Start_Button)) {
      driftMode = !driftMode;
    }*/
  

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    //differentialDrive.arcadeDrive(0,0);
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
