/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.adapter;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.PacketTranslatorRegister;
import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.ClientListener;
import com.github.steveice10.mc.protocol.MinecraftConstants;
//import org.dragonet.proxy.network.PacketTranslatorRegister;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSuccessPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSentEvent;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

/**
 *
 * @author robotman3000
 */
public class MCPCServerProtocolAdapter extends ClientListener implements ServerProtocolAdapter<Packet> {

    @Getter
    @Setter
    private MinecraftProtocol protocol;

    private Client client;
    private ClientConnection upstream;
    
    private final String sender = "[PC Serverside] ";

    public MCPCServerProtocolAdapter() {
    }

    @Override
    public void connectToRemoteServer(String address, int port) {
        DragonProxy.getLogger().info(sender + "Connecting to remote pc server at: " + address + ":" + port);

        // TODO: Handle authentication
        protocol = new MinecraftProtocol("robotman3002");

        client = new Client(address, port, protocol, new TcpSessionFactory());
        client.getSession().addListener(this);
        client.getSession().connect(true);
    }

    @Override
    public void setClient(ClientConnection session) {
        this.upstream = session;
    }

    @Override
    public void sendPacket(Packet packet) {
        DragonProxy.getLogger().debug(sender + "Server Sending Packet: " + packet.getClass().getCanonicalName());
        client.getSession().send(packet);
    }

    @Override
    public void handlePacket(Packet packet, ClientConnection identifier) {
        DragonProxy.getLogger().debug(sender + "Server Handling Packet: " + packet.getClass().getCanonicalName());

        if (packet.getClass().getCanonicalName().contains("KeepAlive")) {
            DragonProxy.getLogger().debug(sender + "Server Ignoring KeepAlive");
            return;
        }

        Object[] packets = {packet};
        if (upstream.getUpstreamProtocol().getSupportedPacketType() != getSupportedPacketType()) {
            packets = PacketTranslatorRegister.translateToPE(upstream, packet);
        }

        for (Object pack : packets) {
            upstream.getUpstreamProtocol().sendPacket(pack, identifier);
        }
    }

    @Override
    public void disconnectFromRemoteServer(String reason) {
        client.getSession().disconnect(reason);
    }

    @Override
    public void connected(ConnectedEvent event) {
        DragonProxy.getLogger().info(sender + "Connected to remote server " + event.getSession().getHost());
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        DragonProxy.getLogger().info(sender + "Disconected " + event.getSession().getLocalAddress() + " from remote server " + event.getSession().getHost() + " for " + event.getReason());
        event.getCause().printStackTrace();
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        DragonProxy.getLogger().info(sender + "Disconecting " + event.getSession().getLocalAddress() + " from remote server " + event.getSession().getHost() + " for " + event.getReason());
        event.getCause().printStackTrace();
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        DragonProxy.getLogger().debug(sender + "Received packet from server: " + event.getPacket().getClass().getCanonicalName());
        if (((MinecraftProtocol) event.getSession().getPacketProtocol()).getSubProtocol() == SubProtocol.GAME || event.getClass().isAssignableFrom(ServerJoinGamePacket.class) || event.getPacket() instanceof LoginSuccessPacket) {
            handlePacket(event.getPacket(), DragonProxy.getSelf().getNetwork().getSessionRegister().getSession(upstream.getSessionID()));
        }
    }

    @Override
    public void packetSent(PacketSentEvent event) {
        DragonProxy.getLogger().debug(sender + "Sent packet to server: " + event.getPacket().getClass().getCanonicalName());
    }

    @Override
    public Class<Packet> getSupportedPacketType() {
        return Packet.class;
    }

    /*public void connect(String addr, int port) {
        if (this.protocol == null) {
            upstream.onConnected(); // Clear the flags
            upstream.disconnect("ERROR! ");
            return;
        }
        remoteClient = new Client(addr, port, protocol, new TcpSessionFactory());
        remoteClient.getSession().addListener(new SessionAdapter() {
            @Override
            public void connected(ConnectedEvent event) {
                proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_REMOTE_CONNECTED, upstream.getUsername(), upstream.getRemoteAddress()));
                upstream.onConnected();
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
            	DragonProxy.getLogger().info("Disconnected Client " + event.getSession().getHost() + ":" + event.getSession().getPort() + " for reason " + event.getReason());
            	event.getCause().printStackTrace();
            	Thread.dumpStack();
                upstream.disconnect(proxy.getLang().get(event.getReason()));
            }

            @Override
            public void packetReceived(PacketReceivedEvent event) {
                //Handle the packet
                try {
                    DataPacket[] packets = PacketTranslatorRegister.translateToPE(upstream, event.getPacket());
                    if (packets == null || packets.length <= 0) {
                        return;
                    }
                    if (packets.length == 1) {
                        upstream.sendPacket(packets[0]);
                    } else {
                        upstream.sendAllPackets(packets, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            
            @Override
            public void packetSent(PacketSentEvent event) {
            	DragonProxy.getLogger().info("PC Sending Packet: " + event.getPacket().toString());
            }
            
            @Override
            public void disconnecting(DisconnectingEvent event) {
            	DragonProxy.getLogger().info("Disconnecting Client " + event.getSession().getHost() + ":" + event.getSession().getPort() + " for reason " + event.getReason());
            	event.getCause().printStackTrace();
            	Thread.dumpStack();
            }
        });
        
        try {
        	remoteClient.getSession().connect();
        } catch (Exception e){
        	e.printStackTrace();
        }
    }*/
}
