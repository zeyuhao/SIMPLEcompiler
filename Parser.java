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
  private static final int curr_val = 5; // the current value, 5 for now

  private ArrayList<Token> tokens; // list of tokens we are parsing
  private Observer obs; // Observer to attach to this Parser
  private int position; // current position in token list
  private Token curr_token; // current token we are parsing
  private Scope universe; // universe Scope
  private Scope program; // program Scope
  private Scope curr_scope; // current Scope
  private Type INTEGER; // single instance of Integer class
  private AST ast; // instance of Abstract Syntax Tree


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
    if (this.curr_token.returnType().equals(token.returnType()) &&
        this.curr_token.returnVal().equals(token.returnVal())) {
      this.notify(token);
      this.next_token();
    } else {
      this.tokenException(token.returnVal());
    }
    return token;
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
        this.match(token);
        return token;
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
    int start = this.curr_token.returnPosition()[0];
    int end = this.curr_token.returnPosition()[1];
    throw new Exception("identifier after PROGRAM and " +
      "identifier after final END must be identical. Expected " +
      begin.returnVal() + " but found " + last.toString());
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

  private void identifierNotArrayException(Location loc, Token token)
    throws Exception {
    throw new Exception("Element " + token.toString() + " of Variable " +
      loc.toString() + " is not an Array");
  }

  private void identifierNotRecordException(Location loc, Token token)
    throws Exception {
    throw new Exception("Element " + token.toString() + " of Variable " +
      loc.toString() + " is not a Record");
  }

  private void identifierNotIntegerException(Token token) throws Exception {
    throw new Exception("Variable " + token.toString() + " is not an Integer");
  }


  private void typeInDesignatorException(Token token) throws Exception {
    throw new Exception("Type " + token.toString() + " found in designator");
  }

  private void illegalSelectorException(Token record, Token selector)
    throws Exception {
    throw new Exception("Selector " + selector.toString() + " of record "
                        + record.toString() + " does not exist");
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

  /**
   * Checks that the given Selector of a Record exists.
   * If not, throw an exception
   */
  private void find_record(Location loc, Token selector) throws Exception {
    Record rec = (Record) this.find_entry(loc.getToken()).getType();
    if (!rec.getFields().local(selector.returnVal())) {
      this.illegalSelectorException(loc.getToken(), selector);
    }
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

  private void isArray(Location loc) throws Exception {
    if (!loc.getType().returnType().equals("array")) {
      this.identifierNotArrayException(loc, loc.getToken());
    }
  }

  private void isRecord(Location loc) throws Exception {
    if (!loc.getType().returnType().equals("record")) {
      this.identifierNotRecordException(loc, loc.getToken());
    }
  }

  private void isInteger(Location loc) throws Exception {
    if (!loc.getType().returnType().equals("integer")) {
      this.identifierNotIntegerException(loc.getToken());
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
    if (this.match_opt(this.BEGIN)) {
      this.match(this.BEGIN);
      this.instructions();
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
      // Expression is required
      this.expression();
      // ; is required
      this.match(this.SEMICOLON);
      // Insert a new ConstDecl into symbol table
      this.table_insert(curr, new Constant(this.INTEGER, this.curr_val));
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
    this.match(this.ARRAY);
    this.expression();
    this.match(this.OF);
    Type array_type = this.type();
    return new Array(array_type, new Constant(this.INTEGER, this.curr_val));
  }

  // "RECORD" {IdentifierList ":" Type ";"} "END"
  private Type record() throws Exception {
    this.match(this.RECORD);
    // set Record's outer Scope to the current Scope
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
    // If + or -, we are either doing 0 + (Expression) or 0 - (Expression)
    Token zero_op = this.match(list);
    Expression left = this.term();
    Token op = null;
    while ((op = this.match(list)) != null) {
      Expression right = this.term();
      Binary binary = new Binary(op, left, right);
      left = binary;
    }
    if (zero_op != null) {
      Expression zero = new Expression(new Constant(this.INTEGER, 0),
        new Token("integer", "0", 0, 0));
      left = new Binary(zero_op, zero, left);
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
      Binary binary = new Binary(op, left, right);
      left = binary;
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
      // Either we have an Number or Location from a Variable
      Node node = this.designator();
      if (node.nodeType().equals("location")) {
        exp = new Expression((Location) node);
      } else if (node.nodeType().equals("expression")) {
        exp = (Expression) node;
      }
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
  private void instructions() throws Exception {
    this.notify("Instructions");
    this.instruction();
    while (this.match_opt(this.SEMICOLON)) {
      this.match(this.SEMICOLON);
      this.instruction();
    }
    this.notify_end();
  }

  // Assign | If | Repeat | While | Read | Write .
  private void instruction() throws Exception {
    this.notify("Instruction");
    if (this.curr_token.isIdentifier()) {
      this.assign();
    } else if (this.match_opt(this.IF)) {
      this.If();
    } else if (this.match_opt(this.REPEAT)) {
      this.repeat();
    } else if (this.match_opt(this.WHILE)) {
      this.While();
    } else if (this.match_opt(this.READ)) {
      this.read();
    } else if (this.match_opt(this.WRITE)) {
      this.write();
    } else {
      this.productionException("Instruction");
    }
    this.notify_end();
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
    assign = new Assign(loc, exp);
    this.notify_end();
    return assign;
  }

  // "IF" Condition "THEN" Instructions ["ELSE" Instructions] "END" .
  private void If() throws Exception {
    this.notify("If");
    this.match(this.IF);
    this.condition();
    this.match(this.THEN);
    this.instructions();
    if (this.match_opt(this.ELSE)) {
      this.match(this.ELSE);
      this.instructions();
    }
    this.match(this.END);
    this.notify_end();
  }

  // "REPEAT" Instructions "UNTIL" Condition "END" .
  private void repeat() throws Exception {
    this.notify("Repeat");
    this.match(this.REPEAT);
    this.instructions();
    this.match(this.UNTIL);
    this.condition();
    this.match(this.END);
    this.notify_end();
  }

  // "WHILE" Condition "DO" Instructions "END" .
  private void While() throws Exception {
    this.notify("While");
    this.match(this.WHILE);
    this.condition();
    this.match(this.DO);
    this.instructions();
    this.match(this.END);
    this.notify_end();
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
  private void write() throws Exception {
    this.notify("Write");
    this.match(this.WRITE);
    this.expression();
    this.notify_end();
  }

  // "READ" Designator .
  private void read() throws Exception {
    this.notify("Read");
    this.match(this.READ);
    this.designator();
    this.notify_end();
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
    // Create a Variable node if we have a variable
    if (this.isVariable(token)) {
      node = new Location(this.getVariable(token), token);
    }
    // Create an Expression with a Number if we have a constant
    else {
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
    // Keep checking for an ExpressionList / .Identifier
    boolean done = false;
    Node start = node;
    while (this.match_opt(this.OPENBR) || this.match_opt(this.PERIOD)) {
      // Check that node is a Variable, else throw exception
      this.getVariable(node.getToken());
      select = (Location) start;
      if (this.match_opt(this.OPENBR)) {
        // Check if select is a variable of type Array, else throw exception
        this.isArray(select);
        select = this.selector_expression(select);
        start = select;
      } else if (this.match_opt(this.PERIOD)) {
        // Check if loc is a variable of type Record, else throw exception
        this.isRecord(select);
        this.match(this.PERIOD);
        this.match("identifier");
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
      this.isArray(array);
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
    this.program();
  }

  public String toString() {
    return this.curr_scope.toString();
  }
}
