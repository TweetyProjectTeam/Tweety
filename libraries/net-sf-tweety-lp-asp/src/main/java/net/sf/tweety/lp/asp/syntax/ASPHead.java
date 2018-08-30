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
package net.sf.tweety.lp.asp.syntax;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.syntax.AssociativeFormulaSupport;
import net.sf.tweety.logics.commons.syntax.AssociativeFormulaSupport.AssociativeSupportBridge;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.interfaces.AssociativeFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Atom;
import net.sf.tweety.logics.commons.syntax.interfaces.ComplexLogicalFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Disjunctable;
import net.sf.tweety.logics.commons.syntax.interfaces.SimpleLogicalFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;
import net.sf.tweety.logics.fol.syntax.FolSignature;

/**
 * This formula represents the head of an disjunctive rule which is a
 * disjunction of literals.
 * 
 * @author Tim Janus
 * @author Anna Gessler
 *
 */
public class ASPHead implements ASPElement, AssociativeFormula<ASPLiteral>, Disjunctable, AssociativeSupportBridge {

	private AssociativeFormulaSupport<ASPLiteral> assocSupport = new AssociativeFormulaSupport<ASPLiteral>(this);

	/**
	 * Empty Constructor.
	 */
	public ASPHead() {
	}

	/**
	 * Creates a new head with the given elements.
	 * 
	 * @param head_elements
	 *            list of literals
	 */
	public ASPHead(List<ASPLiteral> head_elements) {
		this.addAll(head_elements);
	}

	/**
	 * Creates a new head with a single element.
	 * 
	 * @param head
	 */
	public ASPHead(ASPLiteral head) {
		this.add(head);
	}

	/**
	 * Copy-Constructor
	 * 
	 * @param other
	 */
	public ASPHead(ASPHead other) {
		this(other.getFormulas());
	}

	@Override
	public ComplexLogicalFormula substitute(Map<? extends Term<?>, ? extends Term<?>> map)
			throws IllegalArgumentException {
		return assocSupport.substitute(map);
	}

	@Override
	public ComplexLogicalFormula exchange(Term<?> v, Term<?> t) throws IllegalArgumentException {
		return assocSupport.exchange(v, t);
	}

	@Override
	public boolean isGround() {
		for (ASPLiteral l : this.getFormulas())
			if (!l.isGround())
				return false;
		return true;
	}

	@Override
	public boolean isWellFormed() {
		return assocSupport.isWellFormed();
	}

	@Override
	public Class<? extends Predicate> getPredicateCls() {
		return assocSupport.getPredicateCls();
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public Set<Term<?>> getTerms() {
		Set<Term<?>> terms = new HashSet<Term<?>>();
		for (ASPLiteral l : this.getFormulas())
			terms.addAll(l.getTerms());
		return terms;
	}

	@Override
	public <C extends Term<?>> Set<C> getTerms(Class<C> cls) {
		Set<C> terms = new HashSet<C>();
		for (ASPLiteral l : this.getFormulas())
			terms.addAll(l.getTerms(cls));
		return terms;
	}

	@Override
	public <C extends Term<?>> boolean containsTermsOfType(Class<C> cls) {
		return assocSupport.containsTermsOfType(cls);
	}

	@Override
	public SortedSet<ASPLiteral> getLiterals() {
		SortedSet<ASPLiteral> literals = new TreeSet<ASPLiteral>();
		for (ASPLiteral element : this) {
			literals.addAll(element.getLiterals());
		}
		return literals;
	}

	@Override
	public Set<Predicate> getPredicates() {
		Set<Predicate> reval = new HashSet<Predicate>();
		for (Predicate pred : assocSupport.getPredicates()) {
			if (pred instanceof Predicate) {
				reval.add((Predicate) pred);
			}
		}
		return reval;
	}

	@Override
	public Set<ASPAtom> getAtoms() {
		Set<ASPAtom> reval = new HashSet<ASPAtom>();
		for (Atom at : assocSupport.getAtoms()) {
			if (at instanceof ASPAtom) {
				reval.add((ASPAtom) at);
			}
		}
		return reval;
	}

	@Override
	public ASPHead substitute(Term<?> t, Term<?> v) {
		return (ASPHead) assocSupport.substitute(v, t);
	}

	@Override
	public FolSignature getSignature() {
		return (FolSignature)assocSupport.getSignature();
	}

	@Override
	public ASPHead clone() {
		return new ASPHead(this);
	}

	@Override
	public boolean add(ASPLiteral e) {
		return assocSupport.add(e);
	}

	@Override
	public void add(int index, ASPLiteral element) {
		assocSupport.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends ASPLiteral> c) {
		return assocSupport.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends ASPLiteral> c) {
		return assocSupport.addAll(index, c);
	}

	@Override
	public void clear() {
		assocSupport.clear();
	}

	@Override
	public boolean contains(Object o) {
		return assocSupport.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return assocSupport.containsAll(c);
	}

	@Override
	public ASPLiteral get(int index) {
		return assocSupport.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return assocSupport.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return assocSupport.isEmpty();
	}

	@Override
	public Iterator<ASPLiteral> iterator() {
		return assocSupport.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return assocSupport.lastIndexOf(o);
	}

	@Override
	public ListIterator<ASPLiteral> listIterator() {
		return assocSupport.listIterator();
	}

	@Override
	public ListIterator<ASPLiteral> listIterator(int index) {
		return assocSupport.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return assocSupport.remove(o);
	}

	@Override
	public ASPLiteral remove(int index) {
		return assocSupport.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return assocSupport.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return assocSupport.retainAll(c);
	}

	@Override
	public ASPLiteral set(int index, ASPLiteral element) {
		return assocSupport.set(index, element);
	}

	@Override
	public int size() {
		return assocSupport.size();
	}

	@Override
	public List<ASPLiteral> subList(int fromIndex, int toIndex) {
		return assocSupport.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return assocSupport.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return assocSupport.toArray(a);
	}

	@Override
	public List<ASPLiteral> getFormulas() {
		return assocSupport.getFormulas();
	}

	@Override
	public <C extends SimpleLogicalFormula> Set<C> getFormulas(Class<C> cls) {
		return assocSupport.getFormulas(cls);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ASPHead createEmptyFormula() {
		return new ASPHead();
	}

	@Override
	public FolSignature createEmptySignature() {
		return new FolSignature();
	}

	@Override
	public String getOperatorSymbol() {
		return ";";
	}

	@Override
	public String getEmptySymbol() {
		return "";
	}

	@Override
	public SimpleLogicalFormula combineWithOr(Disjunctable f) {
		if (!(f instanceof ASPLiteral))
			throw new IllegalArgumentException();
		ASPLiteral arg = (ASPLiteral) f;
		ASPHead reval = new ASPHead(this);
		reval.add(arg);
		return reval;
	}

	@Override
	public String toString() {
		return assocSupport.toString();
	}

}