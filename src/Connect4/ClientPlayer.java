package Connect4;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientPlayer {
    public static final int turnNumber = 1;

    public static void main(String[] args) throws IOException {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        InetAddress address = InetAddress.getByName("localhost");
        System.out.println("address = " + address);
        Socket socket = new Socket(address, ServerPlayer.PORT);
        try{
            System.out.println("socket = " + socket);
            BufferedReader In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            // ここに処理を書く
            int num, row, col=-1;
            while (true){
                // 自分のターンの処理をする
                if(col != -1) System.out.println("相手は"+(col+1)+"列目に石を入れました");
                System.out.println("あなたの番です");
                board.printBoard();
                while (true) {
                    System.out.print("石を置く列を選択して下さい(1列目〜7列目)：");
                    col = scanner.nextInt();
                    row = board.canPlace(col - 1);
                    if(row != -1){
                        col--;
                        break;
                    }
                }
                System.out.println("石を置きました");
                board.set(row, col, turnNumber);
                board.printBoard();
                if(board.isWin() == turnNumber){
                    System.out.println("あなたの勝ちです！");
                    Out.println(-1);
                    break;
                }else{
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
