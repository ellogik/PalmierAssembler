package native_code_generation.helpers.operating_systems

interface IELFSupportInOs {
    fun toELFAbi(): Byte
}