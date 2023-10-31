package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import java.util.concurrent.TimeUnit;
@TeleOp(name = "TestTeleop")

public class TestTeleop extends LinearOpMode {


    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftRear = null;
    private DcMotor rightRear = null;
    private DcMotor rLift = null;
    private DcMotor lLift = null;
    private DcMotor vector = null;
    private CRServo leftIntake = null;
    private CRServo rightIntake = null;
    private CRServo dread = null;
    private Servo leftclaw = null;
    private Servo rightclaw = null;



    /*
     * Change the pattern every 10 seconds in AUTO mode.
     */
    private final static int LED_PERIOD = 10;

    /*
     * Rate limit gamepad button presses to every 500ms.
     */
    private final static int GAMEPAD_LOCKOUT = 500;


    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

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
        leftclaw = hardwareMap.get(Servo.class,"leftclaw");
        rightclaw = hardwareMap.get(Servo.class,"rightclaw");
        vector = hardwareMap.get(DcMotor.class,"vector");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.FORWARD);
        rLift.setDirection(DcMotor.Direction.FORWARD);
        lLift.setDirection(DcMotor.Direction.REVERSE);
        vector.setDirection(DcMotor.Direction.FORWARD);
        displayKind = Blink.DisplayKind.AUTO;

        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        pattern = RevBlinkinLedDriver.BlinkinPattern.CP1_BREATH_FAST;
        blinkinLedDriver.setPattern(pattern);

        display = telemetry.addData("Display Kind: ", displayKind.toString());
        patternName = telemetry.addData("Pattern: ", pattern.toString());

        ledCycleDeadline = new Deadline(LED_PERIOD, TimeUnit.SECONDS);
        gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);




        waitForStart();

        while (opModeIsActive()) {

            double G1rightStickY = gamepad1.right_stick_y;
            double G1leftStickY = gamepad1.left_stick_y;
            double G1rightStickX = gamepad1.right_stick_x;
            double G1leftStickX = gamepad1.left_stick_x;
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
            boolean G2A = gamepad2.a;
            boolean G2UD = gamepad2.dpad_up; // up dpad
            boolean G2DD = gamepad2.dpad_down; // down dpad
            boolean G2RD = gamepad2.dpad_right;// right dpad
            boolean G2LD = gamepad2.dpad_left; //left dpad


            //Driving movements (First controller)

            if (G1rightStickX > 0) {  // Clockwise
                leftFront.setPower(0.5);
                leftRear.setPower(0.5);
                rightFront.setPower(-0.5);
                rightRear.setPower(-0.5);

            } else if (G1rightStickX < 0) { // Counterclockwise
                leftFront.setPower(-0.5);
                leftRear.setPower(-0.5);
                rightFront.setPower(0.5);
                rightRear.setPower(0.5);
            } else if (G1leftStickY > 0) { // Backwards
                leftFront.setPower(-0.5);
                leftRear.setPower(-0.5);
                rightFront.setPower(-0.5);
                rightRear.setPower(-0.5);
            } else if (G1leftStickY < 0) { // Forwards
                leftFront.setPower(.5);
                leftRear.setPower(.5);
                rightFront.setPower(.5);
                rightRear.setPower(.5);
            } else if (G1RT==1) { //strafe right
                leftFront.setPower(.5);
                rightFront.setPower(0);
                leftRear.setPower(0);
                rightRear.setPower(.5);
            } else if (G1LT==1) { //strafe left
                leftFront.setPower(0);
                rightFront.setPower(.5);
                leftRear.setPower(.5);
                rightRear.setPower(0);
            } else if (G1Y) { // Intake + treadmill going up
                leftIntake.setPower(-1);
                rightIntake.setPower(1);
                dread.setPower(-1);
                pattern = RevBlinkinLedDriver.BlinkinPattern.SHOT_BLUE;
                displayPattern();
                gamepadRateLimit.reset();
            } else if (G2A) { // Outtake the Intake (second controller)
                pattern = RevBlinkinLedDriver.BlinkinPattern.SHOT_RED;
                displayPattern();
                gamepadRateLimit.reset();
                leftIntake.setPower(1);
                rightIntake.setPower(-1);
                //dread.setPower(1); in case
            } else if (G1leftBumper) { // Diagonal: Upper left (First controller)
                leftFront.setPower(-0.5);
                rightFront.setPower(0);
                leftRear.setPower(0);
                rightRear.setPower(-.5);
            } else if (G1B) { // Diagonal: Lower Right (First controller)
                leftFront.setPower(.5);
                rightFront.setPower(-.5);
                leftRear.setPower(-.5);
                rightRear.setPower(.5);
            } else if (G1X) { // Diagonal: Upper Right (First controller)
                leftFront.setPower(-.5);
                rightFront.setPower(.5);
                leftRear.setPower(.5);
                rightRear.setPower(-.5);
            } else if (G1rightBumper) { // Diagonal: Lower left (First controller)
                leftFront.setPower(0);
                rightFront.setPower(-.5);
                leftRear.setPower(-.5);
                rightRear.setPower(0);
                //moving into claw and linear slides (second controller)
            } else if (G2UD) { // Linear pillars move up (second controller)
                rLift.setPower(1);
                lLift.setPower(1);
            } else if (G2DD) { // Linear pillars move down (second controller)
                rLift.setPower(-1);
                lLift.setPower(-1);
            } else if (G2leftStickY > 0) { //linear SLIDE moves up (second controller)
                vector.setPower(.5);
            } else if (G2leftStickY < 0) { //linear SLIDE moves down (second controller)
                vector.setPower(-.5);
            } else if (G2RD) { //outtake moves inward (second controller)
                leftclaw.setPosition(10);
                rightclaw.setPosition(-10);
            } else if (G2LD) { //outtake moves outward (second controller)
                leftclaw.setPosition(10);
                rightclaw.setPosition(-10);
            } else { //STOP IN THE NAME OF THE LAW!
                leftFront.setPower(0);
                rightFront.setPower(0);
                leftRear.setPower(0);
                rightRear.setPower(0);
                leftIntake.setPower(0);
                rightIntake.setPower(0);
                dread.setPower(0);
                pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
                displayPattern();
                gamepadRateLimit.reset();
                rLift.setPower(0);
                lLift.setPower(0);
                vector.setPower(0);
            }


            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
    protected void setDisplayKind(Blink.DisplayKind displayKind)
    {
        this.displayKind = displayKind;
        display.setValue(displayKind.toString());
    }

    protected void doAutoDisplay()
    {
        if (ledCycleDeadline.hasExpired()) {
            pattern = pattern.next();
            displayPattern();
            ledCycleDeadline.reset();
        }
    }
    protected void displayPattern()
    {
        blinkinLedDriver.setPattern(pattern);
        patternName.setValue(pattern.toString());
    }

}
