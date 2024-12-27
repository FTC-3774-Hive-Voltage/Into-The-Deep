package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Arrays;

@TeleOp(name = "Teleop")
public class Teleop extends LinearOpMode {
   Robot robot = new Robot();

   double slowToggle = 0.7;

   @Override
   public void runOpMode() throws InterruptedException {

       // Initialization variables, notifying robot is initialized and shows how long robot ran for
       telemetry.addData("Status", "Initialized");
       telemetry.update();

       robot.init(hardwareMap);

       robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       robot.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       robot.slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       robot.slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

       robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       robot.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       robot.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       robot.slideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       robot.slideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

       waitForStart();
       resetRuntime();

       while (opModeIsActive()) {
           telemetry.addData("Match Time (s)", getRuntime());
           telemetry.addData("FL Count", robot.frontLeft.getCurrentPosition());
           telemetry.addData("FR Count", robot.frontRight.getCurrentPosition());
           telemetry.addData("BL Count", robot.backLeft.getCurrentPosition());
           telemetry.addData("BR Count", robot.backRight.getCurrentPosition());
           telemetry.update();

           // Controller 1 functions
           double FrontLeftVal = gamepad1.left_stick_y - gamepad1.left_stick_x + (-gamepad1.right_stick_x * 1);
           double FrontRightVal = gamepad1.left_stick_y + (gamepad1.left_stick_x) - (-gamepad1.right_stick_x * 1);
           double BackLeftVal = gamepad1.left_stick_y + (gamepad1.left_stick_x) + (-gamepad1.right_stick_x * 1);
           double BackRightVal = gamepad1.left_stick_y - (gamepad1.left_stick_x) - (-gamepad1.right_stick_x * 1);

           if (gamepad1.left_trigger != 0) {   // To make it slower
               slowToggle = 0.5;
           }
           else if (gamepad1.right_trigger != 0) { // To make it faster
               slowToggle = 10;
           }
           else {
               slowToggle = 1;
           }

           // Controller 2 Functions
           if (gamepad2.x) {
               robot.pivot.setPosition(1); // Move forward while X is pressed
           }
           if (gamepad2.y) {
               robot.pivot.setPosition(0.65); // Move backward while Y is pressed
           }

           // For both slides
           robot.slideLeft.setPower(-gamepad2.left_stick_y);
           robot.slideRight.setPower(-gamepad2.left_stick_y);

           // For the claw
           if (gamepad2.left_bumper) {
               robot.claw.setPosition(0.7);
           }
           if (gamepad2.right_bumper) {
               robot.claw.setPosition(0);
           }

           // Move range to between 0 and +1, if not already
           double[] wheelPowers = {FrontRightVal, FrontLeftVal, BackLeftVal, BackRightVal};
           Arrays.sort(wheelPowers);
           if (wheelPowers[3] > 1) {
               FrontLeftVal /= wheelPowers[3];
               FrontRightVal /= wheelPowers[3];
               BackLeftVal /= wheelPowers[3];
               BackRightVal /= wheelPowers[3];
           }
           robot.frontLeft.setPower(-FrontLeftVal * 1 * slowToggle);
           robot.frontRight.setPower(-FrontRightVal * 1 * slowToggle);
           robot.backLeft.setPower(-BackLeftVal * 1 * slowToggle);
           robot.backRight.setPower(-BackRightVal * 1 * slowToggle);
           idle();
       }
   }
}
