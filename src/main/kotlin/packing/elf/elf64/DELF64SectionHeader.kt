package packing.elf.elf64

import native_code_generation.helpers.architectures.IELFSupportInArch
import packing.elf.elf64.PackerELF64.ARCH
import packing.elf.elf64.PackerELF64.HEADER_SIZE
import packing.elf.elf64.PackerELF64.PROGRAM_HEADER_SIZE
import packing.elf.elf64.PackerELF64.SECTION_HEADER_SIZE
import packing.elf.elf64.PackerELF64.SH_STR_TAB_INDEX
import packing.elf.elf64.PackerELF64.SH_STR_TAB_NAME_INDEX
import packing.elf.elf64.PackerELF64.STR_TAB_INDEX
import packing.elf.elf64.PackerELF64.STR_TAB_NAME_INDEX
import packing.elf.elf64.PackerELF64.SYMTAB_NAME_INDEX
import packing.elf.elf64.PackerELF64.TEXT_NAME_INDEX
import java.nio.ByteBuffer

data class DELF64SectionHeader(
    val name: Int,
    val type: Int,
    val flags: Long,
    val address: Long,
    val offset: Long,
    val size: Long,
    val link: Int,
    val info: Int,
    val address_align: Long,
    val entry_size: Long
) {
    companion object {
        private var DOT_TEXT_SIZE: Long? = null
        private var DOT_SH_STR_TAB_SIZE: Long? = null
        private var DOT_SYM_TAB_SIZE: Long? = null
        private var DOT_STR_TAB_SIZE: Long? = null

        fun forText(size: Long): DELF64SectionHeader {
            DOT_TEXT_SIZE = size

            return DELF64SectionHeader(
                name = TEXT_NAME_INDEX, //.text
                type = 1, // progbits
                flags = 0x6, // SHF_ALLOC | SHF_EXECINSTR
                address = (ARCH as IELFSupportInArch).ELF_ENTRY,
                offset = (
                            HEADER_SIZE +
                                (PROGRAM_HEADER_SIZE * PackerELF64.num_of_phs) +
                                (SECTION_HEADER_SIZE * PackerELF64.num_of_shs)
                         ).toLong(),
                size = size,
                link = 0,
                info = 0,
                address_align = 0x1000,
                entry_size = 0
            )
        }

        fun forNull() = DELF64SectionHeader(
            name = 0,
            type = 0,
            flags = 0,
            address = 0,
            offset = 0,
            size = 0,
            link = 0,
            info = 0,
            address_align = 0,
            entry_size = 0
        )

        fun forShStrTab(size: Long): DELF64SectionHeader {
            DOT_SH_STR_TAB_SIZE = size

            return DELF64SectionHeader(
                name = SH_STR_TAB_NAME_INDEX, //.shstrtab
                type = 0x3, // SHT_STRTAB
                flags = 0x20,
                address = 0,
                offset = HEADER_SIZE +
                            (PROGRAM_HEADER_SIZE * PackerELF64.num_of_phs) +
                            (SECTION_HEADER_SIZE * PackerELF64.num_of_shs) + DOT_TEXT_SIZE!!,
                size = size,
                link = 0,
                info = 0,
                address_align = 2,
                entry_size = 0
            )
        }

        fun forSymTab(size: Long): DELF64SectionHeader {
            DOT_SYM_TAB_SIZE = size

            return DELF64SectionHeader(
                name = SYMTAB_NAME_INDEX, // .symtab
                type = 2, // symtab
                flags = 0,
                address = 0,
                offset = HEADER_SIZE +
                        (PROGRAM_HEADER_SIZE * PackerELF64.num_of_phs) +
                        (SECTION_HEADER_SIZE * PackerELF64.num_of_shs) + DOT_TEXT_SIZE!! + DOT_SH_STR_TAB_SIZE!!,
                size = size,
                link = STR_TAB_INDEX.toInt(),
                info = (size / 24).toInt(),
                address_align = 8,
                entry_size = 24
            )
        }

        fun forStrTab(size: Long): DELF64SectionHeader {
            DOT_STR_TAB_SIZE = size

            return DELF64SectionHeader(
                name = STR_TAB_NAME_INDEX,
                type = 0x3, // strtab
                flags = 0x20,
                address = 0,
                offset =  HEADER_SIZE +
                        (PROGRAM_HEADER_SIZE * PackerELF64.num_of_phs) +
                        (SECTION_HEADER_SIZE * PackerELF64.num_of_shs) + DOT_TEXT_SIZE!! + DOT_SH_STR_TAB_SIZE!! + DOT_SYM_TAB_SIZE!!,
                size = size,
                link = 0,
                info = 0,
                address_align = 2,
                entry_size = 0
            )
        }
    }


    fun toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(SECTION_HEADER_SIZE.toInt()).order(ARCH.BYTE_ORDER.toJavaByteOrder())

        buffer.put(name.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(type.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(flags.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(address.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(offset.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(size.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(link.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(info.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(address_align.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(entry_size.toByteBuffer(ARCH.BYTE_ORDER).array())

        return buffer.array()
    }
}