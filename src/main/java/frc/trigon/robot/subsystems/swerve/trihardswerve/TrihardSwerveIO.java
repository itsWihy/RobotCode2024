package frc.trigon.robot.subsystems.swerve.trihardswerve;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.trigon.robot.poseestimation.poseestimator.TalonFXOdometryThread;
import frc.trigon.robot.subsystems.swerve.SwerveIO;
import frc.trigon.robot.subsystems.swerve.SwerveInputsAutoLogged;

import java.util.Queue;

public class TrihardSwerveIO extends SwerveIO {
    private final Pigeon2 gyro = TrihardSwerveConstants.GYRO.get();
    private final Queue<Double> yawQueue = TalonFXOdometryThread.getInstance().registerSignal(gyro, TrihardSwerveConstants.YAW_SIGNAL);

    @Override
    protected void updateInputs(SwerveInputsAutoLogged inputs) {
        refreshStatusSignals();

        inputs.gyroYawDegrees = TrihardSwerveConstants.YAW_SIGNAL.getValue();
        inputs.gyroPitchDegrees = TrihardSwerveConstants.PITCH_SIGNAL.getValue();
        inputs.accelerationX = TrihardSwerveConstants.X_ACCELERATION_SIGNAL.getValue();
        inputs.accelerationY = TrihardSwerveConstants.Y_ACCELERATION_SIGNAL.getValue();
        inputs.accelerationZ = TrihardSwerveConstants.Z_ACCELERATION_SIGNAL.getValue();
        inputs.odometryYawsDegrees = yawQueue.stream().mapToDouble(Double::doubleValue).toArray();
    }

    @Override
    protected void setHeading(Rotation2d heading) {
        gyro.setYaw(heading.getDegrees());
    }

    private void refreshStatusSignals() {
        BaseStatusSignal.refreshAll(
                TrihardSwerveConstants.YAW_SIGNAL,
                TrihardSwerveConstants.PITCH_SIGNAL,
                TrihardSwerveConstants.X_ACCELERATION_SIGNAL,
                TrihardSwerveConstants.Y_ACCELERATION_SIGNAL,
                TrihardSwerveConstants.Z_ACCELERATION_SIGNAL
        );
    }
}
