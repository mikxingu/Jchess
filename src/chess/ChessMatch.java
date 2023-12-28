package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	
	private List<ChessPiece> capturedPieces = new ArrayList<>();
	private List<ChessPiece> piecesOnBoard = new ArrayList<>() ;

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getTurn() {
		return turn;
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
		nextTurn();
		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position sourcePosition, Position targetPosition) {
		Piece p = board.removePiece(sourcePosition);
		Piece capturedPiece = board.removePiece(targetPosition);
		
		if (capturedPiece != null) {
			piecesOnBoard.remove(capturedPiece);
			capturedPieces.add((ChessPiece)capturedPiece);
		}

		board.placePiece(p, targetPosition);

		return capturedPiece;
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
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));	
	}

	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
}
