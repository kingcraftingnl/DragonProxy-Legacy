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
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;

import cn.nukkit.network.protocol.MoveEntityPacket;
import net.marfgamer.jraknet.RakNetPacket;

public class PCEntityPositionPacketTranslator implements PCPacketTranslator<ServerEntityPositionPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerEntityPositionPacket packet) {
        CachedEntity e = session.getEntityCache().get(packet.getEntityId());
        if (e == null) {
            return new RakNetPacket[0];
        }

        e.relativeMove(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ());

        MoveEntityPacket pk = new MoveEntityPacket();
        pk.eid = e.eid;
        pk.yaw = (byte) e.yaw;
        pk.headYaw = (byte) e.yaw;
        pk.pitch = (byte) e.pitch;
        pk.x = (float) e.x;  
        pk.y = (float) (e.player ? e.y + 1.62f : e.y);
        pk.z = (float) e.z;
        
        return fromSulPackets(pk);
    }

}
