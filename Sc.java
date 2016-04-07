/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.io.*;
import java.lang.*;
import java.util.*;

public class Sc {
  static final String error_begin = "error: ";
  static final String error_end = "compiler options provided";
  static final String error_opt = "compiler options must";
  static final String error_format = " follow [\"-\"" +
    "(\"s\"|\"c\"|\"t\"|\"a\"|\"i\")] [\"-g\"] [filename]";
  static ArrayList<Token> token_list = null;
  static Scanner sc = null;
  static Parser parser = null;
  static String source = "";
  static boolean graphical = false;
  static Observer obs;

  // Exit the program
  static void quit() {
    System.exit(0);
  }

  static void printTokens(ArrayList<Token> token_list) {
    for (Token token : token_list) {
      System.out.println(token.toString());
    }
  }

  static void read_file(String filename) throws Exception {
    FileInputStream in = null;
    try {
      in = new FileInputStream(filename);
      read_source(in);
    } catch(FileNotFoundException f) {
      System.err.println(error_begin + filename +
                    ": No such file or directory");
      quit();
    } finally {
      // close the InputStream
      if (in != null) {
        in.close();
      }
    }
  }

  static void read_source(InputStream stream) throws Exception {
    BufferedReader in = null;
    int value = 0;
    char c;
    try {
      in = new BufferedReader(new InputStreamReader(stream));
      // reads until an EOF
      while ((value = in.read()) != -1) {
        c = (char) value;
        source += c;
      }
    } finally {
      // close the InputStream
      if (in != null) {
        in.close();
      }
    }
  }

  public static void main (String[] args) throws Exception {
    try {
      int num_args = args.length;
      // Parse compiler options:
      // A max of 3 and a minimum of 1 command-line args must be provided
      if (num_args > 3) {
        throw new Exception("extraneous " + error_end);
      }
      // if no args were provided, ArrayIndexOutOfBoundsException will be caught
      if (num_args < 1) {
        throw new Exception("no " + error_end);
      }
      String option = args[0];
      char comp_opt = option.charAt(1);
      // compiler option should be ["-" ("s"|"c"|"t"|"a"|"i")]
      if (option.length() == 2) {
        // First char should be "-"
        if (option.charAt(0) != '-') {
          throw new Exception(error_opt + " begin with -");
        }
        // Option should be -s, -c, -t, -a for now
        if (comp_opt != 's' && comp_opt != 'c' && comp_opt != 't'
          && comp_opt != 'a' && comp_opt != 'i') {
          throw new Exception(error_opt + " be -s, -c, -t, -a, or -i");
        }
      } else throw new Exception(error_opt + error_format);

      // no -g or filename provided
      if (num_args == 1) {
        read_source(System.in);
      }
      // If only 2 args provided, assume it's either a filename or -g
      else if (num_args == 2) {
        // if -g
        if (args[1].charAt(0) == '-') {
          if (args[1].charAt(1) == 'g') {
            graphical = true;
          } else throw new Exception(error_opt + error_format);
          // If second arg not a filename, read from stdin
          read_source(System.in);
        } else {
          // a filename
          read_file(args[1]);
        }
      }
      // if 3 args are provided assume we have -g then a filename
      else {
        // -g
        if (args[1].charAt(0) == '-') {
          if (args[1].charAt(1) == 'g') {
            graphical = true;
          } else throw new Exception(error_opt + error_format);
        } else throw new Exception(error_opt + error_format);
        // a filename
        read_file(args[2]);
      }

      // Set the Observer type
      if (graphical) {
        obs = new GraphicalObserver();
      } else {
        obs = new TextualObserver();
      }

      // Begin compiling operations
      sc = new Scanner(source);
      token_list = sc.all();
      if (comp_opt == 's') {
        if (graphical)
          throw new Exception("-g can only be supplied with -c, -t, -a, or -i");
        if (!token_list.isEmpty())
          printTokens(token_list);
      } else if (comp_opt == 'c') {
        parser = new Parser(token_list, obs);
        parser.parse();
        System.out.println(obs.toString());
      } else if (comp_opt == 't') {
        parser = new Parser(token_list, obs);
        parser.parse();
        System.out.println(parser.toString());
      } else if (comp_opt == 'a') {
        System.out.println("Creating abstract syntax tree");
      } else if (comp_opt == 'i') {
        System.out.println("Running the interpreter");
      }
    } catch (Exception e) {
      System.err.println(error_begin + e.getMessage());
      quit();
    }
  }
}
