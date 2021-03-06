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
package org.tweetyproject.arg.prob.dynamics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.prob.semantics.PASemantics;
import org.tweetyproject.arg.prob.semantics.ProbabilisticExtension;
import org.tweetyproject.arg.prob.syntax.PartialProbabilityAssignment;
import org.tweetyproject.math.GeneralMathException;
import org.tweetyproject.math.func.SimpleRealValuedFunction;
import org.tweetyproject.math.norm.RealVectorNorm;
import org.tweetyproject.math.opt.problem.OptimizationProblem;
import org.tweetyproject.math.opt.solver.Solver;
import org.tweetyproject.math.probability.Probability;
import org.tweetyproject.math.term.FloatConstant;
import org.tweetyproject.math.term.FloatVariable;
import org.tweetyproject.math.term.Term;
import org.tweetyproject.math.term.Variable;

/**
 * This operator implements a revision of some probabilistic assessment of 
 * arguments upon the observation of an argumentation theory. More specifically, for
 * a given probabilistic semantics S, some norm N, a function f, and a partial probability assignment PPA,
 * it computes the PPA-compliant prob-function that 1.) has minimal N-distance to S-prob'functions and 2.) maximizes
 * function f.  
 * @author Matthias Thimm
 */
public class PARevisionOperator extends AbstractPAChangeOperator {
		
	/**
	 * Creates a new change operator for the given semantics that uses the specified norm
	 * for distance measuring and the given function for optimizing.
	 * @param semantics the semantics used for change.
	 * @param norm the norm used for distance measurement between probabilistic extensions.
	 * @param f the function that is maximized on the set of probabilistic extensions with minimal distance. 
	 */
	public PARevisionOperator(PASemantics semantics, RealVectorNorm norm, SimpleRealValuedFunction f) {
		super(semantics, norm, f);
	}
	
	/* (non-Javadoc)
	 * @see org.tweetyproject.arg.prob.dynamics.ChangeOperator#change(org.tweetyproject.arg.prob.PartialProbabilityAssignment, org.tweetyproject.arg.dung.DungTheory)
	 */
	@Override
	public ProbabilisticExtension change(PartialProbabilityAssignment ppa, DungTheory theory) {
		// construct optimization problem
		// Note that we have in fact two optimization problems to solve:
		// 1.) first we determine the set of PPA-compliant prob'functions with minimal N-distance to the S-prob'functions
		// 2.) from this set we take a PPA-compliant prob'function which maximizes f
		// Instead of solving two consecutive optimization problems we just solve one by combining the target functions and
		// weighing the first one by a large factor (therefore imposing a preference of the first optimization problem)
		OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
		// for pi-compliant prob'functions
		Map<Collection<Argument>,FloatVariable> varsComp = new HashMap<Collection<Argument>,FloatVariable>();
		Vector<Term> varsCompVector = new Vector<Term>();
		// for semantically satisfying prob'functions
		Map<Collection<Argument>,FloatVariable> varsSem = new HashMap<Collection<Argument>,FloatVariable>();
		Vector<Term> varsSemVector = new Vector<Term>();
		// prepare optimization problem
		this.prepareOptimizationProblem(ppa, theory, problem, varsComp, varsSem, varsCompVector, varsSemVector);
		// Target function
		// the part that is to be maximized (we maximize over the set of PPA-compliant functions)
		Term max = this.getFunction().getTerm(varsCompVector);
		// the part that is to be minimized (it is weighted as it is preferred) 
		Term min = new FloatConstant(AbstractPAChangeOperator.FIRST_OPTIMIZATION_WEIGHT).mult(this.getNorm().distanceTerm(varsCompVector,varsSemVector));		
		problem.setTargetFunction(max.minus(min));				
		// Do the optimization
		try{			
			Map<Variable,Term> solution = Solver.getDefaultGeneralSolver().solve(problem);
			ProbabilisticExtension ext = new ProbabilisticExtension();
			// select the best PPA-compliant prob'function
			for(Collection<Argument> args: varsComp.keySet())
				ext.put(new Extension(args), new Probability(solution.get(varsComp.get(args)).doubleValue()));
			return ext;
		}catch (GeneralMathException ex){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to compute the closest and best probabilistic extension is not feasible.");
		}
	}
}
