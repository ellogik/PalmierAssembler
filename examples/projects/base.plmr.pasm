block start {
    move: $general_reg1, 1;                 / write ;
    move: $general_reg6, 1;                 / stdout ;
    move: $general_reg5, "Hello, World!";   / text ;
    move: $general_reg3, 2;                 / len ;

    systemCall;

    move: $general_reg1, 60; / exit();
    move: $general_reg6, 0; / exit(0);


    systemCall; / call exit(0);
}