import native_code_generation.helpers.architectures.ArchX86_64
import lexicalization.ETokenType
import lexicalization.Lexer
import native_code_generation.Generator
import parsing.Parser
import utils.typing.EAppType
import utils.typing.EOperatingSystem
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
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
    val current_arch = ArchX86_64
//    val current_packer = PackerELF
    val current_os = EOperatingSystem.LINUX

    val code_gen = Generator(current_arch, parsed)
    val compiled = code_gen.compile()
    println(compiled)

    println("--------------------ELF_TEST--------------------")
//    current_packer.setSettings(current_arch, current_os, EAppType.EXECUTABLE)
//    current_packer.pack(compiled)
}