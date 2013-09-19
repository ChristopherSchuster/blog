package co.ntweb.maigfrga.observer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import co.ntweb.maigfrga.observer.EmailObserver;
import co.ntweb.maigfrga.observer.NotificationSubject;
import co.ntweb.maigfrga.observer.SMSObserver;


public class ObserverTest extends TestCase
{
    public ObserverTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( ObserverTest.class );
    }

    public void testObserver()
    {
        NotificationSubject subject = new NotificationSubject();
        EmailObserver email_observer = new EmailObserver();
        SMSObserver sms_observer = new SMSObserver();

        subject.addObserver(email_observer);
        subject.addObserver(sms_observer);
        subject.sendMessage("Hello world :-)");
    }
}
