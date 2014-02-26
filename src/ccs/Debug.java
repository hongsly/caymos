/*
 This file is part of CayMos. 

 CayMos is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 CayMos is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ccs;

import ccs.graph.Edge;

public class Debug {

	private static boolean DEBUGGING = true;

	/**
	 * Negative Result
	 * 
	 * @param s
	 *            message to be shown
	 */
	public static void warnMsg(String s) {
		if (DEBUGGING)
			System.err.println(s);
	}

	public static void warnMsg(Object o) {
		if (o == null)
			warnMsg("null");
		warnMsg(o.toString());
	}


	public static void msg(Object o) {
		if (DEBUGGING) {
			if (o == null)
				System.out.println("null");
			else
				System.out.println(o);
		}
	}

}
