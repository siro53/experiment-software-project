package Connect4;

public class Board {
    // 6 rows 7 columns
    // まだ置かれていない -> "."
    // player1 -> "o"
    // player2 -> "x"
    public static final int H = 6;
    public static final int W = 7;
    private int[][] Grid;

    Board() {
        this.Grid = new int[H][W];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                Grid[i][j] = 0;
            }
        }
    }

    void printBoard() {
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                switch (Grid[i][j]) {
                    case 0:
                        System.out.print(". ");
                        break;
                    case 1:
                        System.out.print("o ");
                        break;
                    case 2:
                        System.out.print("x ");
                        break;
                }
            }
            System.out.println();
        }
    }

    // player1の勝ち -> 1
    // player2の勝ち -> 2
    // 勝敗決まらず -> 0
    int isWin() {
        // ヨコ
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W - 3; j++) {
                if (Grid[i][j] == Grid[i][j + 1] && Grid[i][j + 1] == Grid[i][j + 2] && Grid[i][j + 2] == Grid[i][j + 3]) {
                    if (Grid[i][j] == 1) {
                        return 1;
                    } else if (Grid[i][j] == 2) {
                        return 2;
                    }
                }
            }
        }
        // タテ
        for (int j = 0; j < W; j++) {
            for (int i = 0; i < H - 3; i++) {
                if (Grid[i][j] == Grid[i + 1][j] && Grid[i + 1][j] == Grid[i + 2][j] && Grid[i + 2][j] == Grid[i + 3][j]) {
                    if (Grid[i][j] == 1) {
                        return 1;
                    } else if (Grid[i][j] == 2) {
                        return 2;
                    }
                }
            }
        }
        // 右下
        for (int i = 0; i < H - 3; i++) {
            for (int j = 0; j < W - 3; j++) {
                if (Grid[i][j] == Grid[i + 1][j + 1] && Grid[i + 1][j + 1] == Grid[i + 2][j + 2] && Grid[i + 2][j + 2] == Grid[i + 3][j + 3]) {
                    if (Grid[i][j] == 1) {
                        return 1;
                    } else if (Grid[i][j] == 2) {
                        return 2;
                    }
                }
            }
        }
        // 右上
        for (int i = H - 1; i >= H - 3; i--) {
            for (int j = 0; j < W - 3; j++) {
                if (Grid[i][j] == Grid[i - 1][j + 1] && Grid[i - 1][j + 1] == Grid[i - 2][j + 2] && Grid[i - 2][j + 2] == Grid[i - 3][j + 3]) {
                    if (Grid[i][j] == 1) {
                        return 1;
                    } else if (Grid[i][j] == 2) {
                        return 2;
                    }
                }
            }
        }
        // 左下
        for (int i = 0; i < H - 3; i++) {
            for (int j = W - 1; j >= W - 4; j--) {
                if (Grid[i][j] == Grid[i + 1][j - 1] && Grid[i + 1][j - 1] == Grid[i + 2][j - 2] && Grid[i + 2][j - 2] == Grid[i + 3][j - 3]) {
                    if (Grid[i][j] == 1) {
                        return 1;
                    } else if (Grid[i][j] == 2) {
                        return 2;
                    }
                }
            }
        }
        // 左上
        for (int i = H - 1; i >= H - 3; i--) {
            for (int j = W - 1; j >= W - 4; j--) {
                if (Grid[i][j] == Grid[i - 1][j - 1] && Grid[i - 1][j - 1] == Grid[i - 2][j - 2] && Grid[i - 2][j - 2] == Grid[i - 3][j - 3]) {
                    if (Grid[i][j] == 1) {
                        return 1;
                    } else if (Grid[i][j] == 2) {
                        return 2;
                    }
                }
            }
        }
        return 0;
    }

    // j列目に置けるかどうか
    int canPlace(int j) {
        if (j < 0 || j >= W) {
            return -1;
        }
        for (int h = 0; h < H; h++) {
            if (Grid[h][j] != 0) {
                return h - 1;
            }
        }
        return H - 1;
    }

    // player{turn}の番
    void set(int i, int j, int turn) {
        Grid[i][j] = turn;
    }

    int get(int i, int j){
        return Grid[i][j];
    }
}
