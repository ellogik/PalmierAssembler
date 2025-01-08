import native_code_generation.helpers.architectures.ArchX86_64
import native_code_generation.helpers.packages.elf.elf64.DELF64Header
import utils.typing.EOperatingSystem
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    //UserInterface.start(args)

    Files.write(Paths.get("1.elf"), DELF64Header.fromStuff(ArchX86_64, EOperatingSystem.LINUX).toByteArray())
}