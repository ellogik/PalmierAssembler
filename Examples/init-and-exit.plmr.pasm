// Only for UNIX
block start {
    move: %syscall_id, 0;       // exit
    move: %syscall_arg1, 0;     // 0

    syscall;                    // system call - exit(0)
}