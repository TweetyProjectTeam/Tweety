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
 package org.tweetyproject.arg.aspic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.junit.Test;

import org.tweetyproject.arg.aspic.order.LastLinkOrder;
import org.tweetyproject.arg.aspic.order.RuleComparator;
import org.tweetyproject.arg.aspic.order.SimpleAspicOrder;
import org.tweetyproject.arg.aspic.order.WeakestLinkOrder;
import org.tweetyproject.arg.aspic.parser.AspicParser;
import org.tweetyproject.arg.aspic.reasoner.DirectionalAspicReasoner;
import org.tweetyproject.arg.aspic.reasoner.SimpleAspicReasoner;
import org.tweetyproject.arg.aspic.ruleformulagenerator.FolFormulaGenerator;
import org.tweetyproject.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import org.tweetyproject.arg.aspic.semantics.AspicAttack;
import org.tweetyproject.arg.aspic.syntax.AspicArgument;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.aspic.syntax.DefeasibleInferenceRule;
import org.tweetyproject.arg.aspic.syntax.InferenceRule;
import org.tweetyproject.arg.aspic.syntax.StrictInferenceRule;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Semantics;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.commons.InferenceMode;
import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.fol.parser.FolParser;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.Proposition;
import org.tweetyproject.logics.pl.syntax.PlFormula;

/**
 * @author Nils Geilen Several JUnit test for the package arg.aspic
 */
public class AspicTest {

	/**
	 * Implements examples 3.7, 3.9 and 3.11 from Modgil and Prakken
	 * @throws Exception if something goes wrong
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void Example1() throws Exception {
		AspicParser<PlFormula> parser = new AspicParser<>(new PlParser(), new PlFormulaGenerator());
		AspicArgumentationTheory<PlFormula> at = parser
				.parseBeliefBaseFromFile(AspicTest.class.getResource("/ex1.aspic").getFile());

		AspicArgument<PlFormula> A1 = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("->p"));
		AspicArgument<PlFormula> A2 = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("d1: p => q"));
		A2.addDirectSub(A1);
		AspicArgument<PlFormula> A3 = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("s1: p, q -> r"));
		A3.addDirectSub(A1);
		A3.addDirectSub(A2);
		AspicArgument<PlFormula> A4 = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("s1: p, q -> r"));
		A4.addDirectSub(A2);
		A4.addDirectSub(A1);

		Collection<AspicArgument<PlFormula>> args = at.getArguments();

		// example 3.7
		assertTrue(args.contains(A3));
		assertTrue(args.contains(A4));

		// example 3.9
		assertTrue(A1.isStrict());
		assertTrue(A1.isFirm());
		assertFalse(A2.isStrict());
		assertTrue(A2.isFirm());
		assertFalse(A3.isStrict());
		assertTrue(A3.isFirm());

		DungTheory dt = at.asDungTheory();

		// example 3.11
		AspicArgument<PlFormula> B1 = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("=>s"));
		AspicArgument<PlFormula> B2 = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("d2: s => t"));
		B2.addDirectSub(B1);
		AspicArgument<PlFormula> B3 = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("d3: t => ! d1"));
		B3.addDirectSub(B2);
		for (Attack attack : dt.getAttacks())
			if (attack.getAttacked().equals(A3))
				assertTrue(attack.getAttacker().equals(B3));
	}

	/**
	 * Implements example 3.25 from Modgil and Prakken
	 * @throws Exception if something goes wrong
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void Example2() throws Exception {
		// example 3.25
		PlParser plparser = new PlParser();
		AspicParser<PlFormula> parser = new AspicParser<>(plparser, new PlFormulaGenerator());
		AspicArgumentationTheory<PlFormula> at = parser
				.parseBeliefBaseFromFile(AspicTest.class.getResource("/ex2.aspic").getFile());

		InferenceRule<PlFormula> snores = (InferenceRule<PlFormula>) parser
				.parseFormula("p1: => Snores"),
				professor = (InferenceRule<PlFormula>) parser.parseFormula("p2: => Professor");
		Comparator<InferenceRule<PlFormula>> prem_comp = new RuleComparator<>(Arrays.asList("p1", "p2")),
				rule_comp = new RuleComparator<>(Arrays.asList("d1", "d3", "d2"));
		assertTrue(prem_comp.compare(snores, professor) < 0);

		Collection<AspicArgument<PlFormula>> args = at.getArguments();
		AspicArgument<PlFormula> A3 = null, B1 = null;
		PlFormula access = (PlFormula) plparser.parseFormula("!AccessDenied"),
				no_access = (PlFormula) plparser.parseFormula("AccessDenied");
		for (AspicArgument<PlFormula> arg : args)
			if (arg.getConclusion().equals(access))
				B1 = arg;
			else if (arg.getConclusion().equals(no_access))
				A3 = arg;
		assertTrue(A3 != null);
		assertTrue(B1 != null);

		at.setOrder(new LastLinkOrder<>(rule_comp, prem_comp, true));
		DungTheory dt = at.asDungTheory();
		assertTrue(((AspicArgument<PlFormula>) dt.getAttacks().iterator().next().getAttacker())
				.getConclusion().equals(no_access));

		at.setOrder(new WeakestLinkOrder<>(rule_comp, prem_comp, true));
		dt = at.asDungTheory();
		assertTrue(((AspicArgument<PlFormula>) dt.getAttacks().iterator().next().getAttacker())
				.getConclusion().equals(access));
	}

	/**
	 * Implements example 3.26 from Modgil and Prakken
	 * @throws Exception if something goes wrong
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void Example3() throws Exception {
		// example 3.26
		PlParser plparser = new PlParser();
		AspicParser<PlFormula> parser = new AspicParser<>(plparser, new PlFormulaGenerator());
		AspicArgumentationTheory<PlFormula> at = parser
				.parseBeliefBaseFromFile(AspicTest.class.getResource("/ex3.aspic").getFile());

		Comparator<InferenceRule<PlFormula>> rule_comp = new RuleComparator<>(
				Arrays.asList("d1", "d3", "d2"));

		Collection<AspicArgument<PlFormula>> args = at.getArguments();
		AspicArgument<PlFormula> B2 = null;
		PlFormula not = (PlFormula) plparser.parseFormula("! LikesWhisky");
		for (AspicArgument<PlFormula> arg : args)
			if (arg.getConclusion().equals(not))
				B2 = arg;
		assertTrue(B2 != null);

		at.setOrder(new WeakestLinkOrder<>(rule_comp, new RuleComparator<>(new ArrayList<>()), true));
		DungTheory dt = at.asDungTheory();
		assertTrue(((AspicArgument<PlFormula>) dt.getAttacks().iterator().next().getAttacker())
				.getConclusion().equals(not));
	}

	/**
	 * Implements example 4.1 from Modgil and Prakken
	 * @throws Exception if something goes wrong
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void Example4() throws Exception {
		// example 4.1
		PlParser plparser = new PlParser();
		AspicParser<PlFormula> parser = new AspicParser<>(plparser, new PlFormulaGenerator());
		AspicArgumentationTheory<PlFormula> at = parser
				.parseBeliefBaseFromFile(AspicTest.class.getResource("/ex4.aspic").getFile());

		DungTheory dt = at.asDungTheory();
		assertTrue(dt.getAttacks().size() == 4);
		for (Attack a : dt.getAttacks())
			assertFalse(((AspicArgument<PlFormula>) a.getAttacker()).getTopRule().isDefeasible());
	}

	@Test
	public void ManualTest() {
		Proposition a = new Proposition("a");
		Proposition b = new Proposition("b");
		Proposition c = new Proposition("c");

		AspicArgumentationTheory<PlFormula> t = new AspicArgumentationTheory<>(new PlFormulaGenerator());

		DefeasibleInferenceRule<PlFormula> r1 = new DefeasibleInferenceRule<>();
		r1.setConclusion(a);
		r1.addPremise(b);
		r1.addPremise(c);
		t.addRule(r1);

		StrictInferenceRule<PlFormula> s1 = new StrictInferenceRule<PlFormula>();
		s1.setConclusion(b);
		t.addRule(s1);

		s1 = new StrictInferenceRule<PlFormula>();
		s1.setConclusion(c);
		t.addRule(s1);

		System.out.println(t);

		System.out.println(t.asDungTheory());
	}

	@Test
	public void ComplementTest() throws Exception {
		PlFormula f = new Proposition("a");
		assertTrue(f.equals(f.complement().complement()));
	}

	@Test
	public void ParserTest1() throws Exception {
		FolParser folparser = new FolParser();
		String folbsp = "Animal = {horse, cow, lion} \n" + "type(Tame(Animal)) \n" + "type(Ridable(Animal)) \n";
		folparser.parseBeliefBase(folbsp);
		AspicParser<FolFormula> aspicparser = new AspicParser<>(folparser, folfg);
		aspicparser.setSymbolComma(";");
		aspicparser.setSymbolDefeasible("==>");
		aspicparser.setSymbolStrict("-->");
		String aspicbsp = "d1: Tame(cow) ==> Ridable(cow)\n" + "s1 : Tame(horse) && Ridable(lion) --> Tame(horse)";
		AspicArgumentationTheory<FolFormula> aat = aspicparser.parseBeliefBase(aspicbsp);
		assertTrue(aat.size() == 2);
	}

	@Test
	public void ParserTest2() throws Exception {
		PlParser plparser = new PlParser();
		AspicParser<FolFormula> aspicparser = new AspicParser<>(plparser, folfg);
		aspicparser.setSymbolComma(";");
		aspicparser.setSymbolDefeasible("==>");
		aspicparser.setSymbolStrict("-->");
		String aspicbsp = "d1: a ==> b\n" + "s1 : c; d ==> e \n" + "d ; r --> a";
		AspicArgumentationTheory<FolFormula> aat = aspicparser.parseBeliefBase(aspicbsp);
		assertTrue(aat.size() == 3);
	}

	@Test
	public void ArgSysTest() throws Exception {
		AspicParser<PlFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "-> a \n => b \n b,c =>d \n a-> e \n b -> e \n e, b=> f \n a, f -> g";
		AspicArgumentationTheory<PlFormula> at = parser.parseBeliefBase(input);
		Collection<AspicArgument<PlFormula>> args = at.getArguments();
		// for(AspicArgument<PropositionalFormula> a:args)
		// System.out.println(a);
		assertTrue(args.size() == 8);
		for (AspicArgument<PlFormula> a : args)
			if (a.getConclusion().equals(new Proposition("f")) || a.getConclusion().equals(new Proposition("g")))
				assertTrue(a.hasDefeasibleSub());
			else
				assertFalse(a.hasDefeasibleSub());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void AttackTest() throws Exception {
		AspicParser<PlFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "=> ! a \n" + " => a \n" + "-> ! b \n" + "-> b \n" + "a,b->c\n";
		AspicArgumentationTheory<PlFormula> at = parser.parseBeliefBase(input);
		Collection<AspicArgument<PlFormula>> args = at.getArguments();

		AspicArgument<PlFormula> not_a = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("=> ! a"));
		AspicArgument<PlFormula> arg_a = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("=> a"));
		AspicArgument<PlFormula> not_b = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("-> ! b"));
		AspicArgument<PlFormula> not_c = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("-> ! c"));
		AspicArgument<PlFormula> ab_mapsto_c = new AspicArgument<>(
				(InferenceRule<PlFormula>) parser.parseFormula("a,b->c"));
		assertTrue(args.contains(not_a));
		assertTrue(args.contains(not_b));
		assertFalse(args.contains(not_c));
		assertFalse(args.contains(ab_mapsto_c));

		int sum = 0;
		for (AspicArgument<PlFormula> arg : args) {
			if (AspicAttack.isAttack(not_a, arg, null, null))
				sum++;
		}
		assertTrue(sum == 2);
		for (AspicArgument<PlFormula> arg : args) {
			assertFalse(AspicAttack.isAttack(not_b, arg, null, null));
		}
		for (AspicArgument<PlFormula> arg : args) {
			if (arg.equals(not_a))
				assertTrue(AspicAttack.isAttack(arg_a, arg, null, null));
			else
				assertFalse(AspicAttack.isAttack(arg_a, arg, null, null));
		}

	}

	final PlFormulaGenerator pfg = new PlFormulaGenerator();
	final FolFormulaGenerator folfg = new FolFormulaGenerator();

	@SuppressWarnings("unchecked")
	@Test
	public void PropositionalFormulaGeneratorTest() throws Exception {
		AspicParser<PlFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "-> a \n" + "d1: a => b \n" + "d2: a => !d1 \n" + "s1: a -> e \n" + "s2: a -> !s1";
		AspicArgumentationTheory<PlFormula> at = parser.parseBeliefBase(input);
		DungTheory dt = at.asDungTheory();
		assertTrue(dt.getAttacks().size() == 1);
		assertTrue(((AspicArgument<PlFormula>) dt.getAttacks().iterator().next().getAttacked())
				.getConclusion().equals(new Proposition("b")));
	}

	
//	@SuppressWarnings("unchecked")
//	@Test
//	public void FolFormulaGeneratorTest() throws Exception {
//		FolParser parser = new FolParser();
//		String kb = "Rule = {d1,d2,s1,s2} \n" + "type(a) \n type(b) \n type(c) \n type(e) \n" + "type(__rule(Rule)) \n";
//		parser.parseBeliefBase(kb);
//		AspicParser<FolFormula> aspicparser = new AspicParser<>(parser, folfg);
//		String input = "-> a \n" + "d1: a => b \n" + "d2: a => !__rule(d1) \n" + "s1: a -> e \n"
//				+ "s2: a -> !__rule(s1)";
//		AspicArgumentationTheory<FolFormula> at = aspicparser.parseBeliefBase(input);
//		DungTheory dt = at.asDungTheory();
//		assertTrue(dt.getAttacks().size() == 1);
//		assertTrue(((AspicArgument<FolFormula>) dt.getAttacks().iterator().next().getAttacked()).getConclusion()
//				.equals(new FOLAtom(new Predicate("b"))));
//	}

	@SuppressWarnings("unchecked")
	@Test
	public void SimpleOrderTest() throws Exception {
		AspicParser<PlFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "=> BornInScotland\n" + " => FitnessLover \n" + "d1: BornInScotland => Scottish \n"
				+ "d2: Scottish => LikesWhiskey \n" + "d3: FitnessLover => ! LikesWhiskey\n", order = "d1<d3<d2";
		AspicArgumentationTheory<PlFormula> at = parser.parseBeliefBase(input + order);

		DungTheory dt = at.asDungTheory();
		assertTrue(dt.getNodes().size() == 5);
		assertTrue(dt.getAttacks().size() == 1);
		assertTrue(((AspicArgument<PlFormula>) dt.getAttacks().iterator().next().getAttacker()).getTopRule()
				.getName().equals("d2"));

		Comparator<AspicArgument<PlFormula>> new_order = parser.parseSimpleOrder("d1<d2<d3");
		at.setOrder(new_order);
		dt = at.asDungTheory();
		assertTrue(dt.getNodes().size() == 5);
		assertTrue(dt.getAttacks().size() == 1);
		assertTrue(((AspicArgument<PlFormula>) dt.getAttacks().iterator().next().getAttacker()).getTopRule()
				.getName().equals("d3"));

		at.setOrder(new SimpleAspicOrder<>());
		dt = at.asDungTheory();
		assertTrue(dt.getNodes().size() == 5);
		assertTrue(dt.getAttacks().size() == 2);

		at = parser.parseBeliefBase(input);
		at.setRuleFormulaGenerator(pfg);
		dt = at.asDungTheory();
		assertTrue(dt.getNodes().size() == 5);
		assertTrue(dt.getAttacks().size() == 2);
	}

	
	@Test
	public void ReasonerTest2() throws Exception {
		PlParser plparser = new PlParser();
		AspicParser<PlFormula> parser = new AspicParser<>(plparser, new PlFormulaGenerator());
		AspicArgumentationTheory<PlFormula> at = parser
				.parseBeliefBaseFromFile(AspicTest.class.getResource("/ex1.aspic").getFile());
		
		SimpleAspicReasoner<PlFormula> ar = new SimpleAspicReasoner<PlFormula>(AbstractExtensionReasoner.getSimpleReasonerForSemantics(Semantics.CONFLICTFREE_SEMANTICS));

		PlFormula pf = (PlFormula)plparser.parseFormula("p");

		System.out.println(pf);
		assertTrue(ar.query(at,pf,InferenceMode.CREDULOUS));
	}
	
	@Test
	public void directionalReasonerTest() throws Exception {

		String full, partial;
		
		full    = "=> a \n d1: a => b \n -> c \n s1: c -> ! d1 \n => !c \n => !a";
		partial = "=> a \n d1: a => b \n -> c \n s1: c -> ! d1          \n => !a";
		assertTrue(testDirectionalEquality("b", full, partial));

		full    = "=> a \n d1: a => b \n -> c \n s1: c -> ! d1 \n => !c \n => !a";
		partial = "=> a \n d1: a => b \n -> c \n s1: c -> ! d1                  ";
		assertFalse(testDirectionalEquality("b", full, partial));

		full    = "=> a \n d1: a => b \n -> c \n s1: c -> ! d1 \n => !c \n => !a";
		partial = "=> a \n d1: a => b         \n s1: c -> ! d1 \n => !a";
		assertFalse(testDirectionalEquality("b", full, partial));

		full    = "=> a \n d1: a => b \n -> c \n s1: c -> ! d1 \n => !c \n => !a";
		partial = "                      -> c                  ";
		assertTrue(testDirectionalEquality("c", full, partial));

		full    = "=> a \n d1: a => b \n -> c \n s1: c -> ! d1 \n => !c \n => !a";
		partial = "                      -> c                  \n => !c";
		assertTrue(testDirectionalEquality("!c", full, partial));
		
		full    = "=> a \n d1: a => b \n -> c \n s1: c -> ! d1 \n => !c \n => !a";
		partial = "                                            \n => !c";
		assertFalse(testDirectionalEquality("!c", full, partial));
		
		full    = "-> a \n d1: a => b \n => ! b \n => ! a";
		partial = "-> a \n d1: a => b \n => ! b";
		assertTrue(testDirectionalEquality("b", full, partial));

	}
	
	private boolean testDirectionalEquality(String query, String full, String partial) throws ParserException, IOException {

		PlParser plparser = new PlParser();
		AspicParser<PlFormula> parser = new AspicParser<>(plparser, new PlFormulaGenerator());
		DirectionalAspicReasoner<PlFormula> directionalReasoner = new DirectionalAspicReasoner<PlFormula>(AbstractExtensionReasoner.getSimpleReasonerForSemantics(Semantics.GR));
		PlFormula queryFormula = (PlFormula)plparser.parseFormula(query);
		
		// Generate full
		AspicArgumentationTheory<PlFormula> fullAt = parser.parseBeliefBase(full);
		DungTheory dt = directionalReasoner.asRestrictedDungTheory(fullAt, false, queryFormula);
		Collection<Argument> fullArgs = new ArrayList<Argument>();
		fullArgs.addAll(dt);

		// Generate partial
		AspicArgumentationTheory<PlFormula> partialAt = parser.parseBeliefBase(partial);
		dt = directionalReasoner.asRestrictedDungTheory(partialAt, false, queryFormula);
		Collection<Argument> partialArgs = new ArrayList<Argument>();
		partialArgs.addAll(dt);
		
		return fullArgs.equals(partialArgs);

	}

}