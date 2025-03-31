package subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.StartEndCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.DoubleSupplier;
import java.util.stream.Stream;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

public class Drivetrain extends SubsystemBase {
    private Follower follower;
    private Telemetry telemetry;
    private Pose startingPose;

    private double multiplier = 0.7;

    private void configureMotors(DcMotor motor) {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public Drivetrain(HardwareMap hardwareMap, Telemetry telemetry, Pose startingPose) {
        this.follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        this.follower.setStartingPose(startingPose);

        this.startingPose = startingPose;


        // Iterates over all the motor names, gets them from the hardware map, and configures them
        // Map consumes one element in the stream and produces a new one
        // In our case it consumes a string that is the name of the motor, and produces a
        // DcMotor from the hardware map
        // forEach iterates over the elements and applies a given function to that element returning nothing.
        // in this case it calls the configureMotor function
        Stream.of(FollowerConstants.leftFrontMotorName,
            FollowerConstants.rightFrontMotorName,
            FollowerConstants.leftRearMotorName,
            FollowerConstants.rightRearMotorName)
        .map(str -> hardwareMap.get(DcMotor.class, str))
        .forEach(this::configureMotors);
    }

    public Drivetrain(HardwareMap hardwareMap, Telemetry telemetry) {
        this(hardwareMap, telemetry, new Pose(0,0,0));
    }

    @Override
    public void periodic() {
        this.follower.update();
    }

    private void releaseLimiter() {
        this.multiplier = 1;
    }

    private void enableLimiter() {
        this.multiplier = 0.7;
    }


    /* Inline Commands
    *  Instead of having a bunch on unnamed instant commands in your opModes,
    *  you can just have a method in the subsystem that returns a command.
    * */

    public CommandBase startTeleopDrive() {
        return new InstantCommand(follower::startTeleopDrive, this);
    }

    public CommandBase teleopRobotCentricDrive(DoubleSupplier fwd, DoubleSupplier strafe, DoubleSupplier rot) {
        return new RunCommand(() -> this.follower.setTeleOpMovementVectors(
            fwd.getAsDouble() * multiplier,
            strafe.getAsDouble() * multiplier,
            rot.getAsDouble() * multiplier,
            true), this);
    }

    public CommandBase teleopFieldCentricDrive(DoubleSupplier fwd, DoubleSupplier strafe, DoubleSupplier rot) {
        return new RunCommand(() -> this.follower.setTeleOpMovementVectors(
            fwd.getAsDouble() * multiplier,
            strafe.getAsDouble() * multiplier,
            rot.getAsDouble() * multiplier,
            false), this);
    }

    public CommandBase zoomZoom() {
        // releases the limiter when the command starts, renables it when the command stops.
        // can be used with a buttons whileHeld() method, or a triggers whileActive() method

        return new StartEndCommand(this::releaseLimiter, this::enableLimiter);
    }


}
