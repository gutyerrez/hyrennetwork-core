package net.hyren.core.shared.echo.packets.project

import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket

/**
 * @author Gutyerrez
 */
class ProjectSuccessBuildEchoPacket(
	var buildId: Int? = null,
	var elapsedTimeMillis: Long = 0
) : EchoPacket() {

	override fun write(
		buffer: EchoBufferOutput
	) {
		buffer.writeInt(buildId)
		buffer.writeLong(elapsedTimeMillis)
	}

	override fun read(
		buffer: EchoBufferInput
	) {
		buildId = buffer.readInt()
		elapsedTimeMillis = buffer.readLong()
	}

}