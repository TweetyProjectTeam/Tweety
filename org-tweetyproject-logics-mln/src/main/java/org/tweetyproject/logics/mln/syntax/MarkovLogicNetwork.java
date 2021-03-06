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
package org.tweetyproject.logics.mln.syntax;

import java.io.Serializable;
import java.util.Collection;

import org.tweetyproject.commons.BeliefSet;
import org.tweetyproject.commons.Signature;
import org.tweetyproject.logics.commons.syntax.Constant;
import org.tweetyproject.logics.fol.syntax.FolSignature;

/**
 * Instances of this class represent Markov Logic Networks [Domingos et. al.].
 * 
 * @author Matthias Thimm
 */
public class MarkovLogicNetwork extends BeliefSet<MlnFormula,FolSignature> implements Serializable {

	private static final long serialVersionUID = 3313039501304912746L;

	/**
	 * Creates a new (empty) MLN.
	 */
	public MarkovLogicNetwork(){
		super();
	}
	
	/**
	 * Creates a new conditional MLN with the given collection of
	 * MLN formulas.
	 * @param formulas a collection of MLN formulas.
	 */
	public MarkovLogicNetwork(Collection<? extends MlnFormula> formulas){
		super(formulas);
	}
	
	/* (non-Javadoc)
	 * @see org.tweetyproject.BeliefSet#getSignature()
	 */
	@Override
	public Signature getMinimalSignature() {
		FolSignature sig = new FolSignature();
		for(MlnFormula formula: this){
			sig.addAll(formula.getPredicates());
			sig.addAll(formula.getTerms(Constant.class));
			sig.addAll(formula.getFunctors());
		}
		return sig;
	}

	@Override
	protected FolSignature instantiateSignature() {
		return new FolSignature();
	}

}
