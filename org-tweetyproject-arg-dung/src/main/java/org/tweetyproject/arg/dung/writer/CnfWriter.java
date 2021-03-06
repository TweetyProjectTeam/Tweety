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
 package org.tweetyproject.arg.dung.writer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.arg.dung.syntax.Argument;;

/**
 * Writes an abstract argumentation framework into a file of the
 * CNF format. Note that the order of the arguments may change
 * by using this writer.
 * 
 * @author Matthias Thimm
 */
public class CnfWriter extends AbstractDungWriter{

	/* (non-Javadoc)
	 * @see org.tweetyproject.arg.dung.writer.AbstractDungWriter#write(org.tweetyproject.arg.dung.DungTheory, java.io.File)
	 */
	@Override
	public void write(DungTheory aaf, File f) throws IOException {
		PrintWriter writer = new PrintWriter(f, "UTF-8");
		writer.println("p af " + aaf.size() + " " + aaf.getAttacks().size());
		Map<Argument,Integer> map = new HashMap<Argument,Integer>();
		int idx = 1;
		for(Argument arg: aaf)
			map.put(arg, idx++);
		for(Attack att: aaf.getAttacks())
			writer.println(map.get(att.getAttacker()) + " -" + map.get(att.getAttacked()) + " 0");		
		writer.close();		
	}
}
