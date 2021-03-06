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
 *  Copyright 2018 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package org.tweetyproject.arg.aspic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.tweetyproject.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.aspic.syntax.DefeasibleInferenceRule;
import org.tweetyproject.arg.aspic.syntax.InferenceRule;
import org.tweetyproject.arg.aspic.syntax.StrictInferenceRule;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.Proposition;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.PlSignature;

/**
 * Generates random ASPIC argumentation theories.
 * 
 * @author Matthias Thimm
 *
 */
public class RandomAspicArgumentationTheoryGenerator{ 

	private int numPropositions;
	private int numFormulas;
	private int maxBodyLiterals;
	private double probStrict;
		
	/** Creates a random ASPIC argumentation theory generatir with <code>numPropositions</code>
	 * and <code>numFormulas</code> formulas (inference rules).
	 * @param numPropositions the number of propositions
	 * @param numFormulas the number of formulas
	 * @param maxBodyLiterals the maximal number of body literals in each rule.
	 * @param probStrict the probability of each rule being strict.
	 */
	public RandomAspicArgumentationTheoryGenerator(int numPropositions, int numFormulas, int maxBodyLiterals, double probStrict) {
		this.numPropositions = numPropositions;
		this.numFormulas = numFormulas;
		this.maxBodyLiterals = maxBodyLiterals;
		this.probStrict = probStrict;
	}
	
	/**
	 * Returns an ASPIC argumentation theory
	 * @return an ASPIC argumentation theory
	 */
	public AspicArgumentationTheory<PlFormula> next(){
		Random rand = new Random();
		PlSignature sig = new PlSignature(numPropositions);
		List<Proposition> atoms = new ArrayList<Proposition>(sig.toCollection());
		AspicArgumentationTheory<PlFormula> theory = new AspicArgumentationTheory<PlFormula>(new PlFormulaGenerator());
		for(int i = 0; i < numFormulas; i++) {
			InferenceRule<PlFormula> rule;
			if(rand.nextFloat() < probStrict) 
				rule = new StrictInferenceRule<PlFormula>();
			else
				rule = new DefeasibleInferenceRule<PlFormula>();
			if(rand.nextBoolean())
				rule.setConclusion(atoms.get(rand.nextInt(atoms.size())));
			else
				rule.setConclusion(new Negation(atoms.get(rand.nextInt(atoms.size()))));
			int numBodyLiterals = rand.nextInt(maxBodyLiterals);
			for(int j = 0; j < numBodyLiterals; j++)
				if(rand.nextBoolean())
					rule.addPremise(atoms.get(rand.nextInt(atoms.size())));
				else
					rule.addPremise(new Negation(atoms.get(rand.nextInt(atoms.size()))));
			theory.addRule(rule);
		}		
		return theory;
	}
}
