#pragma once

#include <string>
#include <vector>
#include "Token.hpp"

namespace Lexer {
	class Lexer {
	public:
		explicit Lexer(std::string text);

		[[nodiscard]] std::vector<std::vector<Token>> tokenize() const;

	private:
		const std::string text;

		static Token processIdentifier(const std::string& line, size_t& index);

		static Token processNumber(const std::string& line, size_t& index);

		static short processSimple(char character, std::vector<Token>& tokens_per_line);
	};
}
