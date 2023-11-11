import java.util.Scanner;

public class Battleship {
    public static void main(String[] args){

        //10 x 10 Array of Ship objects for BattleShip game
        Ship[][] ships = new Ship[10][10];

        //create the 5 Ships for Battleship game
        Ship Carrier = new Ship("Carrier", 5);
        Ship Battleship = new Ship("Battleship", 4);
        Ship Cruiser = new Ship("Cruiser", 3);
        Ship Submarine = new Ship("Submarine", 3);
        Ship Destroyer = new Ship("Destroyer", 2);

        //randomly place the 5 Ships
        placeShip(ships, Carrier);
        placeShip(ships, Battleship);
        placeShip(ships, Cruiser);
        placeShip(ships, Submarine);
        placeShip(ships, Destroyer);

        //start the game
        startGame(ships, Carrier, Battleship, Cruiser, Submarine, Destroyer);
    }

    //randomly place ship on the ships array
    public static void placeShip(Ship[][] ships, Ship ship){
        //initialized to false intentionally for use in the while loop, such that when a valid ship placement is found, it will be changed to true to exit the while loop
        boolean validLocation = false;

        //runs the ship location randomization algorithm until a valid ship placement is found
        while (!validLocation){

            //Generate random orientation (later conditionals assume less than 0.5 is Vertical and greater than or equal to 0.5 is Horizontal)
            double orientation = Math.random();

            //Generate random row index location
            int randomRow = (int) (Math.random() * 10);
            if (orientation < 0.5){
                //check if ship would go off the board if placed here based on random orientation and generate a new random index if needed
                while ((randomRow + ship.getSize()) > 9){
                    randomRow = (int) (Math.random() * 10);
                }
            }
            
            //Generate random column index location
            int randomColumn = (int) (Math.random() * 10);
            if (orientation >= 0.5){
                //check if ship would go off the board if placed here based on random orientation and generate a new random index if needed
                while ((randomColumn + ship.getSize()) > 9){
                    randomColumn = (int) (Math.random() * 10);
                }
            }
            
            //counter to check if there are any conflicts with potential ship placement
            int shipSizeCheck = 0;

            //check potential ship placement for conflicts with other previously placed ships
            for (int i = 0; i < ship.getSize(); i++){
                //check potential ship placement if random orientation is Vertical
                if(orientation < 0.5){
                    if(ships[randomRow + i][randomColumn] == null){
                        shipSizeCheck++;
                    }
                    else break;
                }

                //check potential ship placement if random orientation is Horizonal
                else{
                    if(ships[randomRow][randomColumn + i] == null){
                        shipSizeCheck++;
                    }
                    else break;
                }
            }

            //places ship in selected random location in selected random orientation if location size check passes
            if(shipSizeCheck == ship.getSize()){
                for (int i = 0; i < ship.getSize(); i++){
                    if(orientation < 0.5){
                        ships[randomRow + i][randomColumn] = ship;
                    }
                    else{
                        ships[randomRow][randomColumn + i] = ship;
                    }
                }
                //used to exit loop if the ship has been placed successfully 
                validLocation = true;
            }
        }
    }

    //print the current board for user visualization
    public static void displayBoard(String[][] spaces){
        //print numbers for top of board
        for (int i = 1; i <= 10; i++){
            System.out.print(" " + i);
        }
        System.out.println();

        for (int i = 0; i < spaces.length; i++){
            //print characters for left side of the board
            System.out.print((char)(i + 65));

            for (int j = 0; j < spaces[0].length; j++){
                if(spaces[i][j] == null){
                    System.out.print(" ");
                }
                else{
                    System.out.print(spaces[i][j]);
                }
                if (j < 9){
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    //start Battleship game
    public static void startGame(Ship[][] ships, Ship Carrier, Ship Battleship, Ship Cruiser, Ship Submarine, Ship Destroyer){
        //create scanner for user input stream
        Scanner userInput = new Scanner(System.in);

        //array to store player guess outcomes
        String[][] spaces = new String[10][10];

        //keep track of player guesses throughout the game
        int guessCount = 0;

        //start the game and plays until player has sunk all ships
        while(!Carrier.isSunk() || !Battleship.isSunk() || !Cruiser.isSunk() || !Submarine.isSunk() || !Destroyer.isSunk()){
            //display board
            displayBoard(spaces);

            //prompt user for guess
            System.out.print("Enter your guess: ");
            
            //store user guess (assumes the user enters a properly formatted guess)
            String userGuess = userInput.next();
            System.out.println();

            //checks guess against current board
            boolean validGuess = checkGuess(ships, spaces, userGuess);

            //increases guess count if the guess was not a duplicate guess
            if (validGuess){
                guessCount++;
            }
        }

        //print the amount of guesses player took to sink all ships
        System.out.println("Victory! You sunk all ships in " + guessCount + " guesses.");

        //print the final board
        displayBoard(spaces);

        //close user input stream
        userInput.close();
    }

    //print and record outcome of user guess
    public static boolean checkGuess(Ship[][] ships, String[][] spaces, String userGuess){
        //initialize index location variables
        int guessRow = 0;
        int guessColumn = 0;

        //parse user input and store user guess as array index locations for specifically the 10th column (edge case)
        if (userGuess.length() > 2){
            //Subtract 65 to convert ASCII letter to integer
            guessRow = userGuess.charAt(0) - 65;
            //Sets user input '10' to integer 9 (since the array starts at 0 in the program and starts at 1 in the game display)
            guessColumn = 9;
        }
        //parse user input and store user guess as array index locations
        else{
            //Subtract 65 to convert ASCII letter to integer
            guessRow = userGuess.charAt(0) - 65;
            //subtract '1' to convert ASCII numbers to integer representation of the number, less 1 (since the array starts at 0 in the program and starts at 1 in the game display)
            guessColumn = userGuess.charAt(1) - '1';
        }

        //check if player entered a duplicate guess and explain to player that this is a duplicate guess
        if(spaces[guessRow][guessColumn] == "X" || spaces[guessRow][guessColumn] == "."){
            System.out.println("You already guessed that space!");
            return false;
        }

        //check if current guess location is a ship and explain to player that the hit was successful and if the whole ship was sunk
        if (ships[guessRow][guessColumn] != null){
            System.out.println("Hit!");
            ships[guessRow][guessColumn].hit();
            spaces[guessRow][guessColumn] = "X";
            if(ships[guessRow][guessColumn].isSunk()){
                System.out.println("You sunk the " + ships[guessRow][guessColumn] + "!");
            }
            return true;
        }

        //explain to player that the guess was a miss
        else{
            System.out.println("Miss!");
            spaces[guessRow][guessColumn] = ".";
            return true;
        }
    }
}