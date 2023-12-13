package chess;

import boardgame.Board;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
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
}
