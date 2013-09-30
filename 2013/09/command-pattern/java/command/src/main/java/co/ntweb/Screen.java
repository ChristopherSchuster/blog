package co.ntweb;

public class Screen {
    private StringBuffer text;
    private StringBuffer clipBoard;

    public Screen(String text){
        this.text = new StringBuffer(text);
        this.clipBoard = new StringBuffer();
    }

    public void clearClipBoard(){
        this.clipBoard.delete(0, this.clipBoard.length());
    }

    public void cut(int start, int end){
        clearClipBoard();
 	    this.clipBoard.append(this.text.substring(start, end));
        this.text.delete(start, end);
    }

    public void copy(int start, int end){
        clearClipBoard();
 	    this.clipBoard.append(this.text.substring(start, end));
    }

    public void paste(int offset){
 	    this.text.insert(offset, getClipBoard());
    }

    public String getClipBoard(){
        return this.clipBoard.toString();
    }

    public String toString(){
        return this.text.toString();
    }

    public void setText(String text){
        this.text = new StringBuffer(text);
    }

    public int length(){
        return this.text.length();
    }
}
