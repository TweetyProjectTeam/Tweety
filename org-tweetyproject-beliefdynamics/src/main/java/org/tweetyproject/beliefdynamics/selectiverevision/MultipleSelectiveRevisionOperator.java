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
package org.tweetyproject.beliefdynamics.selectiverevision;

import java.util.Collection;

import org.tweetyproject.beliefdynamics.MultipleBaseRevisionOperator;
import org.tweetyproject.commons.Formula;

/**
 * This class implements a multiple selective revision operator following [Kruempelmann:2011,Ferme:1999].
 * 
 * @author Matthias Thimm
 *
 * @param <T> The type of formulas this operator works on.
 */
public class MultipleSelectiveRevisionOperator<T extends Formula> extends MultipleBaseRevisionOperator<T> {

	/**
	 * The transformation function for this revision.
	 */
	private MultipleTransformationFunction<T> transformationFunction;
	
	/**
	 * The revision operator for the inner revision.
	 */
	private MultipleBaseRevisionOperator<T> revisionOperator;
	
	/**
	 * Creates a new multiple selective revision operator for the given transformation function
	 * and inner revision.
	 * @param transformationFunction a transformation function.
	 * @param revisionOperator the inner revision.
	 */
	public MultipleSelectiveRevisionOperator(MultipleTransformationFunction<T> transformationFunction, MultipleBaseRevisionOperator<T> revisionOperator){
		this.transformationFunction = transformationFunction;
		this.revisionOperator = revisionOperator;
	}

	/* (non-Javadoc)
	 * @see org.tweetyproject.beliefdynamics.MultipleBaseRevisionOperator#revise(java.util.Collection, java.util.Collection)
	 */
	@Override
	public Collection<T> revise(Collection<T> base, Collection<T> formulas) {
		return this.revisionOperator.revise(base, this.transformationFunction.transform(formulas));
	}

}
