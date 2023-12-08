package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "LeviTeleOp", group = "LinearOpMode")

public class LeviTeleOp extends LinearOpMode {


    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftRear = null;
    private DcMotor rightRear = null;
    private DcMotor rLift = null;
    private DcMotor lLift = null;
    private CRServo intakein = null;
    private CRServo leftIntake = null;
    private CRServo rightIntake = null;
    private CRServo dread = null;
    private Servo leftPull = null;
    private Servo rightPull = null;
    private Servo drone = null;
    private Servo outtake = null;



    /*
     * Change the pattern every 10 seconds in AUTO mode.
     */
    private final static int LED_PERIOD = 10;

    /*
     * Rate limit gamepad button presses to every 500ms.
     */
    private final static int GAMEPAD_LOCKOUT = 500;

    Telemetry.Item patternName;
    Telemetry.Item display;
    Blink.DisplayKind displayKind;
    Deadline ledCycleDeadline;
    Deadline gamepadRateLimit;

    protected enum DisplayKind {
        MANUAL,
        AUTO

    }
    @Override
    public void runOpMode() {

        double left;
        double right;
        double drive;
        double turn;
        double max;

        telemetry.addData("Status", "Initialized");
        //telemetry.addData("getRuntime","watermelon");
        telemetry.update();

        leftFront = hardwareMap.get(DcMotor.class,"leftFront"); //frontleft, port 0
        rightFront = hardwareMap.get(DcMotor.class,"rightFront");  //frontright, port 1
        leftRear = hardwareMap.get(DcMotor.class,"leftRear"); //backleft, port 3
        rightRear = hardwareMap.get(DcMotor.class,"rightRear");  //backright, port 2
        rLift = hardwareMap.get(DcMotor.class,"rLift");
        lLift = hardwareMap.get(DcMotor.class,"lLift");
        leftIntake = hardwareMap.get(CRServo.class,"leftIntake");
        rightIntake = hardwareMap.get(CRServo.class,"rightIntake");
        dread = hardwareMap.get(CRServo.class,"dread");
        intakein = hardwareMap.get(CRServo.class,"intakein");
        rightPull = hardwareMap.get(Servo.class, "rightPull");
        leftPull = hardwareMap.get(Servo.class, "leftPull");
        drone = hardwareMap.get(Servo.class,"drone");
        outtake = hardwareMap.get(Servo.class,"outtake");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        rLift.setDirection(DcMotor.Direction.FORWARD);
        lLift.setDirection(DcMotor.Direction.REVERSE);
        displayKind = Blink.DisplayKind.AUTO;


        ledCycleDeadline = new Deadline(LED_PERIOD, TimeUnit.SECONDS);
        gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);




        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("Status", "Running");
            telemetry.update();

            double G1rightStickY = gamepad1.right_stick_y;
            double G1leftStickY = gamepad1.left_stick_y;
            double G1rightStickX = gamepad1.right_stick_x;
            double G1leftStickX = gamepad1.left_stick_x;
            //leftPower = Range.clip(G1leftStickY +G1rightStickX, -1.0, 1.0);
            //rightPower = Range.clip(G1leftStickY -G1rightStickX, -1.0, 1.0);
            boolean G1rightBumper = gamepad1.right_bumper;
            boolean G1leftBumper = gamepad1.left_bumper;
            boolean G1UD = gamepad1.dpad_up;   // up dpad
            boolean G1DD = gamepad1.dpad_down; //Down dpad
            boolean G1RD = gamepad1.dpad_right;// right dpad
            boolean G1LD = gamepad1.dpad_left; //left dpad
            boolean G1Y = gamepad1.y;
            boolean G1B = gamepad1.b;
            boolean G1X = gamepad1.x;
            boolean G1A = gamepad1.a;
            double G1RT = gamepad1.right_trigger;
            double G1LT = gamepad1.left_trigger;
            //Second controller (Intake/linear slide)
            double G2leftStickY = gamepad2.left_stick_y;
            boolean G2B = gamepad2.b;
            boolean G2Y = gamepad2.y;
            boolean G2A = gamepad2.a;
            boolean G2X = gamepad2.x;
            boolean G2UD = gamepad2.dpad_up; // up dpad
            boolean G2DD = gamepad2.dpad_down; // down dpad
            boolean G2RD = gamepad2.dpad_right;// right dpad
            boolean G2LD = gamepad2.dpad_left; //left dpad
            double G2LT = gamepad2.left_trigger;
            double G2RT = gamepad2.right_trigger;
            boolean G2rightBumper = gamepad2.right_bumper;
            boolean G2leftBumper = gamepad2.left_bumper;
            boolean G2back = gamepad2.back;

            // Run wheels in POV mode (note: The joystick goes negative when pushed forward, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            // This way it's also easy to just drive straight, or just turn.
            drive = -gamepad1.left_stick_y;
            turn  =  gamepad1.right_stick_x;

            // Combine drive and turn for blended motion.
            left  = drive + turn;
            right = drive - turn;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0)
            {
                left /= max;
                right /= max;
            }

            // Output the safe vales to the motor drives.
            leftFront.setPower(left/2);
            rightFront.setPower(right/2);
            leftRear.setPower(left/2);
            rightRear.setPower(right/2);

            if (G2A) { // Intake + treadmill going up
                leftIntake.setPower(1);
                rightIntake.setPower(1);
                intakein.setPower(1);
                //dread.setPower(1);
                gamepadRateLimit.reset();
            }if (G2B) { // Outtake the Intake (reverse intake)
                gamepadRateLimit.reset();
                leftIntake.setPower(-1);
                rightIntake.setPower(-1);
                intakein.setPower(-1);
                //dread.setPower(-1);
            }if (G2back) {
                drone.setPosition(0);
                sleep(3000);
                drone.setPosition(1);
                //moving into claw and linear slides (second controller)
            }if (G2LT == 1) { // Linear pillars move up (second controller)
                rLift.setPower(1.01);
                lLift.setPower(-1);
            }if (G2RT == 1) { // Linear pillars move down (second controller)
                rLift.setPower(-1);
                lLift.setPower(1);
            }if (G2UD) { //linear SLIDE moves up (second controller)
                dread.setPower(1);
            }if (G2DD) { //linear SLIDE moves down (second controller)
                dread.setPower(-1);
            }if (G2leftBumper) { //outtake moves inward (second controller)
                leftPull.setPosition(1);
                rightPull.setPosition(0);
            }if (G2rightBumper) { //outtake moves outward (second controller)
                leftPull.setPosition(0);
                rightPull.setPosition(1);
            }if (G2X) { //outtake outtake
                outtake.setPosition(1);
            }if (G2Y) {
                outtake.setPosition(0);
            }

        }
    }
}
