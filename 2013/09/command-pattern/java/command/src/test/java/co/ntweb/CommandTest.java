package co.ntweb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import co.ntweb.Screen;
import co.ntweb.CopyCommand;

public class CommandTest extends TestCase{

    public CommandTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( CommandTest.class );
    }

    public void testCopy()
    {
        Screen screen = new Screen("hello world!!");
        screen.copy(4, 8);
        String text_copy = screen.getClipBoard();
        assertEquals(text_copy, "o wo");
        assertFalse(text_copy.equals(screen.toString()));
        assertTrue("hello world!!".equals(screen.toString()));
    }

    public void testPaste(){
        Screen screen = new Screen("hello world!!");
        screen.copy(0, 5);
        screen.paste(screen.length());
        assertTrue("hello world!!hello".equals(screen.toString()));
    }

    public void testCut(){
        Screen screen = new Screen("hello world!!");
        screen.cut(5, 11);
        String text_cut = screen.getClipBoard();
        assertEquals(text_cut, " world");
        assertFalse(text_cut.equals(screen.toString()));
        assertTrue("hello!!".equals(screen.toString()));
    }

    public void testCopyCommand()
    {
        Screen screen = new Screen("hello world!!");
        CopyCommand command = new CopyCommand(screen, 4 ,8);
        command.execute();
        String text_copy = screen.getClipBoard();
        assertEquals(text_copy, "o wo");
        assertFalse(text_copy.equals(screen.toString()));
        assertTrue("hello world!!".equals(screen.toString()));
    }
}
