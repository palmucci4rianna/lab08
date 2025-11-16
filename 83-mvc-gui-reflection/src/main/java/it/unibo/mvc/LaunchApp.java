package it.unibo.mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.controller.DrawNumberControllerImpl;
import it.unibo.mvc.model.DrawNumberImpl;
//import it.unibo.mvc.view.DrawNumberSwingView;

/**
 * Application entry-point.
 */
public final class LaunchApp {
    private static final int TIMES = 2;

    private LaunchApp() { }

    /**
     * Runs the application.
     *
     * @param args ignored
     * @throws ClassNotFoundException if the fetches class does not exist
     * @throws NoSuchMethodException if the 0-ary constructor do not exist
     * @throws InvocationTargetException if the constructor throws exceptions
     * @throws InstantiationException if the constructor throws exceptions
     * @throws IllegalAccessException in case of reflection issues
     * @throws IllegalArgumentException in case of reflection issues
     */
    public static void main(final String... args) 
        //final var model = new DrawNumberImpl();
        //final DrawNumberController app = new DrawNumberControllerImpl(model);
        //app.addView(new DrawNumberSwingView());
        throws
        ClassNotFoundException,
        NoSuchMethodException,
        InvocationTargetException,
        InstantiationException,
        IllegalAccessException {
            final var model = new DrawNumberImpl(); //creation of the model
            final DrawNumberController app = new DrawNumberControllerImpl(model); //creation of the controller
            for (final var viewType: List.of("Stdout", "Swing")) { //cicles type of views
                final var clazz = Class.forName("it.unibo.mvc.view.DrawNumber" + viewType + "View"); //fetches the class
                for (int i = 0; i < TIMES; i++) { //twice for each type
                    final var newView = clazz.getConstructor().newInstance(); //contructs via reflection
                    if (DrawNumberView.class.isAssignableFrom(newView.getClass())) {
                        app.addView((DrawNumberView) newView); //adds the view to the controller
                    } else {
                        throw new IllegalStateException(
                            newView.getClass() + " is not a subclass of " + DrawNumberView.class //excption message
                        );
                    }
                }
            }
        }
}
