package controlboards;

import com.arcrobotics.ftclib.command.button.Trigger;

import java.util.function.DoubleSupplier;

public interface IDriverControlBoard {

    DoubleSupplier getFwd();
    DoubleSupplier getStrafe();
    DoubleSupplier getRotation();
    DoubleSupplier getIntakeTrim();

    Trigger wantZoomZoom();
    Trigger wantExtendIntake();
    Trigger wantDeployIntake();


}
