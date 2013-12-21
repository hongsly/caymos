package ccs.graph;


public class Vector2D {
	Point2D p;

	public Vector2D(double x, double y) {
		p = new Point2D(x, y);
	}

	public Vector2D(Point2D start, Point2D end) {
		this(end.x() - start.x(), end.y() - start.y());
	}

	public double x() {
		return p.x();
	}

	public double y() {
		return p.y();
	}

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

	// return ccw angle from this to that
	// ~ this. angle - that.angle, ccw
	public double angle(Vector2D that) {
		double cos = innerProduct(that) / (norm() * that.norm());

		// Handle Inaccuracy
		if (cos > 1 && cos - 1 < TreeDecomp.ACCURACY)
			cos = 1;
		if (cos < -1 && (-1 - cos) < TreeDecomp.ACCURACY)
			cos = -1;

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
