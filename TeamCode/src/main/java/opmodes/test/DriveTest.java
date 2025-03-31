package opmodes.test;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.RunCommand;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import controlboards.DriverControlBoard;
import controlboards.IDriverControlBoard;
import subsystems.Drivetrain;

@TeleOp(name = "Drive Test", group = "Test")
public class DriveTest extends CommandOpMode {
    private Drivetrain drivetrain;

    @Override
    public void initialize() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        IDriverControlBoard driverControlBoard = new DriverControlBoard(this.gamepad1);

        setDriverBindings(driverControlBoard);

        drivetrain.setDefaultCommand(drivetrain.teleopRobotCentricDrive(
            driverControlBoard.getFwd(),
            driverControlBoard.getStrafe(),
            driverControlBoard.getRotation()
        ));


        waitForStart();

        schedule(new RunCommand(telemetry::update));
        schedule(drivetrain.startTeleopDrive());
    }


    private void setDriverBindings(IDriverControlBoard driver) {
        /* Drivetrain */
        driver.wantZoomZoom().whileActiveOnce(drivetrain.zoomZoom());
    }
}
