#pragma once

#include <string>
#include <vector>
#include <stdexcept>
#include <bits/stdc++.h>

#include "Token.hpp"


namespace Lexer {
	class Lexer {
	public:
		explicit Lexer(std::string text);

		[[nodiscard]] std::vector<std::vector<Token>> tokenize();

	private:
		const std::string text;
		uint position_in_line = 0;

		Token processIdentifier(const std::string &line);
		Token processNumber(const std::string &line);

		short processSimple(char character, std::vector<Token>& tokens_per_line);
	};
} // Lexer