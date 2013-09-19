package co.ntweb.maigfrga.observer;

import java.util.Observable;

/* Subject implementation Observer pattern*/

public class NotificationSubject extends Observable
{
    public void sendMessage(String message){
        System.out.println( "Change status and notify observers" );
        setChanged();
        notifyObservers(message);
    }

}
