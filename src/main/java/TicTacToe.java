import java.util.*;

public class TicTacToe {

    static List<Integer> playerPosition = new ArrayList();
    static List<Integer> cpuPosition = new ArrayList();
    public static void main(String args[]){

        char[][] gameBoard = {{' ', '|',' ','|',' '},
                {'-','+','-','+','-'},
                {' ', '|',' ','|',' '},
                {'-', '+','-','+','-'},
                {' ', '|',' ','|',' '}};



        while(true){
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter the position :");
            int playerPos = scan.nextInt();
            while(playerPosition.contains(playerPos) || cpuPosition.contains(playerPos)){
                System.out.println("Position taken... Select other position :");
                playerPos = scan.nextInt();
            }
            placement(gameBoard,playerPos,"player");
            Random rand = new Random();
            int cpuPos = rand.nextInt(9)+1;
            while(playerPosition.contains(cpuPos) || cpuPosition.contains(cpuPos)){
                cpuPos = rand.nextInt(9)+1;
            }
            placement(gameBoard,cpuPos,"cpu");
            printBoardGame(gameBoard);
            String result = checkWinner();
            if(!result.equals("") ) {
                System.out.println(result);
                return;
            }
        }

    }

    public static void placement(char[][] gameBoard, int pos, String user){
        char symbol=' ';
        if(user.equals("player")){
            symbol = 'X';
            playerPosition.add(pos);
        }else{
            symbol = '0';
            cpuPosition.add(pos);
        }
        switch (pos){
            case 1: gameBoard[0][0] = symbol;break;
            case 2: gameBoard[0][2] = symbol;break;
            case 3: gameBoard[0][4] = symbol;break;
            case 4: gameBoard[2][0] = symbol;break;
            case 5: gameBoard[2][2] = symbol;break;
            case 6: gameBoard[2][4] = symbol;break;
            case 7: gameBoard[4][0] = symbol;break;
            case 8: gameBoard[4][2] = symbol;break;
            case 9: gameBoard[4][4] = symbol;break;
        }

    }

    public static void printBoardGame(char [][] gameBoard){

        for(int i=0;i<gameBoard.length;i++){
            for(int j=0;j<gameBoard[0].length;j++){
                System.out.print(gameBoard[i][j]);
            }
            System.out.println();
        }
    }

    public static String checkWinner(){
        List topRow = Arrays.asList(1,2,3);
        List midRow = Arrays.asList(4,5,6);
        List botRow = Arrays.asList(7,8,9);
        List lefCol = Arrays.asList(1,4,7);
        List midCol = Arrays.asList(2,5,8);
        List rigCol = Arrays.asList(3,6,9);
        List cross1 = Arrays.asList(1,5,9);
        List cross2 = Arrays.asList(3,5,7);

        List<List> winning = new ArrayList<List>();
        winning.add(topRow);
        winning.add(midRow);
        winning.add(botRow);
        winning.add(lefCol);
        winning.add(midCol);
        winning.add(rigCol);
        winning.add(cross1);
        winning.add(cross2);

        for(List l : winning){
            if(playerPosition.containsAll(l)){
                return  "Congrations You Won";
            } else if(cpuPosition.containsAll(l)){
                return "CPU Won";
            }else if(playerPosition.size()+cpuPosition.size() == 9){
                return "Tie";
            }
        }
        return "";
    }
}
