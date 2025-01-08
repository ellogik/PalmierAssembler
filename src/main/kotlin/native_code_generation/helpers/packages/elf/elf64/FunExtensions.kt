package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.architectures.ArchX86_64
import utils.byte_order.EByteOrder
import utils.errors.DInvalidArgumentError
import utils.typing.EOperatingSystem

fun EOperatingSystem.isSupportELF(): Boolean =  when (this){
    EOperatingSystem.LINUX -> true
    else -> false
}

fun EByteOrder.toELF(): Byte = when(this) {
    EByteOrder.LITTLE_ENDIAN -> 1
    EByteOrder.BIG_ENDIAN -> 2
}

fun EOperatingSystem.toELFAbi(): Byte = when(this) {
    EOperatingSystem.LINUX -> 3
    else -> throw DInvalidArgumentError("$this not supported ELF")
}

fun AArchitecture.toELF(): Short = when(this) {
    is ArchX86_64 -> 0x3E // x86-64
    else -> throw DInvalidArgumentError("ELF or PASM not supported '$this' architecture")
}