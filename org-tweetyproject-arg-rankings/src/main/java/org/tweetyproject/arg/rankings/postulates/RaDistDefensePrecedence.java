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
 *  Copyright 2019 The TweetyProject Team <http://tweetyproject.org/contact/>
 */
package org.tweetyproject.arg.rankings.postulates;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.tweetyproject.arg.rankings.reasoner.AbstractRankingReasoner;
import org.tweetyproject.arg.rankings.semantics.ArgumentRanking;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;

/**
 * The "distributed-defense precedence" postulate for ranking semantics as
 * proposed in [Amgoud, Ben-Naim. Ranking-based semantics for argumentation
 * frameworks. 2013]: The best defense is when each defender attacks a distinct
 * attacker.
 * 
 * @author Anna Gessler
 *
 */
public class RaDistDefensePrecedence extends RankingPostulate {

	@Override
	public String getName() {
		return "Distributed-Defense Precedence";
	}

	@Override
	public boolean isApplicable(Collection<Argument> kb) {
		if (!(kb instanceof DungTheory))
			return false;
		return(kb.size() >= 2);
	}

	@Override
	public boolean isSatisfied(Collection<Argument> kb, AbstractRankingReasoner<ArgumentRanking> ev) {
		if (!this.isApplicable(kb))
			return true;
		if (ev.getModel((DungTheory) kb) == null)
			return true;
		
		DungTheory dt = (DungTheory) kb;
		Iterator<Argument> it = dt.iterator();
		Argument a = it.next();
		Argument b = it.next();
		
		if (dt.getAttackers(a).size() != dt.getAttackers(b).size())
			return true;
		Set<Argument> defenders_a = new HashSet<Argument>();
		Set<Argument> defenders_b = new HashSet<Argument>();
		for (Argument at : dt.getAttackers(a))
			defenders_a.addAll(dt.getAttackers(at));
		for (Argument bt : dt.getAttackers(b))
			defenders_b.addAll(dt.getAttackers(bt));
		if (defenders_a.size() != defenders_b.size())
			return true;
	
		// check if defense of a and b is simple
		for (Argument defender : defenders_a) {
			Set<Argument> attackers_a = new HashSet<Argument>(dt.getAttackers(a));
			attackers_a.retainAll(dt.getAttacked(defender));
			if (attackers_a.size() > 1)
				return true;
		}
		for (Argument defender : defenders_b) {
			Set<Argument> attackers_b = new HashSet<Argument>(dt.getAttackers(b));
			attackers_b.retainAll(dt.getAttacked(defender));
			if (attackers_b.size() > 1)
				return true;
		}

		// check if defense of a is distributed
		for (Argument attacker : dt.getAttackers(a)) {
			if (dt.getAttackers(attacker).size() > 1)
				return true;
		}

		// check if defense of b is not distributed
		boolean defenseIsDistributed = true;
		for (Argument attacker : dt.getAttackers(a)) {
			if (dt.getAttackers(attacker).size() > 1)
				defenseIsDistributed = false;
		}
		if (defenseIsDistributed)
			return true;

		ArgumentRanking ranking = ev.getModel(dt);
		return ranking.isStrictlyMoreAcceptableThan(a, b);
	}

}