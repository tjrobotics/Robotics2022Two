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

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
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
  //SpeedControllers

  //Controller Buttons
  int A_Button = 1;
  int B_Button = 2;
  int X_Button = 3;
  int Y_Button = 4;
  int LB_Button = 5;
  int RB_Button = 6;
  int Back_Button = 7;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    controller = new XboxController(0);
    
    //Change Motor ID HERE
    Left_Back_Motor = new CANSparkMax(6, MotorType.kBrushless);
    Left_Front_Motor = new CANSparkMax(4, MotorType.kBrushed);
    Right_Back_Motor = new CANSparkMax(3, MotorType.kBrushed);
    Right_Front_Motor = new CANSparkMax(2, MotorType.kBrushed);
    Conveyer_Motor = new CANSparkMax(1, MotorType.kBrushed);
    Input_Motor = new CANSparkMax(5, MotorType.kBrushed);


    compressor = new Compressor(0, ourType);
    Shooting_Piston = new DoubleSolenoid(ourType, 0,1);
    Input_Piston = new DoubleSolenoid(ourType, 2,3);



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
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    var speed = 10;
    Left_Back_Motor.set(controller.getLeftY()*speed);
    Left_Front_Motor.set(controller.getLeftY()*speed);
    Right_Back_Motor.set(controller.getLeftY()*speed);
    Right_Front_Motor.set(controller.getLeftY()*speed);
    if(compressor.getPressure()> Constants.pressureMax-1) {
      compressor.disable();
    }
    if (controller.getRawButton(A_Button)) {
      Shooting_Piston.set(DoubleSolenoid.Value.kForward);
    }
    if (controller.getRawButton(B_Button)) {
      Shooting_Piston.set(DoubleSolenoid.Value.kReverse);
    }
    if (controller.getRawButton(X_Button)) {
      compressor.enableDigital();
    }
    if (controller.getRawButton(Y_Button)) {
      compressor.disable();
    }
    if (controller.getRawButton(LB_Button)) {
      shooting_servo.setAngle(Constants.servo_up_angle);
      shooting_servo_2.setAngle(Constants.servo_up_angle);
    }
    if (controller.getRawButton(RB_Button)) {
      shooting_servo.setAngle(Constants.servo_down_angle);
      shooting_servo_2.setAngle(Constants.servo_down_angle);
    }
    if (controller.getRawButton(Back_Button)) {
      shooting_servo.setAngle(Constants.servo_shooting_angle);
      shooting_servo_2.setAngle(Constants.servo_shooting_angle);
    }
    if (controller.getLeftTriggerAxis()>0) {
      Input_Motor.set(10);
    } else {
      Input_Motor.set(0);
    }
  }

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
  public void autonomousPeriodic() {}

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
