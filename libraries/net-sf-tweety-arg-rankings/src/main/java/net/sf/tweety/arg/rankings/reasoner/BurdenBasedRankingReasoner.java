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
package net.sf.tweety.arg.rankings.reasoner;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.rankings.semantics.NumericalArgumentRanking;

/**
 * This class implements the "Burden-based" argument semantics approach as proposed
 * by [Amgoud, Ben-Naim. Ranking-based semantics for argumentation frameworks. 2013]. 
 * It assigns a "Burden number" to every argument.
 * 
 * @author Anna Gessler
 */
public class BurdenBasedRankingReasoner extends AbstractRankingReasoner<NumericalArgumentRanking> {
	
	@Override
	public Collection<NumericalArgumentRanking> getModels(DungTheory bbase) {
		Collection<NumericalArgumentRanking> ranks = new HashSet<NumericalArgumentRanking>();
		ranks.add(this.getModel(bbase));
		return ranks;
	}

	@Override
	public NumericalArgumentRanking getModel(DungTheory base) {
		//Number of steps
		int i_max = 3;
		//Map for storing burden numbers of previous steps
		Map<Argument, double[]> burdenNumbers = new HashMap<Argument, double[]>();	
		
		//Initialize burden numbers array
		for (Argument a : base) {
			double[] initial_numbers = new double[i_max+1];
			initial_numbers[0] = 1.0; //burden number for step 0 is 1.0 for all arguments
 			burdenNumbers.put(a, initial_numbers);
		}
		
		//Compute burden numbers for all steps i
		for (int i = 1; i <= i_max; i++ ) {
			for (Argument a : base) {
				Set<Argument> attackers = base.getAttackers(a);
				double new_burden = 1.0;
				for (Argument b: attackers) {
					double[] attacker_burden_numbers = burdenNumbers.get(b);
					new_burden += 1.0/(attacker_burden_numbers[i-1]);
				}
				double[] burden_numbers = burdenNumbers.get(a);
				burden_numbers[i] = new_burden;
				burdenNumbers.put(a,burden_numbers);
			}
		}
		
		//Use the lexicographical order of the burden numbers as ranking
		NumericalArgumentRanking ranking = new NumericalArgumentRanking();
		ranking.setSortingType(NumericalArgumentRanking.SortingType.LEXICOGRAPHIC);
		for (Argument a : base) {
			double[] burdens_a = burdenNumbers.get(a);
			ranking.put(a, burdens_a[i_max]);
		}
		return ranking;
	}
	
}
