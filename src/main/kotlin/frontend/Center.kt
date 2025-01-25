package frontend

import lexicalization.DToken
import lexicalization.Lexer
import native_code_generation.Generator
import native_code_generation.helpers.AArchitecture
import packing.APacker
import parsing.Parser
import parsing.nodes.AASTNode

import utils.typing.EOperatingSystem

object Center {
    lateinit var CURRENT_ARCHITECTURE: AArchitecture
    lateinit var CURRENT_OS: EOperatingSystem
    lateinit var CURRENT_PACKER: APacker
    lateinit var TEXT: String
    private lateinit var TOKENS: List<List<DToken>>
    private lateinit var PARSED: List<AASTNode>
    private lateinit var NATIVE_CODE: List<UInt>
    private lateinit var PACKED_CODE: ByteArray

    fun fullCompile(): ByteArray {
        lexerize()
        parse()
        compile()
        pack()

        return PACKED_CODE
    }

    private fun lexerize() { TOKENS = Lexer(TEXT).tokenize() }
    private fun parse() { PARSED = Parser(TOKENS).parse() }
    private fun compile()  { NATIVE_CODE = Generator( CURRENT_ARCHITECTURE, PARSED ).compile() }
    private fun pack() {
        CURRENT_PACKER.setSettings(CURRENT_ARCHITECTURE, CURRENT_OS)
        PACKED_CODE = CURRENT_PACKER.pack(NATIVE_CODE)
    }
}