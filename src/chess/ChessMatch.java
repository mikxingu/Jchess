package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;

	private List<ChessPiece> capturedPieces = new ArrayList<>();
	private List<ChessPiece> piecesOnBoard = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);

		return board.piece(position).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();

		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check!");
		}

		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}
		
		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position sourcePosition, Position targetPosition) {
		ChessPiece p = (ChessPiece)board.removePiece(sourcePosition);
		p.increaseMoveCount();
		
		Piece capturedPiece = board.removePiece(targetPosition);

		if (capturedPiece != null) {
			piecesOnBoard.remove(capturedPiece);
			capturedPieces.add((ChessPiece) capturedPiece);
		}

		board.placePiece(p, targetPosition);

		return capturedPiece;
	}

	private void undoMove(Position sourcePosition, Position targetPosition, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(targetPosition);
		p.decreaseMoveCount();
		
		board.placePiece(p, sourcePosition);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, targetPosition);
			capturedPieces.remove(capturedPiece);
			piecesOnBoard.add((ChessPiece) capturedPiece);
		}
	}

	private void validateSourcePosition(Position position) {
		if (!board.hasPiece(position)) {
			throw new ChessException("There is no piece on source position.");
		}

		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours!");
		}

		if (!board.piece(position).isAnyMovePossible()) {
			throw new ChessException("There are no possible moves for the chosen piece.");
		}

	}

	private void validateTargetPosition(Position sourcePosition, Position targetPosition) {
		if (!board.piece(sourcePosition).possibleMove(targetPosition)) {
			throw new ChessException("The chosen piece cannot move to target position!");
		}
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnBoard.add(piece);
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getColumns()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				// FORCA UM DOWNCASTING PARA CHESSPIECE, POIS A PARTIDA SO DEVE CONHECER
				// CHESSPIECE E NAO BOARDGAME PIECE
				matrix[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return matrix;
	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE));
		

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
	}

	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<Piece> pieces = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());

		for (Piece p : pieces) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board!");
	}

	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();

		List<Piece> opponentPieces = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color))
				.collect(Collectors.toList());

		for (Piece p : opponentPieces) {
			boolean[][] matrix = p.possibleMoves();

			if (matrix[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}

		List<Piece> pieces = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());

		for (Piece p : pieces) {
			boolean matrix[][] = p.possibleMoves();

			// RUNS THE PIECES OF A GIVEN TEAM AND CHECKS IF ANY PIECE HAS A MOVEMENT TO
			// CANCEL THE CHECKMATE
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (matrix[i][j]) {
						Position sourcePosition = ((ChessPiece) p).getChessPosition().toPosition();
						Position targetPosition = new Position(i, j);

						Piece capturedPiece = makeMove(sourcePosition, targetPosition);
						boolean testCheck = testCheck(color);
						undoMove(sourcePosition, targetPosition, capturedPiece);

						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
}
