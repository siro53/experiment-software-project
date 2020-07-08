package Connect4;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientPlayer {
    public static final int turnNumber = 1;

    public Board board;
    public InetAddress address;
    public Socket socket;
    public BufferedReader In;
    public PrintWriter Out;
    private int num;
    private int row;
    private int col;
    private int nowTurn;

    public ClientPlayer() {
        this.board = new Board();
        this.nowTurn = 1;
        try {
            this.address = InetAddress.getByName("localhost");
            System.out.println("address = " + address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            this.socket = new Socket(this.address, ServerPlayer.PORT);
            System.out.println("socket = " + socket);
            this.In = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(Main main) {
        try{
            switch (nowTurn) {
                case 0:
                    // 相手のターンの処理をする
                    try {
                        num = Integer.parseInt(In.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (num == -1) {
                        main.text("あなたの負けです...", 230, 450);
                    } else {
                        row = num / 100;
                        col = num - (num / 100) * 100;
                        board.set(row, col, ServerPlayer.turnNumber);
                    }
                    main.colOfMouseClicked = -1;
                    nowTurn = (nowTurn + 1) % 3;
                    break;
                case 1:
                    // 自分のターンの処理をする
                    main.fill(0);
                    main.text("あなたのターンです", 230, 50);
                    if (main.colOfMouseClicked != -1) {
                        col = main.colOfMouseClicked;
                        row = board.canPlace(col);
                        if (row != -1) {
                            board.set(row, col, turnNumber);
                            if (board.isWin() == turnNumber) {
                                main.text("あなたの勝ちです！", 230, 450);
                                main.colOfMouseClicked = -1;
                                Out.println(-1);
                            } else {
                                main.colOfMouseClicked = -1;
                                Out.println(row * 100 + col);
                            }
                            nowTurn = (nowTurn + 1) % 3;
                        } else {
                            main.colOfMouseClicked = -1;
                        }
                    }
                    break;
                case 2:
                    // テキスト表示のためにこれを噛ませる
                    main.fill(0);
                    main.text("相手のターンです", 230, 50);
                    main.colOfMouseClicked = -1;
                    nowTurn = (nowTurn + 1) % 3;
                    break;
            }
        }catch (NumberFormatException e){
            System.exit(0);
        }
    }

//    public static void main(String[] args) throws IOException {
//        Board board = new Board();
//        Scanner scanner = new Scanner(System.in);
//
//        InetAddress address = InetAddress.getByName("localhost");
//        System.out.println("address = " + address);
//        Socket socket = new Socket(address, ServerPlayer.PORT);
//        try {
//            System.out.println("socket = " + socket);
//            BufferedReader In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//
//            // ここに処理を書く
//            int num, row, col = -1;
//            while (true) {
//                // 自分のターンの処理をする
//                if (col != -1) System.out.println("相手は" + (col + 1) + "列目に石を入れました");
//                System.out.println("あなたの番です");
//                board.printBoard();
//                while (true) {
//                    System.out.print("石を置く列を選択して下さい(1列目〜7列目)：");
//                    col = scanner.nextInt();
//                    row = board.canPlace(col - 1);
//                    if (row != -1) {
//                        col--;
//                        break;
//                    }
//                }
//                System.out.println("石を置きました");
//                board.set(row, col, turnNumber);
//                board.printBoard();
//                if (board.isWin() == turnNumber) {
//                    System.out.println("あなたの勝ちです！");
//                    Out.println(-1);
//                    break;
//                } else {
//                    Out.println(row * 100 + col);
//                }
//                // 相手のターンの処理をする
//                num = Integer.parseInt(In.readLine());
//                if (num == -1) {
//                    board.printBoard();
//                    System.out.println("あなたの負けです...");
//                    break;
//                }
//                row = num / 100;
//                col = num - (num / 100) * 100;
//                board.set(row, col, ServerPlayer.turnNumber);
//            }
//
//        } finally {
//            System.out.println("Closing...");
//            socket.close();
//        }
//    }
}
