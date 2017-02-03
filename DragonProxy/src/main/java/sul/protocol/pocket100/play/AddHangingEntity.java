/*
 * This file was automatically generated by sel-utils and
 * released under the GNU General Public License version 3.
 *
 * License: https://github.com/sel-project/sel-utils/blob/master/LICENSE
 * Repository: https://github.com/sel-project/sel-utils
 * Generated from https://github.com/sel-project/sel-utils/blob/master/xml/protocol/pocket100.xml
 */
package sul.protocol.pocket100.play;

import sul.utils.*;

public class AddHangingEntity extends Packet {

	public static final byte ID = (byte)17;

	public static final boolean CLIENTBOUND = true;
	public static final boolean SERVERBOUND = false;

	public long entityId;
	public long runtimeId;
	public sul.protocol.pocket100.types.BlockPosition position;
	public int unknown3;

	public AddHangingEntity() {}

	public AddHangingEntity(long entityId, long runtimeId, sul.protocol.pocket100.types.BlockPosition position, int unknown3) {
		this.entityId = entityId;
		this.runtimeId = runtimeId;
		this.position = position;
		this.unknown3 = unknown3;
	}

	@Override
	public int length() {
		return Buffer.varlongLength(entityId) + Buffer.varlongLength(runtimeId) + position.length() + Buffer.varintLength(unknown3) + 1;
	}

	@Override
	public byte[] encode() {
		this._buffer = new byte[this.length()];
		this.writeBigEndianByte(ID);
		this.writeVarlong(entityId);
		this.writeVarlong(runtimeId);
		this.writeBytes(position.encode());
		this.writeVarint(unknown3);
		return this._buffer;
	}

	@Override
	public void decode(byte[] buffer) {
		this._buffer = buffer;
		readBigEndianByte();
		entityId=this.readVarlong();
		runtimeId=this.readVarlong();
		position=new sul.protocol.pocket100.types.BlockPosition(); position._index=this._index; position.decode(this._buffer); this._index=position._index;
		unknown3=this.readVarint();
	}

	public static AddHangingEntity fromBuffer(byte[] buffer) {
		AddHangingEntity ret = new AddHangingEntity();
		ret.decode(buffer);
		return ret;
	}

}
