package Connect4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerPlayer {
    public static final int PORT = 8000;
    public static final int turnNumber = 2;

    public Board board;
    public ServerSocket serverSocket;
    public Socket socket;
    public BufferedReader In;
    public PrintWriter Out;
    private int num;
    private int row;
    private int col;
    public int nowTurn;

    public ServerPlayer() {
        this.board = new Board();
        this.nowTurn = 2;
        try {
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("Started: " + this.serverSocket);
            this.socket = this.serverSocket.accept();
            System.out.println("Connection Accepted! : " + socket);
            this.In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(Main main) {
        try {
            switch (nowTurn) {
                case 0:
                    // 相手のターンの処理をする
                    try {
                        num = Integer.parseInt(In.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (num >= 10000) {
                        num -= 10000;
                        row = num / 100;
                        col = num - (num / 100) * 100;
                        board.set(row, col, ClientPlayer.turnNumber);
                        nowTurn = 4;
                    } else {
                        row = num / 100;
                        col = num - (num / 100) * 100;
                        board.set(row, col, ClientPlayer.turnNumber);
                        nowTurn = (nowTurn + 1) % 3;
                    }
                    main.colOfMouseClicked = -1;
                    break;
                case 1:
                    // 自分のターンの処理をする
                    main.fill(0);
                    main.text("あなたのターンです", 200, 35);
                    if (main.colOfMouseClicked != -1) {
                        col = main.colOfMouseClicked;
                        row = board.canPlace(col);
                        if (row != -1) {
                            board.set(row, col, turnNumber);
                            if (board.isWin() == turnNumber) {
                                main.colOfMouseClicked = -1;
                                Out.println(row * 100 + col + 10000);
                                nowTurn = 3;
                            } else {
                                main.colOfMouseClicked = -1;
                                Out.println(row * 100 + col);
                                nowTurn = (nowTurn + 1) % 3;
                            }
                        } else {
                            main.colOfMouseClicked = -1;
                        }
                    }
                    break;
                case 2:
                    // テキスト表示のためにこれを噛ませる
                    main.fill(0);
                    main.text("相手のターンです", 200, 35);
                    main.colOfMouseClicked = -1;
                    nowTurn = (nowTurn + 1) % 3;
                    break;
                case 3:
                    main.fill(0);
                    main.text("あなたの勝ちです！", 120, 450);
                    break;
                case 4:
                    main.fill(0);
                    main.text("あなたの負けです...", 120, 450);
                    break;
            }
        } catch (NumberFormatException e) {
            System.exit(0);
        }
    }
}
