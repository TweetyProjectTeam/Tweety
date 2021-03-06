/*
 *  This file is part of "TweetyProject", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  TweetyProject is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2016 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
/* Generated By:JavaCC: Do not edit this line. RpclCondensedProbabilityDistributionParser.java */
package org.tweetyproject.logics.rpcl.parser.rpclcondensedprobabilitydistributionparser;

import java.io.*;
import java.util.*;

import org.tweetyproject.commons.*;
import org.tweetyproject.commons.util.*;
import org.tweetyproject.logics.commons.syntax.Constant;
import org.tweetyproject.logics.commons.syntax.Predicate;
import org.tweetyproject.logics.fol.syntax.*;
import org.tweetyproject.logics.rpcl.semantics.*;
import org.tweetyproject.math.probability.*;

/*
 * This class implements a parser for condensed relational probability distributions. The BNF for 
 * condensed relational probability distributions is given by (start symbol is DISTRIBUTION)
 * <br>
 * <br>DISTRIBUTION				::== (PROBABILITYASSIGNMENT)*
 * <br>PROBABILITYASSIGNMENT	::== INTERPRETATION "=" PROBABILITY
 * <br>INTERPRETATION			::== "{" (INSTANCEASSIGNMENT ("," INSTANCEASSIGNMENT)*)? "}"
 * <br>INSTANCEASSIGNMENT		::== "&lt;" PREDICATE "," "{" (CONSTANTSET "=" MULTIPLICATOR ("," CONSTANTSET "=" MULTIPLICATOR)*)?  "}" "&gt;"
 * <br>CONSTANTSET				::== "{" CONSTANT ("," CONSTANT)* "}"
 * <br>
 * <br>PREDICATE is a sequence of symbols from {a,...,z,A,...,Z,0,...,9} with a lowercase letter at the beginning.<br>
 * <br>CONSTANT is a sequence of symbols from {a,...,z,A,...,Z,0,...,9} with a lowercase letter at the beginning.<br>
 * <br>MULTIPLICATOR is a natural number.<br>
 */
@SuppressWarnings("all")
public class RpclCondensedProbabilityDistributionParser implements RpclCondensedProbabilityDistributionParserConstants {

        /*
     * The semantics used for the distribution to be read. 
     */
        private RpclSemantics semantics;

        /*
	 * The signature for this parser (if one has been given)
	 */
        private FolSignature signature = null;

        public RpclCondensedProbabilityDistributionParser(){
        }

        public RpclCondensedProbabilityDistributionParser(RpclSemantics semantics){
                this(semantics,null);
        }

        public RpclCondensedProbabilityDistributionParser(RpclSemantics semantics, FolSignature signature){
                this.semantics = semantics;
                this.signature = signature;
        }

        public void setSemantics(RpclSemantics semantics){
                this.semantics = semantics;
        }

        public void setSignature(FolSignature signature){
                this.signature = signature;
        }

        public CondensedProbabilityDistribution parseCondensedProbabilityDistribution(Reader reader) throws ParserException{
                try
                {
                        RpclCondensedProbabilityDistributionParser theParser = new RpclCondensedProbabilityDistributionParser(reader);
                        return RpclCondensedProbabilityDistributionParser.Distribution(this.semantics, this.signature);
                }catch(ParseException e){
                        throw new ParserException(e);
                }
        }

  static final public CondensedProbabilityDistribution Distribution(RpclSemantics semantics, FolSignature signature) throws ParseException {
        Set<Pair<ReferenceWorld,Probability>> assignments = new HashSet<Pair<ReferenceWorld,Probability>>();
        Pair<ReferenceWorld,Probability> assignment;
                if(signature == null)
                        signature = new FolSignature();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 9:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      assignment = ProbabilityAssignment(signature);
                assignments.add(assignment);
    }
    jj_consume_token(0);
        CondensedProbabilityDistribution distribution = new CondensedProbabilityDistribution(semantics,signature);
        for(Pair<ReferenceWorld,Probability> a: assignments)
                distribution.put(a.getFirst(),a.getSecond());
        {if (true) return distribution;}
    throw new Error("Missing return statement in function");
  }

  static final public Pair<ReferenceWorld,Probability> ProbabilityAssignment(FolSignature signature) throws ParseException {
        ReferenceWorld world;
        Token probability;
    // NOTE: probability might be "0" or "1" => maybe parse as multiplicator
            world = Interpretation(signature);
    jj_consume_token(8);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PROBABILITY:
      probability = jj_consume_token(PROBABILITY);
      break;
    case MULTIPLICATOR:
      probability = jj_consume_token(MULTIPLICATOR);
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                {if (true) return new Pair<ReferenceWorld,Probability>(world,new Probability(new Double(token.image)));}
    throw new Error("Missing return statement in function");
  }

  static final public ReferenceWorld Interpretation(FolSignature signature) throws ParseException {
        Set<InstanceAssignment> assignments = new HashSet<InstanceAssignment>();
        InstanceAssignment assignment;
        Collection<? extends Collection<? extends Constant>> equivalenceClasses = new HashSet<Collection<Constant>>();
        Set<Predicate> predicates = new HashSet<Predicate>();
    jj_consume_token(9);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 12:
      assignment = InstanceAssignment(signature);
                assignments.add(assignment);
                equivalenceClasses = assignment.keySet();
                predicates.add(assignment.getPredicate());
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 10:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_2;
        }
        jj_consume_token(10);
        assignment = InstanceAssignment(signature);
                assignments.add(assignment);
                predicates.add(assignment.getPredicate());
      }
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    jj_consume_token(11);
                ReferenceWorld world = new ReferenceWorld(equivalenceClasses,predicates);
                for(InstanceAssignment a: assignments)
                        world.put(a.getPredicate(),a);
                {if (true) return world;}
    throw new Error("Missing return statement in function");
  }

  static final public InstanceAssignment InstanceAssignment(FolSignature signature) throws ParseException {
        Token predicate;
        Token multiplicator;
        Set<Constant> constantSet;
        InstanceAssignment assignment;
    jj_consume_token(12);
    predicate = jj_consume_token(STRUCTURENAME);
                if(signature.containsPredicate(predicate.image)){
                        Predicate p = signature.getPredicate(predicate.image);
                        assignment = new InstanceAssignment(p);
                }else{
                        Predicate p = new Predicate(predicate.image);
                        signature.add(p);
                        assignment = new InstanceAssignment(p);
                }
    jj_consume_token(10);
    jj_consume_token(9);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 9:
      constantSet = ConstantSet(signature);
      jj_consume_token(8);
      multiplicator = jj_consume_token(MULTIPLICATOR);
                assignment.put(constantSet, new Integer(multiplicator.image));
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 10:
          ;
          break;
        default:
          jj_la1[4] = jj_gen;
          break label_3;
        }
        jj_consume_token(10);
        constantSet = ConstantSet(signature);
        jj_consume_token(8);
        multiplicator = jj_consume_token(MULTIPLICATOR);
                assignment.put(constantSet, new Integer(multiplicator.image));
      }
      break;
    default:
      jj_la1[5] = jj_gen;
      ;
    }
    jj_consume_token(11);
    jj_consume_token(13);
                {if (true) return assignment;}
    throw new Error("Missing return statement in function");
  }

  static final public Set<Constant> ConstantSet(FolSignature signature) throws ParseException {
        Token constantName;
        Set<Constant> constants = new HashSet<Constant>();
    jj_consume_token(9);
    constantName = jj_consume_token(STRUCTURENAME);
                if(signature.containsConstant(constantName.image)){
                        constants.add(signature.getConstant(constantName.image));
                }else{
                        Constant c = new Constant(constantName.image);
                        signature.add(c);
                        constants.add(c);
                }
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 10:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_4;
      }
      jj_consume_token(10);
      constantName = jj_consume_token(STRUCTURENAME);
                if(signature.containsConstant(constantName.image)){
                        constants.add(signature.getConstant(constantName.image));
                }else{
                        Constant c = new Constant(constantName.image);
                        signature.add(c);
                        constants.add(c);
                }
    }
    jj_consume_token(11);
                {if (true) return constants;}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  /* Generated Token Manager. */
  static public RpclCondensedProbabilityDistributionParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /* Current token. */
  static public Token token;
  /* Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[7];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x200,0xc0,0x400,0x1000,0x400,0x200,0x400,};
   }

  /* Constructor with InputStream. */
  public RpclCondensedProbabilityDistributionParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /* Constructor with InputStream and supplied encoding */
  public RpclCondensedProbabilityDistributionParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new RpclCondensedProbabilityDistributionParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /* Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /* Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    RpclCondensedProbabilityDistributionParserTokenManager.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /* Constructor. */
  public RpclCondensedProbabilityDistributionParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new RpclCondensedProbabilityDistributionParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /* Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    RpclCondensedProbabilityDistributionParserTokenManager.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /* Constructor with generated Token Manager. */
  public RpclCondensedProbabilityDistributionParser(RpclCondensedProbabilityDistributionParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /* Reinitialise. */
  public void ReInit(RpclCondensedProbabilityDistributionParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = RpclCondensedProbabilityDistributionParserTokenManager.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/* Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = RpclCondensedProbabilityDistributionParserTokenManager.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/* Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = RpclCondensedProbabilityDistributionParserTokenManager.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=RpclCondensedProbabilityDistributionParserTokenManager.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /* Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[14];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 7; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 14; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /* Enable tracing. */
  static final public void enable_tracing() {
  }

  /* Disable tracing. */
  static final public void disable_tracing() {
  }

}
