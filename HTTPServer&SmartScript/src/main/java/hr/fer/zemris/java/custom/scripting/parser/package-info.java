
/**
 * This package implements a parser that parses a below specified documents.
 * It uses a SmartScriptLexer as a lexer.
 * 
 * Specification of a document
 * The document consists of tags and rest of the text.
 * 
 * Tags are bounded by {$ and $}. Arguments of the tags
 * are element between the group of symbols {$ and $}.
 * Every tag must have at least one argument. 
 * The first argument of the tag is the tag name.
 * 
 * A special kind of tags are for tags and end tags.
 * For tags represent for loops and have 3 to 4 arguments :
 * variable, startExpression, endExpression, stepExpression(optional).
 * Every for loop that has to have its enclosing tag which
 * is the first end tag after the for loop.
 * For tags have "for"(case insensitive) as their tag name.
 * 
 * End tags have "end"(case insensitive) as their tag name and
 * no other arguments.
 * Every end tag has to have its opening for loop which is the
 * first for loop befote the end tag.
 * 
 * @author ltomic
 *
 */
package hr.fer.zemris.java.custom.scripting.parser;