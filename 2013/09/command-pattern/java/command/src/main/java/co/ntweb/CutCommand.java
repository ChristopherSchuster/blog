package co.ntweb;

import co.ntweb.Command;
import co.ntweb.Screen;

public class CutCommand implements Command{
    private Screen screen;
    private String previousStatus;
    private int start;
    private int end;

    public CutCommand(Screen screen, int start, int end){
        this.screen = screen;
        this.previousStatus = screen.toString();
        this.start = start;
        this.end = end;
    }

    public void execute(){
        this.screen.cut(this.start, this.end);
    }

    public void undo(){
        this.screen.clearClipBoard();
        this.screen.setText(this.previousStatus);
    }
}
