permanent message: "Hello World\n";
permanent length: term %current_address - message;

block start {
    move: %system_call_id, 1;           / write ;
    move: %system_call_arg1, 1;         / std out ;
    move: %system_call_arg2, message;   / message to print ;
    move: %system_call_arg3, length;    / message length   ;

    systemCall; / write(STD_OUT, "Hello, World\n", 13) ;

    / exit ;
    move: %system_call_id, 60; / exit ;
    move: %system_call_arg1, 0; / exit code ;

    systemCall; / exit(0) ;
}

block sc {
    systemCall;
}