package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Bishop extends ChessPiece{

	public Bishop(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "B";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position p = new Position(0, 0);

		// look nw
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().hasPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;

			p.setValues(p.getRow() -1 , p.getColumn() - 1);
		}

		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}

		// look ne
		p.setValues(position.getRow() - 1, position.getColumn() + 1);

		while (getBoard().positionExists(p) && !getBoard().hasPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;

			p.setValues(p.getRow() - 1, p.getColumn() + 1);
		}

		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}

		// look sw
		p.setValues(position.getRow() + 1, position.getColumn() + 1);

		while (getBoard().positionExists(p) && !getBoard().hasPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;

			p.setValues(p.getRow() + 1, p.getColumn() + 1);
		}

		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}

		// look se
		p.setValues(position.getRow() + 1, position.getColumn() - 1);

		while (getBoard().positionExists(p) && !getBoard().hasPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;

			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}

		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
		}

		return matrix;
	}
}
