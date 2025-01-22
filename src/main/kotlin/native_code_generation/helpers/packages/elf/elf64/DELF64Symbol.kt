package native_code_generation.helpers.packages.elf.elf64

import native_code_generation.helpers.packages.elf.elf64.PackerELF64.ARCH
import java.nio.ByteBuffer

data class DELF64Symbol(
    val name: Int,              // Name offset
    val info: Byte,             // Type and link
    val other: Byte,            // Visability
    val section_index: Short,   // Index of section
    val value: Long,            // Symbol value
    val size: Long              // Size of symbol
) {
    fun toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(24).order(ARCH.BYTE_ORDER.toJavaByteOrder())
        buffer.put(name.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(info)
        buffer.put(other)
        buffer.putShort(section_index)
        buffer.put(value.toByteBuffer(ARCH.BYTE_ORDER).array())
        buffer.put(size.toByteBuffer(ARCH.BYTE_ORDER).array())
        return buffer.array()
    }
}
