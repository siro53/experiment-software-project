package Connect4;

import processing.core.*;

import java.util.Scanner;

//public class Main extends PApplet {
//    State nowState;
//
//    @Override
//    public void settings() {
//        size(400, 400);
//    }
//
//    @Override
//    public void setup() {
//        textSize(32);
//        textAlign(CENTER);
//        fill(255);
//        nowState = new TitleState(this);
//    }
//
//    @Override
//    public void draw() {
//        background(0);
//        nowState = nowState.doState();
//    }
//
//    public static void main(String[] args) {
//        PApplet.main("Connect4.Main");
//    }
//}

public class Main {
    public static void main(String[] args) {
        Scanner In = new Scanner(System.in);
        Board board = new Board();
        int nowTurn = 1;
        while (true) {
            board.printBoard();
            int i, j;
            while (true) {
                System.out.println("置く場所を入力して下さい");
                System.out.print("列：");
                j = In.nextInt();
                j--;
                i = board.canPlace(j);
                if (i != -1) {
                    break;
                }
            }
            board.set(i, j, nowTurn);
            if (board.isWin() == nowTurn) {
                System.out.println("Player" + nowTurn + "の勝ちです");
                break;
            }
            if (nowTurn == 1) {
                nowTurn = 2;
            } else {
                nowTurn = 1;
            }
        }
    }
}