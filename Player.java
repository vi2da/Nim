1 import java.util.Random;
2 import java.util.Scanner;
3
4 /**
5 * The Player class represents a player in the Nim game, producing Moves as a response to a Board state.
6 * Each player is initialized with a type, either human or one of several computer strategies, which defines
7 * the move he produces when given a board in some state. The heuristic strategy of the player is already
8 * implemented. You are required to implement the rest of the player types according to the exercise
9 * description.
10 * @author OOP course staff
11 */
12 public class Player {
13
14 //Constants that represent the different players.
15 /** The constant integer representing the Random player type. */
16 public static final int RANDOM = 1;
17 /** The constant integer representing the Heuristic player type. */
18 public static final int HEURISTIC = 2;
19 /** The constant integer representing the Smart player type. */
20 public static final int SMART = 3;
21 /** The constant integer representing the Human player type. */
22 public static final int HUMAN = 4;
23
24 //Used by produceHeuristicMove() for binary representation of board rows.
25 private static final int BINARY_LENGTH = 3;
26
27 private final int playerType;
28 private final int playerId;
29 private Scanner scanner;
30
31 /**
32 * Initializes a new player of the given type and the given id, and an initialized scanner.
33 * @param type The type of the player to create.
34 * @param id The id of the player (either 1 or 2).
35 * @param inputScanner The Scanner object through which to get user input
36 * for the Human player type.
37 */
38 public Player(int type, int id, Scanner inputScanner){
39 // Check for legal player type (we will see better ways to do this in the future).
40 if ((type != RANDOM && type != HEURISTIC) && (type != SMART && type != HUMAN)){
41 System.out.println("Received an unknown player type as a parameter"
42 + " in Player constructor. Terminating.");
43 System.exit(-1);
44 }
45 playerType = type;
46 playerId = id;
47 scanner = inputScanner;
48 }
49
50 /**
51 * @return an integer matching the player type.
52 */
53 public int getPlayerType(){
54 return playerType;
55 }
56
57 /**
58 * @return the players id number.
59 */
13
60 public int getPlayerId(){
61 return playerId;
62 }
63
64
65 /**
66 * @return a String matching the player type.
67 */
68 public String getTypeName(){
69 switch(playerType){
70
71 case RANDOM:
72 return "Random";
73
74 case SMART:
75 return "Smart";
76
77 case HEURISTIC:
78 return "Heuristic";
79
80 case HUMAN:
81 return "Human";
82 }
83 //Because we checked for legal player types in the
84 //constructor, this line shouldn't be reachable.
85 return "UnkownPlayerType";
86 }
87
88 /**
89 * This method encapsulates all the reasoning of the player about the game. The player is given the
90 * board object, and is required to return his next move on the board. The choice of the move depends
91 * on the type of the player: a human player chooses his move manually; the random player should
92 * return some random move; the Smart player can represent any reasonable strategy; the Heuristic
93 * player uses a strong heuristic to choose a move.
94 * @param board - a Board object representing the current state of the game.
95 * @return a Move object representing the move that the current player will play according to his
96 * strategy.
97 */
98 public Move produceMove(Board board){
99
100 switch(playerType){
101
102 case RANDOM:
103 return produceRandomMove(board);
104
105 case SMART:
106 return produceSmartMove(board);
107
108 case HEURISTIC:
109 return produceHeuristicMove(board);
110
111 case HUMAN:
112 return produceHumanMove(board);
113
114 //Because we checked for legal player types in the
115 //constructor, this line shouldn't be reachable.
116 default:
117 return null;
118 }
119 }
120
121 /** Check if we have at least one stick unmarked in the current row
122 * @param board
123 * @param row - the number of the row to check
124 * @return - true if there is at least one stick in the row, else returns false
125 * Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
126 */
127 private boolean legalRow(Board board, int row){
14
128 for(int stickNum = 1; stickNum <= board.getRowLength(row); stickNum++){
129 if(board.isStickUnmarked(row,stickNum)){
130 return true;
131 }
132 }
133 return false;
134 }
135
136 /** Produces a random move
137 * @param board - the board
138 * @return - Move object: The move decided by the random function
139 */
140 private Move produceRandomMove(Board board){
141 // Check that the board is not null - should never happen
142 if(board == null){
143 return null;
144 }
145 Random random = new Random();
146
147 int row;
148 do{
149 // Calculate the row randomly
150 row = random.nextInt(board.getNumberOfRows()) + 1;
151 // if the row is not legal, re-calculate it
152 }while(!legalRow(board, row));
153
154 int left;
155 do{
156 // Calculate the position in the given row randomly
157 left = random.nextInt(board.getRowLength(row)) + 1;
158 // if the position is not legal, re-calculate it
159 }while(!board.isStickUnmarked(row, left));
160
161 int right = left;
162 // Check if the next stick is unmarked (then can be taken) AND decide randomly to take it or not
163 while(board.isStickUnmarked(row, right + 1) && random.nextBoolean()){
164 right += 1;
165 }
166 return new Move(row, left, right);
167 }
168
169
170 /** Produce some smart strategy to produce a move
171 * In that function, a sequence indicates a group of more than 1 stick continuous (not separated by 0)
172 * The goal is to reach a state where there is only one sequence on the board, the other sticks are
173 * "lonely" (i.e. they are alone), or no other sticks at all.
174 * The move should leave a odd number of lonely sticks to fail the other player.
175 * @param board - the board
176 * @return - Move object: The move decided by the smart function
177 */
178 private Move produceSmartMove(Board board){
179 // Check that the board is not null - should never happen
180 if(board == null){
181 return null;
182 }
183
184 // Initialize the move
185 Move move;
186
187 // If there is only one sequence in the board (sequence of sticks greater than 1)
188 if(numSequence(board) == 1){
189 // Returns the data that identifies the only sequence on the board, as an array {row,left,right}
190 int[] seq = theLonelySequence(board);
191 // Perform the wanted move: if the number of lonely sticks is even then keep one stick in the
192 // "one sequence" to get an odd number of lonely sticks. Else remove all the sticks from the
193 // "one sequence".
194 move = new Move(seq[ROW_INDX], seq[LEFT_INDX] + (numLonely(board) + 1) % 2, seq[RIGHT_INDX]);
195 }else{
15
196 // In case there is more than one sequence in the board, perform a random move until we reach
197 // the case of only one sequence in the board to start the smart moves
198 move = produceRandomMove(board);
199 }
200 return move;
201 }
202
203 /** Check if the given stick is lonely which means not near another stick.
204 * @param board - The Board
205 * @param row - The row
206 * @param numStick - the stick to check in the row
207 * @return - True if the stick is lonely (not near another stick) - Else returns false
208 * Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
209 */
210 private boolean isLonely(Board board, int row, int numStick){
211 // if the stick is still in the board
212 if(board.isStickUnmarked(row, numStick)){
213 // case that the stick is the first in row
214 if(numStick == 1){
215 // Returns True if the stick on its right is already marked (is absent)
216 return (!board.isStickUnmarked(row, numStick + 1));
217 }
218 // case that the stick is the last in row
219 if(numStick == board.getRowLength(row)){
220 // Returns True if the stick on its left is already marked (is absent)
221 return (!board.isStickUnmarked(row, numStick - 1));
222 }
223 // Case stick not on the bound
224 if(!board.isStickUnmarked(row, numStick - 1) && !board.isStickUnmarked(row, numStick + 1)){
225 // Returns True if the stick on its right AND on its left are both already marked (absent)
226 return true;
227 }
228 }
229 return false;
230 }
231
232 /** Calculate how many lonely sticks (single sticks) are on the board
233 * @param board
234 * @return - The number of lonely (single) sticks on the board
235 * Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
236 */
237 private int numLonely(Board board){
238 int numOfLonely = 0;
239 for(int row = 1; row <= board.getNumberOfRows(); row++){
240 for(int stick = 1; stick <= board.getRowLength(row); stick++){
241 // Check if the current stick in the current row is lonely (single)
242 if(isLonely(board, row, stick)){
243 // If yes, increase the count
244 numOfLonely += 1;
245 }
246 }
247 }
248 return numOfLonely;
249 }
250
251 /** Calculate the number of sequences in the current row
252 * @param board
253 * @param row - The row number to check
254 * @return - The number of sequences found in the row
255 * Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
256 */
257 private int NumSequenceInRow(Board board, int row){
258 int numSeq = 0;
259 boolean flag = true;
260 for(int stick = 1; stick <= board.getRowLength(row); stick++){
261 // Flag true indicates if we did not started a sequence and must continue to search
262 if(flag){
263 // If the current stick is not marked (i.e. is 1) and is not lonely (not single) then we
16
264 // started a sequence
265 if( (board.isStickUnmarked(row, stick)) && !(isLonely(board, row, stick)) ){
266 flag = false;
267 // Increment the number of sequences
268 numSeq += 1;
269 }
270 // the else indicate we started a sequence and we search for its end
271 }else{
272 // If we reach a 0 (marked) then we reach the end of the sequence
273 if( !(board.isStickUnmarked(row, stick))){
274 flag = true;
275 }
276 }
277 }
278 // Number of sequences found in the row
279 return numSeq;
280 }
281 /** Calculate the number of sequences in the board (for all the rows)
282 * @param board
283 * @return - The number of sequences found in the board
284 * Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
285 */
286 private int numSequence(Board board){
287 int numSeq = 0;
288 // Loop over the rows of the board
289 for(int row = 1; row <= board.getNumberOfRows(); row++){
290 numSeq += NumSequenceInRow(board, row);
291 }
292 return numSeq;
293 }
294
295
296 private static final int ROW_INDX = 0;
297 private static final int LEFT_INDX = 1;
298 private static final int RIGHT_INDX = 2;
299
300 /** The function is called when there is only one sequence left on the board
301 * Produce an array {Row, Left stick, Right stick} representing the sequence
302 * @param board
303 * @return the array of the sequence
304 * Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
305 */
306 private int[] theLonelySequence(Board board){
307 int[] seq = {0,0,0};
308 for(int row = 1; row <= board.getNumberOfRows(); row++){
309 // When we reach the row containing that last sequence
310 if(NumSequenceInRow(board, row) == 1){
311 seq[ROW_INDX] = row;
312 seq[LEFT_INDX] = 1;
313 seq[RIGHT_INDX] = board.getRowLength(row);
314 boolean flag = true;
315 // Flag true indicates we did not identify the sequence completely
316 while(flag){
317 // If the stick is 0 or is lonely (single) then this is not the start oif the sequence,
318 // then move one place right
319 if( !(board.isStickUnmarked(row, seq[LEFT_INDX])) ||
320 (isLonely(board, row, seq[LEFT_INDX])) ){
321 seq[LEFT_INDX] += 1;
322 // In case we found the first stick of the sequence, look for the end of the sequence
323 // the same way we did above but this time we go from right to left
324 }else if( !(board.isStickUnmarked(row, seq[RIGHT_INDX])) ||
325 (isLonely(board, row, seq[RIGHT_INDX])) ){
326 seq[RIGHT_INDX] -= 1;
327 // We found the sequence, stop searching
328 }else{
329 flag = false;
330 }
331 }
17
332 }
333 }
334 return seq;
335 }
336
337 /** Print output */
338 private void println(String str){
339 System.out.println(str);
340 }
341
342 /** Interact with the user to produce his move.
343 * @param board
344 * @return - Move selected by the user
345 */
346
347 private Move produceHumanMove(Board board){
348 if(board == null){
349 return null;
350 }
351
352 boolean legalMove = false;
353 int firstInput;
354 do{
355 // Instructions to the user
356 println("Press 1 to display the board. Press 2 to make a move: ");
357 // Get input
358 firstInput = scanner.nextInt();
359 // If input invalid display error message and go back to input
360 if((firstInput != 1) && (firstInput != 2)){
361 println("Unknown input.");
362 }else if(firstInput == 1){
363 // Display the board
364 println(board.toString());
365 }
366 else{
367 legalMove = true;
368 }
369 // While the user did not select option 2
370 }while(!legalMove);
371
372
373 // The user propose a move (row and group of sticks)
374 println("Enter the row number:");
375 int row = scanner.nextInt();
376 println("Enter the index of the leftmost stick:");
377 int left = scanner.nextInt();
378 println("Enter the index of the rightmost stick:");
379 int right = scanner.nextInt();
380
381 return (new Move(row, left, right));
382 }
383
384 /*
385 * Uses a winning heuristic for the Nim game to produce a move.
386 */
387 private Move produceHeuristicMove(Board board){
388
389 if(board == null){
390 return null;
391 }
392
393 int numRows = board.getNumberOfRows();
394 int[][] bins = new int[numRows][BINARY_LENGTH];
395 int[] binarySum = new int[BINARY_LENGTH];
396 int bitIndex,higherThenOne=0,totalOnes=0,lastRow=0,lastLeft=0,lastSize=0,lastOneRow=0,lastOneLeft=0;
397
398 for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
399 binarySum[bitIndex] = 0;
18
400 }
401
402 for(int k=0;k<numRows;k++){
403
404 int curRowLength = board.getRowLength(k+1);
405 int i = 0;
406 int numOnes = 0;
407
408 for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
409 bins[k][bitIndex] = 0;
410 }
411
412 do {
413 if(i<curRowLength && board.isStickUnmarked(k+1,i+1) ){
414 numOnes++;
415 } else {
416
417 if(numOnes>0){
418
419 String curNum = Integer.toBinaryString(numOnes);
420 while(curNum.length()<BINARY_LENGTH){
421 curNum = "0" + curNum;
422 }
423 for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
424 bins[k][bitIndex] += curNum.charAt(bitIndex)-'0'; //Convert from char to int
425 }
426
427 if(numOnes>1){
428 higherThenOne++;
429 lastRow = k +1;
430 lastLeft = i - numOnes + 1;
431 lastSize = numOnes;
432 } else {
433 totalOnes++;
434 }
435 lastOneRow = k+1;
436 lastOneLeft = i;
437
438 numOnes = 0;
439 }
440 }
441 i++;
442 }while(i<=curRowLength);
443
444 for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
445 binarySum[bitIndex] = (binarySum[bitIndex]+bins[k][bitIndex])%2;
446 }
447 }
448
449
450 //We only have single sticks
451 if(higherThenOne==0){
452 return new Move(lastOneRow,lastOneLeft,lastOneLeft);
453 }
454
455 //We are at a finishing state
456 if(higherThenOne<=1){
457
458 if(totalOnes == 0){
459 return new Move(lastRow,lastLeft,lastLeft+(lastSize-1) - 1);
460 } else {
461 return new Move(lastRow,lastLeft,lastLeft+(lastSize-1)-(1-totalOnes%2));
462 }
463
464 }
465
466 for(bitIndex = 0;bitIndex<BINARY_LENGTH-1;bitIndex++){
467
19
468 if(binarySum[bitIndex]>0){
469
470 int finalSum = 0,eraseRow = 0,eraseSize = 0,numRemove = 0;
471 for(int k=0;k<numRows;k++){
472
473 if(bins[k][bitIndex]>0){
474 eraseRow = k+1;
475 eraseSize = (int)Math.pow(2,BINARY_LENGTH-bitIndex-1);
476
477 for(int b2 = bitIndex+1;b2<BINARY_LENGTH;b2++){
478
479 if(binarySum[b2]>0){
480
481 if(bins[k][b2]==0){
482 finalSum = finalSum + (int)Math.pow(2,BINARY_LENGTH-b2-1);
483 } else {
484 finalSum = finalSum - (int)Math.pow(2,BINARY_LENGTH-b2-1);
485 }
486
487 }
488
489 }
490 break;
491 }
492 }
493
494 numRemove = eraseSize - finalSum;
495
496 //Now we find that part and remove from it the required piece
497 int numOnes=0,i=0;
498 //while(numOnes<eraseSize){
499 while(numOnes<numRemove && i<board.getRowLength(eraseRow)){
500
501 if(board.isStickUnmarked(eraseRow,i+1)){
502 numOnes++;
503 } else {
504 numOnes=0;
505 }
506 i++;
507
508 }
509
510 //This is the case that we cannot perform a smart move because there are marked
511 //Sticks in the middle
512 if(numOnes == numRemove){
513 return new Move(eraseRow,i-numOnes+1,i-numOnes+numRemove);
514 } else {
515 return new Move(lastRow,lastLeft,lastLeft);
516 }
517
518 }
519 }
520
521 //If we reached here, and the board is not symmetric, then we only need to erase a single stick
522 if(binarySum[BINARY_LENGTH-1]>0){
523 return new Move(lastOneRow,lastOneLeft,lastOneLeft);
524 }
525
526 //If we reached here, it means that the board is already symmetric,
527 //and then we simply mark one stick from the last sequence we saw:
528 return new Move(lastRow,lastLeft,lastLeft);
529 }
530
531
532 }
20
