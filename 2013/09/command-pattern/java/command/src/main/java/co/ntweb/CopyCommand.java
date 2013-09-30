package co.ntweb;

import co.ntweb.Command;
import co.ntweb.Screen;

public class CopyCommand implements Command{
    private Screen screen;
    private String previous_status;
    private int start;
    private int end;

    public CopyCommand(Screen screen, int start, int end){
        this.screen = screen;
        this.previous_status = screen.toString();
        this.start = start;
        this.end = end;
    }

    public void execute(){
        this.screen.copy(this.start, this.end);
    }

    public void undo(){
        this.screen.clearClipBoard();
    }
}
