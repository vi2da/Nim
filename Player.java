import java.util.Random;
import java.util.Scanner;
/**
* The Player class represents a player in the Nim game, producing Moves as a response to a Board state.
* Each player is initialized with a type, either human or one of several computer strategies, which defines
* the move he produces when given a board in some state. The heuristic strategy of the player is already
* implemented. You are required to implement the rest of the player types according to the exercise
* description.
* @author OOP course staff
*/
public class Player {
//Constants that represent the different players.
/** The constant integer representing the Random player type. */
public static final int RANDOM = 1;
/** The constant integer representing the Heuristic player type. */
public static final int HEURISTIC = 2;
/** The constant integer representing the Smart player type. */
public static final int SMART = 3;
/** The constant integer representing the Human player type. */
public static final int HUMAN = 4;
//Used by produceHeuristicMove() for binary representation of board rows.
private static final int BINARY_LENGTH = 3;
private final int playerType;
private final int playerId;
private Scanner scanner;
/**
* Initializes a new player of the given type and the given id, and an initialized scanner.
* @param type The type of the player to create.
* @param id The id of the player (either 1 or 2).
* @param inputScanner The Scanner object through which to get user input
* for the Human player type.
*/
public Player(int type, int id, Scanner inputScanner){
// Check for legal player type (we will see better ways to do this in the future).
if ((type != RANDOM && type != HEURISTIC) && (type != SMART && type != HUMAN)){
System.out.println("Received an unknown player type as a parameter"
+ " in Player constructor. Terminating.");
System.exit(-1);
}
playerType = type;
playerId = id;
scanner = inputScanner;
}
/**
* @return an integer matching the player type.
*/
public int getPlayerType(){
return playerType;
}
/**
* @return the players id number.
*/
public int getPlayerId(){
return playerId;
}
/**
* @return a String matching the player type.
*/
public String getTypeName(){
switch(playerType){
case RANDOM:
return "Random";
case SMART:
return "Smart";
case HEURISTIC:
return "Heuristic";
case HUMAN:
return "Human";
}
//Because we checked for legal player types in the
//constructor, this line shouldn't be reachable.
return "UnkownPlayerType";
}
/**
* This method encapsulates all the reasoning of the player about the game. The player is given the
* board object, and is required to return his next move on the board. The choice of the move depends
* on the type of the player: a human player chooses his move manually; the random player should
* return some random move; the Smart player can represent any reasonable strategy; the Heuristic
* player uses a strong heuristic to choose a move.
* @param board - a Board object representing the current state of the game.
* @return a Move object representing the move that the current player will play according to his
* strategy.
*/
public Move produceMove(Board board){
switch(playerType){
case RANDOM:
return produceRandomMove(board);
case SMART:
return produceSmartMove(board);
case HEURISTIC:
return produceHeuristicMove(board);
case HUMAN:
return produceHumanMove(board);
//Because we checked for legal player types in the
//constructor, this line shouldn't be reachable.
default:
return null;
}
}
/** Check if we have at least one stick unmarked in the current row
* @param board
* @param row - the number of the row to check
* @return - true if there is at least one stick in the row, else returns false
* Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
*/
private boolean legalRow(Board board, int row){
for(int stickNum = 1; stickNum <= board.getRowLength(row); stickNum++){
if(board.isStickUnmarked(row,stickNum)){
return true;
}
}
return false;
}
/** Produces a random move
* @param board - the board
* @return - Move object: The move decided by the random function
*/
private Move produceRandomMove(Board board){
// Check that the board is not null - should never happen
if(board == null){
return null;
}
Random random = new Random();
int row;
do{
// Calculate the row randomly
row = random.nextInt(board.getNumberOfRows()) + 1;
// if the row is not legal, re-calculate it
}while(!legalRow(board, row));
int left;
do{
// Calculate the position in the given row randomly
left = random.nextInt(board.getRowLength(row)) + 1;
// if the position is not legal, re-calculate it
}while(!board.isStickUnmarked(row, left));
int right = left;
// Check if the next stick is unmarked (then can be taken) AND decide randomly to take it or not
while(board.isStickUnmarked(row, right + 1) && random.nextBoolean()){
right += 1;
}
return new Move(row, left, right);
}
/** Produce some smart strategy to produce a move
* In that function, a sequence indicates a group of more than 1 stick continuous (not separated by 0)
* The goal is to reach a state where there is only one sequence on the board, the other sticks are
* "lonely" (i.e. they are alone), or no other sticks at all.
* The move should leave a odd number of lonely sticks to fail the other player.
* @param board - the board
* @return - Move object: The move decided by the smart function
*/
private Move produceSmartMove(Board board){
// Check that the board is not null - should never happen
if(board == null){
return null;
}
// Initialize the move
Move move;
// If there is only one sequence in the board (sequence of sticks greater than 1)
if(numSequence(board) == 1){
// Returns the data that identifies the only sequence on the board, as an array {row,left,right}
int[] seq = theLonelySequence(board);
// Perform the wanted move: if the number of lonely sticks is even then keep one stick in the
// "one sequence" to get an odd number of lonely sticks. Else remove all the sticks from the
// "one sequence".
move = new Move(seq[ROW_INDX], seq[LEFT_INDX] + (numLonely(board) + 1) % 2, seq[RIGHT_INDX]);
}else{
// In case there is more than one sequence in the board, perform a random move until we reach
// the case of only one sequence in the board to start the smart moves
move = produceRandomMove(board);
}
return move;
}
/** Check if the given stick is lonely which means not near another stick.
* @param board - The Board
* @param row - The row
* @param numStick - the stick to check in the row
* @return - True if the stick is lonely (not near another stick) - Else returns false
* Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
*/
private boolean isLonely(Board board, int row, int numStick){
// if the stick is still in the board
if(board.isStickUnmarked(row, numStick)){
// case that the stick is the first in row
if(numStick == 1){
// Returns True if the stick on its right is already marked (is absent)
return (!board.isStickUnmarked(row, numStick + 1));
}
// case that the stick is the last in row
if(numStick == board.getRowLength(row)){
// Returns True if the stick on its left is already marked (is absent)
return (!board.isStickUnmarked(row, numStick - 1));
}
// Case stick not on the bound
if(!board.isStickUnmarked(row, numStick - 1) && !board.isStickUnmarked(row, numStick + 1)){
// Returns True if the stick on its right AND on its left are both already marked (absent)
return true;
}
}
return false;
}
/** Calculate how many lonely sticks (single sticks) are on the board
* @param board
* @return - The number of lonely (single) sticks on the board
* Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
*/
private int numLonely(Board board){
int numOfLonely = 0;
for(int row = 1; row <= board.getNumberOfRows(); row++){
for(int stick = 1; stick <= board.getRowLength(row); stick++){
// Check if the current stick in the current row is lonely (single)
if(isLonely(board, row, stick)){
// If yes, increase the count
numOfLonely += 1;
}
}
}
return numOfLonely;
}
/** Calculate the number of sequences in the current row
* @param board
* @param row - The row number to check
* @return - The number of sequences found in the row
* Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
*/
private int NumSequenceInRow(Board board, int row){
int numSeq = 0;
boolean flag = true;
for(int stick = 1; stick <= board.getRowLength(row); stick++){
// Flag true indicates if we did not started a sequence and must continue to search
if(flag){
// If the current stick is not marked (i.e. is 1) and is not lonely (not single) then we
// started a sequence
if( (board.isStickUnmarked(row, stick)) && !(isLonely(board, row, stick)) ){
flag = false;
// Increment the number of sequences
numSeq += 1;
}
// the else indicate we started a sequence and we search for its end
}else{
// If we reach a 0 (marked) then we reach the end of the sequence
if( !(board.isStickUnmarked(row, stick))){
flag = true;
}
}
}
// Number of sequences found in the row
return numSeq;
}
/** Calculate the number of sequences in the board (for all the rows)
* @param board
* @return - The number of sequences found in the board
* Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
*/
private int numSequence(Board board){
int numSeq = 0;
// Loop over the rows of the board
for(int row = 1; row <= board.getNumberOfRows(); row++){
numSeq += NumSequenceInRow(board, row);
}
return numSeq;
}
private static final int ROW_INDX = 0;
private static final int LEFT_INDX = 1;
private static final int RIGHT_INDX = 2;
/** The function is called when there is only one sequence left on the board
* Produce an array {Row, Left stick, Right stick} representing the sequence
* @param board
* @return the array of the sequence
* Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 2
*/
private int[] theLonelySequence(Board board){
int[] seq = {0,0,0};
for(int row = 1; row <= board.getNumberOfRows(); row++){
// When we reach the row containing that last sequence
if(NumSequenceInRow(board, row) == 1){
seq[ROW_INDX] = row;
seq[LEFT_INDX] = 1;
seq[RIGHT_INDX] = board.getRowLength(row);
boolean flag = true;
// Flag true indicates we did not identify the sequence completely
while(flag){
// If the stick is 0 or is lonely (single) then this is not the start oif the sequence,
// then move one place right
if( !(board.isStickUnmarked(row, seq[LEFT_INDX])) ||
(isLonely(board, row, seq[LEFT_INDX])) ){
seq[LEFT_INDX] += 1;
// In case we found the first stick of the sequence, look for the end of the sequence
// the same way we did above but this time we go from right to left
}else if( !(board.isStickUnmarked(row, seq[RIGHT_INDX])) ||
(isLonely(board, row, seq[RIGHT_INDX])) ){
seq[RIGHT_INDX] -= 1;
// We found the sequence, stop searching
}else{
flag = false;
}
}
}
}
return seq;
}
/** Print output */
private void println(String str){
System.out.println(str);
}
/** Interact with the user to produce his move.
* @param board
* @return - Move selected by the user
*/
private Move produceHumanMove(Board board){
if(board == null){
return null;
}
boolean legalMove = false;
int firstInput;
do{
// Instructions to the user
println("Press 1 to display the board. Press 2 to make a move: ");
// Get input
firstInput = scanner.nextInt();
// If input invalid display error message and go back to input
if((firstInput != 1) && (firstInput != 2)){
println("Unknown input.");
}else if(firstInput == 1){
// Display the board
println(board.toString());
}
else{
legalMove = true;
}
// While the user did not select option 2
}while(!legalMove);
// The user propose a move (row and group of sticks)
println("Enter the row number:");
int row = scanner.nextInt();
println("Enter the index of the leftmost stick:");
int left = scanner.nextInt();
println("Enter the index of the rightmost stick:");
int right = scanner.nextInt();
return (new Move(row, left, right));
}
/*
* Uses a winning heuristic for the Nim game to produce a move.
*/
private Move produceHeuristicMove(Board board){
if(board == null){
return null;
}
int numRows = board.getNumberOfRows();
int[][] bins = new int[numRows][BINARY_LENGTH];
int[] binarySum = new int[BINARY_LENGTH];
int bitIndex,higherThenOne=0,totalOnes=0,lastRow=0,lastLeft=0,lastSize=0,lastOneRow=0,lastOneLeft=0;
for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
binarySum[bitIndex] = 0;
}
for(int k=0;k<numRows;k++){
int curRowLength = board.getRowLength(k+1);
int i = 0;
int numOnes = 0;
for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
bins[k][bitIndex] = 0;
}
do {
if(i<curRowLength && board.isStickUnmarked(k+1,i+1) ){
numOnes++;
} else {
if(numOnes>0){
String curNum = Integer.toBinaryString(numOnes);
while(curNum.length()<BINARY_LENGTH){
curNum = "0" + curNum;
}
for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
bins[k][bitIndex] += curNum.charAt(bitIndex)-'0'; //Convert from char to int
}
if(numOnes>1){
higherThenOne++;
lastRow = k +1;
lastLeft = i - numOnes + 1;
lastSize = numOnes;
} else {
totalOnes++;
}
lastOneRow = k+1;
lastOneLeft = i;
numOnes = 0;
}
}
i++;
}while(i<=curRowLength);
for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
binarySum[bitIndex] = (binarySum[bitIndex]+bins[k][bitIndex])%2;
}
}
//We only have single sticks
if(higherThenOne==0){
return new Move(lastOneRow,lastOneLeft,lastOneLeft);
}
//We are at a finishing state
if(higherThenOne<=1){
if(totalOnes == 0){
return new Move(lastRow,lastLeft,lastLeft+(lastSize-1) - 1);
} else {
return new Move(lastRow,lastLeft,lastLeft+(lastSize-1)-(1-totalOnes%2));
}
}
for(bitIndex = 0;bitIndex<BINARY_LENGTH-1;bitIndex++){
if(binarySum[bitIndex]>0){
int finalSum = 0,eraseRow = 0,eraseSize = 0,numRemove = 0;
for(int k=0;k<numRows;k++){
if(bins[k][bitIndex]>0){
eraseRow = k+1;
eraseSize = (int)Math.pow(2,BINARY_LENGTH-bitIndex-1);
for(int b2 = bitIndex+1;b2<BINARY_LENGTH;b2++){
if(binarySum[b2]>0){
if(bins[k][b2]==0){
finalSum = finalSum + (int)Math.pow(2,BINARY_LENGTH-b2-1);
} else {
finalSum = finalSum - (int)Math.pow(2,BINARY_LENGTH-b2-1);
}
}
}
break;
}
}
numRemove = eraseSize - finalSum;
//Now we find that part and remove from it the required piece
int numOnes=0,i=0;
//while(numOnes<eraseSize){
while(numOnes<numRemove && i<board.getRowLength(eraseRow)){
if(board.isStickUnmarked(eraseRow,i+1)){
numOnes++;
} else {
numOnes=0;
}
i++;
}
//This is the case that we cannot perform a smart move because there are marked
//Sticks in the middle
if(numOnes == numRemove){
return new Move(eraseRow,i-numOnes+1,i-numOnes+numRemove);
} else {
return new Move(lastRow,lastLeft,lastLeft);
}
}
}
//If we reached here, and the board is not symmetric, then we only need to erase a single stick
if(binarySum[BINARY_LENGTH-1]>0){
return new Move(lastOneRow,lastOneLeft,lastOneLeft);
}
//If we reached here, it means that the board is already symmetric,
//and then we simply mark one stick from the last sequence we saw:
return new Move(lastRow,lastLeft,lastLeft);
}
}
