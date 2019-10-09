/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

import java.util.ArrayList;

public class BoggleSolver {

    private TrieSET words = new TrieSET();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        boolean debug = true;
        if (debug) StdOut.println("Hello, constructing dictionary");
        for (String word : dictionary) words.add(word);
    }

    private void dfsBoard(BoggleBoard board, boolean[][] marked, String curWordArr[][],
                          String prevWord, int curX,
                          int curY) {
        boolean debug = true;
        marked[curX][curY] = true;

        // curWordArr[curX][curY] = curWord;
        // if (debug) StdOut.println("Currently at : " + board.getLetter(curX, curY));
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;
                if (curX + i > board.rows() - 1) continue;
                if (curY + j > board.cols() - 1) continue;
                if (curX + i < 0) continue;
                if (curY + j < 0) continue;
                if (marked[curX + i][curY + j]) continue;
                String curWord = prevWord + board.getLetter(curX, curY);
                dfsBoard(board, marked, curWordArr, curWord, curX + i, curY + j);
                if (debug) StdOut.println("Word formed : " + curWord);

            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boolean debug = true;
        if (debug) StdOut.println(board.toString());
        ArrayList<String> validWords = new ArrayList<String>();

        for (int x = 0; x < board.rows(); x++) {
            for (int y = 0; y < board.cols(); y++) {
                if (debug) StdOut.println(board.getLetter(x, y));
                boolean marked[][] = new boolean[board.rows()][board.cols()];
                String curWordArr[][] = new String[board.rows()][board.cols()];
                dfsBoard(board, marked, curWordArr, "", x, y);
            }

        }

        // rv.add("Hi");
        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
