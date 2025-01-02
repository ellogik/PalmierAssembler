import StandardMacroLib as sml;

block main {
    sml.Platform.name: %general_reg1;

    sml.Terminal.print: ("I know your platform! It's " ++ %general_reg1 ++ "! Right?");

    / will be ;
}