package plugins

abstract class APackerPlugin : IPlugin {
    abstract fun pack(executable_code: List<UInt>): List<UInt>
}