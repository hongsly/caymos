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
