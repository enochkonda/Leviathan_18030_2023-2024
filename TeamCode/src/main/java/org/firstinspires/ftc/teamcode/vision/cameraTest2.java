package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;

@Autonomous (name = "cameraTest2", group = "trollbot")

public class cameraTest2 extends OpMode {
    static final int STREAM_WIDTH = 320; // modify for your camera
    static final int STREAM_HEIGHT = 240; // modify for your camera
    OpenCvWebcam webcam;
    SamplePipeline pipeline;
    @Override
    public void init() {
        telemetry.addLine("Testing?");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = null;
        webcamName = hardwareMap.get(WebcamName.class, "WebcamMain"); // put your camera's name here
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        pipeline = new SamplePipeline();
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(STREAM_WIDTH, STREAM_HEIGHT, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Camera Failed","");
                telemetry.update();
            }
        });

    }

    @Override
    public void loop() {
        telemetry.addData("Image Analysis:",pipeline.getAnalysis());
        telemetry.update();
    }


}
class SamplePipeline extends OpenCvPipeline {

    Mat YCrCb = new Mat();
    Mat Y = new Mat();
    Mat RectA_Y = new Mat();
    int avg;
    int avgA;
    static final int STREAM_WIDTH = 320; // modify for your camera
    static final int STREAM_HEIGHT = 240; // modify for your camera


    static final int WidthRectA = 130;
    static final int HeightRectA = 110;


    static final Point RectATopLeftAnchor = new Point((STREAM_WIDTH - WidthRectA) / 2 + 300, ((STREAM_HEIGHT - HeightRectA) / 2) - 100);
    Point RectATLCorner = new Point(
            RectATopLeftAnchor.x,
            RectATopLeftAnchor.y);
    Point RectABRCorner = new Point(
            RectATopLeftAnchor.x + WidthRectA,
            RectATopLeftAnchor.y + HeightRectA);




    /*
     * This function takes the RGB frame, converts to YCrCb,
     * and extracts the Y channel to the 'Y' variable
     */
    void inputToY(Mat input) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        ArrayList<Mat> yCrCbChannels = new ArrayList<Mat>(3);
        Core.split(YCrCb, yCrCbChannels);
        Y = yCrCbChannels.get(0);

    }

    @Override
    public void init(Mat firstFrame) {
        inputToY(firstFrame);
        RectA_Y = Y.submat(new Rect(RectATLCorner, RectABRCorner));
    }

    @Override
    public Mat processFrame(Mat input) {
        inputToY(input);
        System.out.println("processing requested");
        avg = (int) Core.mean(Y).val[0];
        avgA = (int) Core.mean(RectA_Y).val[0];
        YCrCb.release(); // don't leak memory!
        Y.release(); // don't leak memory!


        Imgproc.rectangle(input,
                RectATLCorner,
                RectABRCorner,
                new Scalar(0, 0, 255),
                2);
        return input;

    }

    public int getAnalysis() {
        return avg;
    }
    public int getRectA_Analysis() {
        return avgA;
    }

}
