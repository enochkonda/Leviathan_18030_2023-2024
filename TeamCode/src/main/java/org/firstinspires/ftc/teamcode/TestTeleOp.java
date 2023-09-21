package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp (name = "TestTeleOp", group = "trollbot")
public class TestTeleOp extends OpMode {
    public void init() {
        gamepad1 = new Gamepad();
        gamepad2 = new Gamepad();
        MecanumDriveTrainTest dt = new MecanumDriveTrainTest();

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        dt.init(hardwareMap);

        if (gamepad1.options) {
            dt.imu.resetYaw();
        }

        double botHeading = dt.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = (x * Math.cos(-botHeading) - y * Math.sin(-botHeading)) * 1.1;
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        dt.frontLeft.setPower((rotY + rotX + rx) / denominator);
        dt.rearRight.setPower((rotY - rotX + rx) / denominator);
        dt.frontRight.setPower((rotY - rotX - rx) / denominator);
        dt.rearRight.setPower((rotY + rotX - rx) / denominator);
    }

    public void start() {

    }

    public void loop() {
    }
}
