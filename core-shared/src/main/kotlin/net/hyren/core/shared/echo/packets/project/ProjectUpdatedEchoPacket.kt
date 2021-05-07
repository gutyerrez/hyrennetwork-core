package net.hyren.core.shared.echo.packets.project

import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket

/**
 * @author Gutyerrez
 */
class ProjectUpdatedEchoPacket(
	var name: String? = null,
	var description: String? = null,
	var commitId: String? = null,
	var commitParent: String? = null,
	var commitTimeMillis: Long = 0,
	var commitUser: String? = null,
	var commitDescription: String? = null
) : EchoPacket() {

	override fun write(
		buffer: EchoBufferOutput
	) {
		buffer.writeString(name)
		buffer.writeString(description)
		buffer.writeString(commitId)
		buffer.writeString(commitParent)
		buffer.writeLong(commitTimeMillis)
		buffer.writeString(commitUser)
		buffer.writeString(commitDescription)
	}

	override fun read(
		buffer: EchoBufferInput
	) {
		name = buffer.readString()
		description = buffer.readString()
		commitId = buffer.readString()
		commitParent = buffer.readString()
		commitTimeMillis = buffer.readLong()
		commitUser = buffer.readString()
		commitDescription = buffer.readString()
	}

}