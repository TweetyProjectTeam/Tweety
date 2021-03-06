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
package org.tweetyproject.argumentation.parameterisedhierarchy;

import static org.junit.Assert.*;

import org.tweetyproject.arg.lp.semantics.AttackRelation;
import org.tweetyproject.arg.lp.semantics.attack.Attack;
import org.tweetyproject.arg.lp.semantics.attack.AttackStrategy;
import org.tweetyproject.arg.lp.semantics.attack.Defeat;
import org.tweetyproject.arg.lp.semantics.attack.Rebut;
import org.tweetyproject.arg.lp.semantics.attack.StrongAttack;
import org.tweetyproject.arg.lp.semantics.attack.Undercut;
import org.tweetyproject.arg.lp.syntax.Argument;
import org.tweetyproject.arg.lp.syntax.ArgumentationKnowledgeBase;
import org.tweetyproject.lp.asp.syntax.*;

import org.junit.Test;

public class AttackTest {

	@Test
	public void testUndercut() {
		AttackStrategy strategy = Undercut.getInstance();
		
		ASPRule r1 = new ASPRule( new ASPAtom("b") );
		ASPRule r2 = new ASPRule( new ASPAtom("a"), new DefaultNegation( new ASPAtom("b") ) ); 
		Program p = new Program();
		p.add(r1); p.add(r2);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		assertTrue(strategy.attacks(A, B));
		
		ArgumentationKnowledgeBase kb = new ArgumentationKnowledgeBase(p);
		AttackRelation notion = new AttackRelation(kb, strategy);
		assertTrue(notion.attacks(A, B));
		assertTrue( notion.getAttackingArguments(B).contains(A) );
	}
	
	@Test
	public void testRebut() {
		AttackStrategy strategy = Rebut.getInstance();
		
		ASPRule r1 = new ASPRule( new ASPAtom("b") );
		ASPRule r2 = new ASPRule( new StrictNegation( new ASPAtom("b") ) ); 
		Program p = new Program();
		p.add(r1); p.add(r2);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		assertTrue( strategy.attacks(A, B));
		assertTrue( strategy.attacks(B, A));
		
		ArgumentationKnowledgeBase kb = new ArgumentationKnowledgeBase(p);
		AttackRelation notion = new AttackRelation(kb, strategy );
		assertTrue( notion.attacks(A, B));
		assertTrue( notion.attacks(B, A));
		assertTrue( notion.getAttackingArguments(B).contains(A) );
		assertTrue( notion.getAttackingArguments(A).contains(B) );
	}
	
	@Test
	public void testAttack() {
		AttackStrategy strategy = Attack.getInstance();
		
		ASPRule r1 = new ASPRule( new ASPAtom("b") );
		ASPRule r2 = new ASPRule( new StrictNegation( new ASPAtom("b") ) );
		ASPRule r3 = new ASPRule( new ASPAtom("a"), new DefaultNegation( new ASPAtom("b") ) ); 
		Program p = new Program();
		p.add(r1); p.add(r2); p.add(r3);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		Argument C = new Argument(r3);
		assertTrue( strategy.attacks(A, B));
		assertTrue( strategy.attacks(B, A));
		assertTrue( strategy.attacks(A, C));
		assertFalse( strategy.attacks(C, A));
		assertFalse( strategy.attacks(B, C));
		assertFalse( strategy.attacks(C, B));
		
		
		ArgumentationKnowledgeBase kb = new ArgumentationKnowledgeBase(p);
		AttackRelation notion = new AttackRelation(kb, strategy );
		assertTrue( notion.attacks(A, B) );
		assertTrue( notion.attacks(A, C) );
		assertTrue( notion.getAttackingArguments(B).contains(A) );
		assertTrue( notion.getAttackingArguments(C).contains(A) );
		assertTrue( notion.getAttackingArguments(A).contains(B) );	
	}

	@Test
	public void testDefeat() {
		AttackStrategy strategy = Defeat.getInstance();
		
		ASPRule r1 = new ASPRule( new ASPAtom("a") );
		ASPRule r2 = new ASPRule( new ASPAtom("b"), new DefaultNegation(new ASPAtom("a")) );
		ASPRule r3 = new ASPRule( new ASPAtom("c"), new DefaultNegation(new ASPAtom("d")) );
		ASPRule r4 = new ASPRule( new StrictNegation(new ASPAtom("c")), new DefaultNegation(new ASPAtom("c")) );
		Program p = new Program();
		p.add(r1); p.add(r2); p.add(r3); p.add(r4);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		Argument C = new Argument(r3);
		Argument D = new Argument(r4);
		
		assertTrue( strategy.attacks(A,B));
		assertTrue( strategy.attacks(C,D));
		assertFalse( strategy.attacks(D,C));
	}

	@Test
	public void testStrongAttack() {
		AttackStrategy strategy = StrongAttack.getInstance();
		
		ASPRule r1 = new ASPRule( new ASPAtom("a") );
		ASPRule r2 = new ASPRule( new ASPAtom("b"), new DefaultNegation(new ASPAtom("a")) );
		ASPRule r3 = new ASPRule( new StrictNegation(new ASPAtom("a")), new DefaultNegation(new ASPAtom("a")) );
		Program p = new Program();
		p.add(r1); p.add(r2); p.add(r3);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		Argument C = new Argument(r3);
		
		assertTrue( strategy.attacks(A,B));
		assertTrue( strategy.attacks(A,C));
		assertFalse( strategy.attacks(B,A));
		assertFalse( strategy.attacks(C,A));
	}
	
	@Test
	public void testStrongUndercut() {
		AttackStrategy strategy = StrongAttack.getInstance();

		ASPRule r1 = new ASPRule( new ASPAtom("b"), new DefaultNegation(new ASPAtom("c")) );
		ASPRule r2 = new ASPRule( new ASPAtom("a"), new DefaultNegation(new ASPAtom("b")) );
		Program p = new Program();
		p.add(r1); p.add(r2);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		
		assertTrue( strategy.attacks(A,B));
		assertFalse( strategy.attacks(B,A));
	}

}
