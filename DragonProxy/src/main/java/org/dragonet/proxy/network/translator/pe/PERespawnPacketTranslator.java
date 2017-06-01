package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.packetlib.packet.Packet;

import sul.protocol.pocket101.play.Respawn;

public class PERespawnPacketTranslator implements PEPacketTranslator<Respawn> {

	@Override
	public Packet[] translate(ClientConnection session, Respawn packet) {
		
		System.err.println("Respawning Client");
		ClientRequestPacket pack = new ClientRequestPacket(ClientRequest.RESPAWN);
		
		return new Packet[]{pack};
	}

}
