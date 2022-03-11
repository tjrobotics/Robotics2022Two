package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;

public class AutoCommands {
    CANSparkMax left_motor;
    CANSparkMax right_motor;
    CANSparkMax input_motor;
    CANSparkMax conveyor_motor;
    Servo shooting_servo;
    DoubleSolenoid input_ramp;
    DoubleSolenoid shooting_piston;
    public AutoCommands(CANSparkMax left_motor, CANSparkMax right_motor, CANSparkMax input_motor, CANSparkMax conveyor_motor, Servo shooting_servo, DoubleSolenoid input_ramp, DoubleSolenoid shooting_piston) {
        this.left_motor = left_motor;
        this.right_motor = right_motor;
        this.input_motor = input_motor;
        this.conveyor_motor = conveyor_motor;
        this.shooting_servo = shooting_servo;
        this.input_ramp = input_ramp;
        this.shooting_piston = shooting_piston;
    }

    public void driveStraight(double speed, double duration) {
        left_motor.set(speed);
        right_motor.set(speed);
        Timer.delay(duration);
        left_motor.set(0);
        right_motor.set(0);
    }

    public void turnRobot(double angle, double duration) {
        left_motor.set(duration*Constants.turingConversionRate*angle);
        right_motor.set(duration*Constants.turingConversionRate*angle*-1);
        Timer.delay(duration);
        left_motor.set(0);
        right_motor.set(0);
    }
    
    public void shootPiston(boolean shootPiston) {
       shooting_piston.set(shootPiston ? DoubleSolenoid.Value.kReverse:DoubleSolenoid.Value.kForward);
    }

    public void raiseInputRamp() {
        input_ramp.set(DoubleSolenoid.Value.kReverse);
    }

    public void setServoRamp(String setting) {
        if (setting == "up") {
            shooting_servo.set(Constants.servo_up_angle);
        } else if (setting == "down") {
            shooting_servo.set(Constants.servo_down_angle);
        } else {
            shooting_servo.set(Constants.servo_shooting_angle);
        }
    }

    public void runInput(double duration) {
        input_motor.set(10);
        conveyor_motor.set(15);
        Timer.delay(duration);
        input_motor.set(0);
        conveyor_motor.set(0);
    }

    //EDIT AUTO COMMANDS here -------------------------------------------------------------------


    //INPUY SYSTEM AUTO COMMAND
    public void COMMANDinputSystem() {
        setServoRamp("up");
        raiseInputRamp();
        Timer.delay(2);
        runInput(4);
        Timer.delay(2);
        setServoRamp("shooting");
        Timer.delay(2);
        shootPiston(true);
        Timer.delay(2);
        shootPiston(false);
    }

    //DRIVING SYSTEM AUTO COMMAND
    public void COMMANDdriving() {
        driveStraight(-0.2, 0.8);
        raiseInputRamp();
        Timer.delay(1);
        setServoRamp("up");
        runInput(4);
        Timer.delay(0.5);
        setServoRamp("shooting");
        Timer.delay(0.5);
        shootPiston(true);
        Timer.delay(0.5);
        shootPiston(false);
        Timer.delay(0.5);
        setServoRamp("up");
        Timer.delay(1);
        driveStraight(0.5, 1);
    }

}
