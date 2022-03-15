// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    //CHANGE FOR TWO PLAYERS
    public static final boolean twoControllers = true;

    //CHANGE SERVO ANGLES HERE (< IS DOWN, > IS HIGHER)
    public static final int servo_up_angle = 175;
    public static final int servo_shooting_angle = 160;
    public static final int servo_down_angle = 130;

    public static final int ramp_hold_position = 100;
    public static final int ramp_down_position = 0;
    
    //CHANGE MIN AND MAX PRESSURE HERE
    public static final int pressureMin = 40;
    public static final int pressureMax = 60;

    //ROBOT SPEED
    public static final double robotSpeed = 0.8;
   
    //ACCELERATION, > IS SLOWER ACCELERATION
    public static final int robotSpeedInterval = 20;

    //so we don't get controller drift
    public static final double joystickTolerance = 0.1;

    //Controller Buttons (DO NOT CHANGE)
    public static final int A_Button = 1;
    public static final int B_Button = 2;
    public static final int X_Button = 3;
    public static final int Y_Button = 4;
    public static final int LB_Button = 5;
    public static final int RB_Button = 6;
    public static final int Back_Button = 7;
    public static final int Start_Button = 8;
    public static final int Left_Axis_Press = 9;
    public static final int Right_Axis_Press = 10;

    //controller mapping for single use. CHANGE FOR DIFFERENT BUTTONS FOR EACH FUNCTION
    public static final int PISTON_SHOOTING_UP = A_Button;
    public static final int PISTON_SHOOTING_DOWN = B_Button;
    public static final int COMPRESSOR_TOGGLE = X_Button;
    public static final int SERVO_DOWN = RB_Button;
    public static final int SERVO_UP = LB_Button;
    public static final int SERVO_SHOOTING = Back_Button;
    public static final int RAISE_INPUT_RAMP = Start_Button;
    public static final int SWITCH_DRIVING_DIRECTION = Y_Button;
    //Left Trigger is the Input
    //Left Stick Vertical for forward and backward
    //Left Stick Horizontal for Turning on a dime
    //Right Stick Horizonal for turning while driving forward/backward


    //NO TOUCHY. ITS THE MAGIC NUMBER
    public static final double turingConversionRate = 0.714285714/90;








    
        
}