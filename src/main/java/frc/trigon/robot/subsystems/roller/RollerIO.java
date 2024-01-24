package frc.trigon.robot.subsystems.roller;

import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.subsystems.roller.placeholderroller.PLACEHOLDERRollerIO;
import frc.trigon.robot.subsystems.roller.simulationroller.SimulationRollerIO;
import org.littletonrobotics.junction.AutoLog;

public class RollerIO {
    static RollerIO generateIO() {
        if (RobotConstants.IS_REPLAY)
            return new RollerIO();
        if (RobotConstants.ROBOT_TYPE == RobotConstants.RobotType.PLACEHOLDER)
            return new PLACEHOLDERRollerIO();
        return new SimulationRollerIO();
    }

    protected void updateInputs(RollerInputsAutoLogged inputs) {
    }

    protected void setTargetVelocity(double targetVelocityRevolutionsPerSecond) {
    }

    protected void stopMotor() {
    }

    @AutoLog
    protected static class RollerInputs {
        public double motorVoltage = 0;
        public double motorCurrent = 0;
        public double motorVelocityRevolutionsPerSecond = 0;

        public boolean infraredSensorTriggered = false;
    }
}