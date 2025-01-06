import plugins.packages.elf.*
import utils.byte_order.LittleEndianThings

/*
import lexicalization.ETokenType
import lexicalization.Lexer
import native_code_generation.Generator
import parsing.Parser
import plugins.architectures.ArchX86_64
import plugins.packages.elf.PackerELF
import java.nio.file.Files
import java.nio.file.Paths
*/
fun main() {

    val ELF_HEADER_SIZE = 64
    val PROGRAM_HEADER_SIZE = 56
    val SECTION_HEADER_SIZE = 40

    val numProgramHeaders = 3
    val numSectionHeaders = 5

    val e_phoff = ELF_HEADER_SIZE + (PROGRAM_HEADER_SIZE * numProgramHeaders)
    val e_shoff = e_phoff + (PROGRAM_HEADER_SIZE * numProgramHeaders) + (SECTION_HEADER_SIZE * numSectionHeaders)

    val header = DELFHeader(
        clazz = EELFClass.ELF64,
        format_of_data = LittleEndianThings.ELF_ID,
        abi = EELFAbi.LINUX,
        type = EELFType.EXECUTABLE,
        machine = EELFMachine.X86,
        entry = PackerELF.code_start_point.toLong(),
        bias_to_ph = e_phoff.toLong(),
        bias_to_sh = e_shoff.toLong(),
        flags = 0,
        header_size = ELF_HEADER_SIZE.toShort(),
        phs_size = PROGRAM_HEADER_SIZE.toShort(),
        num_of_phs = numProgramHeaders.toShort(),
        sh_size = SECTION_HEADER_SIZE.toShort(),
        num_of_sections = numSectionHeaders.toShort(),
        index_of_section_with_string_and_names = 0
    )

    println(header.toByteArray())

    /*
    val currentDir = Paths.get("").toAbsolutePath()
    val filePath = currentDir.resolve("examples/projects/base.plmr.pasm")
    var source = ""
    Files.readAllLines(filePath).forEach { source += it + "\n" }

    println("--------------------TEXT--------------------")
    println(source)


    println("--------------------LEXER--------------------")
    val lex = Lexer(source)
    val tokens = lex.tokenize()

    tokens.forEach { line ->
        line.forEach {token ->
            if( token.type == ETokenType.IDENTIFIER || token.type == ETokenType.NUMBER ) {
                print("${token.value} ")
            }

            else {
                when (token.type) {
                    ETokenType.CMD_AND_ARGS_DIVIDER -> print(": ")
                    ETokenType.ARGS_DIVIDER -> print(",\t")
                    ETokenType.START_CODE_SPACE -> print("{")
                    ETokenType.END_CODE_SPACE -> print("}")
                    ETokenType.VAR_PREFIX -> print("%")
                    ETokenType.REG_PREFIX -> print("$")
                    ETokenType.CMD_MOVE -> print("MOVE")
                    ETokenType.CMD_SYSTEM_CALL -> print("SYSTEM_CALL")
                    ETokenType.KW_BLOCK -> print("BLOCK ")


                    else -> {}
                }
            }
        }
        print("\n")
    }

    println("--------------------PARSER--------------------")
    val prs = Parser(tokens)
    val parsed = prs.parse()

    println(parsed)

    println("--------------------CODE_GENERATOR--------------------")
    val current_arch = ArchX86_64()
    val current_packer = PackerELF()

    val code_gen = Generator(current_arch, current_packer, parsed)
    val compiled = code_gen.compile()

    println(current_packer.pack(compiled))

    */
}
