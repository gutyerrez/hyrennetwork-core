/**
 * @author Gutyerrez
 */
object TestClass {

    @JvmStatic
    fun main(args: Array<String>) {
        val text = "org.bukkit.craftbukkit.v1_8_R3"

        val splits = text.split(".")

        println("Text: $text")

        splits.forEachIndexed { index, text ->
            println("$index - $text")
        }
    }

}