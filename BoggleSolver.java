/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

public class BoggleSolver {

    private TrieSET words = new TrieSET();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        boolean debug = true;
        if (debug) StdOut.println("Hello, constructing dictionary");
        for (String word : dictionary) words.add(word);
    }

    private void dfsBoard(BoggleBoard board, boolean[][] marked,
                          String prevWord, int curX,
                          int curY, TrieSET validWords) {
        boolean debug = false;

        if (curX > board.rows() - 1) return;
        if (curY > board.cols() - 1) return;
        if (curX < 0) return;
        if (curY < 0) return;
        if (marked[curX][curY]) return;
        Iterable<String> possibleWords = words.keysWithPrefix(prevWord);
        if (!possibleWords.iterator().hasNext()) return;

        marked[curX][curY] = true;
        prevWord = prevWord + board.getLetter(curX, curY);
        if (debug) StdOut.println("Word formed : " + prevWord);
        // if (debug) StdOut.println("Len of word formed : " + curWord.length());
        if (words.contains(prevWord) && prevWord.length() > 2) validWords.add(prevWord);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;
                //prefix optimization

                dfsBoard(board, marked, prevWord, curX + i, curY + j, validWords);
            }
        }
        marked[curX][curY] = false;

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boolean debug = false;

        TrieSET validWords = new TrieSET();
        if (debug) StdOut.println(board.toString());

        for (int x = 0; x < board.cols(); x++) {
            for (int y = 0; y < board.rows(); y++) {
                if (debug) StdOut.println(board.getLetter(x, y));
                boolean marked[][] = new boolean[board.rows()][board.cols()];
                dfsBoard(board, marked, "", x, y, validWords);
            }

        }

        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int len = word.length();
        if (len == 3) return 1;
        if (len == 4) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        if (len > 8) return 11;
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
