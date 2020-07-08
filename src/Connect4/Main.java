package Connect4;

import processing.core.*;

public class Main extends PApplet {

    public static boolean isServer;
    // -1ならまだクリックされていない
    public int colOfMouseClicked = -1;
    private ServerPlayer serverPlayer;
    private ClientPlayer clientPlayer;
    public static final int OFFSET_X = 75;
    public static final int OFFSET_Y = 100;

    public static void main(String[] args) {
        try {
            if (args[0].equals("--server")) {
                isServer = true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        PApplet.main("Connect4.Main");
    }

    @Override
    public void settings() {
        size(500, 500);
    }

    @Override
    public void setup() {
        if (isServer) {
            serverPlayer = new ServerPlayer();
        } else {
            clientPlayer = new ClientPlayer();
        }
        textSize(32);
    }

    @Override
    public void draw() {
//        System.out.println("mouse position: "+colOfMouseClicked);
        background(255);
        // 盤面を作成
        fill(0);
        text(isServer ? "Server" : "Client", 40, 40);
        // ヨコ
        for (int i = 0; i < 7; i++) {
            line(OFFSET_X, OFFSET_Y + 50 * i, OFFSET_X + 350, OFFSET_Y + 50 * i);
        }
        // タテ
        for (int j = 0; j < 8; j++) {
            line(OFFSET_X + 50 * j, OFFSET_Y, OFFSET_X + 50 * j, OFFSET_Y + 300);
        }
        // サーバー側(2)：青
        // クライアント側(1)：赤
        if (isServer) {
            if (serverPlayer.socket == null) {
                fill(0);
                text("待機中...", 250, 50);
            } else {
                for (int i = 0; i < Board.H; i++) {
                    for (int j = 0; j < Board.W; j++) {
                        switch (this.serverPlayer.board.get(i, j)) {
                            case 1:
                                fill(255, 0, 0);
                                ellipse(OFFSET_X + 25 + 50 * j, OFFSET_Y + 25 + 50 * i, 25, 25);
                                break;
                            case 2:
                                fill(0, 0, 255);
                                ellipse(OFFSET_X + 25 + 50 * j, OFFSET_Y + 25 + 50 * i, 25, 25);
                                break;
                        }
                    }
                }
                try {
                    serverPlayer.play(this);
                } catch (NumberFormatException e) {
                    System.exit(0);
                }
            }
        } else {
            for (int i = 0; i < Board.H; i++) {
                for (int j = 0; j < Board.W; j++) {
                    switch (this.clientPlayer.board.get(i, j)) {
                        case 1:
                            fill(255, 0, 0);
                            ellipse(OFFSET_X + 25 + 50 * j, OFFSET_Y + 25 + 50 * i, 25, 25);
                            break;
                        case 2:
                            fill(0, 0, 255);
                            ellipse(OFFSET_X + 25 + 50 * j, OFFSET_Y + 25 + 50 * i, 25, 25);
                            break;
                    }
                }
            }
            try {
                clientPlayer.play(this);
            } catch (NumberFormatException e) {
                System.exit(0);
            }
        }
    }

    @Override
    public void mouseClicked() {
        for (int j = 0; j < Board.W; j++) {
            if (OFFSET_Y <= mouseY && mouseY <= OFFSET_Y + 300 && OFFSET_X + 50 * j <= mouseX && mouseX < OFFSET_X + 50 * (j + 1)) {
                colOfMouseClicked = j;
            }
        }
        System.out.println(colOfMouseClicked);
    }

}

//public class Main {
//    public static void main(String[] args) {
//        Scanner In = new Scanner(System.in);
//        Board board = new Board();
//        int nowTurn = 1;
//        while (true) {
//            board.printBoard();
//            int i, j;
//            while (true) {
//                System.out.println("置く場所を入力して下さい");
//                System.out.print("列：");
//                j = In.nextInt();
//                j--;
//                i = board.canPlace(j);
//                if (i != -1) {
//                    break;
//                }
//            }
//            board.set(i, j, nowTurn);
//            if (board.isWin() == nowTurn) {
//                System.out.println("Player" + nowTurn + "の勝ちです");
//                break;
//            }
//            if (nowTurn == 1) {
//                nowTurn = 2;
//            } else {
//                nowTurn = 1;
//            }
//        }
//    }
//}