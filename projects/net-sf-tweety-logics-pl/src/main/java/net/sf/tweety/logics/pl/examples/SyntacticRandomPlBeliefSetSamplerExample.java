/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
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
 *  Copyright 2016 The Tweety Project Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.logics.pl.examples;

import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.logics.pl.util.SyntacticRandomSampler;
import net.sf.tweety.math.probability.Probability;

public class SyntacticRandomPlBeliefSetSamplerExample {

	public static void main(String[] args){
		PropositionalSignature sig = new PropositionalSignature(4);
		SyntacticRandomSampler sampler = new SyntacticRandomSampler(sig,new Probability(0.2),new Probability(0.35),new Probability(0.35),0.5,1, 1);
		for(int i = 0; i< 10; i++)
			System.out.println(sampler.next());
		
	}
}