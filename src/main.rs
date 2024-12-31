mod lexerization;
mod parsing;

fn main() {
    let txt = String::from("\
    / comment ;\
    block start {\
        move: %syscall_id, 0;\
        move: %syscall_arg1, 0;\
        syscall;\
        syscall;\
    }\
    \
    block end {\
        syscall;\
    }");

    let mut lex = lexerization::Lexer::new(txt.clone());

    println!("----------------TEXT----------------");
    println!("{txt:#?}");
    println!("----------------LEXER----------------");

    let tokenized = lex.tokenize();

    tokenized.iter().for_each(
        |token|
            {
                for tk in token {
                    print!("{:#?} ", tk.token_type);
                }

                print!("\n")
            }
    );

    println!("----------------PARSER----------------");

    let mut prs = parsing::Parser::new(tokenized);

    let parsed = prs.parse();

    parsed.iter().for_each(| node | {
        println!("{:#?}", node);
    })

}
