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
 *  Copyright 2017 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package org.tweetyproject.arg.deductive.syntax;

import java.util.Collection;

import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.commons.BeliefSet;
import org.tweetyproject.commons.Signature;
import org.tweetyproject.commons.util.rules.Derivation;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.PlSignature;

/**
 * 
 * @author Federico Cerutti (federico.cerutti@acm.org)
 * 
 * According to 
 * http://www0.cs.ucl.ac.uk/staff/a.hunter/papers/ac13t.pdf
 * a simple logic knowledge base (propositional version only in this implementation) is 
 * a set of literals---in this implementation rules with empty body---and a set of 
 * simple rules, @see SimplePlRule
 * 
 */
public class SimplePlLogicDeductiveKnowledgebase extends BeliefSet<SimplePlRule,PlSignature> {
	
	public SimplePlLogicDeductiveKnowledgebase(){
		super();
	}

	public SimplePlLogicDeductiveKnowledgebase(Collection<SimplePlRule> _kb){
		super(_kb);
	}
	
	public Signature getMinimalSignature() {
		PlSignature signature = new PlSignature();
		for(SimplePlRule f: this)
			signature.addSignature(f.getSignature());
		return signature;
	}
	
	/**
	 * Builds simple logic arguments and attacks among them---simple undercut and 
	 * simple rebuttal---as described in http://www0.cs.ucl.ac.uk/staff/a.hunter/papers/ac13t.pdf
	 * 
	 * @return 	the DungTheory built on the simple logic knowledge base 
	 * 			following http://www0.cs.ucl.ac.uk/staff/a.hunter/papers/ac13t.pdf 
	 */
	public DungTheory getAF(){
		DungTheory af = new DungTheory();
		
		for (Derivation<SimplePlRule> derivation : Derivation.allDerivations(this)) {
			if (derivation.size() != 1)
				af.add(new SimplePlLogicArgument(derivation));
		}
		
		for (Argument arga : af.getNodes()){
			for (Argument argb: af.getNodes()){
				SimplePlLogicArgument larga = (SimplePlLogicArgument)arga;
				SimplePlLogicArgument largb = (SimplePlLogicArgument)argb;
				
				Sat4jSolver solver = new Sat4jSolver();
				solver.getWitness((PlFormula)(larga.getClaim()).combineWithAnd(largb.getClaim()));
				
				if (!solver.isConsistent((PlFormula)(larga.getClaim()).combineWithAnd(largb.getClaim()))){
					af.add(new Attack(arga, argb));
				}
				
				for (SimplePlRule r : largb.getSupport()){
					for (PlFormula p : r.getPremise()){
						if (!solver.isConsistent((PlFormula)(larga.getClaim()).combineWithAnd(p))){
							af.add(new Attack(arga, argb));
						}
					}
				}
					
			}
		}
		
		return af;
		
	}

	@Override
	protected PlSignature instantiateSignature() {
		return new PlSignature();
	}
	
	
}
