#!/usr/bin/python3

#
# x86-64 Arch plugin for PalmierAssembler
#

#
# WARNING: This is NOT fully python script, this is plugin for PalmierAssembler
#

import sys

TYPE = "Architecture"
FOR_ARCH = "x86-64"

NATIVE_REGS = {
    "rax": "000",
    "rbx": "011",
    "rcx": "001",
    "rdx": "010",
    "rsi": "110",
    "rdi": "111",
    "rsp": "100",
    "rbp": "101",
    "r8": "000",
    "r9": "001",
    "r10": "010",
    "r11": "011",
    "r12": "100",
    "r13": "101",
    "r14": "110",
    "r15": "111",
}

NATIVE_COMMANDS = {
    "mov": {
        "opcode": "0x48 0x89",
        "is_simple": False,
    },
    "syscall": {
        "opcode": "0F 05",
        "is_simple": True,
    }
}

PALMIER_REGS_TO_NATIVE = {
    "general_reg1": NATIVE_REGS["rax"],
    "general_reg2": NATIVE_REGS["rbx"]
}

PALMIER_COMMANDS_TO_NATIVE = {
    "move": NATIVE_COMMANDS["mov"],
    "syscall": NATIVE_COMMANDS["syscall"]
}


def modRMPlusSIB(reg, base_reg, disp=None, scale=None, index=None):
    # Get Mod
    if disp is None:
        mod = "00"
    elif -128 <= disp <= 127:
        mod = "01"
    else:
        mod = "10"

    # Get rm
    if scale or index:
        rm = "100"  # Attach SIB
    else:
        rm = base_reg

    # Gen ModRM
    modrm = f"{mod}{reg}{rm}"

    # Gen SIB
    if rm == "100":
        scale_bin = format(int(scale), "02b") if scale else "00"
        index_bin = NATIVE_REGS.get(index, "100")
        base_bin = NATIVE_REGS.get(base_reg)
        sib = f"{scale_bin}{index_bin}{base_bin}"
        return modrm, sib

    return modrm, None


def combineDifficultCommand(command_opcode: str, args: list[str]) -> str:
    match command_opcode:
        case "0x48 0x89":  # mov
            receiver = args[0]
            value = args[1]

            try:
                disp = args[2]
            except IndexError:
                disp = None
            try:
                scale = args[3]
            except IndexError:
                scale = None

            modrm, sib = modRMPlusSIB(receiver, value, disp, scale)

            return f"{command_opcode} {hex(int(modrm, 2))} {hex(int(sib, 2)) if sib else '' }"

    return "UNSUPPORTED"


if __name__ == "__main__":
    match sys.argv[1]:
        case "base_info":
            print(f"{TYPE},{FOR_ARCH}")
        case "get_reg":
            print(PALMIER_REGS_TO_NATIVE.get(sys.argv[2]))
        case "get_cmd":
            cmd = PALMIER_COMMANDS_TO_NATIVE.get(sys.argv[2])
            print(f"{cmd["opcode"]},{cmd["is_simple"]}".lower())
        case "combine_difficult_cmd":
            print(combineDifficultCommand(sys.argv[2], sys.argv[3:]))

        case _:
            print("UNKNOWN_CMD")
