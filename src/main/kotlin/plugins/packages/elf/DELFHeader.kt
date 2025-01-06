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
    val clazz: UByte,
    val format_of_data: UByte,
    val abi: UByte,
    val type: UShort,
    val machine: UShort,
    val entry: ULong,
    val bias_to_ph: ULong,
    val bias_to_sh: ULong,
    val flags: UInt,
    val header_size: UShort,
    val phs_size: UShort,
    val num_of_phs: UShort,
    val sh_size: UShort,
    val num_of_sections: UShort,
    val index_of_section_with_string_and_names: UShort
) {
    companion object {
        val ELF_MAGIC: ByteArray = byteArrayOf(0x7F, 0x45, 0x4C, 0x46) // Magic number
        const val VERSION: Byte = 0x01 // ELF version

        fun fromStuff(arch: AArchitecturePlugin, os: EOperatingSystem, app_type: EAppType): DELFHeader {
            if( !os.isSupportELF() ) throw DInvalidArgumentError("OS '$os' not supports ELF")
            if( arch.ELF_REQUIREMENTS == null ) throw DInvalidArgumentError("${arch.FOR_ARCH} is not implements ELF things")

            val b_to_ph = (arch.ELF_REQUIREMENTS!!.HEADER_SIZE + (arch.ELF_REQUIREMENTS!!.PROGRAM_HEADER_SIZE * PackerELF.NUM_PROGRAM_HEADERS)).toULong()
            val b_to_sh = b_to_ph +
                    (arch.ELF_REQUIREMENTS!!.PROGRAM_HEADER_SIZE * PackerELF.NUM_PROGRAM_HEADERS) +
                    (arch.ELF_REQUIREMENTS!!.SECTION_HEADER_SIZE * PackerELF.NUM_SECTION_HEADERS)

            return DELFHeader(
                clazz = arch.BITS_TYPE.toELFArch(),
                format_of_data = arch.BYTE_ORDER.toELF(),
                abi = os.toELFAbi(),
                type = app_type.toELF(),
                machine = arch.FOR_ARCH.toELF(),
                entry = arch.ELF_REQUIREMENTS!!.ENTRY,
                bias_to_ph = b_to_ph,
                bias_to_sh = b_to_sh,
                flags = 0u,
                header_size = arch.ELF_REQUIREMENTS!!.HEADER_SIZE,
                phs_size = arch.ELF_REQUIREMENTS!!.PROGRAM_HEADER_SIZE,
                sh_size = arch.ELF_REQUIREMENTS!!.SECTION_HEADER_SIZE,
                num_of_sections = PackerELF.NUM_SECTION_HEADERS,
                num_of_phs = PackerELF.NUM_PROGRAM_HEADERS,
                index_of_section_with_string_and_names = 0u
            )
        }
    }

    // Function for listing header
    fun toByteArray(): List<Byte> {
        var byte_order = ByteOrder.LITTLE_ENDIAN
        val byte: UByte = 0x01u // little endian
        if (format_of_data != byte) byte_order = ByteOrder.BIG_ENDIAN

        val buffer = ByteBuffer.allocate(64).order(byte_order)

        // e_ident
        buffer.put(ELF_MAGIC) // Magic
        buffer.put(clazz.toByte()) // Class (64 or 32-bit)
        buffer.put(format_of_data.toByte()) // Format of Data
        buffer.put(VERSION) // Version
        buffer.put(abi.toByte()) // OS/ABI
        buffer.put(ByteArray(7)) // Filling

        // Main
        buffer.putShort(type.toShort()) // Type
        buffer.putShort(machine.toShort()) // Machine Type
        buffer.putInt(1) // ELF Version
        buffer.putLong(entry.toLong()) // Entry point
        buffer.putLong(bias_to_ph.toLong()) // Bias to PH(not PH what you think, just a Program Header)
        buffer.putLong(bias_to_sh.toLong()) // Bias to headers of sections
        buffer.putInt(flags.toInt()) // Flags
        buffer.putShort(header_size.toShort()) // Size of ELF Header
        buffer.putShort(phs_size.toShort()) // Size of PHs
        buffer.putShort(num_of_phs.toShort()) // Num of PHs
        buffer.putShort(sh_size.toShort()) // Size of sections headers
        buffer.putShort(num_of_sections.toShort()) // Num of sections
        buffer.putShort(index_of_section_with_string_and_names.toShort()) // Index of section with strings and names

        return buffer.array().toList()
    }
}

fun EBitType.toELFArch(): UByte {
    return when(this) {
        EBitType.X32 -> 0x01u
        EBitType.X64 -> 0x02u

        else -> throw DInvalidArgumentError("'$this' is not supports ELF format")
    }
}

fun EOperatingSystem.toELFAbi(): UByte {
    return when(this){
        EOperatingSystem.LINUX -> 0x03u
        else -> throw DInvalidArgumentError("ELF is not supports ABI for '$this' operating system")
    }
}

fun EByteOrder.toELF(): UByte {
    return when(this){
        EByteOrder.LITTLE_ENDIAN -> 0x01u
        EByteOrder.BIG_ENDIAN -> 0x02u
    }
}

fun EAppType.toELF(): UShort {
    return when(this){
        EAppType.SHARED_LIBRARY -> 3u
        EAppType.EXECUTABLE -> 2u
        EAppType.STATIC_LIBRARY -> 1u
    }
}

fun EArchitecture.toELF(): UShort {
    return when(this){
        EArchitecture.X86_64 -> 0x03u
        EArchitecture.RISC_V -> 0xF3u
        EArchitecture.ARM64 -> 0xB7u

        else -> throw DInvalidArgumentError("")
    }
}
