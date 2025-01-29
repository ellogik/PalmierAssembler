package packing

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.AOperatingSystem
import parsing.nodes.AASTNode

abstract class APacker {
    open var ENTRY: Number = 0

    abstract fun setSettings(arch: AArchitecture, os: AOperatingSystem)
    abstract fun packCode(executable_code: List<UInt>): ByteArray
    abstract fun packVariables(target: List<AASTNode>): Map<String, Long>
    abstract fun registerBlocks(target: Map<String, Pair<Number, Number>>)
}