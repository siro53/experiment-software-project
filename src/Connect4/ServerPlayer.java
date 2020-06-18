package Connect4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerPlayer {
    public static final int PORT = 8000;
    public static final int turnNumber = 2;

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
                    System.out.println("相手は"+(col+1)+"列目に石を入れました");
                    System.out.println("あなたの番です");
                    board.set(row, col, ClientPlayer.turnNumber);
                    board.printBoard();
                    // 自分のターンの処理をする
                    while (true) {
                        System.out.print("石を置く列を選択して下さい(1列目〜7列目)：");
                        col = scanner.nextInt();
                        row = board.canPlace(col - 1);
                        if(row != -1){
                            col--;
                            break;
                        }
                    }
                    board.set(row, col, turnNumber);
                    System.out.println("石を置きました");
                    board.printBoard();
                    if(board.isWin() == turnNumber){
                        System.out.println("あなたの勝ちです！");
                        Out.println(-1);
                        break;
                    }else{
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
