package CS355.LWJGL;


//You might notice a lot of imports here.
//You are probably wondering why I didn't just import org.lwjgl.opengl.GL11.*
//Well, I did it as a hint to you.
//OpenGL has a lot of commands, and it can be kind of intimidating.
//This is a list of all the commands I used when I implemented my project.
//Therefore, if a command appears in this list, you probably need it.
//If it doesn't appear in this list, you probably don't.
//Of course, your milage may vary. Don't feel restricted by this list of imports.

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.security.Key;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController {

    // Useful constants
    private final double RADIANS_TO_DEGREES = 180.0 / Math.PI;
    private final double DEGREES_TO_RADIANS = Math.PI / 180.0;

    // Virtual camera (everything for it)
    private final double ORIGINAL_CAMERA_ROTATION = 0;
    private final double CAMERA_START_X =0;// -4.2;//0;
    private final double CAMERA_START_Y =-4;// -5;//0;
    private final double CAMERA_START_Z = -12;// 4.8;//0;
    private final Point3D ORIGINAL_CAMERA_POSITION = new Point3D(CAMERA_START_X, CAMERA_START_Y, CAMERA_START_Z);
    private final double CAM_MOVE_DIFF = .18;
    private final double CAM_ROTATE_DIFF = 1.0; // 1 degree
    private Point3D cameraPosition;
    private double cameraRotation; // in degrees

    // Perspective/orthographic
    private boolean perspectiveMode = true;

    //This is a model of a house.
    //It has a single method that returns an iterator full of Line3Ds.
    //A "Line3D" is a wrapper class around two Point2Ds.
    //It should all be fairly intuitive if you look at those classes.
    //If not, I apologize.
    private WireFrame model = new HouseModel();

    //This method is called to "resize" the viewport to match the screen.
    //When you first start, have it be in perspective mode.
    @Override
    public void resizeGL() {
        // TODO: initialize stuff here
        perspectiveMode = true;
        cameraPosition = new Point3D(CAMERA_START_X, CAMERA_START_Y, CAMERA_START_Z);
        cameraRotation = ORIGINAL_CAMERA_ROTATION;
    }

    @Override
    public void update() {
        // Don't need anything here
    }

    //This is called every frame, and should be responsible for keyboard updates.
    //An example keyboard event is captured below.
    //The "Keyboard" static class should contain everything you need to finish
    // this up.
    @Override
    public void updateKeyboard() {
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            // Move left
            cameraPosition.x += CAM_MOVE_DIFF;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            // Move right
            cameraPosition.x -= CAM_MOVE_DIFF;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            // Move backward
            cameraPosition.z -= CAM_MOVE_DIFF;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            // Move forward
            cameraPosition.z += CAM_MOVE_DIFF;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            // Move up
            cameraPosition.y -= CAM_MOVE_DIFF;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            // Move down
            cameraPosition.y += CAM_MOVE_DIFF;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            // Rotate left
            cameraRotation -= CAM_ROTATE_DIFF;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            // Rotate right
            cameraRotation += CAM_ROTATE_DIFF;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
            // Reset camera
            cameraPosition.x = CAMERA_START_X;
            cameraPosition.y = CAMERA_START_Y;
            cameraPosition.z = CAMERA_START_Z;
            cameraRotation = ORIGINAL_CAMERA_ROTATION;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
            // Set to orthographic mode
            perspectiveMode = false;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
            // Set to perspective mode
            perspectiveMode = true;
        }
    }

    // TODO: I have no idea what I'm doing here... or in makeOrtho
    private void makePerspective() {
        glMatrixMode(GL_PROJECTION);
    }

    private void makeOrtho() {
        glMatrixMode(GL_MODELVIEW);
    }

    // This method is the one that actually draws to the screen.
    @Override
    public void render() {
        // This clears the screen.
        glClear(GL_COLOR_BUFFER_BIT);
        glColor3d(128,128,128);

        // Set viewing mde
//        if (perspectiveMode)
//            makePerspective();
//        else
//            makeOrtho();

        // Do your drawing here.
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        GLU.gluPerspective(90, 2, 1, 40);
        glTranslated(cameraPosition.x, cameraPosition.y, cameraPosition.z); // move to camera
        glRotated(-cameraRotation, 0, 1, 0); // rotate about y axis
//        glOrtho();

//        glScaled();

        for (Line3D line : model.getModelLines()) {
            glBegin(GL_LINES);
            glVertex3d(line.start.x, line.start.y, line.start.z);
            glVertex3d(line.end.x, line.end.y, line.end.z);
            glEnd();
        }
    }

}
