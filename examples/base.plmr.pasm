block start {
    move: $general_reg1, 60; / exit();
    move: $general_reg6, 0; / exit(0);

    systemCall; / call exit(0);
}