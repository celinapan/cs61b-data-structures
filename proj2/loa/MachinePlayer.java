/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.List;

import static loa.Piece.*;

/** An automated Player.
 *  @author Trang Van
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {

        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        assert !work.gameOver() : "_winnerKnown error probably";
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (depth == 0 || board.gameOver()) {
            return heuristicScore(board);
        }
        List<Move> possibleMoves = board.legalMoves();
        int bestScore = sense == 1 ? -INFTY : INFTY;
        Move bestMove = possibleMoves.get(0);
        for (Move m: possibleMoves) {
            Board copy = new Board(board);
            copy.makeMove(m);
            int score = findMove(copy, depth - 1, false,
                    sense * -1, alpha, beta);
            if (sense == 1) {
                if (bestScore < score) {
                    bestScore = score;
                    bestMove = m;
                }
                alpha = Math.max(score, alpha);
                if (bestScore >= beta) {
                    break;
                }
                alpha = Math.max(score, alpha);
            } else if (sense == -1) {
                if (bestScore > score) {
                    bestScore = score;
                    bestMove = m;
                }
                beta = Math.min(score, beta);
                if (bestScore <= beta) {
                    break;
                }
            }
        }
        if (saveMove) {
            _foundMove = bestMove;
        }
        return bestScore;
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 3;
    }

    /**
     * Calculates a static score for the game tree.
     * Computes the board's region size and gets the max
     * size for each player. Score is the difference between them.
     * Determines if game is over and returns the WINNING_VALUE or 0
     * @param board the board instance for the game
     * @return int score for findMove
     */
    private int heuristicScore(Board board) {
        int black = board.getRegionSizes(BP).size();
        int white = board.getRegionSizes(WP).size();
        if (board.gameOver() && board.winner() == WP) {
            return WINNING_VALUE;
        } else if (board.gameOver() && board.winner() == BP) {
            return -WINNING_VALUE;
        } else {
            return ((black - white)
                    + getGame().randInt(10)) * chooseDepth();
        }
    }
    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;
}
