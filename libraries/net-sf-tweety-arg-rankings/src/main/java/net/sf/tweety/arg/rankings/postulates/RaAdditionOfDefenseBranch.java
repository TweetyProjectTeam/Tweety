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
package net.sf.tweety.arg.rankings.postulates;

import java.util.Collection;
import java.util.Iterator;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.syntax.DungTheory;
import net.sf.tweety.arg.rankings.reasoner.AbstractRankingReasoner;
import net.sf.tweety.arg.rankings.semantics.ArgumentRanking;

/**
 * The "addition of defense branch" postulate for ranking semantics as
 * formalized in [Bonzon, Delobelle, Konieczny, Maudet. A Comparative Study of
 * Ranking-Based Semantics for Abstract Argumentation. 2016]: Adding a defense
 * branch to any attacked argument improves its ranking.
 * 
 * @see net.sf.tweety.arg.rankings.postulates.RaStrictAdditionOfDefenseBranch
 * @author Anna Gessler
 *
 */
public class RaAdditionOfDefenseBranch extends RankingPostulate {

	@Override
	public String getName() {
		return "Addition of Defense Branch";
	}

	@Override
	public boolean isApplicable(Collection<Argument> kb) {
		if (!(kb instanceof DungTheory))
			return false;
		else if (kb.size() < 1)
			return false;
		Argument a_old = ((DungTheory) kb).iterator().next();
		return (!((DungTheory) kb).getAttackers(a_old).isEmpty() && !kb.contains(new Argument("t1"))
				&& !kb.contains(new Argument(a_old.getName() + "clone")));
	}

	@Override
	public boolean isSatisfied(Collection<Argument> kb, AbstractRankingReasoner<ArgumentRanking> ev) {
		if (!this.isApplicable(kb))
			return true;
		DungTheory dt = new DungTheory((DungTheory) kb);
		Iterator<Argument> it = dt.iterator();
		Argument a_old = it.next();

		// clone argument and relations
		Argument a_clone = new Argument(a_old.getName() + "clone");
		dt.add(a_clone);
		for (Argument attacker : dt.getAttackers(a_old)) {
			if (attacker.equals(a_old))
				dt.add(new Attack(a_clone, a_clone));
			else
				dt.add(new Attack(attacker, a_clone));
		}
		for (Argument attacked : dt.getAttacked(a_old)) {
			if (!attacked.equals(a_old)) 
				dt.add(new Attack(a_clone, attacked));
		}
		
		// add new defense branch
		Argument t1 = new Argument("t1");
		Argument t2 = new Argument("t2");
		dt.add(t1);
		dt.add(t2);
		;
		dt.add(new Attack(t1, a_clone));
		dt.add(new Attack(t2, t1));
		ArgumentRanking ranking = ev.getModel((DungTheory) dt);
		if (ranking.isIncomparable(a_clone, a_old)) {
			if (IGNORE_INCOMPARABLE_ARGUMENTS)
				return true;
			else
				return false;
		}
		return ranking.isStrictlyMoreAcceptableThan(a_clone, a_old);
	}

}
