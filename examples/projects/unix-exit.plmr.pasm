/ comment ;
block start {
    move: %syscall_id, 60;
    move: %syscall_arg1, 0;

    syscall;
}