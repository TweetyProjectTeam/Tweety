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
package net.sf.tweety.arg.prob.reasoner;

import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.semantics.Semantics;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.prob.syntax.ProbabilisticArgumentationFramework;
import net.sf.tweety.commons.InferenceMode;
import net.sf.tweety.commons.QuantitativeReasoner;

/**
 * Abstract anecestor for PAF reasoner.
 * 
 * @author Matthias Thimm
 *
 */
public abstract class AbstractPafReasoner implements QuantitativeReasoner<ProbabilisticArgumentationFramework,Argument>{

	/** Semantics for plain AAFs. */
	private Semantics semantics;
	
	/**
	 * Creates a new reasoner.
	 * @param semantics Semantics for plain AAFs.
	 */
	public AbstractPafReasoner(Semantics semantics) {
		this.semantics = semantics;
	}
	
	/**
	 * The semantics of this reasoner.
	 * @return The semantics of this reasoner.
	 */
	protected Semantics getSemantics() {
		return this.semantics;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.commons.Reasoner#query(net.sf.tweety.commons.BeliefBase, net.sf.tweety.commons.Formula)
	 */
	@Override
	public Double query(ProbabilisticArgumentationFramework beliefbase, Argument formula) {
		return this.query(beliefbase, formula, InferenceMode.SKEPTICAL);
	}
	
	/**
	 * Queries the given PAF for the given argument using the given 
	 * inference type.
	 * @param beliefbase an PAF
	 * @param formula a single argument
	 * @param inferenceMode either InferenceMode.SKEPTICAL or InferenceMode.CREDULOUS
	 * @return probability of the argument
	 */
	public abstract Double query(ProbabilisticArgumentationFramework beliefbase, Argument formula, InferenceMode inferenceMode);
	
	/**
	 * Estimates the probability that the given set of
	 * arguments is an extension
	 * @param ext some set of arguments
	 * @return the estimated probability of the given set to be 
	 * an extension
	 */
	public abstract Double query(ProbabilisticArgumentationFramework paf, Extension ext);
}
