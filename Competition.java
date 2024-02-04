1 import java.util.Scanner;
2
3 /**
4 * The Competition class represents a Nim competition between two players, consisting of a given number of
5 * rounds.
6 * It also keeps track of the number of victories of each player.
7 */
8 public class Competition {
9
10 /** Number of players in a competition */
11 private static final int NUM_OF_PLAYERS = 2;
12 /** Index of the first player in the lists */
13 private static final int FIRST = 0;
14 /** Index of the second player in the lists */
15 private static final int SECOND = 1;
16 /** Difference between the real id of a player and its index in the lists */
17 private static final int DIFF = 1;
18 /** Error return code value */
19 private static final int ERROR_CODE = -1;
20 /** List of players */
21 private Player [] players;
22 /** List of scores */
23 private int [] scores;
24 /** Indicates if displaying message to a user (verbose mode) */
25 private boolean displayMessage;
26 /** Message for illegal move */
27 private static final String ILLEGALMOVE = "Invalid move. Enter another:";
28
29 /** Constructor of the Competition class */
30 public Competition(Player player1, Player player2, boolean displayMessage){
31 this.players = new Player[]{player1,player2};
32 this.scores = new int[]{0,0};
33 this.displayMessage = displayMessage;
34 }
35
36 /** Get score for a player (identified by its position) and returns it */
37 public int getPlayerScore(int playerPosition){
38 // If position does not fit any player return error
39 if((playerPosition != 1) && (playerPosition != 2)){
40 return ERROR_CODE;
41 }else{
42 // Adjust the index (decrease by -1) based on the player position
43 return this.scores[playerPosition - DIFF];
44 }
45 }
46
47 /** Run the game for the given number of rounds */
48 public void playMultipleRounds(int numberOfRounds){
49 // Display the entry game message
50 System.out.println("Starting a Nim competition of " + numberOfRounds + " rounds between a " +
51 this.players[FIRST].getTypeName() +" player and a "+
52 this.players[SECOND].getTypeName() +" player.");
53 for(int i = 0; i < numberOfRounds; i++){
54 // At the beginning of each round, display the Welcome message
55 checkPrint("Welcome to the sticks game!");
56 // Play the round
57 playSingleRound();
58 }
59 // At the end of all the rounds (end of the competition) display the final scores.
9
60 System.out.print("The results are " + this.scores[FIRST] + ":" + this.scores[SECOND] );
61 }
62
63 /** Play a round */
64 private void playSingleRound(){
65 // Define a new board clean (and not null)
66 Board board = new Board();
67 // Start with the first player
68 int indxCurrPlayer = FIRST;
69 /** While the board still contains some stick */
70 while(board.getNumberOfUnmarkedSticks() > 0){
71 /** Play */
72 playOneTurn(indxCurrPlayer, board);
73 // Next player
74 indxCurrPlayer = (indxCurrPlayer + 1) % NUM_OF_PLAYERS;
75 }
76 // Winner message (if verbose mode)
77 checkPrint("Player "+ (indxCurrPlayer + DIFF) +" won!");
78 // Update the score of the winner of the round
79 this.scores[indxCurrPlayer] += 1;
80 }
81
82 /** Play one Turn
83 * @param indxPlayer index of the player who plays that turn, Board
84 * Always get a legal Board then no need to check the case Board is Null as explained in Readme Note 1
85 */
86 private void playOneTurn(int indxPlayer, Board board){
87 // No need to check that the board is not Null as it is handled in PlaySingleRound
88 // Your turn message (if verbose mode)
89 checkPrint("Player "+ (indxPlayer + DIFF) +", it is now your turn!");
90 boolean legalMove = false;
91 while(!legalMove){
92 Move currMove = this.players[indxPlayer].produceMove(board);
93 // markStickSequence will return 1 if it was a legal move
94 if(board.markStickSequence(currMove) == 1){
95 legalMove = true;
96 // Message of the change done by the move (if verbose mode)
97 checkPrint("Player " + (indxPlayer + DIFF) + " made the move: " + currMove.toString() );
98 }
99 // If not a legal move, replay the turn (and display message if verbose mode)
100 else{
101 checkPrint(ILLEGALMOVE);
102 }
103 }
104 }
105
106 /** Displays a message if this is in verbose mode
107 * @param str - String to display
108 */
109 private void checkPrint(String str){
110 // If this is in verbose mode
111 if(this.displayMessage){
112 System.out.println(str);
113 }
114 }
115
116
117 /*
118 * Returns the integer representing the type of the player; returns -1 on bad
119 * input.
120 */
121 private static int parsePlayerType(String[] args,int index){
122 try{
123 return Integer.parseInt(args[index]);
124 } catch (Exception E){
125 return -1;
126 }
127 }
10
128
129 /*
130 * Returns the integer representing the number of required game; returns -1 on bad input.
131 */
132 private static int parseNumberOfGames(String[] args){
133 try{
134 return Integer.parseInt(args[2]);
135 } catch (Exception E){
136 return -1;
137 }
138 }
139
140 /**
141 * The method runs a Nim competition between two players according to the three user-specified arguments.
142 * (1) The type of the first player, which is a positive integer between 1 and 4: 1 for a Random computer
143 * player, 2 for a Heuristic computer player, 3 for a Smart computer player and 4 for a human player.
144 * (2) The type of the second player, which is a positive integer between 1 and 4.
145 * (3) The number of rounds to be played in the competition.
146 * @param args an array of string representations of the three input arguments, as detailed above.
147 */
148 public static void main(String[] args) {
149
150 int p1Type = parsePlayerType(args,0);
151 int p2Type = parsePlayerType(args,1);
152 int numGames = parseNumberOfGames(args);
153
154 Scanner scanner = new Scanner(System.in);
155 // Initialize the players
156 Player player1 = new Player(p1Type, 1, scanner);
157 Player player2 = new Player(p2Type, 2, scanner);
158 boolean displayMessage = false;
159 //In case at least one player is Human, enter verbose mode
160 if((player1.getPlayerType() == 4) || (player2.getPlayerType() == 4)){
161 displayMessage = true;
162 }
163 // Define a new competition
164 Competition compet = new Competition(player1 , player2, displayMessage);
165 // Play the number of rounds required
166 compet.playMultipleRounds(numGames);
167 }
168 }
