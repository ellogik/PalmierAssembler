block start {
    !Linux.print: "Hello, World!";
    !Linux.exit;
}

group Linux {
    block exit {
        move: $general_reg1, 60; / exit();
        move: $general_reg2, 0; / exit(0);

        systemCall; / call exit(0);
    }
    macro print(str: string) {
        /...;
    }
}