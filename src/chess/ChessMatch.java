package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		return (ChessPiece)capturedPiece;
	}

	private Piece makeMove(Position sourcePosition, Position targetPosition) {
		Piece p = board.removePiece(sourcePosition);
		Piece capturedPiece = board.removePiece(targetPosition);
		
		board.placePiece(p, targetPosition);
		
		return capturedPiece;
	}
	
	
	private void validateSourcePosition(Position position) {
		if (!board.hasPiece(position)) {
			throw new ChessException("There is no piece on source position.");
		}
		
		if (!board.piece(position).isAnyMovePossible()){
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
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getColumns()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				//FORCA UM DOWNCASTING PARA CHESSPIECE, POIS A PARTIDA SO DEVE CONHECER CHESSPIECE E NAO BOARDGAME PIECE
				matrix[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return matrix;
	}
	
	
	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));
	}
}
