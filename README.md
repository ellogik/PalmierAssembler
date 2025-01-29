# PalmierAssembler
A crossplatform assembler for Palmier Platform

### Basic Exit(In UNIX/POSIX):
```PASM
block start {
    move: %system_call_id, 60;
    move: %system_call_arg1, 0;
    
    systemCall;
}
```
### Variables and Registers
|     Type    | Name                                   | Description                        |
|:-----------:|----------------------------------------|------------------------------------|
| Register($) | $general_reg(num from one to nine)     | Primary registers for architecture |
| Variable(%) | %system_call_id                        | ID for system call for OS          |
| Variable(%) | %system_call_arg(num from one to nine) | Arguments to system call for OS    |
### How to use
```shell
java -jar <path/to/PASM.jar> <path/to/source.plmr.pasm> <format>
```