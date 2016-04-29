/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.*;
public class Parser {

  private static final Token IF = new Token("keyword", "IF", 0, 0);
  private static final Token ELSE = new Token("keyword", "ELSE", 0, 0);
  private static final Token WHILE = new Token("keyword", "WHILE", 0, 0);
  private static final Token PROGRAM = new Token("keyword", "PROGRAM", 0, 0);
  private static final Token BEGIN = new Token("keyword", "BEGIN", 0, 0);
  private static final Token END = new Token("keyword", "END", 0, 0);
  private static final Token CONST = new Token("keyword", "CONST", 0, 0);
  private static final Token TYPE = new Token("keyword", "TYPE", 0, 0);
  private static final Token VAR = new Token("keyword", "VAR", 0, 0);
  private static final Token PROC = new Token("keyword", "PROCEDURE", 0, 0);
  private static final Token RETURN = new Token("keyword", "RETURN", 0, 0);
  private static final Token ARRAY = new Token("keyword", "ARRAY", 0, 0);
  private static final Token OF = new Token("keyword", "OF", 0, 0);
  private static final Token RECORD = new Token("keyword", "RECORD", 0, 0);
  private static final Token DIV = new Token("keyword", "DIV", 0, 0);
  private static final Token MOD = new Token("keyword", "MOD", 0, 0);
  private static final Token THEN = new Token("keyword", "THEN", 0, 0);
  private static final Token REPEAT = new Token("keyword", "REPEAT", 0, 0);
  private static final Token UNTIL = new Token("keyword", "UNTIL", 0, 0);
  private static final Token DO = new Token("keyword", "DO", 0, 0);
  private static final Token WRITE = new Token("keyword", "WRITE", 0, 0);
  private static final Token READ = new Token("keyword", "READ", 0, 0);

  private static final Token PERIOD = new Token("keyword", ".", 0, 0);
  private static final Token SEMICOLON = new Token("keyword", ";", 0, 0);
  private static final Token COLON = new Token("keyword", ":", 0, 0);
  private static final Token COLONEQ = new Token("keyword", ":=", 0, 0);
  private static final Token EQUAL = new Token("keyword", "=", 0, 0);
  private static final Token PLUS = new Token("keyword", "+", 0, 0);
  private static final Token MINUS = new Token("keyword", "-", 0, 0);
  private static final Token STAR = new Token("keyword", "*", 0, 0);
  private static final Token OPENPAR = new Token("keyword", "(", 0, 0);
  private static final Token CLOSEPAR = new Token("keyword", ")", 0, 0);
  private static final Token HASHTAG = new Token("keyword", "#", 0, 0);
  private static final Token LT = new Token("keyword", "<", 0, 0);
  private static final Token LTEQ = new Token("keyword", "<=", 0, 0);
  private static final Token GT = new Token("keyword", ">", 0, 0);
  private static final Token GTEQ = new Token("keyword", ">=", 0, 0);
  private static final Token OPENBR = new Token("keyword", "[", 0, 0);
  private static final Token CLOSEBR = new Token("keyword", "]", 0, 0);
  private static final Token COMMA = new Token("keyword", ",", 0, 0);
  private static final Token EOF = new Token("eof", "eof", 0, 0);

  private ArrayList<Token> tokens; // list of tokens we are parsing
  private Observer obs; // Observer to attach to this Parser
  private int position; // current position in token list
  private Token curr_token; // current token we are parsing
  private Scope universe; // universe Scope
  private Scope program; // program Scope
  private Scope curr_scope; // current Scope
  private Type INTEGER; // single instance of Integer class
  private AST ast; // instance of Abstract Syntax Tree
  private Environment env; // instance of the Environment
  private Generator gen;


  // Pass in the full list of tokens from Scanner
  public Parser(ArrayList<Token> tokens, Observer obs) {
    this.tokens = tokens;
    this.obs = obs;
    this.position = 0;
    this.curr_token = this.tokens.get(this.position); // set to first token
    this.universe = new Scope();
    this.program = new Scope(this.universe); // set universe as parent Scope
    this.curr_scope = this.program; // set current Scope to program Scope
    this.INTEGER = new Integer();
    this.universe.insert("INTEGER", this.INTEGER); // Insert Integer class
    this.env = new Environment();
  }

  private void next_token() {
    // If eof token not found, then get next token,
    // else we stay at current token
    if (!this.match_opt(this.EOF)) {
      this.position++;
      this.curr_token = this.tokens.get(this.position);
    }
  }

  private Token match(Token token) throws Exception {
    Token tok = null;
    if (this.match_opt(token)) {
      this.notify(this.curr_token);
      tok = this.curr_token;
      this.next_token();
    } else {
      this.tokenException(token.returnVal());
    }
    return tok;
  }

  private Token match(String token_type) throws Exception {
    switch(token_type) {
      case "identifier":
        if (!this.curr_token.isIdentifier()) {
          this.tokenException(token_type);
        }
        break;
      case "keyword":
        if (!this.curr_token.isKeyword()) {
          this.tokenException(token_type);
        }
        break;
      case "integer":
        if (!this.curr_token.isInteger()) {
          this.tokenException(token_type);
        }
        break;
      default: break;
    }
    this.notify(this.curr_token);
    Token tok = this.curr_token;
    this.next_token();
    return tok;
  }

  private Token match(ArrayList<Token> list) throws Exception {
    for (Token token : list) {
      if (this.match_opt(token)) {
        return this.match(token);
      }
    }
    return null;
  }

  private boolean match_opt(Token token) {
    return this.curr_token.returnType().equals(token.returnType()) &&
           this.curr_token.returnVal().equals(token.returnVal());
  }

  private void tokenException(String token) throws Exception {
    throw new Exception("expected " + token + " but instead found " +
                        this.curr_token.toString());
  }

  private void productionException(String production) throws Exception {
    throw new Exception("expected " + production + " but instead found " +
                        this.curr_token.toString());
  }

  private void unmatchedProgramNameException(Token begin, Token last)
    throws Exception {
    throw new Exception("identifier after PROGRAM and " +
      "identifier after final END must be identical. Expected " +
      begin.toString() + " but found " + last.toString());
  }

  private void unmatchedProcNameException(Token begin, Token last)
    throws Exception {
    throw new Exception("identifier after PROCEDURE and " +
      "identifier after END must be identical. Expected " +
      begin.toString() + " but found " + last.toString());
  }

  private void identifierExistsException(Token token) throws Exception {
    throw new Exception("duplicate declaration of " + token.toString());
  }

  private void identifierNotFoundException(Token token) throws Exception {
    throw new Exception(token.toString() + " has not been declared yet");
  }

  private void identifierNotConstantException(Token token) throws Exception {
    throw new Exception(token.toString() + " is not a Constant");
  }

  private void identifierNotTypeException(Token token) throws Exception {
    throw new Exception(token.toString() + " is not a Type");
  }

  private void identifierNotVarException(Token token) throws Exception {
    throw new Exception(token.toString() + " is not a Variable");
  }

  // Defensive programming - should never reach this exception
  private void identifierNotProcException(Token token) throws Exception {
    throw new Exception(token.toString() + " is not a Procedure");
  }

  private void identifierNotArrayException(Location loc, Token token)
    throws Exception {
    throw new Exception("Element " + token.toString() + " of Location \"" +
      loc.toString() + "\" is not an Array");
  }

  private void identifierNotRecordException(Location loc, Token token)
    throws Exception {
    throw new Exception("Element " + token.toString() + " of Variable \"" +
      loc.toString() + "\" is not a Record");
  }

  private void identifierNotIntegerException(Node node, Token token)
    throws Exception {
    throw new Exception(node.toString() +  token.posString() +
      " is not an Integer");
  }

  private void expressionNotPositiveConstantException(Node node, Token token)
    throws Exception {
    throw new Exception("Expression (" + node.toString() + ")" +
      token.posString() + " must be constant, of type integer, " +
      "and greater than zero");
  }

  private void expressionNotConstantException(Node node, Token token)
    throws Exception {
    throw new Exception("Expression (" + node.toString() + ")" +
      token.posString() + " must be a constant, of type integer");
  }

  private void typeInDesignatorException(Token token) throws Exception {
    throw new Exception("Type " + token.toString() + " found in designator");
  }

  private void illegalSelectorException(Location loc, Token selector)
    throws Exception {
    throw new Exception("Selector " + selector.toString() + " of record "
                        + loc.toString() + " does not exist");
  }

  private void nodeNotLocationException(Node node, Token token)
    throws Exception {
    throw new Exception("Expected a Location variable but instead found "
      + token.toString() + " of Node type: " + node.nodeType());
  }

  private void unmatchedTypeException(Node left, Node right) throws Exception {
    throw new Exception("Mismatched Types in assignment:\n" +
      "Location " + left.toString() + left.getToken().posString() +
      " is of Type: " + left.getType().toString() +
      "\nbut found Expression " + right.toString() +
      right.getToken().posString() + " of Type: " +
      right.getType().toString());
  }

  private void procedureTypeException(Type type) throws Exception {
    throw new Exception("Return Type of a Procedure must be Integer. " +
      "Instead, found: " + type.toString());
  }

  private void procedureExpressionTypeException(Type type) throws Exception {
    throw new Exception("Return Type of Expression inside Procedure must " +
      "be Integer. Instead, found: " + type.toString());
  }

  private void unnamedTypeException(Token token) throws Exception {
    throw new Exception("Parameter Type inside Procedure must " +
      "be a previously declared Type.\nFound " +
      token.returnVal() + token.posString());
  }

  private void returnInProcedureException(Token token) throws Exception {
    throw new Exception("Return Expression found inside non-functional " +
      "Procedure: " + token.returnVal() + token.posString());
  }

  // Initializes the Observer
  private void notify_start() {
    this.obs.start();
  }

  // Ends the Observer
  private void notify_eof() {
    this.obs.end();
  }

  // Reduce tab depth by one
  private void notify_end() {
    this.obs.update_end();
  }

  // Updates Observer with a Production
  private void notify(String token) {
    this.obs.update(token);
  }
  // Updates Observer with a new token
  private void notify(Token token) {
    this.obs.update(token);
  }

  // table insertion with a single identifier
  private void table_insert(Token token, Entry entry) throws Exception {
    String name = token.returnVal();
    // check if another entry with same name was already declared
    if (this.curr_scope.local(name)) {
      this.identifierExistsException(token);
    }
    this.curr_scope.insert(name, entry);
  }

  // table insertion with IdentifierList
  private void table_insert(ArrayList<Token> list, Type type)
    throws Exception {
    for (Token token : list) {
      String name = token.returnVal();
      // check if another entry with same name was already declared
      if (this.curr_scope.local(name)) {
        this.identifierExistsException(token);
      }
      this.curr_scope.insert(name, new Variable(type));
    }
  }

  // record insertion with IdentifierList
  private void record_insert(ArrayList<Token> list, Type type)
    throws Exception {
    for (Token token : list) {
      String name = token.returnVal();
      // check if another entry with same name was already declared
      if (this.curr_scope.local(name)) {
        this.identifierExistsException(token);
      }
      this.curr_scope.insert(name, new Field(type));
    }
  }

  // record insertion with IdentifierList
  private void formal_insert(ArrayList<Token> list, Type type)
    throws Exception {
    for (Token token : list) {
      String name = token.returnVal();
      // check if another entry with same name was already declared
      if (this.curr_scope.local(name)) {
        this.identifierExistsException(token);
      }
      this.curr_scope.insert(name, new FormalVariable(type));
    }
  }

  /**
   * Checks that the given Selector of a Record exists.
   * If the Record or Selector doesn't exist, throw an exception
   */
  private Field find_record(Location loc, Token selector) throws Exception {
    Record rec = null;
    // First check that the Location is a Record Type
    this.checkRecord(loc);
    // If Location is an Index, then we can't search directly through S.T
    // If location is an Entry in S.T, then directly search for it.
    if (loc.isIndex()) {
      rec = (Record) loc.getType();
    } else {
      rec = (Record) this.find_entry(loc.getToken()).getType();
    }
    Field field = (Field) rec.getFields().local_find(selector.returnVal());
    if (field == null) {
      this.illegalSelectorException(loc, selector);
    }
    return field;
  }

  /**
   * Search the current scope for an identifier.
   * Throw an exception if the identifier is not found.
   * @return entry the found entry
   */
  private Entry find_entry(Token token) throws Exception {
    String name = token.returnVal();
    Entry entry = this.curr_scope.find(name);
    if (entry == null) {
      this.identifierNotFoundException(token);
    }
    return entry;
  }

  /**
   * Return a Constant entry from Symbol Table
   * Throw an exception if given Token is not a Constant Type.
   * @param token the token to search for
   * return the Constant Entry from Symbol Table
   */
  private Constant getConstant(Token token) throws Exception {
    if (!this.isConstant(token)) {
      this.identifierNotConstantException(token);
    }
    return (Constant) this.find_entry(token);
  }

  /**
   * Check if Identifier is a Constant.
   * If identifier has not been declared already, throw exception
   * @return true if Type, else false
   */
  private boolean isConstant(Token token) throws Exception {
    Entry entry = this.find_entry(token);
    return entry.isConstant();
  }

  /**
   * Return true if Expression is a Constant/literal greater than 0, else false
   */
  private boolean checkPositiveConstant(Expression exp) throws Exception {
    String name = exp.getToken().returnVal();
    Entry entry = this.curr_scope.find(name);
    // If Expression points to an Entry in S.T
    if (entry != null) {
      if (!entry.isConstant()) {
        return false;
      }
      // Greater than 0?
      else if (((Constant) entry).returnVal() <= 0) {
        return false;
      }
    // Else if we have a Literal not declared in S.T
    } else {
      if (!exp.isConstant()) {
        return false;
      }
      // Greater than 0?
      else if (exp.getToken().returnIntVal() <= 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return true if Expression is a Constant/literal, else false
   *
   */
  private Boolean checkConstant(Expression exp) throws Exception {
    String name = exp.getToken().returnVal();
    Entry entry = this.curr_scope.find(name);
    // If Expression points to an Entry in S.T
    if (entry != null) {
      if (!entry.isConstant()) {
        return false;
      }
    // Else if we have a Literal not declared in S.T
    } else {
      if (!exp.isConstant()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return the Entry Type associated with a given identifier name.
   * If identifier is not a Type, throw exception
   * @return entry the found Type
   */
  private Type getType(Token token) throws Exception {
    if (!this.isType(token)) {
      this.identifierNotTypeException(token);
    }
    return (Type) this.find_entry(token);
  }

  /**
   * Check if Identifier is a Type.
   * If identifier has not been declared already, throw exception
   * @return true if Type, else false
   */
  private boolean isType(Token token) throws Exception {
    Entry entry = this.find_entry(token);
    return entry.isType();
  }

  /**
   * Return the a Variable associated with a given identifier name.
   * If identifier is not a variable, throw exception.
   * @return entry the found Variable
   */
  private Variable getVariable(Token token) throws Exception {
    if (!this.isVariable(token)) {
      this.identifierNotVarException(token);
    }
    return (Variable) this.find_entry(token);
  }

  /**
   * Check if Identifier is a Variable.
   * If identifier has not been declared already, throw exception
   * @return true if Type, else false
   */
  private boolean isVariable(Token token) throws Exception {
    Entry entry = this.find_entry(token);
    return entry.isVariable();
  }

  /**
   * Return the a Procedure associated with a given identifier name.
   * If identifier is not a Procedure, throw exception.
   * @return entry the found Variable
   */
  private Procedure getProcedure(Token token) throws Exception {
    if (!this.isProcedure(token)) {
      this.identifierNotProcException(token);
    }
    return (Procedure) this.find_entry(token);
  }

  /**
   * Check if Identifier is a Procedure.
   * If identifier has not been declared already, throw exception
   * @return true if Type, else false
   */
  private boolean isProcedure(Token token) throws Exception {
    Entry entry = this.find_entry(token);
    return entry.isProcedure();
  }

  private void checkArray(Location loc) throws Exception {
    if (!loc.getType().isArray()) {
      this.identifierNotArrayException(loc, loc.getToken());
    }
  }

  private void checkRecord(Location loc) throws Exception {
    if (!loc.getType().isRecord()) {
      this.identifierNotRecordException(loc, loc.getToken());
    }
  }

  private void checkInteger(Node node) throws Exception {
    if (!node.getType().isInteger()) {
      this.identifierNotIntegerException(node, node.getToken());
    }
  }

  private Location getLocation(Node node) throws Exception {
    if (!node.isLocation()) {
      this.nodeNotLocationException(node, node.getToken());
    }
    // Cast below not really needed, but included for safety
    return (Location) node;
  }

  private void matchType(Node left, Node right) throws Exception {
    if (!left.getType().matchType(right.getType())) {
      this.unmatchedTypeException(left, right);
    }
  }

  // Function to get the converted Condition for Repeat instructions
  // '=' -> '#'
  // '#' -> '='
  // '<' -> '>='
  // '>' -> '<='
  // '<=' -> '>'
  // '>=' -> '<'
  private Condition convertCond(Condition cond) throws Exception {
    String op = cond.getRelation().returnVal();
    if (op.equals("=")) {
      return cond.copyCondition(this.HASHTAG);
    } else if (op.equals("#")) {
      return cond.copyCondition(this.EQUAL);
    } else if (op.equals("<")) {
      return cond.copyCondition(this.GTEQ);
    } else if (op.equals(">")) {
      return cond.copyCondition(this.LTEQ);
    } else if (op.equals("<=")) {
      return cond.copyCondition(this.GT);
    } else {
      return cond.copyCondition(this.LT);
    }
  }

  /*
    Program = "PROGRAM" identifier ";" Declarations
      ["BEGIN" Instructions] "END" identifier "." .
  */
  private boolean program() throws Exception {
    this.notify_start();
    this.notify("Program");
    this.match(this.PROGRAM);
    Token begin = this.match("identifier");
    this.match(this.SEMICOLON);
    this.declarations();
    // Create the environment from the Symbol Table
    this.curr_scope.createEnvironment(this.env);
    if (this.match_opt(this.BEGIN)) {
      this.match(this.BEGIN);
      this.ast = this.instructions();
    }
    this.match(this.END);
    Token last = this.match("identifier");
    // The identifier after PROGRAM and the corresponding identifier after
    // the corresponding END must be identical;
    if (!begin.returnVal().equals(last.returnVal())) {
      this.unmatchedProgramNameException(begin, last);
    }
    this.match(this.PERIOD);
    if(!this.match_opt(this.EOF))
      this.tokenException("eof");
    this.notify_eof();
    return true;
  }

  // Declarations = { ConstDecl | TypeDecl | VarDecl } .
  private void declarations() throws Exception {
    this.notify("Declarations");
    while (true) {
      if (this.match_opt(this.CONST)) {
        this.constDecl();
      } else if (this.match_opt(this.TYPE)) {
        this.typeDecl();
      } else if (this.match_opt(this.VAR)) {
        this.varDecl();
      } else if (this.match_opt(this.PROC)) {
        this.procDecl();
      } else {
        break;
      }
    }
    this.notify_end();
  }

  // ConstDecl = "CONST" {identifier "=" Expression ";"} .
  private void constDecl() throws Exception {
    this.notify("ConstDecl");
    this.match(this.CONST);
    // Keep checking for {identifier "=" Expression ";"} until done
    while (this.curr_token.isIdentifier()) {
      // get the identifier name
      Token curr = this.match("identifier");
      // = is required
      this.match(this.EQUAL);
      // Expression that is a Constant (completely foldable) is required
      Expression exp = this.expression();
      if (!this.checkConstant(exp)) {
        this.expressionNotConstantException(exp, exp.getToken());
      }
      // ; is required
      this.match(this.SEMICOLON);
      // Insert a new ConstDecl into symbol table
      int value = exp.returnNumber().returnVal();
      this.table_insert(curr, new Constant(this.INTEGER, value));
    }
    this.notify_end();
  }

  // "TYPE" {identifier "=" Type ";"} .
  private void typeDecl() throws Exception {
    this.notify("TypeDecl");
    this.match(this.TYPE);
    // Keep checking for {identifier "=" Type ";"} until done
    while (this.curr_token.isIdentifier()) {
      // get the identifier name
      Token curr = this.match("identifier");
      // = is required
      this.match(this.EQUAL);
      // Type is required
      Type curr_type = this.type();
      // ; is required
      this.match(this.SEMICOLON);
      this.table_insert(curr, curr_type);
    }
    this.notify_end();
  }

  // "VAR" {IdentifierList ":" Type ";"} .
  private void varDecl() throws Exception {
    this.notify("VarDecl");
    this.match(this.VAR);
    // Keep checking for {IdentifierList ":" Type ";"}
    while (this.curr_token.isIdentifier()) {
      ArrayList<Token> id_list = this.identifierList();
      this.match(this.COLON);
      Type curr_type = this.type(); // pointer to the Type returned
      this.match(this.SEMICOLON);
      this.table_insert(id_list, curr_type);
    }
    this.notify_end();
  }

  /*  ProcDecl = "PROCEDURE" identifier "(" [Formals] ")" [":" Type] ";"
        { VarDecl } [ "BEGIN" Instructions ] [ "RETURN" Expression ]
        "END" identifier ";" .
  */
  private void procDecl() throws Exception {
    this.notify("ProcDecl");
    Scope proc_scope = new Scope(this.curr_scope);
    // Set the current Scope to the new Scope
    this.curr_scope = proc_scope;
    Type curr_type = null;
    AST instructions = null;
    Expression exp = null;
    this.match(this.PROC);
    Token begin = this.match("identifier");
    this.match(this.OPENPAR);
    if (this.curr_token.isIdentifier()) {
      this.formals();
    }
    this.match(this.CLOSEPAR);
    if (this.match_opt(this.COLON)) {
      this.match(this.COLON);
      curr_type = this.type();
      // Return Type must be Integer
      if (!curr_type.isInteger()) {
        this.procedureTypeException(curr_type);
      }
    }
    this.match(this.SEMICOLON);
    if (this.match_opt(this.VAR)) {
      this.varDecl();
    }
    if (this.match_opt(this.BEGIN)) {
      this.match(this.BEGIN);
      instructions = this.instructions();
    }
    if (this.match_opt(this.RETURN)) {
      this.match(this.RETURN);
      exp = this.expression();
      // Expression must also be Integer
      if (!exp.getType().isInteger()) {
        this.procedureExpressionTypeException(exp.getType());
      }
    }
    this.match(this.END);
    Token last = this.match("identifier");
    if (!begin.returnVal().equals(last.returnVal())) {
      this.unmatchedProcNameException(begin, last);
    }
    this.match(this.SEMICOLON);
    Procedure proc = new Procedure(proc_scope, curr_type, instructions, exp);
    this.curr_scope = proc_scope.getParent();
    proc_scope.setParent(null);
    this.table_insert(begin, proc);
    this.notify_end();
  }

  // Formals = Formal { ";" Formal } .
  private void formals() throws Exception {
    this.notify("Formals");
    this.formal();
    while (this.match_opt(this.SEMICOLON)) {
      this.match(this.SEMICOLON);
      this.formal();
    }
    this.notify_end();
  }

  // Formal = IdentifierList ":" Type .
  private void formal() throws Exception {
    this.notify("Formal");
    ArrayList<Token> id_list = this.identifierList();
    this.match(this.COLON);
    try {
      this.isType(this.curr_token);
    } catch (Exception e) {
      this.unnamedTypeException(this.curr_token);
    }
    Type curr_type = this.type();
    this.formal_insert(id_list, curr_type);
    this.notify_end();
  }

  // Call = identifier "(" [Actuals] ")" .
  private Expression functionCall() throws Exception {
    this.notify("Call");
    // TODO: Fix this
    Expression exp = new Expression(new Constant(this.INTEGER, 0),
      new Token("integer", "0", 0, 0));
    ArrayList<Expression> exp_list = null;
    this.match("identifier");
    this.match(this.OPENPAR);
    // Optional ExpressionList
    try {
      exp_list = this.actuals();
    } catch (Exception e) {}
    this.match(this.CLOSEPAR);
    this.notify_end();
    return exp;
  }

  private ArrayList<Expression> actuals() throws Exception {
    this.notify("Actuals");
    ArrayList<Expression> exp_list = this.expressionList();
    this.notify_end();
    return exp_list;
  }

  /*
    identifier | "ARRAY" Expression "OF" Type |
      "RECORD" {IdentifierList ":" Type ";"} "END" .
  */
  private Type type() throws Exception {
    Type type = null;
    this.notify("Type");
    if (this.curr_token.isIdentifier()) {
      type = this.getType(this.match("identifier"));
    }
    else if (this.match_opt(this.ARRAY)) {
      type = this.array();
    }
    else if (this.match_opt(this.RECORD)) {
      type = this.record();
    }
    else {
      this.productionException("Type");
    }
    this.notify_end();
    return type;
  }

  // "ARRAY" Expression "OF" Type
  private Type array() throws Exception {
    Token token = this.match(this.ARRAY);
    Expression exp = this.expression();
    if (!this.checkPositiveConstant(exp)) {
      int pos1 = token.returnPos()[0];
      int pos2 = token.returnPos()[1];
      exp.getToken().setPos(pos1, pos2);
      this.expressionNotPositiveConstantException(exp, exp.getToken());
    }
    this.match(this.OF);
    Type array_type = this.type();
    int value = exp.returnNumber().returnVal();
    return new Array(array_type, new Constant(this.INTEGER, value));
  }

  // "RECORD" {IdentifierList ":" Type ";"} "END"
  private Type record() throws Exception {
    this.match(this.RECORD);
    // set Record's parent Scope to the current Scope
    Scope rec_scope = new Scope(this.curr_scope);
    // Set the current Scope to the new Scope
    this.curr_scope = rec_scope;
    while (this.curr_token.isIdentifier()) {
      ArrayList<Token> id_list = this.identifierList();
      this.match(this.COLON);
      Type curr_type = this.type();
      this.match(this.SEMICOLON);
      this.record_insert(id_list, curr_type);
    }
    this.match(this.END);
    Record rec = new Record(rec_scope);
    this.curr_scope = rec_scope.getParent();
    rec_scope.setParent(null);
    return rec;
  }

  // ["+"|"-"] Term {("+"|"-") Term} .
  private Expression expression() throws Exception {
    ArrayList<Token> list =
      new ArrayList<Token>(Arrays.asList(this.PLUS, this.MINUS));
    this.notify("Expression");
    Token zero_op = this.match(list);
    // First term here
    Expression left = this.term();
    // Operator next
    Token op = null;
    while ((op = this.match(list)) != null) {
      // Second term here
      Expression right = this.term();
      Binary binary = new Binary(op, left, right, this.INTEGER);
      // Constant folding here
      // If fold() returns null, means binary is not constant
      if ((left = binary.fold()) == null) {
        left = binary;
      }
    }
    // If + or -, we are either doing 0 + (Expression) or 0 - (Expression)
    if (zero_op != null) {
      Expression zero = new Expression(new Constant(this.INTEGER, 0),
        new Token("integer", "0", 0, 0));
      Binary binary = new Binary(zero_op, zero, left, this.INTEGER);
      // Constant folding here
      // If fold() returns null, means binary is not constant
      if ((left = binary.fold()) == null) {
        left = binary;
      }
    }
    this.notify_end();
    return left;
  }

  // Factor {("*"|"DIV"|"MOD") Factor} .
  private Expression term() throws Exception {
    ArrayList<Token> list =
      new ArrayList<Token>(Arrays.asList(this.STAR, this.DIV, this.MOD));
    this.notify("Term");
    Expression left = this.factor();
    Token op = null;
    while ((op = this.match(list)) != null) {
      Expression right = this.factor();
      Binary binary = new Binary(op, left, right, this.INTEGER);
      // Constant folding here
      // If fold() returns null, means binary is not constant
      if ((left = binary.fold()) == null) {
        left = binary;
      }
    }
    this.notify_end();
    return left;
  }

  // integer | Designator | "(" Expression ")" .
  private Expression factor() throws Exception {
    this.notify("Factor");
    Expression exp = null;
    if (this.curr_token.isInteger()) {
      Token token = this.match("integer");
      exp = new Expression(new Constant(
        this.INTEGER, token.returnIntVal()), token);
    } else if (this.curr_token.isIdentifier()) {
      if (this.isProcedure(this.curr_token)) {
        exp = this.functionCall();
      } else {
        // Either we have a Number or Location from a Variable
        Node node = this.designator();
        if (node.isLocation()) {
          exp = new Expression((Location) node);
        } else if (node.isExpression()) {
          exp = (Expression) node;
        }
      }
    } else if (this.match_opt(this.OPENPAR)) {
      exp = this.factor_expression();
    } else if (this.match_opt(this.OPENPAR)) {
      exp = this.factor_expression();
    } else {
      this.productionException("Factor");
    }
    this.notify_end();
    return exp;
  }

  // "(" Expression ")"
  private Expression factor_expression() throws Exception {
    this.match(this.OPENPAR);
    Expression exp = this.expression();
    this.match(this.CLOSEPAR);
    return exp;
  }

  // Instruction {";" Instruction} .
  private AST instructions() throws Exception {
    this.notify("Instructions");
    AST instructions = new AST(this.instruction());
    while (this.match_opt(this.SEMICOLON)) {
      this.match(this.SEMICOLON);
      instructions.addNode(this.instruction());
    }
    this.notify_end();
    return instructions;
  }

  // Assign | If | Repeat | While | Read | Write .
  private Instruction instruction() throws Exception {
    Instruction instr = null;
    this.notify("Instruction");
    if (this.curr_token.isIdentifier()) {
      if (this.isProcedure(this.curr_token)) {
        instr = this.procedureCall();
      } else {
        instr = this.assign();
      }
    } else if (this.match_opt(this.IF)) {
      instr = this.If();
    } else if (this.match_opt(this.REPEAT)) {
      instr = this.repeat();
    } else if (this.match_opt(this.WHILE)) {
      instr = this.While();
    } else if (this.match_opt(this.READ)) {
      instr = this.read();
    } else if (this.match_opt(this.WRITE)) {
      instr = this.write();
    } else {
      this.productionException("Instruction");
    }
    this.notify_end();
    return instr;
  }

  // Designator ":=" Expression .
  private Instruction assign() throws Exception {
    Instruction assign = null;
    this.notify("Assign");
    // First check that we have a variable, else throw exception
    this.getVariable(this.curr_token);
    Location loc = (Location) this.designator();
    this.match(this.COLONEQ);
    Expression exp = this.expression();
    // Check that the Location Type matches the Expression Type, else exception
    this.matchType(loc, exp);
    assign = new Assign(loc, exp);
    this.notify_end();
    return assign;
  }

  // "IF" Condition "THEN" Instructions ["ELSE" Instructions] "END" .
  private Instruction If() throws Exception {
    If if_ = null;
    this.notify("If");
    this.match(this.IF);
    Condition cond = this.condition();
    this.match(this.THEN);
    AST ins_true = this.instructions();
    if (this.match_opt(this.ELSE)) {
      this.match(this.ELSE);
      AST ins_false = this.instructions();
      if_ = new If(cond, ins_true, ins_false);
    } else {
      if_ = new If(cond, ins_true);
    }
    this.match(this.END);
    this.notify_end();
    return if_;
  }

  // "REPEAT" Instructions "UNTIL" Condition "END" .
  private Instruction repeat() throws Exception {
    this.notify("Repeat");
    this.match(this.REPEAT);
    AST instr = this.instructions();
    this.match(this.UNTIL);
    Condition cond = this.condition();
    Condition conv_cond = this.convertCond(cond);
    this.match(this.END);
    this.notify_end();
    Repeat repeat = new Repeat(cond, instr);
    repeat.setConvCond(conv_cond);
    return repeat;
  }

  // "WHILE" Condition "DO" Instructions "END" .
  private Instruction While() throws Exception {
    this.notify("While");
    this.match(this.WHILE);
    Condition cond = this.condition();
    this.match(this.DO);
    AST instr = this.instructions();
    this.match(this.END);
    this.notify_end();
    // Set a new Condition for the Repeat with a converted operator
    Condition repeat_cond = this.convertCond(cond);
    Repeat rep = new Repeat(repeat_cond, instr);
    rep.setConvCond(cond);
    AST repeat = new AST(rep);
    // Nest the Repeat inside an If
    return new If(cond, repeat);
  }

  // Expression ("="|"#"|"<"|">"|"<="|">=") Expression .
  private Condition condition() throws Exception {
    ArrayList<Token> list =
      new ArrayList<Token>(Arrays.asList
        (this.EQUAL, this.HASHTAG, this.LT, this.GT, this.LTEQ, this.GTEQ));
    this.notify("Condition");
    Expression left = this.expression();
    Token relation = this.match(list);
    Expression right = this.expression();
    this.notify_end();
    return new Condition(relation, left, right);
  }

  // "WRITE" Expression .
  private Instruction write() throws Exception {
    this.notify("Write");
    this.match(this.WRITE);
    Expression exp = this.expression();
    // Check that the Expression holds an Integer Type
    this.checkInteger(exp);
    this.notify_end();
    return new Write(exp);
  }

  // "READ" Designator .
  private Instruction read() throws Exception {
    this.notify("Read");
    this.match(this.READ);
    // Check that designator returns a Location
    Location loc = this.getLocation(this.designator());
    // Check that the Location holds a variable of Type Integer
    this.checkInteger(loc);
    this.notify_end();
    return new Read(loc);
  }

  private Instruction procedureCall() throws Exception {
    this.notify("Call");
    // TODO: Fix this
    Instruction call = null;
    ArrayList<Expression> exp_list = null;
    Token token = this.match("identifier");
    // Get Procedure Entry from S.T, else throw exception
    Procedure proc = this.getProcedure(token);
    if (proc.hasReturn()) {
      this.returnInProcedureException(token);
    }
    this.match(this.OPENPAR);
    // Optional ExpressionList
    try {
      exp_list = this.actuals();
    } catch (Exception e) {}
    this.match(this.CLOSEPAR);
    this.notify_end();
    call = new ProcedureCall(proc, exp_list, token);
    return call;
  }

  // identifier Selector .
  private Node designator() throws Exception {
    this.notify("Designator");
    // If current identifier is a Type, throw exception
    if (this.isType(this.curr_token)) {
      this.typeInDesignatorException(this.curr_token);
    }
    Node node = null;
    Token token = this.match("identifier");
    // Create a Location node if we have a variable.
    // Else, create an Expression with a Number if we have a constant
    if (this.isVariable(token)) {
      node = new Location(this.getVariable(token), token);
    } else {
      node = new Expression(this.getConstant(token), token);
    }
    Location select = this.selector(node);
    if (select != null) {
      node = select;
    }
    this.notify_end();
    return node;
  }

  // {"[" ExpressionList "]" | "." identifier} .
  private Location selector(Node node) throws Exception {
    this.notify("Selector");
    Location select = null;
    Node start = node;
    // Keep checking for an ExpressionList / .Identifier
    boolean done = false;
    while (this.match_opt(this.OPENBR) || this.match_opt(this.PERIOD)) {
      // Check that start is a Location, else throw exception
      select = this.getLocation(start);
      if (this.match_opt(this.OPENBR)) {
        // Check if select is a variable of type Array, else throw exception
        this.checkArray(select);
        select = this.selector_expression(select);
        start = select;
      } else if (this.match_opt(this.PERIOD)) {
        // Check if select is a Variable of type Record, else throw exception
        this.checkRecord(select);
        this.match(this.PERIOD);
        Token selector = this.match("identifier");
        // Check that the field of the record exists, else throw exception
        Field field = this.find_record(select, selector);
        select = new RecordField(select, field, selector);
      }
    }
    this.notify_end();
    return select;
  }

  // "[" ExpressionList "]"
  private Location selector_expression(Location loc) throws Exception {
    Location array = loc;
    this.match(this.OPENBR);
    ArrayList<Expression> exp_list = this.expressionList();
    for (Expression exp : exp_list) {
      // Check if select is a variable of type Array, else throw exception
      this.checkArray(array);
      Index start = new Index(array, exp);
      array = start;
    }
    this.match(this.CLOSEBR);
    return array;
  }

  // return all identifier names in a list in order to be added to
  // symbol table later
  // identifier {"," identifier} .
  private ArrayList<Token> identifierList() throws Exception {
    ArrayList<Token> id_list = new ArrayList<Token>();
    this.notify("IdentifierList");
    id_list.add(this.match("identifier"));
    while (this.match_opt(this.COMMA)) {
      this.match(this.COMMA);
      id_list.add(this.match("identifier"));
    }
    this.notify_end();
    return id_list;
  }

  // Expression {"," Expression} .
  private ArrayList<Expression> expressionList() throws Exception {
    ArrayList<Expression> exp_list = new ArrayList<Expression>();
    this.notify("ExpressionList");
    exp_list.add(this.expression());
    while (this.match_opt(this.COMMA)) {
      this.match(this.COMMA);
      exp_list.add(this.expression());
    }
    this.notify_end();
    return exp_list;
  }

  public void parse() throws Exception {
    // Create the concrete syntax tree, symbol table, abstract syntax tree
    this.program();
  }

  public Scope returnST() {
    return this.curr_scope;
  }

  public AST returnAST() {
    return this.ast != null ? this.ast : null;
  }

  public Environment returnEnv() throws Exception {
    if (this.ast != null) {
      this.ast.interpret(this.env);
      return this.env;
    } else {
      return null;
    }
  }

  public String generateCode() throws Exception {
    this.gen = new Generator(this.curr_scope, this.env, this.ast);
    return this.gen.generateCode();
  }

  // debug function
  private void print(String string) {
    System.out.println(string);
  }
}
