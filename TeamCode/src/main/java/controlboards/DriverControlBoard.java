package controlboards;

import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.function.DoubleSupplier;

public class DriverControlBoard implements IDriverControlBoard {
    private GamepadEx driver;

    public DriverControlBoard(Gamepad gamepad) {
        this.driver = new GamepadEx(gamepad);
    }

    @Override
    public DoubleSupplier getFwd() {
        return () -> driver.getLeftY();
    }

    @Override
    public DoubleSupplier getStrafe() {
        return () -> driver.getLeftX();
    }

    @Override
    public DoubleSupplier getRotation() {
        return () -> driver.getRightX();
    }

    @Override
    public DoubleSupplier getIntakeTrim() {
        return () -> driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
    }

    @Override
    public Trigger wantZoomZoom() {
        return this.driver.getGamepadButton(GamepadKeys.Button.X);
    }

    @Override
    public Trigger wantExtendIntake() {
        return new Trigger(() -> this.driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1);
    }

    @Override
    public Trigger wantDeployIntake() {
        return new Trigger(() -> this.driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1);
    }
}
