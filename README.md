304249386
david ohayon
ex1:
Description of the smart algorithm:
The goal is to leace the other player with an odd number of lonely sticks.
A lonely stick is a single stick (surrounded by marked stiks i.e. 0 or near an edge)
Like this, the other player will have no choice but producing a even number of lonely sticks
and the next move of the smart player will produce again a odd number of lonely sticks.
At the end, the other player will remain with the last lonely stick.
To do this, the smart player will use the random strategy until it reaches the state where
there is only one sequence left. A sequence is a groups of at least 2 contiguous sticks, all
the other sticks in the game being lonely sticks as described above (or no other sticks at all).
For example
if there is: 1 sequence + 1 lonely stick + 1 lonely stick (or any even number of lonely sticks)
the smart player will take all the sticks of the sequence EXCEPT ONE, then the other player
will get: 1 lonely stick + 1 lonely stick + 1 lonely stick
and will leave: 1 lonely stick + 1 lonely stick
then the smart player will take 1 lonely stick
and leave the last stick to the other player who looses!
If there is: 1 sequence + 1 lonely stick (or any odd number of lonely sticks)
the smart player will take all the sticks of the sequence, then the other player
will get: 1 lonely stick and loose!
Note 1: the methods don't check if the object Board is null in Competition Class
because we can see in (1) that playOneTurn is private and is called only by playSingleRound
which produces the Board and ensure it is not Null before calling.
1. private void playOneTurn(int indxPlayer, Board board)
(1) playSingleRound-----> playOneTurn
Note 2:
the other methods that dont check if the object Board is null in Player Class
are below, because (2),(3) call them only after checkin that the board is not null.
1. private boolean legalRow(Board board, int row)
2. private boolean isLonely(Board board, int row, int numStick)
3. private int numLonely(Board board)
4. private int NumSequenceInRow(Board board, int row)
5. private int numSequence(Board board)
6. private int[] theLonelySequence(Board board)
The function produceSmartMove calls the other functions, and before doing this it ensure the
Board is not Null
(2) produceSmartMove-----> numLonely-----> isLonely
|
|----> numSequence----> NumSequenceInRow
|
|----> theLonelySequence
The function produceRandomMove calls the other functions, and before doing this it ensure the
Board is not Null
(3) produceRandomMove-----> legalRow
