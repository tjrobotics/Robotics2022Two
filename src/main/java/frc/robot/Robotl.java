// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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
  CANSparkMax motor_3;
  CANSparkMax motor_4;
  CANSparkMax motor_1;
  CANSparkMax motor_6;
  CANSparkMax motor_2;
  XboxController gamepad;
  Servo servo1;
  PneumaticsModuleType ourType = PneumaticsModuleType.CTREPCM;
  Compressor compressor;
  DoubleSolenoid piston;
  DoubleSolenoid piston_1;
  //SpeedControllerGroup leftMotors = null;
  //SpeedControllerGroup rightMotors = null;

  DifferentialDrive differentialDrive;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    int compresserpsimax = 120;
    int compresserpsimin = 110;
    
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    gamepad = new XboxController(0);
    /*motor_6 = new CANSparkMax(6, MotorType.kBrushless);
    motor_4 = new CANSparkMax(4, MotorType.kBrushed);
    motor_3 = new CANSparkMax(3, MotorType.kBrushed);
    motor_2 = new CANSparkMax(2, MotorType.kBrushed);
    motor_1 = new CANSparkMax(1, MotorType.kBrushed);*/

    compressor = new Compressor(0, ourType);
    //piston = new DoubleSolenoid(ourType, 0,1);
    piston_1 = new DoubleSolenoid(ourType, 0,1);
    remote.a= gamepad.getAButton();
    remote.b= gamepad.getBButton();
    remote.x= gamepad.getXButton();
    remote.y= gamepad.getYButton();
    remote.lJoyX= gamepad.getX(kLeft);
    remote.lJoyY= gamepad.getY(kLeft);
    remote.rJoyX= gamepad.getX(kRight);
    remote.rJoyY= gamepad.getY(kRight);
    remote.lJoyBtn= gamepad.getStickButton(kLeft);
    remote.rJoyBtn= gamepad.getStickButton(kRight);
    remote.DPadCurrentPressedAngle= gamepad.getPOV();
    remote.start= gamepad.getStartButton();
    remote.back= gamepad.getBackButton();
    remote.Ltrigger= gamepad.getTrigger(kLeft);
    remote.Rtrigger= gamepad.getTrigger(kRight);
    remote.LTopTrigger= gamepad.getBumper(kLeft);
    remote.RTopTrigger= gamepad.getBumper(kRight);
    function remote.LRumbleOn() {
      gamepad.setRumble(kLeftRumble, 1);
    }
    function remote.LRumbleOn() {
      gamepad.setRumble(kLeftRumble, 1);
    }
    function remote.LRumbleOff() {
      gamepad.setRumble(kLeftRumble, 0);
    }
    function remote.RRumbleOn() {
      gamepad.setRumble(kRightRumble, 1);
    }
    remote.RRumbleOff() {
      gamepad.setRumble(kRightRumble, 0);
    }
    //compressor.enableAnalog(80, 120);
    //compressor.enableDigital();


    /*motor_3.restoreFactoryDefaults();
    motor_4.restoreFactoryDefaults();
    motor_1.restoreFactoryDefaults();
    motor_2.restoreFactoryDefaults();*/
    servo1 = new Servo(0);
    gamepad = new XboxController(0);

    //leftMotors = new SpeedControllerGroup(motor_2, motor_3);
    //rightMotors = new SpeedControllerGroup(motor_3, motor_4);

    //differentialDrive = new DifferentialDrive(leftMotors, rightMotors);

    //compressor = new Compressor(0, ourType);
    //piston = new DoubleSolenoid(ourType, 0,1);
    //piston_1 = new DoubleSolenoid(ourType, 0,1);

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
    system.out.prntln("current remote:");
    system.out.prntln(remote);
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    /*if(gamepad.getRawButton(1)) {
      servo1.setAngle(160);
    }
    if(gamepad.getRawButton(2)) {
      servo1.setAngle(0);
    }*/
    //motor_3.set(gamepad.getLeftY());
    //motor_1.set(gamepad.getLeftX());

    //motor_4.set(gamepad.getLeftX());
    if(compressor.getPressure()> compresserpsimax) {
      compressor.disable();
    } else if(compressor.getPressure()< compresserpsimin) {
      compressor.enableDigital();
    }

    if (gamepad.getRawButton(1)) {
      //motor_4.set(0.8);
      piston_1.set(DoubleSolenoid.Value.kForward);
    }
    if (gamepad.getRawButton(2)) {
      //motor_4.set(0);
      piston_1.set(DoubleSolenoid.Value.kReverse);
      //servo1.set(1);
    }
    if (gamepad.getRawButton(3)) {
      //motor_4.set(0);
      compressor.enableDigital();

    }
    if (gamepad.getRawButton(4)) {
      //motor_4.set(0);
      compressor.disable();
    }
    if (gamepad.getRawButton(5)) {
      //motor_4.set(0);
      servo1.setAngle(160);
    }
    if (gamepad.getRawButton(6)) {
      //motor_4.set(0);
      servo1.setAngle(40);
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
  public void teleopPeriodic() {}

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
