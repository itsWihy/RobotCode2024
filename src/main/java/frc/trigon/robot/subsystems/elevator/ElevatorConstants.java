package frc.trigon.robot.subsystems.elevator;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class ElevatorConstants {
    public static final double RETRACTED_ELEVATOR_LENGTH_METERS = 0.63;
    public static final double
            DRUM_RADIUS_METERS = 0.0222997564,
            DRUM_DIAMETER_METERS = DRUM_RADIUS_METERS * 2;
    public static final double GEAR_RATIO = 3.44;
    static final double CAMERA_PLATE_HEIGHT_METERS = 0.190193;
    static final double TOLERANCE_METERS = 0.035;
    static final SysIdRoutine.Config SYSID_CONFIG = new SysIdRoutine.Config(
            Units.Volts.of(0.25).per(Units.Second.of(1)),
            Units.Volts.of(2),
            Units.Second.of(1000)
    );
    static final Pose3d
            ELEVATOR_ORIGIN_POINT = new Pose3d(0.10018, 0, 0.04, new Rotation3d(0, edu.wpi.first.math.util.Units.degreesToRadians(10), 0)),
            TRANSPORTER_ORIGIN_POINT = new Pose3d(0.10018, 0, 0.06, new Rotation3d(0, edu.wpi.first.math.util.Units.degreesToRadians(10), 0));
    private static final double
            ELEVATOR_MECHANISM_WIDTH = 3,
            ELEVATOR_MECHANISM_HEIGHT = 3,
            ELEVATOR_MECHANISM_ROOT_X = 1.44,
            ELEVATOR_MECHANISM_ROOT_Y = 0,
            TARGET_ELEVATOR_POSITION_MECHANISM_ROOT_X = 1.57,
            TARGET_ELEVATOR_POSITION_MECHANISM_ROOT_Y = 0,
            LIGAMENT_LINE_WIDTH = 10;
    static final Mechanism2d ELEVATOR_MECHANISM = new Mechanism2d(
            ELEVATOR_MECHANISM_WIDTH,
            ELEVATOR_MECHANISM_HEIGHT
    );
    private static final MechanismRoot2d
            ELEVATOR_ROOT = ELEVATOR_MECHANISM.getRoot("ElevatorRoot", ELEVATOR_MECHANISM_ROOT_X, ELEVATOR_MECHANISM_ROOT_Y),
            TARGET_ELEVATOR_POSITION_ROOT = ELEVATOR_MECHANISM.getRoot("TargetElevatorPositionRoot", TARGET_ELEVATOR_POSITION_MECHANISM_ROOT_X, TARGET_ELEVATOR_POSITION_MECHANISM_ROOT_Y);
    private static final Color8Bit MECHANISM_COLOR = new Color8Bit(Color.kYellow);
    static final MechanismLigament2d
            ELEVATOR_LIGAMENT = ELEVATOR_ROOT.append(new MechanismLigament2d("ElevatorLigament", 0, 90, LIGAMENT_LINE_WIDTH, MECHANISM_COLOR)),
            TARGET_ELEVATOR_POSITION_LIGAMENT = TARGET_ELEVATOR_POSITION_ROOT.append(new MechanismLigament2d("TargetElevatorPositionLigament", 0, 90, LIGAMENT_LINE_WIDTH, new Color8Bit(Color.kGray)));

    public enum ElevatorState {
        RESTING(0, 100),
        FEEDING_FOR_CLOSE_SHOT(0, 100),
        SCORE_AMP(0.45, 100),
        SCORE_TRAP(0.5, 10),
        SCORE_TRAP_LOWERED(0.15, 10),
        FINISH_TRAP(0, 10);

        final double positionMeters;
        final double speedPercentage;

        ElevatorState(double positionMeters, double speedPercentage) {
            this.positionMeters = positionMeters;
            this.speedPercentage = speedPercentage;
        }
    }
}