import java.util.Scanner;
/**
* The Competition class represents a Nim competition between two players, consisting of a given number of
* rounds.
* It also keeps track of the number of victories of each player.
*/
public class Competition {
/** Number of players in a competition */
private static final int NUM_OF_PLAYERS = 2;
/** Index of the first player in the lists */
private static final int FIRST = 0;
/** Index of the second player in the lists */
private static final int SECOND = 1;
/** Difference between the real id of a player and its index in the lists */
private static final int DIFF = 1;
/** Error return code value */
private static final int ERROR_CODE = -1;
/** List of players */
private Player [] players;
/** List of scores */
private int [] scores;
/** Indicates if displaying message to a user (verbose mode) */
private boolean displayMessage;
/** Message for illegal move */
private static final String ILLEGALMOVE = "Invalid move. Enter another:";
/** Constructor of the Competition class */
public Competition(Player player1, Player player2, boolean displayMessage){
this.players = new Player[]{player1,player2};
this.scores = new int[]{0,0};
this.displayMessage = displayMessage;
}
/** Get score for a player (identified by its position) and returns it */
public int getPlayerScore(int playerPosition){
// If position does not fit any player return error
if((playerPosition != 1) && (playerPosition != 2)){
return ERROR_CODE;
}else{
// Adjust the index (decrease by -1) based on the player position
return this.scores[playerPosition - DIFF];
}
}
/** Run the game for the given number of rounds */
public void playMultipleRounds(int numberOfRounds){
// Display the entry game message
System.out.println("Starting a Nim competition of " + numberOfRounds + " rounds between a " +
this.players[FIRST].getTypeName() +" player and a "+
this.players[SECOND].getTypeName() +" player.");
for(int i = 0; i < numberOfRounds; i++){
// At the beginning of each round, display the Welcome message
checkPrint("Welcome to the sticks game!");
// Play the round
playSingleRound();
}
// At the end of all the rounds (end of the competition) display the final scores.
System.out.print("The results are " + this.scores[FIRST] + ":" + this.scores[SECOND] );
}
/** Play a round */
private void playSingleRound(){
// Define a new board clean (and not null)
Board board = new Board();
// Start with the first player
int indxCurrPlayer = FIRST;
/** While the board still contains some stick */
while(board.getNumberOfUnmarkedSticks() > 0){
/** Play */
playOneTurn(indxCurrPlayer, board);
// Next player
indxCurrPlayer = (indxCurrPlayer + 1) % NUM_OF_PLAYERS;
}
// Winner message (if verbose mode)
checkPrint("Player "+ (indxCurrPlayer + DIFF) +" won!");
// Update the score of the winner of the round
this.scores[indxCurrPlayer] += 1;
}
/** Play one Turn
* @param indxPlayer index of the player who plays that turn, Board
* Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 1
*/
private void playOneTurn(int indxPlayer, Board board){
// No need to check that the board is not Null as it is handled in PlaySingleRound
// Your turn message (if verbose mode)
checkPrint("Player "+ (indxPlayer + DIFF) +", it is now your turn!");
boolean legalMove = false;
while(!legalMove){
Move currMove = this.players[indxPlayer].produceMove(board);
// markStickSequence will return 1 if it was a legal move
if(board.markStickSequence(currMove) == 1){
legalMove = true;
// Message of the change done by the move (if verbose mode)
checkPrint("Player " + (indxPlayer + DIFF) + " made the move: " + currMove.toString() );
}
// If not a legal move, replay the turn (and display message if verbose mode)
else{
checkPrint(ILLEGALMOVE);
}
}
}
/** Displays a message if this is in verbose mode
* @param str - String to display
*/
private void checkPrint(String str){
// If this is in verbose mode
if(this.displayMessage){
System.out.println(str);
}
}
/*
* Returns the integer representing the type of the player; returns -1 on bad
* input.
*/
private static int parsePlayerType(String[] args,int index){
try{
return Integer.parseInt(args[index]);
} catch (Exception E){
return -1;
}
}
/*
* Returns the integer representing the number of required game; returns -1 on bad input.
*/
private static int parseNumberOfGames(String[] args){
try{
return Integer.parseInt(args[2]);
} catch (Exception E){
return -1;
}
}
/**
* The method runs a Nim competition between two players according to the three user-specified arguments.
* (1) The type of the first player, which is a positive integer between 1 and 4: 1 for a Random computer
* player, 2 for a Heuristic computer player, 3 for a Smart computer player and 4 for a human player.
* (2) The type of the second player, which is a positive integer between 1 and 4.
* (3) The number of rounds to be played in the competition.
* @param args an array of string representations of the three input arguments, as detailed above.
*/
public static void main(String[] args) {
int p1Type = parsePlayerType(args,0);
int p2Type = parsePlayerType(args,1);
int numGames = parseNumberOfGames(args);
Scanner scanner = new Scanner(System.in);
// Initialize the players
Player player1 = new Player(p1Type, 1, scanner);
Player player2 = new Player(p2Type, 2, scanner);
boolean displayMessage = false;
//In case at least one player is Human, enter verbose mode
if((player1.getPlayerType() == 4) || (player2.getPlayerType() == 4)){
displayMessage = true;
}
// Define a new competition
Competition compet = new Competition(player1 , player2, displayMessage);
// Play the number of rounds required
compet.playMultipleRounds(numGames);
}
}
