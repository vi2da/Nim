# Nim-

vi2da
2
3
4 304249386
5 david ohayon
6
7 ex1:
8
9 Description of the smart algorithm:
10 The goal is to leace the other player with an odd number of lonely sticks.
11 A lonely stick is a single stick (surrounded by marked stiks i.e. 0 or near an edge)
12 Like this, the other player will have no choice but producing a even number of lonely sticks
13 and the next move of the smart player will produce again a odd number of lonely sticks.
14 At the end, the other player will remain with the last lonely stick.
15
16 To do this, the smart player will use the random strategy until it reaches the state where
17 there is only one sequence left. A sequence is a groups of at least 2 contiguous sticks, all
18 the other sticks in the game being lonely sticks as described above (or no other sticks at all).
19
20 For example
21 if there is: 1 sequence + 1 lonely stick + 1 lonely stick (or any even number of lonely sticks)
22 the smart player will take all the sticks of the sequence EXCEPT ONE, then the other player
23 will get: 1 lonely stick + 1 lonely stick + 1 lonely stick
24 and will leave: 1 lonely stick + 1 lonely stick
25 then the smart player will take 1 lonely stick
26 and leave the last stick to the other player who looses!
27
28 If there is: 1 sequence + 1 lonely stick (or any odd number of lonely sticks)
29 the smart player will take all the sticks of the sequence, then the other player
30 will get: 1 lonely stick and loose!
31
32
33 Note 1: the methods don't check if the object Board is null in Competition Class
34 because we can see in (1) that playOneTurn is private and is called only by playSingleRound
35 which produces the Board and ensure it is not Null before calling.
36
37 1. private void playOneTurn(int indxPlayer, Board board)
38
39 (1) playSingleRound-----> playOneTurn
40
41 Note 2:
42 the other methods that dont check if the object Board is null in Player Class
43 are below, because (2),(3) call them only after checkin that the board is not null.
44
45 1. private boolean legalRow(Board board, int row)
46 2. private boolean isLonely(Board board, int row, int numStick)
47 3. private int numLonely(Board board)
48 4. private int NumSequenceInRow(Board board, int row)
49 5. private int numSequence(Board board)
50 6. private int[] theLonelySequence(Board board)
51
52 The function produceSmartMove calls the other functions, and before doing this it ensure the
53 Board is not Null
54
55 (2) produceSmartMove-----> numLonely-----> isLonely
56 |
57 |----> numSequence----> NumSequenceInRow
58 |
59 |----> theLonelySequence
4
60
61 The function produceRandomMove calls the other functions, and before doing this it ensure the
62 Board is not Null
63
64 (3) produceRandomMove-----> legalRow
5
