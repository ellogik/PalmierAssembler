package frontend

import com.github.lalyos.jfiglet.FigletFont
import native_code_generation.helpers.architectures.ArchX86_64
import native_code_generation.helpers.operating_systems.OsLinux
import packing.APacker
import packing.elf.elf64.PackerELF64
import utils.errors.DInvalidArgumentError
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object UserInterface {
    private val CURRENT_DIRECTORY: Path = Paths.get("").toAbsolutePath()
    private var SOURCE: String = ""
    private const val APP_VERSION = "bohrium(0.0.4)"

    fun start(args: Array<String>) {
        val file_path = CURRENT_DIRECTORY.resolve(args[0])
        Files.readAllLines(file_path).forEach { SOURCE += it + "\n" }

        Center.TEXT = SOURCE
        Center.CURRENT_PACKER = fromStringToPacker(args[1])
        Center.CURRENT_OS = OsLinux
        Center.CURRENT_ARCHITECTURE = ArchX86_64
        Center.CURRENT_PACKER.setSettings(Center.CURRENT_ARCHITECTURE, Center.CURRENT_OS)
        Center.CURRENT_ENTRY = Center.CURRENT_PACKER.ENTRY

        println(FigletFont.convertOneLine("P a l m i e r A s s e m b l e r"))
        println("\u001b[1m[SELF]\u001B[0m PASM version is \u001b[4m\u001B[34m'$APP_VERSION'\n\u001b[0m")
        println("\u001b[1m[PROCESS]\u001B[0m Processing file \u001b[4m\u001B[32m'$file_path'\u001B[0m...")

        Files.write(Paths.get(
            file_path.fileName.toString() + ".compiled"),
            Center.fullCompile()
        )

        println("\u001b[1m[PROCESS]\u001B[0m Done! Your static library is \u001b[4m\u001b[32m'${file_path.fileName.toString() + ".compiled"}'\u001b[0m")
    }

    private fun fromStringToPacker(string: String): APacker = when(string) {
        "elf64" -> PackerELF64

        else -> throw DInvalidArgumentError("Unknown Packer:  $string")
    }
}