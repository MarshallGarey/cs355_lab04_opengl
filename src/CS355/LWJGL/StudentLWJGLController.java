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

import java.security.Key;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController {

    // Virtual camera
    private final double ORIGINAL_CAMERA_ROTATION = 0;
    private final double CAMERA_START_X = 0;
    private final double CAMERA_START_Y = 0;
    private final double CAMERA_START_Z = 50;
    private Point3D ORIGINAL_CAMERA_POSITION = new Point3D(CAMERA_START_X, CAMERA_START_Y, CAMERA_START_Z);
    private final double CAM_MOVE_DIFF = 1;
    private final double CAM_ROTATE_DIFF = 0.01;
    private Point3D cameraPosition;
    private double cameraRotation;

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
        cameraPosition = ORIGINAL_CAMERA_POSITION;
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
            cameraPosition.x -= CAM_MOVE_DIFF;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            // Move back
            cameraPosition.z += CAM_MOVE_DIFF;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            // Move right
            cameraPosition.x += CAM_MOVE_DIFF;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            // Move forward
            cameraPosition.z += CAM_MOVE_DIFF;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            // Rotate left
            cameraRotation -= CAM_ROTATE_DIFF;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            // Rotate right
            cameraRotation += CAM_ROTATE_DIFF;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            // Move up
            cameraPosition.y -= CAM_MOVE_DIFF;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            // Move down
            cameraPosition.y += CAM_MOVE_DIFF;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
            // Reset camera
            cameraPosition = ORIGINAL_CAMERA_POSITION;
            cameraRotation = ORIGINAL_CAMERA_ROTATION;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
            // Set to orthographic mode
            perspectiveMode = false;
            render();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
            // Set to perspective mode
            perspectiveMode = true;
            render();
        }
    }

    //This method is the one that actually draws to the screen.
    @Override
    public void render() {
        //This clears the screen.
        glClear(GL_COLOR_BUFFER_BIT);

        //Do your drawing here.
        // Build the transformation matrix. Only do this once, since it's the same.
        Matrix worldToCamera = calculateWorldToCameraTransformation();

        // Build the clip matrix
        Matrix clip = calculateClipMatrix();

        ArrayList<Instance> sceneModels = CS355.getController().getScene().instances();
        for (Instance model : sceneModels) {
            // Set the color once for each model.
            g2d.setColor(model.getColor());
            for (Line3D line : model.getModel().getLines()) {

                // Build 3D homogeneous world-space coordinates
                double startPoint[] = new double[4];
                double endPoint[] = new double[4];

                startPoint[0] = line.start.x;
                startPoint[1] = line.start.y;
                startPoint[2] = line.start.z;
                startPoint[3] = 1;

                endPoint[0] = line.end.x;
                endPoint[1] = line.end.y;
                endPoint[2] = line.end.z;
                endPoint[3] = 1;

                // Multiply matrix by 3D homogeneous world-space coordinates
                startPoint = worldToCamera.vectorMultiply(startPoint);
                endPoint = worldToCamera.vectorMultiply(endPoint);

                // Multiply camera-space coordinates by clip matrix -> clip coordinates
                startPoint = clip.vectorMultiply(startPoint);
                endPoint = clip.vectorMultiply(endPoint);

                // Apply clip test
                if (isInViewFrustum(startPoint, endPoint)) {

                    // Map clip space coordinate to canonical coordinate ([-1,1], where center is origin)
                    clipToCanonical(startPoint);
                    clipToCanonical(endPoint);

                    // Map canonical coordinate to screen coordinate ([0,2047], where upper-left is origin)
                    double screenStart[] = canonicalToScreen(startPoint);
                    double screenEnd[] = canonicalToScreen(endPoint);

                    // Draw the final 2D coordinates.
                    g2d.drawLine((int) screenStart[0], (int) screenStart[1], (int) screenEnd[0], (int) screenEnd[1]);
                }
            }
        }
    }

}
