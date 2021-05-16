package net.hyren.core.shared.tests

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.misc.json.KJson
import java.net.InetSocketAddress

/**
 * @author Gutyerrez
 */
fun main() {
    val preference = ApplicationStatus(
        "test-application",
        ApplicationType.GENERIC,
        null,
        InetSocketAddress(
            "0.0.0.0",
            0
        ),
        System.currentTimeMillis()
    )

    val json = KJson.encodeToString(preference)

    println(json)

    val decoded = KJson.decodeFromString<ApplicationStatus>(json)

    println(decoded)
}