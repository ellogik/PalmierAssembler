block start {
    native: X86-64-BUILD-PLUGIN and UNIX-ELF64-BUILD-PLUGIN {
        move: eax, 0;
        move: ebx, 0;

        interrupt: 0x80;
    };
    native: ARM64-BUILD-PLUGIN and DARWIN-O-MACH-BUILD-PLUGIN {
        // ...
    };
    native: notSupported {
        // ...
    };
}