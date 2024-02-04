1 /**
2 * The Board class represents a board of the Nim game. The board can be of a variable size. In this
3 * implementation it only has 4 rows, 7 sticks in the first row, and 5,3 and 1 sticks in the following rows.
4 * A board object is disposable, meaning that the moves performed on the board are not reversible, and if a
5 * "clean" board is required, the user has to initialize a new board object.
6 * @author OOP course staff
7 */
8 public class Board {
9
10 /** The template of a Nim game board. */
11 private static final int[][] boardTemplate = {{1,1,1,1,1,1,1},{1,1,1,1,1},{1,1,1},{1}};
12 /** The number of rows in a Nim game board. */
13 private static final int NUM_OF_ROWS = boardTemplate.length;
14 /** The maximal number of sticks in a single row in a Nim game board. */
15 private static final int MAX_NUM_OF_STICKS_IN_ROW = boardTemplate[0].length;
16 /** The total number of sticks in a Nim game board. */
17 private static final int NUM_OF_ELEMENTS = NUM_OF_ROWS*(1+MAX_NUM_OF_STICKS_IN_ROW)/2;
18
19 private int[][] gameBoard; //2d array representing the game board.
20 private int numberOfMarkedSticks; //Number of currently marked sticks on the board.
21
22
23 /**
24 * Initializes a clear board.
25 */
26 public Board(){
27 numberOfMarkedSticks = 0;
28 gameBoard = new int[NUM_OF_ROWS][];
29 // Remember: when we have only one line inside a for/while loop,
30 // we do not have to use curly brackets! See below:
31 for(int i = 0; i < NUM_OF_ROWS; i++)
32 gameBoard[i] = (int[]) boardTemplate[i].clone();
33 }
34
35 /**
36 * Returns a multi-line human-readable visual representation of the board
37 * as a String object. Can be used for printing the board to screen and for
38 * debugging.
39 */
40 public String toString(){
41 /*
42 * When we implement a public method named 'toString()' (exactly this name!) which returns
43 * a String for some class, then when we send an object of this class to methods like
44 * System.out.print() or println(), they will print this output String. If the object has
45 * no such method, print() and println() will print using the template class_name@hashCodeIdentifier,
46 * which in this case will look something like oop.ex1.Board@55f96302
47 */
48 String output = "";
49 int currentLength,i,j;
50 for(i = NUM_OF_ROWS-1 ; i>=0 ; i--){
51
52 currentLength = gameBoard[i].length;
53 for(j=0;j<(MAX_NUM_OF_STICKS_IN_ROW-currentLength)/2.0;j++)
54 output += " ";
55
56 for(j=0;j<currentLength;j++)
57 output += gameBoard[i][j];
58
59 for(j=0;j<(MAX_NUM_OF_STICKS_IN_ROW-currentLength)/2.0;j++)
6
60 output += " ";
61
62 output += '\n';
63 }
64
65 return output;
66 }
67
68 /**
69 * Makes an attempt to mark the given stick sequence on the board.
70 * In case the move is illegal the board is not changed and an appropriate error code is returned:
71 * If the given coordinates exceed the boundaries of the board, -1 is returned.
72 * If the current move overlaps with previously marked sticks, 0 is returned.
73 * If the move is legal, the board changes accordingly, the number of marked sticks in the "numMarked"
74 * is updated, 1 is returned.
75 * @param move the move to perform
76 * @return 1 if the move was legal, 0 and -1 if the move is not legal (details above).
77 */
78 public int markStickSequence(Move move){
79
80 int rowNumber,leftBound,rightBound;
81 rowNumber = move.getRow();
82 leftBound = move.getLeftBound();
83 rightBound = move.getRightBound();
84
85 // Checking for legal bounds of the move
86 if( (leftBound < 1) || (leftBound > rightBound) || rowNumber <1
87 || rowNumber>NUM_OF_ROWS || (rightBound>gameBoard[rowNumber-1].length) )
88 return -1;
89
90 // Checking for moves covering already-marked sticks
91 for(int j=leftBound-1;j<rightBound;j++){
92 if(gameBoard[rowNumber-1][j] == 1)
93 gameBoard[rowNumber-1][j] = 0;
94 else{ //Illegal move,revert back and return error.
95 for(int back=j-1;back>=leftBound-1;back--){
96 gameBoard[rowNumber-1][back] = 1;
97 }
98 return 0;
99 }
100 }
101
102 numberOfMarkedSticks += (rightBound-leftBound+1);
103
104 return 1;
105 }
106
107
108 /**
109 * Returns the number of rows in the board
110 */
111 public int getNumberOfRows(){
112 return NUM_OF_ROWS;
113 }
114
115 /**
116 * Returns the total number of sticks (marked and unmarked) in the row. A legal input to this method is
117 * an integer number between 1 and the output of "getNumberOfRows()". Returns -1 in case the input is
118 * invalid.
119 */
120 public int getRowLength(int row){
121
122 if(row<1 || row>NUM_OF_ROWS)
123 return -1;
124
125 return gameBoard[row-1].length;
126 }
127
7
128 /**
129 * Given an index to the stick position (row and number in row - counting from the left side),
130 * this method returns true if the stick is unmarked, and false if it is marked, or if the input is out
131 * of bounds.
132 */
133 public boolean isStickUnmarked(int row,int stickNum){
134
135 if(row<1 || row>NUM_OF_ROWS || stickNum<1 || stickNum>gameBoard[row-1].length)
136 return false;
137
138 return(gameBoard[row-1][stickNum-1]==1);
139
140 }
141
142 /**
143 * @return The number of marked sticks on the board.
144 */
145 public int getNumberOfMarkedSticks() {
146 // This is a classic "getter" method. We use getter methods when we want other classes to be
147 // able to read the value of some private field in some class, without being able to change it.
148 // If we simply made the field (in this case 'numMarked') public, other classes could read AND
149 // change it, so this is a better solution.
150 return numberOfMarkedSticks;
151 }
152
153 /**
154 * @return the number of unmarked sticks on the board.
155 */
156 public int getNumberOfUnmarkedSticks(){
157 return NUM_OF_ELEMENTS - numberOfMarkedSticks;
158 }
159
160
161 }
8
