package it.unibo.mvc.view;
import javax.swing.JFrame;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawResult;
import static java.lang.System.out;

public class DrawNumberStdoutView implements DrawNumberView {
    private static final String FRAME_NAME = "Draw Number App";
    private DrawNumberController controller;
    private final JFrame frame = new JFrame(FRAME_NAME);
    /**
     * Sets the controller controlled by this view (if works as input).
     *
     * @param observer the controller to attach
     */
    public void setController(DrawNumberController observer){
        this.controller = observer;
        //to comment if not used as input view
    }

    /**
     * This method is called before the UI is used. It should finalize its status (if needed).
     */
    public void start(){
        this.frame.setVisible(true);
    }

    /**
     * Tells the UI to display the result of the draw.
     *
     * @param res the result of the last draw
     */
    public void result(DrawResult res){
        out.println(res.getDescription());
    }
}
