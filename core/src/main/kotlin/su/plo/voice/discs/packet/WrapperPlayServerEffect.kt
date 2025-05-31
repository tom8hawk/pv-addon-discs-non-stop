package su.plo.voice.discs.packet

import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.util.Vector3i
import com.github.retrooper.packetevents.wrapper.PacketWrapper

class WrapperPlayServerEffect(
    event: PacketSendEvent
) : PacketWrapper<WrapperPlayServerEffect>(event) {
    var event: Int = 0
        private set
    lateinit var position: Vector3i
        private set
    var data: Int = 0
        private set
    var disableRelativeVolume: Boolean = false
        private set

    override fun read() {
        event = readInt()
        position = readBlockPosition()
        data = readInt()
        disableRelativeVolume = readBoolean()
    }

    override fun write() {
        writeInt(event)
        writeBlockPosition(position)
        writeInt(data)
        writeBoolean(disableRelativeVolume)
    }
}
