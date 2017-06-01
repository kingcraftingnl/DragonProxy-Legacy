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
package org.dragonet.proxy.utilities;

import com.github.steveice10.mc.protocol.MinecraftConstants;

public class Versioning {

    public final static int VERSION_INT = 007; //History: 0.0.6.01 (You can't have preR1 in an int!)
    public final static String RELEASE_VERSION = "0.0.7";

    public final static String MINECRAFT_PC_VERSION = MinecraftConstants.GAME_VERSION;
    public final static int MINECRAFT_PC_PROTOCOL = MinecraftConstants.PROTOCOL_VERSION;

    // This is STRICT to MCPE binary's definition, DO NOT CHANGE
    public final static String MINECRAFT_PE_VERSION = "1.0.9";
    
    public final static int MINECRAFT_PE_PROTOCOL = 107;

}
