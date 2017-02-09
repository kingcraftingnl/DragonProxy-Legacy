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

import org.dragonet.proxy.network.InventoryTranslatorRegister;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;

import net.marfgamer.jraknet.RakNetPacket;

public class PCOpenWindowPacketTranslator implements PCPacketTranslator<ServerOpenWindowPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerOpenWindowPacket packet) {
        DragonProxy.getSelf().getGeneralThreadPool().execute(() -> {
            InventoryTranslatorRegister.open(session, packet);
        });
        return null;
    }

}
