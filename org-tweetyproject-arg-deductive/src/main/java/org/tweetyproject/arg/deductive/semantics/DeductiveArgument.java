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
package org.tweetyproject.arg.deductive.semantics;

import java.util.Collection;

import org.tweetyproject.logics.pl.syntax.PlFormula;

/**
 * Instances of this class represent arguments in the sense
 * of Definition 3.1 in<br>
 * <br>
 * Philippe Besnard and Anthony Hunter. A logic-based theory of deductive arguments.
 * In Artificial Intelligence, 128(1-2):203-235, 2001.
 * 
 * @author Matthias Thimm
 */
public class DeductiveArgument {

	/** The support of this argument. */
	private Collection<? extends PlFormula> support;
	/** The claim of this argument. */
	private PlFormula claim;
	
	/** 
	 * Creates a new deductive argument with the given support
	 * and claim.
	 * @param support a set of formulas.
	 * @param claim a formula.
	 */
	public DeductiveArgument(Collection<? extends PlFormula> support, PlFormula claim){
		this.support = support;
		this.claim = claim;
	}
	
	/**
	 * Returns the support of this argument.
	 * @return the support of this argument.
	 */
	public Collection<? extends PlFormula> getSupport(){
		return this.support;
	}
	
	/**
	 * Returns the claim of this argument.
	 * @return the claim of this argument.
	 */
	public PlFormula getClaim(){
		return this.claim;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "<" + this.support.toString() + "," + this.claim.toString() + ">";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((support == null) ? 0 : support.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// arguments are equal if there support is equal
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeductiveArgument other = (DeductiveArgument) obj;
		if (support == null) {
			if (other.support != null)
				return false;
		} else if (!support.equals(other.support))
			return false;
		return true;
	}
}
