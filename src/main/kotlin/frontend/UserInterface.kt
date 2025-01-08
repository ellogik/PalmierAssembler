package frontend

import native_code_generation.helpers.architectures.ArchX86_64
import native_code_generation.helpers.packages.elf.elf64.PackerELF64
import utils.typing.EOperatingSystem
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object UserInterface {
    private val CURRENT_DIRECTORY: Path = Paths.get("").toAbsolutePath()
    private var SOURCE: String = ""

    fun start(args: Array<String>) {
        val file_path = CURRENT_DIRECTORY.resolve(args[0])
        Files.readAllLines(file_path).forEach { SOURCE += it + "\n" }


        Center.TEXT = SOURCE
        Center.CURRENT_PACKER = PackerELF64
        Center.CURRENT_OS = EOperatingSystem.LINUX
        Center.CURRENT_ARCHITECTURE = ArchX86_64

        Files.write(Paths.get(
            file_path.fileName.toString() + ".compiled"),
            Center.fullCompile()
        )
    }
}