/* *****************************************************************************
 *  Name: BoggleSolver
 *  Date: Oct 10,2019
 *  Description: BoggleSolver for Algorithms II, Princeton University
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.TST;

import java.util.HashSet;

public class BoggleSolver {

    private TST<Integer> words = new TST<Integer>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        boolean debug = false;
        if (debug) StdOut.println("Hello, constructing dictionary");
        for (String word : dictionary) words.put(word, 0);
    }

    private void dfsBoard(BoggleBoard board, boolean[][] marked,
                          String curWord, int curX,
                          int curY, HashSet<String> validWords) {
        boolean debug = false;

        if (curX < 0 || curY < 0) return;
        if (curY > board.rows() - 1) return;
        if (curX > board.cols() - 1) return;
        if (marked[curY][curX]) return;

        marked[curY][curX] = true;
        if (curWord.length() > 2 && words.contains(curWord)) {
            validWords.add(curWord);
            if (debug) StdOut.println("Added " + curWord);
        }
        if (debug) StdOut.println(curX + " " + curY);
        char curLetter = board.getLetter(curY, curX);

        if (Character.compare(curLetter, 'Q') == 0) {
            curWord = curWord + "QU";
        }
        else {
            curWord = curWord + curLetter;
        }
        boolean needDfs = false;
        Iterable<String> possibleWords = words.keysWithPrefix(curWord);
        if (possibleWords.iterator().hasNext()) needDfs = true;
        if (needDfs) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) continue;
                    dfsBoard(board, marked, curWord, curX + i, curY + j, validWords);
                }
            }
        }
        // NOTE: different type of dfs traversal
        // remove trace of the last completed node so all possibilities can be traversed
        marked[curY][curX] = false;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boolean debug = false;

        HashSet<String> validWords = new HashSet<String>();
        if (debug) StdOut.println(board.toString());

        for (int x = 0; x < board.cols(); x++) {
            for (int y = 0; y < board.rows(); y++) {
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                Stopwatch timer = new Stopwatch();
                dfsBoard(board, marked, "", x, y, validWords);
                if (debug) {
                    StdOut.println(board.getLetter(x, y) + " took: " + timer.elapsedTime() + " s");

                }
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
        if (len >= 8) return 11;
        return 0;

    }

    public static void main(String[] args) {
        boolean debug = true;
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();

        Stopwatch timer = new Stopwatch();
        if (debug) StdOut.println("Starting!");
        BoggleSolver solver = new BoggleSolver(dictionary);
        if (debug) StdOut.println("Elapsed to form dictionary = " + timer.elapsedTime());

        // timer = new Stopwatch();
        double totalElapsed = 0.0;
        int LIMITER = 150;
        int size = 4;
        for (int i = 0; i < LIMITER; i++) {
            int score = 0;
            BoggleBoard board;
            // if (i == 0) {
            //     board = new BoggleBoard(args[1]);
            // }
            // else {
            board = new BoggleBoard(size, size);
            // }
            // board = new BoggleBoard(29, 1);
            StdOut.println(board.toString());
            Stopwatch timerInner = new Stopwatch();
            for (String word : solver.getAllValidWords(board)) {
                StdOut.println(word);
                score += solver.scoreOf(word);
            }
            if (debug) StdOut.println("Score = " + score);
            double elapsed = timerInner.elapsedTime();
            totalElapsed += elapsed;
            if (debug) StdOut.println("Elapsed to traverse words = " + elapsed);
        }

        if (debug) StdOut.println("\nElapsed to traverse All = " + totalElapsed);
        if (debug) StdOut.println("\nSolutions per second = " + LIMITER / totalElapsed);
    }
}
