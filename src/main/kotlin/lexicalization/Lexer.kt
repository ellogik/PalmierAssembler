package lexicalization

import utils.errors.DLexerError
import utils.errors.ELexerErrorType

class Lexer(private val _input: String) {
    private val _tokens: MutableList<MutableList<DToken>> = mutableListOf()
    private val _KEYWORDS = mapOf(
        "block" to DToken(ETokenType.KW_BLOCK),
        "macro" to DToken(ETokenType.KW_MACRO),
        "group" to DToken(ETokenType.KW_GROUP),
        "permanent" to DToken(ETokenType.KW_PERMANENT),
        "term" to DToken(ETokenType.KW_TERM),

        "move" to DToken(ETokenType.CMD_MOVE),
        "systemCall" to DToken(ETokenType.CMD_SYSTEM_CALL),
    )
    private var _current_index_in_line = 0

    fun tokenize(): MutableList<MutableList<DToken>> {
        val lines = divideByLinesAndRemoveWhitespaces(_input)

        LINE_MARK@ for(line in lines.withIndex()) {
            val tokens_per_line: MutableList<DToken> = mutableListOf()

            for(char in line.value.withIndex()) {
                if(char.index < _current_index_in_line) continue

                when(char.value) {
                    ',' -> tokens_per_line += DToken(ETokenType.ARGS_DIVIDER)
                    ':' -> tokens_per_line += DToken(ETokenType.CMD_AND_ARGS_DIVIDER)

                    '{' -> tokens_per_line += DToken(ETokenType.START_CODE_SPACE)
                    '}' -> tokens_per_line += DToken(ETokenType.END_CODE_SPACE)

                    '%' -> tokens_per_line += DToken(ETokenType.VAR_PREFIX)
                    '$' -> tokens_per_line += DToken(ETokenType.REG_PREFIX)

                    '-' -> tokens_per_line += DToken(ETokenType.MINUS)
                    '+' -> tokens_per_line += DToken(ETokenType.PLUS)
                    '\\' -> tokens_per_line += DToken(ETokenType.DIVIDE)
                    '*' -> tokens_per_line += DToken(ETokenType.MULTIPLY)


                    '/' -> {
                        _current_index_in_line = 0
                        if(tokens_per_line.isNotEmpty())
                            _tokens += tokens_per_line
                        continue@LINE_MARK
                    }

                    '"' -> {
                        _current_index_in_line++
                        tokens_per_line += tokenizeString(line.value)
                    }

                    else -> tokenizeDifficult(char, tokens_per_line, line)
                }

                _current_index_in_line++
            }

            _current_index_in_line = 0
            if(tokens_per_line.isNotEmpty())
                _tokens += tokens_per_line
        }

        return _tokens
    }

    private fun tokenizeDifficult(
        char: IndexedValue<Char>,
        tokens_per_line: MutableList<DToken>,
        line: IndexedValue<String>
    ) = when {
        char.value.isLetter() -> {
            tokenizeIdentifier(line.value).let {
                tokenizeKeyword(it.value!!).let { it2 ->
                    tokens_per_line += if(it2.type != ETokenType.INVALID_TOKEN)
                        it2
                    else
                        it
                }
            }
        }

        char.value.isWhitespace() -> {}

        char.value.isDigit() -> {
            tokens_per_line += tokenizeNumber(line.value)
        }

        else -> {
            throw DLexerError("Unknown Symbol '${char.value}' at line ${line.index + 1} at ${char.index + 1}", ELexerErrorType.UNKNOWN_SYMBOL, char.toString())
        }
    }

    private fun tokenizeString(current_line: String): DToken {
        var buffer = ""

        for (char in current_line.withIndex()){
            if (char.index < _current_index_in_line) continue

            if (char.value == '"') break

            buffer += char.value
            _current_index_in_line++
        }

        _current_index_in_line++

        return DToken(ETokenType.STRING, buffer)
    }

    private fun tokenizeIdentifier(current_line: String): DToken {
        var buffer = ""

        for(char in current_line.withIndex()) {
            if(char.index < _current_index_in_line) continue

            if (char.value.isLetter() || char.value == '_' || char.value == '-' || char.value.isDigit())
                buffer += char.value
            else {
                if( char.value == ':'  || char.value == ',' ){
                    _current_index_in_line--
                }
                break
            }

            _current_index_in_line++
        }

        return DToken(ETokenType.IDENTIFIER, buffer)
    }

    private fun tokenizeNumber(current_line: String): DToken{
        var buffer = ""

        for(char in current_line.withIndex()) {
            if(char.index < _current_index_in_line) continue

            if (char.value.isDigit() || char.value == '.' || char.value == '-')
                buffer += char.value
            else {
                break
            }

            _current_index_in_line++
        }

        return DToken(ETokenType.NUMBER, buffer)
    }

    private fun tokenizeKeyword(input: String) = when( _KEYWORDS[input] ){
            null -> DToken(ETokenType.INVALID_TOKEN)

            else -> _KEYWORDS[input]!!
        }


    private fun divideByLinesAndRemoveWhitespaces(input: String): MutableList<String>{
        val buffer = mutableListOf<String>()
        var line_buffer = ""

        for (char in input) {
            if(char == ';') {
                if(line_buffer.isNotEmpty()){
                    buffer += line_buffer
                    line_buffer = ""
                }
                continue
            }

            line_buffer += char

            if((char == '{' || char == '}') && line_buffer.isNotEmpty()) {
                buffer += line_buffer
                line_buffer = ""
            }
        }

        return buffer
    }
}