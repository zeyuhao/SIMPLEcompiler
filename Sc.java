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
  static final String error_graphical = "-g can only be supplied " +
    "with -c, -t, or -a";
  static final String error_file = "Could not output generated code to ";
  static ArrayList<Token> token_list = null;
  static Scanner sc = null;
  static Parser parser = null;
  static String source = "";
  static String input_file = "";
  static char comp_opt;
  static boolean graphical = false;
  static boolean gen_code = false;
  static Observer obs = null;

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
    input_file = filename;
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

  // Below, used for obtaining the basename of the input file, w/o extension
  static String stripExtension(String filename) {
        if (filename == null) {
          return null;
        }
        // Get position of last '.'.
        int pos = filename.lastIndexOf(".");
        // If there wasn't any '.' just return the string as is.
        if (pos == -1) {
          return filename;
        }
        // Otherwise return the string, up to the dot.
        return filename.substring(0, pos);
  }

  // Below, used for adding a given extension type to a base filename
  static String addExtention(String filename, String ext) {
    return filename + "." + ext;
  }

  static void createFile(String filename) throws Exception {
    File file = new File(filename);
    file.createNewFile();
  }

  static void writeFile(String filename, String input) throws Exception {
    try {
      FileWriter writer = new FileWriter(filename);
      writer.write(input);
      writer.flush();
      writer.close();
    } catch (Exception e) {
      System.err.println(error_begin + error_file + filename + " - " +
        e.getMessage());
      quit();
    }
  }

  // If valid compiler option detected, return true, else return false
  static boolean checkFormat(String option) throws Exception {
    // compiler option should be ["-" ("s"|"c"|"t"|"a"|"i")]
    if (option.charAt(0) == '-') {
      comp_opt = option.charAt(1);
      // Option should be -s, -c, -t, -a, -i
      if (comp_opt != 's' && comp_opt != 'c' && comp_opt != 't'
        && comp_opt != 'a' && comp_opt != 'i') {
        throw new Exception(error_opt + " be -s, -c, -t, -a, or -i");
      }
      return true;
    }
    return false;
  }

  public static void main (String[] args) throws Exception {
    try {
      int num_args = args.length;
      // Parse compiler options:
      // A max of 3 command-line args can be provided
      if (num_args > 3) {
        throw new Exception("extraneous " + error_end);
      }
      // If no args were provided:
      // Generator code with input from stdin
      if (num_args == 0) {
        gen_code = true;
        read_source(System.in);
      } else if (num_args == 1) {
        // If 1 arg provided, we are either:
        // 1) Using compiler command line option, and inputing code from stdin
        // 2) Specifying input file for code generator
        if (!checkFormat(args[0])) {
          // Specifying an input file for the code generator
          gen_code = true;
          read_file(args[0]);
        } else {
          read_source(System.in);
        }
      } else if (num_args == 2) {
        // If 2 args provided, we are either:
        // 1) Using compiler command line option with -g, code input from stdin
        // 2) Using compiler command line option, code input from file
        if (!checkFormat(args[0])) {
          throw new Exception(error_opt + error_format);
        }
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
      } else {
        // If 3 args are provided:
        // Using compiler command line option with -g, code input from file
        if (!checkFormat(args[0])) {
          throw new Exception(error_opt + error_format);
        }
        // -g must come immediately after first command line option
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
        if (graphical) {
          throw new Exception(error_graphical);
        }
        if (!token_list.isEmpty()) {
          printTokens(token_list);
        }
      } else if (comp_opt == 'c') {
        parser = new Parser(token_list, obs);
        parser.parse();
        System.out.println(obs.toString());
      } else if (comp_opt == 't') {
        parser = new Parser(token_list, obs);
        parser.parse();
        System.out.println(parser.returnST());
      } else if (comp_opt == 'a') {
        parser = new Parser(token_list, obs);
        parser.parse();
        System.out.println(parser.returnAST());
      } else if (comp_opt == 'i') {
        if (graphical) {
          throw new Exception(error_graphical);
        }
        parser = new Parser(token_list, obs);
        parser.parse();
        System.out.println(parser.returnEnv());
      } else if (gen_code) {
        if (graphical) {
          throw new Exception(error_graphical);
        }
        parser = new Parser(token_list, obs);
        parser.parse();
        if (!input_file.isEmpty()) {
          String output_file = addExtention(stripExtension(input_file), "s");
          createFile(output_file);
          writeFile(output_file, "Generating code");
        } else {
          System.out.println("Generating code");
        }
      }
    } catch (Exception e) {
      System.err.println(error_begin + e.getMessage());
      quit();
    }
  }
}
