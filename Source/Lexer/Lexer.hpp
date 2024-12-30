#pragma once

#include <string>
#include <vector>
#include "Token.hpp"

namespace PalmierAssembler::Lexer {
	class Lexer {
	public:
		explicit Lexer(std::string text);

		[[nodiscard]] std::vector<std::vector<Token>> tokenize() const;

	private:
		const std::string text;

		static Token processIdentifier(const std::string& line, size_t& index);
		static Token processNumber(const std::string& line, size_t& index);
		static Token processKeyword(const std::string& stroke);

		static short processSimple(char character, std::vector<Token>& tokens_per_line);

		static std::vector<std::string> splitByDelims(const std::string& input);
	};
}
