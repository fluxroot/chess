package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Author: Leonid
 * Date-time: 19.08.12 18:16
 */
public class WinBoardPlayerTest {
	private static final int PROTOCOL_VERSION = 2; //any??

	//this test should emulate WinBoard behaviour and analyze our reaction on it.
	//in theory in future we could extract some Winboard emulator

	@Test
	public void creationSwitchesToInitMode() {
		WinboardCommander commander = mock( WinboardCommander.class );

		//implicit call of startInit
		new WinboardPlayer( commander );

		//it really checks only 1 method call
		verify( commander ).startInit();
	}

	//ensure need of refactoring into commander instead of communicator
	@Test( timeout = 5000 )
	public void useCommanderForQuitCommand() {
		Communicator quitCommunicator = MockCommunicatorReceiveCreator.getReceiveCommunicator( "quit" );

		WinboardCommander commander = new WinboardCommanderImpl( quitCommunicator );

		WinboardPlayer controller = new WinboardPlayer(
				commander );

		controller.run();
	}

	//ensure need of refactoring into commander instead of communicator
	@Test( timeout = 5000 )
	public void useCommanderForQuitCommandRealTest() {
		//dummy implementation - each time anybody sets a protover listener -
		//we quit IMMEDIATELY
		WinboardCommander commander = new MockCommander() {
			@Override
			public void setQuitListener( final QuitListener quitListener ) {
				quitListener.execute();
			}
		};

		WinboardPlayer controller = new WinboardPlayer(	commander );

		controller.run();
	}

	@Test
	public void correctProtoverReaction() {
		//dummy implementation - each time anybody sets a protover listener -
		//we call it IMMEDIATELY
		MockCommander commander = new MockCommander() {
			@Override
			public void setProtoverListener( ProtoverListener protoverListener ) {
				protoverListener.execute( PROTOCOL_VERSION );
			}
		};

		new WinboardPlayer(	commander );

		//the player must have set its listeners in constructor...

		//TODO: this doesn't check the commands order...
		assertEquals( 1, commander.getFinishInitCallsCount() );
		assertEquals( 1, commander.getEnableUserMovePrefixesCount() );
	}
}