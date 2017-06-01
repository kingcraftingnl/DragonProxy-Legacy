package org.dragonet.proxy.network.translator;

import org.dragonet.proxy.network.ClientConnection;
import com.github.steveice10.packetlib.packet.Packet;

import net.marfgamer.jraknet.RakNetPacket;

public class IgnorePacketTranslator implements PEPacketTranslator, PCPacketTranslator {

    @Override
    public RakNetPacket[] translate(ClientConnection session, Packet packet) {
        System.err.println("[PC to PE] Ignoring packet: " + packet.getClass().getCanonicalName());
        return new RakNetPacket[0];
    }

    @Override
    public Packet[] translate(ClientConnection session, sul.utils.Packet packet) {
        System.err.println("[PE to PC] Ignoring packet: " + packet.getClass().getCanonicalName());
        return new Packet[0];
    }

}
