package net.hyren.core.shared.tests

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import java.net.InetSocketAddress

/**
 * @author Gutyerrez
 */
fun main() {
    CoreProvider.prepare(
        Application(
            "test-application",
            "Test Application",
            0,
            InetSocketAddress(
                "0.0.0.0",
                0
            ),
            ApplicationType.GENERIC
        )
    )
}