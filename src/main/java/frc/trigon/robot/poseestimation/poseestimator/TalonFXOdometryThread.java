// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.trigon.robot.poseestimation.poseestimator;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.jni.CANBusJNI;
import frc.trigon.robot.RobotContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides an interface for asynchronously reading high-frequency measurements to a set of queues.
 *
 * <p>This version is intended for Phoenix 6 devices on both the RIO and CANivore buses. When using
 * a CANivore, the thread uses the "waitForAll" blocking method to enable more consistent sampling.
 * This also allows Phoenix Pro users to benefit from lower latency between devices using CANivore
 * time synchronization.
 */
public class TalonFXOdometryThread extends Thread {
    private final Lock signalsLock = new ReentrantLock();
    private final List<Queue<Double>> queues = new ArrayList<>();
    private BaseStatusSignal[] signals = new BaseStatusSignal[0];
    private boolean isCANFD = false;

    private static TalonFXOdometryThread INSTANCE = null;

    public static TalonFXOdometryThread getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TalonFXOdometryThread();
        }
        return INSTANCE;
    }

    private TalonFXOdometryThread() {
        setName("PhoenixOdometryThread");
        setDaemon(true);
        start();
    }

    public Queue<Double> registerSignal(ParentDevice device, StatusSignal<Double> signal) {
        Queue<Double> queue = new ArrayBlockingQueue<>(100);
        signalsLock.lock();
        RobotContainer.SWERVE.odometryLock.lock();
        try {
            isCANFD = CANBusJNI.JNI_IsNetworkFD(device.getNetwork());
            BaseStatusSignal[] newSignals = new BaseStatusSignal[signals.length + 1];
            System.arraycopy(signals, 0, newSignals, 0, signals.length);
            newSignals[signals.length] = signal;
            signals = newSignals;
            queues.add(queue);
        } finally {
            signalsLock.unlock();
            RobotContainer.SWERVE.odometryLock.unlock();
        }
        return queue;
    }

    @Override
    public void run() {
        while (true) {
            // Wait for updates from all signals
            signalsLock.lock();
            try {
                if (isCANFD) {
                    BaseStatusSignal.waitForAll(2.0 / PoseEstimatorConstants.ODOMETRY_FREQUENCY_HERTZ, signals);
                } else {
                    // "waitForAll" does not support blocking on multiple
                    // signals with a bus that is not CAN FD, regardless
                    // of Pro licensing. No reasoning for this behavior
                    // is provided by the documentation.
                    Thread.sleep((long) (1000.0 / PoseEstimatorConstants.ODOMETRY_FREQUENCY_HERTZ));
                    if (signals.length > 0) BaseStatusSignal.refreshAll(signals);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                signalsLock.unlock();
            }

            // Save new data to queues
            RobotContainer.SWERVE.odometryLock.lock();
            try {
                for (int i = 0; i < signals.length; i++) {
                    queues.get(i).offer(signals[i].getValueAsDouble());
                }
            } finally {
                RobotContainer.SWERVE.odometryLock.unlock();
            }
        }
    }
}