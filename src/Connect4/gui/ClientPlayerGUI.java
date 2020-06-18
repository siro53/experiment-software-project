package Connect4.gui;

import Connect4.Board;
import Connect4.ServerPlayer;
import processing.core.PApplet;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientPlayerGUI {
    public static final int turnNumber = 1;

    public ClientPlayerGUI() {
        try {
            this.board = new Board();
            InetAddress address = InetAddress.getByName("localhost");
            this.socket = new Socket(address, ServerPlayer.PORT);
            this.In = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Board board;
    public Socket socket;
    public BufferedReader In;
    public PrintWriter Out;
    private int num, row, col = -1;
    private int yourTurn = 0;

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
                } else {
                    ProcessingMain.MouseClicked = -1;
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
                board.set(row, col, ServerPlayer.turnNumber);
            }
            ProcessingMain.MouseClicked = -1;
            yourTurn = 0;
        }
    }

    public static void main(String[] args) throws IOException {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        InetAddress address = InetAddress.getByName("localhost");
        System.out.println("address = " + address);
        Socket socket = new Socket(address, ServerPlayer.PORT);
        try {
            System.out.println("socket = " + socket);
            BufferedReader In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            // ここに処理を書く
            int num, row, col = -1;
            while (true) {
                // 自分のターンの処理をする
                if (col != -1) System.out.println("相手は" + (col + 1) + "列目に石を入れました");
                System.out.println("あなたの番です");
                board.printBoard();
                while (true) {
                    System.out.print("石を置く列を選択して下さい(1列目〜7列目)：");
                    col = scanner.nextInt();
                    row = board.canPlace(col - 1);
                    if (row != -1) {
                        col--;
                        break;
                    }
                }
                System.out.println("石を置きました");
                board.set(row, col, turnNumber);
                board.printBoard();
                if (board.isWin() == turnNumber) {
                    System.out.println("あなたの勝ちです！");
                    Out.println(-1);
                    break;
                } else {
                    Out.println(row * 100 + col);
                }
                // 相手のターンの処理をする
                num = Integer.parseInt(In.readLine());
                if (num == -1) {
                    System.out.println("あなたの負けです...");
                    break;
                }
                row = num / 100;
                col = num - (num / 100) * 100;
                board.set(row, col, ServerPlayer.turnNumber);
            }

        } finally {
            System.out.println("Closing...");
            socket.close();
        }
    }
}
