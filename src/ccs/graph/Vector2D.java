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

package ccs.graph;

/**
 * Represents an immutable 2D vector (x,y) in double precision.
 * @author Menghan
 */
public class Vector2D {
	Point2D p;

	/**
	 * @param y
	 */
	public Vector2D(double x, double y) {
		p = new Point2D(x, y);
	}

	/**
	 * Constructs a 2D vector from <code>start</code> to <code>end</code>, i.e.
	 * <code>end - start</code>
	 * @param start
	 * @param end
	 */
	public Vector2D(Point2D start, Point2D end) {
		this(end.x() - start.x(), end.y() - start.y());
	}

	/**
	 * The x coordinate of the vector
	 * @return the x coordinate of the vector
	 */
	public double x() {
		return p.x();
	}

	/**
	 * The y coordinate of the vector
	 * @return the y coordinate of the vector
	 */
	public double y() {
		return p.y();
	}

	/**
	 * Returns the norm of the vector, that is the l2 distance between its two endpoints
	 * @return the norm of the vector
	 */
	public double norm() {
		return p.distance(0, 0);
	}

	public Vector2D inverse() {
		return new Vector2D(-x(), -y());
	}

	public Vector2D add(Vector2D that) {
		return new Vector2D(this.x() + that.x(), this.y() + that.y());
	}

	public Vector2D minus(Vector2D that) {
		return new Vector2D(this.x() - that.x(), this.y() - that.y());
	}

	// ~ this. angle - that.angle, ccw
	/**
	 * Returns the counterclockwise angle from this vector to another vector
	 * @param that
	 * @return the counterclockwise angle from <code>this</code> to <code>that</code>
	 */
	public double angle(Vector2D that) {
		double cos = innerProduct(that) / (norm() * that.norm());

		// Handle Inaccuracy
		if (cos > 1 && cos - 1 < TDLinkage.ACCURACY) cos = 1;
		if (cos < -1 && (-1 - cos) < TDLinkage.ACCURACY) cos = -1;

		// the returned angle is in the range 0.0 through pi
		double angle = Math.acos(cos);

		// want the angle in ccw from e1 to e2
		// handle the situation where angle should be > pi
		int orient = Point2D.orientationOf(Point2D.orig, this.p, that.p);
		if (orient > 0) {
			angle = Math.PI * 2 - angle;
		}
		return angle;
	}

	public Vector2D multiply(double k) {
		return new Vector2D(this.x() * k, this.y() * k);
	}

	public Vector2D divide(double k) {
		assert (k != 0);
		return new Vector2D(this.x() / k, this.y() / k);
	}

	public double innerProduct(Vector2D that) {
		return this.x() * that.x() + this.y() * that.y();
	}

	// public Vector2D crossProduct(Vertor2D that){
	//
	// }

	@Override
	public String toString() {
		return "(" + p.x() + ", " + p.y() + ")";
	}
}
