package com.redefantasy.core.shared.echo.packets.project

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket

/**
 * @author Gutyerrez
 */
class ProjectStartedBuildEchoPacket(
	var buildId: Int? = null,
	var startedBuildTimeMillis: Long = System.currentTimeMillis()
) : EchoPacket() {

	override fun write(
		buffer: EchoBufferOutput
	) {
		buffer.writeInt(buildId)
		buffer.writeLong(startedBuildTimeMillis)
	}

	override fun read(
		buffer: EchoBufferInput
	) {
		buildId = buffer.readInt()
		startedBuildTimeMillis = buffer.readLong()
	}

}