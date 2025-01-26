package packing.elf.elf64

import native_code_generation.helpers.AArchitecture
import native_code_generation.helpers.architectures.ArchX86_64
import utils.byte_order.EByteOrder
import utils.errors.DInvalidArgumentError
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun EByteOrder.toELF(): Byte = when(this) {
    EByteOrder.LITTLE_ENDIAN -> 1
    EByteOrder.BIG_ENDIAN -> 2
}

fun Long.toByteBuffer(endian: EByteOrder): ByteBuffer {
    val buffer = ByteBuffer.allocate(8).order(endian.toJavaByteOrder())

    buffer.putLong(this)

    return buffer
}

fun Int.toByteBuffer(endian: EByteOrder): ByteBuffer {
    val buffer = ByteBuffer.allocate(4).order(endian.toJavaByteOrder())

    buffer.putInt(this)

    return buffer
}