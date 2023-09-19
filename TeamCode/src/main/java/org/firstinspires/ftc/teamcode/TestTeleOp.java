package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp (name = "TestTeleOp", group = "trollbot")
public class TestTeleOp extends OpMode {
    public void init() {
        gamepad1 = new Gamepad();
        gamepad2 = new Gamepad();
    }

    public void start() {

    }

    public void loop() {
    }
}
