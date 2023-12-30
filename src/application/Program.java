package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		ChessMatch match = new ChessMatch();
		List<ChessPiece> capturedPieces = new ArrayList<>();

		while (!match.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(match, capturedPieces);
				System.out.println();
				System.out.println("Source: ");
				ChessPosition source = UI.readChessPosition(sc);

				boolean[][] possibleMoves = match.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(match.getPieces(), possibleMoves);

				System.out.println();
				System.out.println("Target: ");
				ChessPosition target = UI.readChessPosition(sc);

				ChessPiece capturedPiece = match.performChessMove(source, target);

				if (capturedPiece != null) {
					capturedPieces.add(capturedPiece);
				}

				if (match.getPromoted() != null) {
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String type = sc.nextLine().toUpperCase();
					while (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
						System.out.print("Invalid Value! Enter piece for promotion (B/N/R/Q): ");
						type = sc.nextLine().toUpperCase();
					}
					match.replacePromotedChessPiece(type);
				}

			} catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}

		UI.clearScreen();

		UI.printMatch(match, capturedPieces);
	}
}