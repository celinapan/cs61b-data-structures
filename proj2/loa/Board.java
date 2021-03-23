/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Trang Van
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 30;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _turn = side;
        setMoveLimit(DEFAULT_MOVE_LIMIT);
        _moves.clear();
        for (int r = contents.length - 1; r >= 0; r -= 1) {
            for (int c = 0; c < contents.length; c += 1) {
                set(sq(r, c), contents[c][r]);
            }
        }
        _subsetsInitialized = false;
        _winnerKnown = false;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        _turn =  board.turn();
        for (int i = 0; i < board._board.length; i += 1) {
            _board[i] = board._board[i];
        }
        _moves.clear();
        _moves.addAll(board._moves);
        setMoveLimit(DEFAULT_MOVE_LIMIT);

        _winner = board.winner();
        _subsetsInitialized = board._subsetsInitialized;
        _winnerKnown = board._winnerKnown;

        _blackRegionSizes.clear();
        _blackRegionSizes.addAll(board._blackRegionSizes);

        _whiteRegionSizes.clear();
        _whiteRegionSizes.addAll(board._whiteRegionSizes);
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        Square start = move.getFrom();
        Square finish = move.getTo();
        if (finish != start) {
            _moves.add(move.captureMove());
        } else {
            _moves.add(move);
        }
        set(finish, get(start), _turn.opposite());
        set(start, EMP);

        _subsetsInitialized = false;
        _winnerKnown = false;
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move recent = _moves.remove(movesMade() - 1);
        Square previousSq = recent.getFrom();
        Piece currPiece = get(recent.getTo());
        set(previousSq, currPiece);

        if (recent.isCapture()) {
            set(recent.getTo(), currPiece.opposite());
        } else {
            set(recent.getTo(), EMP);
        }
        _turn = _turn.opposite();

        _subsetsInitialized = false;
        _winnerKnown = false;
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        int pieceCt = countPieces(from, to);
        return from.distance(to) == pieceCt && !blocked(from, to);
    }

    /**
     * Count the pieces along the line of action.
     * Counts from both direction and then adds itself as a piece.
     * @param from      Starting square.
     * @param to        Destination or target square.
     * @return number of pieces
     */
    public int countPieces(Square from, Square to) {
        int pieces = 0;
        int dir = from.direction(to);
        for (int i = 1; i <= BOARD_SIZE; i += 1) {
            Square s = from.moveDest(dir, i);
            if (s != null) {
                Piece atSquare = get(s);
                if (atSquare == BP || atSquare == WP) {
                    pieces += 1;
                }
            } else {
                break;
            }
        }
        int opp = dir < 4 ? dir + 4 : dir - 4;
        for (int i = 1; i <= BOARD_SIZE; i += 1) {
            Square s = from.moveDest(opp, i);
            if (s != null) {
                Piece atSquare = get(s);
                if (atSquare == BP || atSquare == WP) {
                    pieces += 1;
                }
            } else {
                break;
            }
        }
        if (get(from) == BP || get(from) == WP) {
            pieces += 1;
        }
        return pieces;
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        List<Move> res = new ArrayList<>();
        for (Square a: ALL_SQUARES) {
            for (Square b: ALL_SQUARES) {
                try {
                    if (get(a) != EMP
                            && a != b && isLegal(a, b)) {
                        res.add(Move.mv(a, b));
                    }
                } catch (AssertionError e) {
                    continue;
                }
            }
        }
        return res;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            if (piecesContiguous(turn())
                    && piecesContiguous(turn().opposite())) {
                _winner = turn().opposite();
                _winnerKnown = true;
            } else if (piecesContiguous(turn())) {
                _winner = turn();
                _winnerKnown = true;
            } else if (piecesContiguous(turn().opposite())) {
                _winner = turn().opposite();
                _winnerKnown = true;
            } else if (!piecesContiguous(turn())
                    && !piecesContiguous(turn().opposite())
                    && movesMade() >= _moveLimit) {
                _winner = EMP;
                _winnerKnown = true;
            } else {
                _winner = null;
            }
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        if (!from.isValidMove(to)) {
            return true;
        }
        Piece toPiece = get(to);
        int dir = from.direction(to);
        for (int i = 1; i < from.distance(to); i += 1) {
            Square s = from.moveDest(dir, i);
            if (s != null) {
                Piece atSquare = get(s);
                if (atSquare == turn().opposite()) {
                    return true;
                }
            } else {
                break;
            }
        }
        return !(toPiece == turn().opposite() || toPiece == EMP);
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        if (p == EMP || _board[sq.index()] != p
                || visited[sq.row()][sq.col()]) {
            return 0;
        }
        visited[sq.row()][sq.col()] = true;
        int count = 1;
        for (Square s: sq.adjacent()) {
            count += numContig(s, visited, p);
        }
        return count;
    }
    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        Piece p = null;
        Square any = null;
        int sizeWP = 0, sizeBP = 0;
        for (Square s: ALL_SQUARES) {
            p = _board[s.index()];
            if (p != EMP) {
                any = s;
                if (p == BP) {
                    sizeBP = numContig(any, visited, p);
                    sizeWP = numContig(any, visited, p.opposite());
                    if (sizeBP > 0) {
                        _blackRegionSizes.add(sizeBP);
                    }
                    if (sizeWP > 0) {
                        _whiteRegionSizes.add(sizeWP);
                    }
                } else if (p == WP) {
                    sizeWP = numContig(any, visited, p);
                    sizeBP = numContig(any, visited, p.opposite());
                    if (sizeWP > 0) {
                        _whiteRegionSizes.add(sizeWP);
                    }
                    if (sizeBP > 0) {
                        _blackRegionSizes.add(sizeBP);
                    }
                }
            }
        }
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
