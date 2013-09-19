package co.ntweb.maigfrga.observer;

import java.util.Observable;
import java.util.Observer;
 
public class EmailObserver implements Observer {
    private String message;

    public void update(Observable obj, Object arg) {
        if (arg instanceof String) {
            message = (String) arg;
            System.out.println("\nSend message by  Email: " + message );
        }
    }
}
