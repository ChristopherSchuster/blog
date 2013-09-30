package co.ntweb;

import co.ntweb.Command;

import java.util.LinkedList;

public class ScreenInvoker{
    private LinkedList<Command> history = new LinkedList<Command>();

    public ScreenInvoker(){
    }

    public void storeAndExecute(Command cmd) {
      this.history.add(cmd);
      cmd.execute();
    }

    public void undoLast(){
        this.history.removeLast().undo();
    }

}
