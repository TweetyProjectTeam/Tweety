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
package org.tweetyproject.logics.cl.syntax;

import java.util.*;

import org.tweetyproject.commons.*;
import org.tweetyproject.logics.pl.syntax.*;

/**
 * This class models a belief set on conditional logic, i.e. a set of conditionals.
 * 
 * @author Matthias Thimm
 *
 */
public class ClBeliefSet extends BeliefSet<Conditional,PlSignature> {
	
	/**
	 * Creates a new (empty) conditional belief set.
	 */
	public ClBeliefSet(){
		super();
	}
	
	/**
	 * Creates a new conditional belief set with the given collection of
	 * conditionals.
	 * @param conditionals a collection of conditionals.
	 */
	public ClBeliefSet(Collection<? extends Conditional> conditionals){
		super(conditionals);
	}
	
	/* (non-Javadoc)
	 * @see org.tweetyproject.kr.BeliefBase#getSignature()
	 */
	@Override
	public PlSignature getMinimalSignature(){
		PlSignature sig = new PlSignature();
		for(Formula f: this){
			Conditional c = (Conditional) f;
			sig.addAll(c.getPremise().iterator().next().getAtoms());
			sig.addAll(c.getConclusion().getAtoms());
		}
		return sig;
	}

	@Override
	public ClBeliefSet clone(){
		ClBeliefSet copy = new ClBeliefSet();
		Iterator<Conditional> i = this.iterator();
		while(i.hasNext()){
			copy.add(i.next());
		}
		return copy;
	}

	@Override
	protected PlSignature instantiateSignature() {
		return new PlSignature();
	}
}
