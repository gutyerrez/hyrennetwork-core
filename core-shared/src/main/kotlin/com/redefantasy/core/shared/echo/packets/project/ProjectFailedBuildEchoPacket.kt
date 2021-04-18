package com.redefantasy.core.shared.echo.packets.project

import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.packets.EchoPacket

/**
 * @author Gutyerrez
 */
class ProjectFailedBuildEchoPacket(
	var buildId: Int? = null,
	var output: String? = null
) : EchoPacket() {

	override fun write(
		buffer: EchoBufferOutput
	) {
		buffer.writeInt(buildId)
		buffer.writeString(output)
	}

	override fun read(
		buffer: EchoBufferInput
	) {
		buildId = buffer.readInt()
		output = buffer.readString()
	}

}