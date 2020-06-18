package Connect4.gui;

import processing.core.PApplet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessingMain extends PApplet {

    public static void main(String[] args) {
        try {
            if (args[0].equals("--server")) isServer = true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        PApplet.main("Connect4.gui.ProcessingMain");
    }

    public static boolean isServer = false;
    private ServerPlayerGUI spg;
    private ClientPlayerGUI cpg;
    public static int MouseClicked = -1;
    public static String text = "";

    @Override
    public void settings() {
        super.settings();
        size(600, 600);
    }

    @Override
    public void setup() {
        super.setup();
        if (isServer) {
            spg = new ServerPlayerGUI();
//            spg.threadstart();
        } else {
            cpg = new ClientPlayerGUI();
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void draw() {
//        super.draw();
        background(0xFF, 0xFF, 0xFF);
        fill(0xFF, 0x00, 0x00);
        text(isServer ? "Server" : "Client", 10, 10);
        fill(0x00, 0x00, 0x00);
        for (int i = 0; i < 8; i++) {
            line(125 + i * 50, 150, 125 + i * 50, 450);
        }
        for (int i = 0; i < 7; i++) {
            line(125, 150 + i * 50, 475, 150 + i * 50);
        }
        if (isServer) {
            if (spg.socket == null) {
                fill(0x00, 0x00, 0x00);
                text("ソケットの待機中", 250, 500);
            } else {
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 7; j++) {
                        switch (spg.board.Grid[i][j]) {
                            case 1:
                                fill(0xFF, 0x00, 0x00);
                                ellipse(150 + j * 50, 175 + i * 50, 40, 40);
                                break;
                            case 2:
                                fill(0x00, 0xFF, 0x00);
                                ellipse(150 + j * 50, 175 + i * 50, 40, 40);
                                break;
                        }
                    }
                }
                spg.DrawandDo(this);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    switch (cpg.board.Grid[i][j]) {
                        case 1:
                            fill(0xFF, 0x00, 0x00);
                            ellipse(150 + j * 50, 175 + i * 50, 40, 40);
                            break;
                        case 2:
                            fill(0x00, 0xFF, 0x00);
                            ellipse(150 + j * 50, 175 + i * 50, 40, 40);
                            break;
                    }
                }
            }
            cpg.DrawandDo(this);
        }
        fill(0x00, 0x00, 0x00);
        text(text, 280, 30);

//        System.out.println(System.currentTimeMillis());
    }

    @Override
    public void mouseClicked() {
        for (int i = 0; i < 7; i++) {
            if (mouseX >= 125 + i * 50 && mouseX <= 175 + i * 50 && mouseY >= 150 && mouseY <= 450) {
                MouseClicked = i;
            }
        }
    }
}
