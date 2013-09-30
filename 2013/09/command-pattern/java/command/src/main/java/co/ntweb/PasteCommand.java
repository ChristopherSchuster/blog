package co.ntweb;

import co.ntweb.Command;
import co.ntweb.Screen;

public class PasteCommand implements Command{
    private Screen screen;
    private String previousStatus;
    private int offset;

    public PasteCommand(Screen screen, int offset){
        this.screen = screen;
        this.previousStatus = screen.toString();
        this.offset = offset;
    }

    public void execute(){
        this.screen.paste(this.offset);
    }

    public void undo(){
        this.screen.clearClipBoard();
        this.screen.setText(this.previousStatus);
    }
}
