/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.io.*;
import java.util.*;

public class Scanner {

  private static final char[] letter =
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private static final char[] digit = "0123456789".toCharArray();
  private static final String[] keywords =
    {"IF", "ELSE", "WHILE", "PROGRAM", "BEGIN", "END", "CONST", "TYPE",
     "VAR", "ARRAY", "OF", "RECORD", "DIV", "MOD", "THEN", "REPEAT",
     "UNTIL", "DO", "WRITE", "READ"};
  private static final char[] operator = ".;:=+-*()#<>[],".toCharArray();
  private static final char[] ignored = {' ', '\t', '\n', '\f', '\r'};

  private ArrayList<Token> tokens = new ArrayList<Token>();
  private char[] source; // source code we are scanning
  private int position; // current position in source code
  private int size; // length of the source code to check for eof
  private boolean comments; // true if currently within a comment
  private int depth; // how many levels deep inside comments we are
  private boolean called; // whether or not next() has been called

  public Scanner(String source) {
    // The source code read in from stdin is stored here as a char array
    // TODO: if source is empty in Sc.java do something??
    this.source = source.toCharArray();
    this.position = 0;
    this.size = source.length();
    this.comments = false;
    this.depth = 0;
    this.called = false;
  }

  private boolean empty(String string) {
    return (string.length() == 0 || string == null);
  }

  private boolean isKeyword(String input) {
    return Arrays.asList(this.keywords).contains(input);
  }

  private boolean isLetter(char input) {
    return new String(this.letter).contains(String.valueOf(input));
  }

  private boolean isDigit(char input) {
    return new String(this.digit).contains(String.valueOf(input));
  }

  private boolean isOperator(char input) {
    return new String(this.operator).contains(String.valueOf(input));
  }

  // Overloaded version of isOperator
  private boolean isOperator(String input) {
    return new String(this.operator).contains(input);
  }

  private boolean isIgnored(char input) {
    return new String(this.ignored).contains(String.valueOf(input));
  }

  private boolean isIdentifier(String input) {
    // first char of an identifier must begin with a letter
    if (this.isLetter(input.charAt(0))) {
      return true;
    } else return false;
  }

  private boolean isInteger(String input) {
    char[] buffer = input.toCharArray();
    // By default, input is an integer until letter is detected
    boolean isInt = true;
    for (char c : buffer) {
      // if a letter is found in input, it's not an integer
      if (!this.isDigit(c)) {
        isInt = false;
      }
    }
    return isInt;
  }

  /**
      Determine if the character input is a valid character
      @param input the char to validate
      @return true if valid, else false
  */
  private boolean isValid(char input) {
    if (this.isLetter(input) || this.isDigit(input) || this.isOperator(input)
    || this.isIgnored(input)) {
      return true;
    } else return false;
  }

  private String getType(String input) {
    if (this.isKeyword(input) || this.isOperator(input)) {
      return "keyword";
    } else if (this.isIdentifier(input)) {
      return "identifier";
    } else if (this.isInteger(input)) {
      return "integer";
      // run into token that starts with an integer but contains letters
    } else return "split";
  }

  /**
      Find the first instance of a letter from input that starts with
      a digit but contains letters as well
      @param input the input to analyze
      @return the position of the first letter
   */
  private int findSplit(String input) {
    char[] buffer = input.toCharArray();
    int pos = 0;
    for (int i = 0; i < buffer.length; i++) {
      if (this.isLetter(buffer[i])) {
        pos = i;
        break;
      }
    }
    return pos;
  }

  /**
      Constructor function for creating a Token
      @return the created Token
  */
  private Token createToken(
    String type, String val, int start, int end) {
    return new Token(type, val, start, end);
  }

  /**
      Get the next char from the source code and increment current
      char position in source code.
      @return the next char.
  */
  private char nextChar() throws Exception {
    char next = this.source[this.position];
    this.position++;
    return next;
  }

  public Token next() throws Exception {
    this.called = true;
    Token token = null;
    boolean found_token = false;
    String type = "";
    String buffer = "";
    int start = 0;
    int end = 0;
    while (!found_token) {
      // While we haven't reached the last character in source code
      if (this.position != this.size) {
        char next = this.nextChar();
        // Check for comments
        if (next == '(') {
          next = this.nextChar();
          if (next == '*') {
            this.comments = true;
            this.depth++;
            if (!buffer.isEmpty()) {
              this.position -= 2; // get back to the position before comment
              found_token = true;
              type = this.getType(buffer);
              // ran into token that starts with digit but contains a letter
              if (type.equals("split")) {
                int length = buffer.length();
                int pos = this.findSplit(buffer);
                buffer = buffer.substring(0, pos);
                type = this.getType(buffer); // should be integer type
                this.position -= (length-pos); // reset scanner back to split
              }
              start = this.position - buffer.length();
              end = this.position - 1; // account for the ignored char
              token = this.createToken(type, buffer, start, end);
              buffer = ""; // reset the buffer we are scanning
              this.position += 2; // continue scanning within comment
            }
          } else {  // not a comment, continue on
            this.position -= 2;
            next = this.nextChar(); // re-assign 'next' back to original char
          }
        } else if (next == '*' && this.comments) {
          next = this.nextChar();
          if (next == ')') {
            this.depth--;
            if (this.depth == 0) {
              this.comments = false;
              next = this.nextChar(); // outside of comment, move on
            }
          }
        }
        // Only parse if not within a comment
        if (!this.comments) {
          // Check that character is valid
          if (!this.isValid(next)) {
            // create a token out of what's in buffer already
            if (!buffer.isEmpty()) {
              found_token = true;
              type = this.getType(buffer);
              // ran into token that starts with digit but contains a letter
              if (type.equals("split")) {
                int length = buffer.length();
                int pos = this.findSplit(buffer);
                buffer = buffer.substring(0, pos);
                type = this.getType(buffer); // should be integer type
                this.position -= (length-pos+1); // reset scanner back to split
                start = this.position - buffer.length();
                end = this.position - 1;
              } else {
                start = this.position - buffer.length() - 1;
                end = this.position - 2; // account for the ignored char
              }
              token = this.createToken(type, buffer, start, end);
              this.tokens.add(token);
            }
            throw new Exception("invalid character '" + String.valueOf(next) +
              "' @(" + (this.position - 1) + ")");
          }
          // Below, checks if next char is a space, tab, newline etc.
          // If so, new token is detected, parse it
          if (this.isIgnored(next)) {
            // Checks that we don't count ignored chars as tokens
            if (!buffer.isEmpty()) {
              found_token = true;
              type = this.getType(buffer);
              // ran into token that starts with digit but contains a letter
              if (type.equals("split")) {
                int length = buffer.length();
                int pos = this.findSplit(buffer);
                buffer = buffer.substring(0, pos);
                type = this.getType(buffer); // should be integer type
                this.position -= (length-pos+1); // reset scanner back to split
                start = this.position - buffer.length();
                end = this.position - 1;
              } else {
                start = this.position - buffer.length() - 1;
                end = this.position - 2; // account for the ignored char
              }
              token = this.createToken(type, buffer, start, end);
              buffer = ""; // reset the buffer we are scanning
            }
          } else if (this.isOperator(next)) {
            found_token = true;
            type = this.getType(buffer); // operators should be keywords
            // Print the token before the operator first and come back later
            if (!buffer.isEmpty()) {
              // ran into token that starts with digit but contains a letter
              if (type.equals("split")) {
                int length = buffer.length();
                int pos = this.findSplit(buffer);
                buffer = buffer.substring(0, pos);
                type = this.getType(buffer); // should be integer type
                this.position -= (length-pos+1); // reset scanner back to split
              } else {
                this.position--;
              }
            // Else if operator is alone, print the operator out
            } else {
              buffer += next;
              // Check for special compound operators :=, <=, >=
              if (next == ':' || next == '<' || next == '>') {
                next = this.nextChar();
                if (next == '=') {
                  buffer += next;
                } else {
                  this.position--;
                }
              }
            }
            start = this.position - buffer.length();
            end = this.position - 1;
            token = this.createToken(type, buffer, start, end);
            buffer = ""; // reset the buffer we are scanning
          } else {
            // Else add next char to the current token we are scanning
            buffer += next;
          }
        }
      } else {
        if (this.comments) {
          throw new Exception ("Reached eof while still " +
            "nested inside " + depth + " comment(s)");
        }
        // Once end of file reached, check if we scanned a token
        if (!buffer.isEmpty()) {
          type = this.getType(buffer);
          // ran into token that starts with digit but contains a letter
          if (type.equals("split")) {
            int length = buffer.length();
            int pos = this.findSplit(buffer);
            buffer = buffer.substring(0, pos);
            type = this.getType(buffer); // should be integer type
            this.position -= (length-pos); // reset scanner back to split
          }
          start = this.position - buffer.length();
          end = this.position - 1; // account for the ignored char
          token = this.createToken(type, buffer, start, end);
          buffer = ""; // reset the buffer we are scanning
        } else {
          // We are done, return eof token
          end = this.position;
          token = this.createToken("eof", "eof", end, end);
        }
        found_token = true;
      }
    }
    return token;
  }

  public ArrayList<Token> all() throws Exception {
    if (this.called)
      throw new Exception("all() cannot be called after call to next()");
    boolean finished = false;
    try {
      while (!finished) {
        Token token = this.next();
        this.tokens.add(token);
        if (token.returnType().equals("eof")) {
          finished = true;
        }
      }
    } catch (Exception e) {
      System.err.println("error: " + e.getMessage());
      System.exit(0);
    } finally {
      // always return the list of tokens
      return this.tokens;
    }
  }
}
