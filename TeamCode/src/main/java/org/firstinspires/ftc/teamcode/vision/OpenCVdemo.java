package org.firstinspires.ftc.teamcode.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class OpenCVdemo extends OpenCvPipeline {
    public enum teamElementPosition {
        LEFT,
        CENTER,
        RIGHT
    }
    //Makes a variable
    private volatile teamElementPosition position;
    double leftValue, centerValue, rightValue, winningValue;

    private final Scalar green = new Scalar(0, 255, 0), red = new Scalar(255, 0, 0);

    Rect leftRectangle = new Rect(100 , 300, 60. 120);
    Rect centerRectangle = new Rect(340 , 220, 120, 60);
    Rect rightRectangle = new Rect(530 , 300, 60, 120);
    @Override
    public Mat processFrame(Mat input) {
        //Makes 3 new images that are cropped sections of the original image
        Mat leftSquare = input.submat(leftRectangle);
        Mat centerSquare = input.submat(centerRectangle);
        Mat rightSquare = input.submat(rightRectangle);

        //makes each image only have red pixels.
        Core.extractChannel(leftSquare, leftSquare, 0);
        Core.extractChannel(centerSquare, centerSquare, 0);
        Core.extractChannel(rightSquare, rightSquare, 0);

        //Gets the average color that is red
        Scalar leftAvg = Core.mean(leftSquare), centerAvg = Core.mean(centerSquare), rightAvg = Core.mean(rightSquare);

        //makes it comparable
        leftValue = leftAvg.val[0];
        centerValue = centerAvg.val[0];
        rightValue = rightAvg.val[0];

        //sets the winning value to the image with the most red
        winningValue = Math.max(leftValue, Math.max(centerValue, rightValue));

        //makes a blank image
        Mat output = new Mat();
        if (winningValue == leftValue) {
            position = teamElementPosition.LEFT;
            //copying input img to output img
            input.copyTo(output);
            //create highlghted rectangle, green for winning rectangle and red for the 2 wrong rectangles
            Imgproc.rectangle(output, leftRectangle, green, 2);
            Imgproc.rectangle(output, centerRectangle, red, 2);
            Imgproc.rectangle(output, rightRectangle, red, 2);
        } else if (rightValue == winningValue) {
             position = teamElementPosition.RIGHT;
             input.copyTo(output);
             Imgproc.rectangle(output, leftRectangle, red, 2);
             Imgproc.rectangle(output, centerRectangle, red, 2);
             Imgproc.rectangle(output, rightRectangle, green, 2);
        } else {
            position = teamElementPosition.CENTER;
            input.copyTo(output);
            Imgproc.rectangle(output, leftRectangle, red, 2);
            Imgproc.rectangle(output, centerRectangle, green, 2);
            Imgproc.rectangle(output, rightRectangle, red, 2);
        }
        //return camera image with the colored rectangles outlining each corresponding rectangle
        return(output);
    }

    public teamElementPosition getObjectPosition() {return position;}
}


