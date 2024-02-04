1 public class Move {
2
3 private final int inRow;
4 private final int inLeft;
5 private final int inRight;
6
7 // Constructor of the class
8 public Move(int inRow, int inLeft, int inRight){
9 this.inRow = inRow;
10 this.inLeft = inLeft;
11 this.inRight = inRight;
12 }
13
14 /** Convert a Move object to a String format
15 */
16 public String toString(){
17 return(inRow + ":" + inLeft + "-" + inRight);
18 }
19
20 /** Returns the row
21 */
22 public int getRow(){
23 return this.inRow;
24 }
25
26 /** Returns the Left stick position
27 */
28 public int getLeftBound(){
29 return this.inLeft;
30 }
31
32 /** Returns the Right stick position
33 */
34 public int getRightBound(){
35 return this.inRight;
36 }
37
38 }
