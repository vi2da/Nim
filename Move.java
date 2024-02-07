public class Move {
    private final int inRow;
    private final int inLeft;
    private final int inRight;
    // Constructor of the class
    public Move(int inRow, int inLeft, int inRight){
    this.inRow = inRow;
    this.inLeft = inLeft;
    this.inRight = inRight;
    }
    /** Convert a Move object to a String format
    */
    public String toString(){
    return(inRow + ":" + inLeft + "-" + inRight);
    }
    /** Returns the row
    */
    public int getRow(){
    return this.inRow;
    }
    /** Returns the Left stick position
    */
    public int getLeftBound(){
    return this.inLeft;
    }
    /** Returns the Right stick position
    */
    public int getRightBound(){
    return this.inRight;
    }
    }
    
    
    
    