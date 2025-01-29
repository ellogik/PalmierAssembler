package frontend

import lexicalization.DToken
import lexicalization.Lexer
import native_code_generation.Generator
import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.AOperatingSystem
import packing.APacker
import parsing.Parser
import parsing.nodes.AASTNode

object Center {
    lateinit var CURRENT_ARCHITECTURE: AArchitecture
    lateinit var CURRENT_OS: AOperatingSystem
    lateinit var CURRENT_PACKER: APacker
    var CURRENT_ENTRY: Number = 0
    lateinit var TEXT: String
    private lateinit var TOKENS: List<List<DToken>>
    private lateinit var PARSED: List<AASTNode>
    private lateinit var NATIVE_CODE: List<UInt>
    private lateinit var PACKED_CODE: ByteArray
    private lateinit var BLOCKS: Map<String, Pair<Number, Number>>

    fun fullCompile(): ByteArray {
        lexerize()
        parse()
        compile()
        compileData()
        pack()

        return PACKED_CODE
    }

    private fun lexerize() { TOKENS = Lexer(TEXT).tokenize() }
    private fun parse() { PARSED = Parser(TOKENS).parse(); println(PARSED) }
    private fun compileData() { CURRENT_PACKER.registerBlocks( BLOCKS ) }
    private fun compile()  {
        Generator( CURRENT_ARCHITECTURE, PARSED, CURRENT_PACKER ).let {
            NATIVE_CODE = it.compile()
            BLOCKS = it.blocks
        }
    }
    private fun pack() { PACKED_CODE = CURRENT_PACKER.packCode(NATIVE_CODE) }
}