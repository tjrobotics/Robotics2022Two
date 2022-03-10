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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

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

  //private RobotContainer m_robotContainer;
  CANSparkMax Left_Back_Motor;
  CANSparkMax Left_Front_Motor;
  CANSparkMax Right_Back_Motor;
  CANSparkMax Right_Front_Motor;
  CANSparkMax Conveyer_Motor;
  CANSparkMax Input_Motor;

  XboxController controller1;
  XboxController controller2;
  XboxController[] controllers;

  Servo shooting_servo;
  PneumaticsModuleType ourType = PneumaticsModuleType.CTREPCM;
  Compressor compressor;
  DoubleSolenoid Shooting_Piston;
  DoubleSolenoid Input_Piston;

  boolean driftMode = true;

  boolean enablecompressor = true;

  //current robot speed
  double currentRobotSpeed = 0;

  //which current facing the robot is
  boolean robotFacingInput = true;

  //decides what controller the button are on
  boolean twoControllers = Constants.twoControllers;
  XboxController buttonController;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    //starts the camera server
    CameraServer.startAutomaticCapture(0);
    CameraServer.startAutomaticCapture(1);

    //sets options for automous
    SmartDashboard.putStringArray("Auto List", new String[]{"Drive", "Input"});
    
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    //m_robotContainer = new RobotContainer();
    controller1 = new XboxController(0);
    controller2 = new XboxController(1);
    controllers = new XboxController[]{controller1, controller2};
    if (twoControllers) {
      buttonController = controllers[1];
    } else {
      buttonController = controllers[0];
    }
    
    //Change Motor ID HERE
    Left_Back_Motor = new CANSparkMax(1, MotorType.kBrushed);
    Left_Front_Motor = new CANSparkMax(5, MotorType.kBrushed);
    Right_Back_Motor = new CANSparkMax(3, MotorType.kBrushed);
    Right_Front_Motor = new CANSparkMax(4, MotorType.kBrushed);
    Conveyer_Motor = new CANSparkMax(2, MotorType.kBrushed);
    Input_Motor = new CANSparkMax(6, MotorType.kBrushed);

    //pnuematics
    compressor = new Compressor(0, ourType);
    Shooting_Piston = new DoubleSolenoid(ourType, 0,1);
    Input_Piston = new DoubleSolenoid(ourType, 2,3);

    //making following commands for wheels
    Left_Back_Motor.follow(Left_Front_Motor);
    Right_Back_Motor.follow(Right_Front_Motor);

    //servos
    shooting_servo = new Servo(0);

    //restore all motors to normal behavior
    Left_Back_Motor.restoreFactoryDefaults();
    Left_Front_Motor.restoreFactoryDefaults();
    Right_Back_Motor.restoreFactoryDefaults();
    Right_Front_Motor.restoreFactoryDefaults();
    Input_Motor.restoreFactoryDefaults();
    Conveyer_Motor.restoreFactoryDefaults();
    
    //lower input piston
    Input_Piston.set(DoubleSolenoid.Value.kForward);

    //making sure the compressor is on a loop
    compressor.enableAnalog(Constants.pressureMin, Constants.pressureMax);
    
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  //go forward and backward
   public void driveForward(double speed)  {
    //normal forward drive with no turn
    if (controllers[0].getRightX() < Constants.joystickTolerance && controllers[0].getRightX() > Constants.joystickTolerance*-1) {
      Left_Front_Motor.set(speed);
      Right_Front_Motor.set(speed);
    } else {
      //sets one side more than the other for moving turn
      if (controllers[0].getRightX() > Constants.joystickTolerance) {
        Left_Front_Motor.set(speed);
        Right_Front_Motor.set(0.2*speed);
      } else {
        Right_Front_Motor.set(speed);
        Left_Back_Motor.set(0.2*speed);
      }
    }
  }
  //turning on a dime
  public void turnRobot(double speed) {
    if (speed>0) {
      Right_Front_Motor.set(speed);
      Left_Front_Motor.set(speed*-1);
    } else {
      Left_Front_Motor.set(speed*-1);
      Right_Front_Motor.set(speed);
    }
  }

  @Override
  public void robotPeriodic() {


    CommandScheduler.getInstance().run();
  }
  

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    Input_Piston.set(DoubleSolenoid.Value.kForward);

  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    /*m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }*/

    AutoCommands auto = new AutoCommands(Left_Front_Motor, Right_Front_Motor, Input_Motor, Conveyer_Motor, shooting_servo, Input_Piston, Shooting_Piston);

    // At the beginning of auto
    String autoName = SmartDashboard.getString("Auto Selector", "Drive"); // This would make "Drive Forwards the default auto
    switch(autoName) {
      case "Drive":
        auto.COMMANDdriving();
      case "Input":
        auto.COMMANDinputSystem();
    }    
    
    /*shooting_servo.setAngle(Constants.servo_up_angle);
    Input_Piston.set(DoubleSolenoid.Value.kReverse);
    Timer.delay(2);
    Input_Motor.set(10);
    Conveyer_Motor.set(10);
    Timer.delay(4);
    Conveyer_Motor.set(0);
    Input_Motor.set(0);
    Timer.delay(2);
    shooting_servo.setAngle(Constants.servo_shooting_angle);
    Timer.delay(2);
    Shooting_Piston.set(DoubleSolenoid.Value.kReverse);
    Timer.delay(2);
    Shooting_Piston.set(DoubleSolenoid.Value.kForward);*/



    /*shooting_servo.setAngle(Constants.servo_up_angle);
    Right_Front_Motor.set(-0.2);
    Left_Front_Motor.set(-0.2);
    Timer.delay(1);
    Right_Front_Motor.set(0);
    Left_Front_Motor.set(0);
    Timer.delay(1);
    Left_Front_Motor.set(-0.5); //0.5 speed at 0.7 timer 90 degree
    Right_Front_Motor.set(0.5);
    Timer.delay(0.65);
    Right_Front_Motor.set(0);
    Left_Front_Motor.set(0);
    Timer.delay(1);
    Left_Front_Motor.set(0.5); //0.5 speed at 0.7 timer
    Right_Front_Motor.set(-0.5);
    Timer.delay(0.65);
    Right_Front_Motor.set(0);
    Left_Front_Motor.set(0);
    Timer.delay(1);
    Right_Front_Motor.set(-0.2);
    Left_Front_Motor.set(-0.2);
    Timer.delay(1);
    Right_Front_Motor.set(0);
    Left_Front_Motor.set(0);*/
  }

  /** This function is called periodically during autonomous. */
  //@Override
  //public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
      
    
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
       // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    //driving forward and backward
    //left stick is forward and backward and dime turning
    //right stick x axis is regular turning
    //change the speed
    double speed = Constants.robotSpeed;
    //change the startup time increment
    double increment = Constants.robotSpeedInterval;
    if (controllers[0].getLeftY() > Constants.joystickTolerance || controllers[0].getLeftY() < Constants.joystickTolerance*-1) {
      if (controllers[0].getLeftY() > Constants.joystickTolerance) {
        if (currentRobotSpeed != speed) {
          currentRobotSpeed += speed/increment;
        }
      } else {
        if (currentRobotSpeed != speed*-1) {
          currentRobotSpeed -= speed/increment;
        }
      }
      
      
      /*if (controllers[0].getLeftY() > Constants.joystickTolerance && speed < 0) {
        driveForward(0);
      } else if(controllers[0].getLeftY() < Constants.joystickTolerance*-1 && speed > 0) {
        driveForward(0);
      } else {
        if (robotFacingInput) {
          driveForward(currentRobotSpeed);
        } else {
          driveForward(currentRobotSpeed*-1);
        }
      }*/
      if (robotFacingInput) {
        driveForward(currentRobotSpeed);
      } else {
        driveForward(currentRobotSpeed*-1);
      }
      
      
    } else if(controllers[0].getRightX() < Constants.joystickTolerance*-1 || controllers[0].getRightX() > Constants.joystickTolerance) {
     turnRobot(controllers[0].getRightX()*speed);
      currentRobotSpeed = 0;
    } else {
      driveForward(0);
      currentRobotSpeed = 0;
    }
  
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
    //System.out.println(compressor.getPressure());
    /*if(compressor.getPressure()> Constants.pressureMax-1) {
      compressor.disable();
    } else if(compressor.getPressure()< Constants.pressureMin+1 && enablecompressor == true) {
      compressor.enableDigital();
    }*/
    if (buttonController.getRawButton(Constants.PISTON_SHOOTING_DOWN)) {
      Shooting_Piston.set(DoubleSolenoid.Value.kForward);
    }
    if (buttonController.getRawButton(Constants.PISTON_SHOOTING_UP)) {
      Shooting_Piston.set(DoubleSolenoid.Value.kReverse);
    }
    if (buttonController.getRawButton(Constants.COMPRESSOR_TOGGLE)) {
      if (enablecompressor) {
        compressor.disable();
        enablecompressor = false;
      } else {
        compressor.enableAnalog(Constants.pressureMin, Constants.pressureMax);
        enablecompressor = true;
      }
    }
    if (buttonController.getRawButton(Constants.SERVO_UP)) {
      shooting_servo.setAngle(Constants.servo_up_angle);
    }
    if (buttonController.getRawButton(Constants.SERVO_DOWN)) {
      shooting_servo.setAngle(Constants.servo_down_angle);
    }
    if (buttonController.getRawButton(Constants.SERVO_SHOOTING)) {
      shooting_servo.setAngle(Constants.servo_shooting_angle);
    }
    if (buttonController.getRawButton(Constants.RAISE_INPUT_RAMP)) {
      Input_Piston.set(DoubleSolenoid.Value.kReverse);
    }
    if (controllers[0].getLeftTriggerAxis()>0) {
      Input_Motor.set(10);
      Conveyer_Motor.set(15);
    } else {
      Input_Motor.set(0);
      Conveyer_Motor.set(0);
    }
    if (controllers[0].getRawButton(Constants.SWITCH_DRIVING_DIRECTION)) {
      robotFacingInput = !robotFacingInput;
    }

    }

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