package co.ntweb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import co.ntweb.Screen;
import co.ntweb.CopyCommand;
import co.ntweb.CutCommand;
import co.ntweb.PasteCommand;
import co.ntweb.ScreenInvoker;

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
        command.undo();
        assertTrue("".equals(screen.getClipBoard()));
    }


    public void testCutCommand()
    {
        Screen screen = new Screen("hello world!!");
        CutCommand command = new CutCommand(screen, 5, 11);
        command.execute();
        String text_cut = screen.getClipBoard();
        assertEquals(text_cut, " world");
        assertFalse(text_cut.equals(screen.toString()));
        assertTrue("hello!!".equals(screen.toString()));
        command.undo();
        assertTrue("hello world!!".equals(screen.toString()));
        assertTrue("".equals(screen.getClipBoard()));
    }

    public void testPasteCommand()
    {
        Screen screen = new Screen("hello world!!");
        CutCommand cutCommand = new CutCommand(screen, 5, 11);
        cutCommand.execute();
        assertEquals(screen.getClipBoard(), " world");
        PasteCommand pasteCommand = new PasteCommand(screen, 0);
        pasteCommand.execute();
        assertEquals(screen.toString(), " worldhello!!");
        pasteCommand.undo();
        assertEquals(screen.toString(), "hello!!");
        cutCommand.undo();
        assertEquals(screen.toString(), "hello world!!");
    }


    public void testInvoker(){
        Screen editor = new Screen("hello world!!");
        ScreenInvoker client = new ScreenInvoker();

        CutCommand cutCommand = new CutCommand(editor, 5, 11);
        client.storeAndExecute(cutCommand);
        assertEquals("hello!!", editor.toString());

        PasteCommand pasteCommand = new PasteCommand(editor, 0);
        client.storeAndExecute(pasteCommand);
        assertEquals(editor.toString(), " worldhello!!");

        CopyCommand copyCommand = new CopyCommand(editor, 0, editor.length());
        client.storeAndExecute(copyCommand);

        PasteCommand pasteCommand2 = new PasteCommand(editor, 0);
        client.storeAndExecute(pasteCommand2);
        assertEquals(editor.toString(), " worldhello!! worldhello!!");

        //undo last paste
        client.undoLast();
        assertEquals(editor.toString(), " worldhello!!");

        //undo copy
        client.undoLast();
        assertEquals(editor.toString(), " worldhello!!");

        //undo paste
        client.undoLast();
        assertEquals("hello!!", editor.toString());

        //undo cut
        client.undoLast();
        assertEquals("hello world!!", editor.toString());
    }

}
