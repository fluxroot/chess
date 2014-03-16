package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 16.02.14 22:02
 */
public class KingAllowedMovesCastlingTest {
	@Test
	public void castlingIsAllowed() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );

		position.add( Side.WHITE, "h1", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e1", "g1" );
	}

	@Test
	public void castlingIsAllowedBlack() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e8", PieceType.KING );

		position.add( Side.BLACK, "h8", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "g8" );
	}

	@Test
	public void castlingQueenSide() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e8", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "c8" );
	}

	@Test
	public void rightToCastlingIsLostIfKingMoved() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		position.add( Side.BLACK, "a8", PieceType.KING );

		Position newPosition = position
			.move( "e1", "e2" )
			.move( "a8", "a7" ) //any valid black move
			.move( "e2", "e1" )
			.move( "a7", "a8" ); //any valid black move

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e1", "g1" );
	}

	@Test
	public void rightToCastlingNoInfluenceToOppositeSide() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "a8", PieceType.ROOK );

		Position newPosition = position
				.move( "e1", "g1" ); //castling

		PositionAsserts.assertAllowedMovesInclude(
				newPosition, "e8", "c8" );
	}

	@Test //not just one move lost
	public void rightToCastlingIsLostPermanently() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );
		position.add( Side.WHITE, "a2", PieceType.PAWN );

		position.add( Side.BLACK, "a8", PieceType.KING );

		Position newPosition = position
				.move( "e1", "e2" )
				.move( "a8", "a7" ) //any valid black move
				.move( "e2", "e1" )
				.move( "a7", "a8" ) //any valid black move
				.move( "a2", "a4" ) //non-king, non-rook related move
				.move( "a8", "a7" ); //any valid black move

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e1", "g1" );
	}

	@Test //not just one move lost
	public void castlingIsImpossibleAfterCastling() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );
		position.add( Side.WHITE, "a2", PieceType.PAWN );

		position.add( Side.BLACK, "a8", PieceType.KING );

		Position newPosition = position
				.move( "e1", "g1" ) //castle
				.move( "a8", "a7" ) //any valid black move
				.move( "f1", "f2" ) //rook movement started
				.move( "a7", "a8" ) //any valid black move
				.move( "f2", "h2" ) //regrouping rook to initial position
				.move( "a8", "a7" )
				.move( "h2", "h1" ) //rook is on the base
				.move( "a7", "a8" )
				.move( "g1", "f1" ) //regrouping king
				.move( "a8", "a7" ) //any valid black move
				.move( "f1", "e1" ) //king is on the base
				.move( "a7", "a8" );

		PositionAsserts.assertAllowedMovesOmit(
				newPosition, "e1", "g1" );
	}
}