package Connect4.gui;

import Connect4.Board;
import Connect4.ClientPlayer;
import Connect4.ServerPlayer;
import processing.core.PApplet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerPlayerGUI {
    public static final int PORT = 8000;
    public static final int turnNumber = 2;

    public ServerPlayerGUI() {
        try {
            this.board = new Board();
            this.serverSocket = new ServerSocket(PORT);
            this.socket = serverSocket.accept();
            this.In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Board board;
    public ServerSocket serverSocket;
    public Socket socket;
    public BufferedReader In;
    public PrintWriter Out;
    private int num, row, col = -1;
    private int yourTurn = 1;

    public void DrawandDo(PApplet pa) {
        if (yourTurn == 0) {
            pa.fill(0x00, 0x00, 0x00);
            pa.text("あなたのターンです", 250, 500);
            if (ProcessingMain.MouseClicked != -1) {
                col = ProcessingMain.MouseClicked;
                row = board.canPlace(col);
                if (row != -1) {
                    board.set(row, col, turnNumber);
                    if (board.isWin() == turnNumber) {
                        ProcessingMain.text = "あなたの勝ちです！";
                        Out.println(-1);
                    } else {
                        Out.println(row * 100 + col);
                    }
                    yourTurn++;
                }
            }
        } else if (yourTurn == 1) {
            pa.fill(0x00, 0x00, 0x00);
            pa.text("相手のターンです", 250, 500);
            yourTurn++;
        } else {
            try {
                num = Integer.parseInt(In.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (num == -1) {
                ProcessingMain.text = "あなたの負けです...";
            } else {
                row = num / 100;
                col = num - (num / 100) * 100;
                board.set(row, col, ClientPlayer.turnNumber);
            }
            ProcessingMain.MouseClicked = -1;
            yourTurn = 0;
        }
    }

    public void threadstart() {
        serverThread th = new serverThread();
        th.start();
    }

    class serverThread extends Thread {
        @Override
        public void run() {
            try {
                socket = serverSocket.accept();
                In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(socket);
        }
    }

    public static void main(String[] args) throws IOException {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Started: " + serverSocket);
        try {
            Socket socket = serverSocket.accept();
            try {
                System.out.println("Connection Accepted! : " + socket);
                BufferedReader In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                // ここに処理を書く
                // num == -1 -> 相手の勝ち
                // num != -1 -> (i, j)に相手がコマを置いたとすると、num == i * 100 + j
                int num, row, col;
                while (true) {
                    // 相手のターンの分を処理する
                    num = Integer.parseInt(In.readLine());
                    if (num == -1) {
                        System.out.println("あなたの負けです...");
                        break;
                    }
                    row = num / 100;
                    col = num - (num / 100) * 100;
                    System.out.println("相手は" + (col + 1) + "列目に石を入れました");
                    System.out.println("あなたの番です");
                    board.set(row, col, ClientPlayer.turnNumber);
                    board.printBoard();
                    // 自分のターンの処理をする
                    while (true) {
                        System.out.print("石を置く列を選択して下さい(1列目〜7列目)：");
                        col = scanner.nextInt();
                        row = board.canPlace(col - 1);
                        if (row != -1) {
                            col--;
                            break;
                        }
                    }
                    board.set(row, col, turnNumber);
                    System.out.println("石を置きました");
                    board.printBoard();
                    if (board.isWin() == turnNumber) {
                        System.out.println("あなたの勝ちです！");
                        Out.println(-1);
                        break;
                    } else {
                        Out.println(row * 100 + col);
                    }
                }
            } finally {
                System.out.println("Closing...");
                socket.close();
            }
        } finally {
            serverSocket.close();
        }
    }
}
