package plugins.packages.elf

import plugins.AArchitecturePlugin
import utils.byte_order.EByteOrder
import utils.errors.DInvalidArgumentError
import utils.typing.EAppType
import utils.typing.EArchitecture
import utils.typing.EBitType
import utils.typing.EOperatingSystem
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class DELFHeader(
    val clazz: Byte,
    val format_of_data: Byte,
    val abi: Byte,
    val type: Short,
    val machine: Short,
    val entry: Long,
    val bias_to_ph: Long,
    val bias_to_sh: Long,
    val flags: Int,
    val header_size: Short,
    val phs_size: Short,
    val num_of_phs: Short,
    val sh_size: Short,
    val num_of_sections: Short,
    val index_of_section_with_string_and_names: Short
) {
    companion object {
        val ELF_MAGIC: ByteArray = byteArrayOf(0x7F, 0x45, 0x4C, 0x46) // Magic number
        const val VERSION: Byte = 0x01 // ELF version

        fun fromStuff(arch: AArchitecturePlugin, os: EOperatingSystem, app_type: EAppType): DELFHeader {
            if( !os.isSupportELF() ) throw DInvalidArgumentError("Unable to generate ELF header for '$os' OS")


            return DELFHeader(
                clazz = arch.BITS_TYPE.toELFArch(),
                format_of_data = arch.BYTE_ORDER.toELF(),
                abi = os.toELFAbi(),
                type = app_type.toELF(),
                machine = arch.FOR_ARCH.toELF(),
                entry = arch.
            )
        }
    }

    // Function for listing header
    fun toByteArray(): List<Byte> {
        var byte_order = ByteOrder.LITTLE_ENDIAN
        val byte: Byte = 0x01 // little endian
        if (format_of_data != byte) byte_order = ByteOrder.BIG_ENDIAN

        val buffer = ByteBuffer.allocate(64).order(byte_order)

        // e_ident
        buffer.put(ELF_MAGIC) // Magic
        buffer.put(clazz) // Class (64 or 32-bit)
        buffer.put(format_of_data) // Format of Data
        buffer.put(VERSION) // Version
        buffer.put(abi) // OS/ABI
        buffer.put(ByteArray(7)) // Filling

        // Main
        buffer.putShort(type) // Type
        buffer.putShort(machine.toShort()) // Machine Type
        buffer.putInt(1) // ELF Version
        buffer.putLong(entry) // Entry point
        buffer.putLong(bias_to_ph) // Bias to PH(not PH what you think, just a Program Header)
        buffer.putLong(bias_to_sh) // Bias to headers of sections
        buffer.putInt(flags) // Flags
        buffer.putShort(header_size) // Size of ELF Header
        buffer.putShort(phs_size) // Size of PHs
        buffer.putShort(num_of_phs) // Num of PHs
        buffer.putShort(sh_size) // Size of sections headers
        buffer.putShort(num_of_sections) // Num of sections
        buffer.putShort(index_of_section_with_string_and_names) // Index of section with strings and names

        return buffer.array().toList()
    }
}

fun EBitType.toELFArch(): Byte {
    return when(this) {
        EBitType.X32 -> 0x01
        EBitType.X64 -> 0x02

        else -> throw DInvalidArgumentError("'$this' is not supports ELF format")
    }
}

fun EOperatingSystem.toELFAbi(): Byte {
    return when(this){
        EOperatingSystem.LINUX -> 0x03
        else -> throw DInvalidArgumentError("ELF is not supports ABI for '$this' operating system")
    }
}

fun EByteOrder.toELF(): Byte{
    return when(this){
        EByteOrder.LITTLE_ENDIAN -> 0x01
        EByteOrder.BIG_ENDIAN -> 0x02
    }
}

fun EAppType.toELF(): Short {
    return when(this){
        EAppType.SHARED_LIBRARY -> 3
        EAppType.EXECUTABLE -> 2
        EAppType.STATIC_LIBRARY -> 1
    }
}

fun EArchitecture.toELF(): Short {
    return when(this){
        EArchitecture.X86_64 -> 0x03
        EArchitecture.RISC_V -> 0xF3
        EArchitecture.ARM64 -> 0xB7

        else -> throw DInvalidArgumentError("")
    }
}
