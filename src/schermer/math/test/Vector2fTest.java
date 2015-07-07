package schermer.math.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static schermer.math.Vector2f.ONE;
import static schermer.math.Vector2f.UNIT_X;
import static schermer.math.Vector2f.UNIT_Y;
import static schermer.math.Vector2f.ZERO;
import static schermer.math.Vector2f.polar;
import static schermer.math.Vector2f.rectangular;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import schermer.math.Vector2f;
import schermer.math.Vector2f.Quadrant;

public class Vector2fTest {

	public static final float DELTA = 0.0001f;
	public static final float π = (float) Math.PI;
	public static final float ROOT2 = (float) Math.sqrt(2);
	public static final int BATTERY_SIZE = 200;
	public static final float MAX_FLOAT = 10e18f;
	private Vector2f v;
	private Vector2f[] v1s;
	private Vector2f[] v2s;

	@Before
	public void setUp() throws Exception {
		Vector2f.setFloatingPointPrecision(DELTA);

		Random r = new Random();
		v1s = new Vector2f[BATTERY_SIZE];
		v2s = new Vector2f[BATTERY_SIZE];
		for (int i = 0; i < BATTERY_SIZE; i++) {
			v1s[i] = rectangular((r.nextFloat() - 0.5f) * MAX_FLOAT,
								 (r.nextFloat() - 0.5f) * MAX_FLOAT);
			v2s[i] = rectangular((r.nextFloat() - 0.5f) * MAX_FLOAT,
								 (r.nextFloat() - 0.5f) * MAX_FLOAT);
		}

	}

	@Test
	public void testConstants() {
		assertEquals("Dimension", 2, Vector2f.DIMENSION);

		v = ZERO;
		assertEquals("ZERO.x", 0, v.getX(), DELTA);
		assertEquals("ZERO.y", 0, v.getY(), DELTA);

		v = ONE;
		assertEquals("ONE.x", 1, v.getX(), DELTA);
		assertEquals("ONE.y", 1, v.getY(), DELTA);

		v = UNIT_X;
		assertEquals("UNIT_X.x", 1, v.getX(), DELTA);
		assertEquals("UNIT_X.y", 0, v.getY(), DELTA);

		v = UNIT_Y;
		assertEquals("UNIT_X.x", 0, v.getX(), DELTA);
		assertEquals("UNIT_X.y", 1, v.getY(), DELTA);
	}

	@Test
	public void testConstructors() {
		/* Test rectangular coordinate constructor. */
		v = rectangular(0, 0);
		assertEquals("r[0, 0].x", 0, v.getX(), DELTA);
		assertEquals("r[0, 0].y", 0, v.getY(), DELTA);

		v = rectangular(1, 1);
		assertEquals("r[1, 1].x", 1, v.getX(), DELTA);
		assertEquals("r[1, 1].y", 1, v.getY(), DELTA);

		v = rectangular(-1, -1);
		assertEquals("r[-1, -1].x", -1, v.getX(), DELTA);
		assertEquals("r[-1, -1].y", -1, v.getY(), DELTA);

		v = rectangular(0.0001f, -0.0001f);
		assertEquals("r[0.0001f, -0.0001f].x", 0.0001, v.getX(), DELTA);
		assertEquals("r[0.0001f, -0.0001f].y", -0.0001, v.getY(), DELTA);

		v = rectangular(-π, -10e35f);
		assertEquals("r[-π, -10e35f].x", -π, v.getX(), DELTA);
		assertEquals("r[-π, -10e35f].y", -10e35f, v.getY(), DELTA);

		v = rectangular(10e-38f, -10e35f);
		assertEquals("r[10e-38f, -10e35f].x", 10e-38f, v.getX(), DELTA);
		assertEquals("r[10e-38f, -10e35f].y", -10e35f, v.getY(), DELTA);


		/* Test polar coordinate constructor. */
		v = polar(0, 0);
		assertEquals("p[0, 0].r", 0, v.getRadius(), DELTA);
		assertEquals("p[0, 0].θ", 0, v.getAngle(), DELTA);

		v = polar(0, π);
		assertEquals("p[0, π].r", 0, v.getRadius(), DELTA);
		assertEquals("p[0, π].θ", 0, v.getAngle(), DELTA);

		v = polar(0, -4.5f * π);
		assertEquals("p[0, -4.5f*π].r", 0, v.getRadius(), DELTA);
		assertEquals("p[0, -4.5f*π].θ", 0, v.getAngle(), DELTA);

		v = polar(1, 2 * π);
		assertEquals("p[1, 2*π].r", 1, v.getRadius(), DELTA);
		assertEquals("p[1, 2*π].θ", 0, v.getAngle(), DELTA);

		v = polar(1, 200 * π);
		assertEquals("p[1, 200*π].r", 1, v.getRadius(), DELTA);
		assertEquals("p[1, 200*π].θ", 0, v.getAngle(), DELTA);

		v = polar(1, π);
		assertEquals("p[1, π].r", 1, v.getRadius(), DELTA);
		assertEquals("p[1, π].θ", -π, v.getAngle(), DELTA);

		v = polar(-1, π);
		assertEquals("p[-1, π].r", 1, v.getRadius(), DELTA);
		assertEquals("p[-1, π].θ", 0, v.getAngle(), DELTA);

		v = polar(-1, -π);
		assertEquals("p[-1, -π].r", 1, v.getRadius(), DELTA);
		assertEquals("p[-1, -π].θ", 0, v.getAngle(), DELTA);

		v = polar(1, -π);
		assertEquals("p[1, -π].r", 1, v.getRadius(), DELTA);
		assertEquals("p[1, -π].θ", π, v.getAngle(), DELTA);

		v = polar(10, 18.344f * π);
		assertEquals("p[10, 18.344f*π].r", 10, v.getRadius(), DELTA);
		assertEquals("p[10, 18.344f*π].θ", 0.344f * π, v.getAngle(), DELTA);

	}

	@Test
	public void testEqualsReflexivity() { 
		assertEquals("ZERO.equals(ZERO)", ZERO, ZERO);
		assertEquals("ONE.equals(ONE)", ONE, ONE);
		assertEquals("UNIT_X.equals(UNIT_X)", UNIT_X, UNIT_X);
		assertEquals("UNIT_Y.equals(UNIT_Y)", UNIT_Y, UNIT_Y);

		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("V.equals(V)", v1s[i], v1s[i]);
			assertEquals("V.equals(V)", v2s[i], v2s[i]);
		}
	}
	
	@Test
	public void testEquals() {
		
		assertNotEquals("!ONE.equals(ZERO)", ONE, ZERO);
		assertNotEquals("!ZERO.equals(ONE)", ZERO, ONE);

		assertNotEquals("!ZERO.equals(UNIT_X)", ZERO, UNIT_X);
		assertNotEquals("!UNIT_X.equals(ZERO)", UNIT_X, ZERO);

		assertNotEquals("!ZERO.equals(UNIT_Y)", ZERO, UNIT_Y);
		assertNotEquals("!UNIT_Y.equals(ZERO)", UNIT_Y, ZERO);

		assertNotEquals("!ONE.equals(UNIT_X)", ONE, UNIT_X);
		assertNotEquals("!UNIT_X.equals(ONE)", UNIT_X, ONE);

		assertNotEquals("!ONE.equals(UNIT_Y)", ONE, UNIT_Y);
		assertNotEquals("!UNIT_Y.equals(ONE)", UNIT_Y, ONE);

		assertNotEquals("!UNIT_X.equals(UNIT_Y)", UNIT_X, UNIT_Y);
		assertNotEquals("!UNIT_Y.equals(UNIT_X)", UNIT_Y, UNIT_X);


		assertEquals("r[10, 1e-24f].equals(r[10, 1e-24f])",
					 rectangular(10, 1e-24f),
					 rectangular(10, 1e-24f));

		assertEquals("p[1, π].equals(p[1, -π])",
					 polar(1, π),
					 polar(1, -π));

		assertEquals("p[1, 0].equals(p[1, -2π])",
					 polar(1, 0),
					 polar(1, -2 * π));

		assertEquals("p[10, 0].equals(p[10, 4π])",
					 polar(10, 0),
					 polar(10, 4 * π));
	}

	@Test
	public void testAddSubtract() {
		/* Verify Commutativity */
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1 + v2 = v2 + v1", v1s[i].add(v2s[i]), v2s[i].add(v1s[i]));
		}


		assertEquals("ZERO + ZERO", ZERO.add(ZERO), ZERO);
		assertEquals("ONE + ONE", ONE.add(ONE), rectangular(2, 2));
		assertEquals("UNIT_X + UNIT_X", UNIT_X.add(UNIT_X), rectangular(2, 0));
		assertEquals("UNIT_Y + UNIT_Y", UNIT_Y.add(UNIT_Y), rectangular(0, 2));


		assertEquals("ZERO + ONE", ONE, ZERO.add(ONE));
		assertEquals("ONE + ZERO", ONE, ONE.add(ZERO));

		assertEquals("ZERO + UNIT_X", UNIT_X, ZERO.add(UNIT_X));
		assertEquals("UNIT_X + ZERO", UNIT_X, UNIT_X.add(ZERO));

		assertEquals("ZERO + UNIT_Y", UNIT_Y, ZERO.add(UNIT_Y));
		assertEquals("UNIT_Y + ZERO", UNIT_Y, UNIT_Y.add(ZERO));

		assertEquals("ONE + UNIT_X", rectangular(2, 1), ONE.add(UNIT_X));
		assertEquals("UNIT_X + ONE", rectangular(2, 1), UNIT_X.add(ONE));

		assertEquals("ONE + UNIT_Y", rectangular(1, 2), ONE.add(UNIT_Y));
		assertEquals("UNIT_Y + ONE", rectangular(1, 2), UNIT_Y.add(ONE));

		assertEquals("UNIT_X + UNIT_Y", ONE, UNIT_X.add(UNIT_Y));
		assertEquals("UNIT_Y + UNIT_X", ONE, UNIT_Y.add(UNIT_X));

		assertEquals("ZERO + 1", ONE, ZERO.add(1));
		assertEquals("ONE + 1", rectangular(2, 2), ONE.add(1));

		assertEquals("ZERO - ZERO", ZERO, ZERO.subtract(ZERO));
		assertEquals("ONE - ZERO", ONE, ONE.subtract(ZERO));
		assertEquals("UNIT_X - ZERO", UNIT_X, UNIT_X.subtract(ZERO));
		assertEquals("UNIT_Y - ZERO", UNIT_Y, UNIT_Y.subtract(ZERO));
		assertEquals("ONE - UNIT_X", UNIT_Y, ONE.subtract(UNIT_X));
		assertEquals("ONE - UNIT_Y", UNIT_X, ONE.subtract(UNIT_Y));

		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("ZERO - v1 = -(v1)", v1s[i].opposite(), ZERO.subtract(v1s[i]));
			assertEquals("ZERO - v2 = -(v2)", v2s[i].opposite(), ZERO.subtract(v2s[i]));
		}

	}

	@Test
	public void testMultiply() {
		/* Test zero product. */
		assertEquals("ZERO * 0", ZERO, ZERO.multiply(0));
		assertEquals("ONE * 0", ZERO, ONE.multiply(0));
		
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1 * 0 = 0", ZERO, v1s[i].multiply(0));
			assertEquals("v2 * 0 = 0", ZERO, v2s[i].multiply(0));
		}
		
		/* Verify commutativity of scalar product. */
		assertEquals("ZERO.dot(ONE) = ONE.dot(ZERO)", ZERO.dot(ONE), ONE.dot(ZERO), DELTA);
		assertEquals("UNIT_X.dot(ZERO) = ZERO.dot(UNIT_X)", UNIT_X.dot(ZERO), ZERO.dot(UNIT_X), DELTA);
		assertEquals("UNIT_Y.dot(ZERO) = ZERO.dot(UNIT_Y)", UNIT_Y.dot(ZERO), ZERO.dot(UNIT_Y), DELTA);
		assertEquals("UNIT_X.dot(ONE) = ONE.dot(UNIT_X)", UNIT_X.dot(ONE), ONE.dot(UNIT_X), DELTA);
		assertEquals("UNIT_Y.dot(ONE) = ONE.dot(UNIT_Y)", UNIT_Y.dot(ONE), ONE.dot(UNIT_Y), DELTA);
		assertEquals("UNIT_X.dot(UNIT_Y) = UNIT_Y.dot(UNIT_X)", UNIT_X.dot(UNIT_Y), UNIT_Y.dot(UNIT_X), DELTA);
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1.dot(v2) = v2.dot(v1)", 
			             v1s[i].dot(v2s[i]), 
			             v2s[i].dot(v1s[i]), 
			             DELTA);
		}
		
		/* Verify self scalar product. */
		assertEquals("ZERO.dot(ZERO) = 0", 0, ZERO.dot(ZERO), DELTA);
		assertEquals("ONE.dot(ONE) = 2", 2, ONE.dot(ONE), DELTA);
		assertEquals("UNIT_X.dot(UNIT_X) = 1", 1, UNIT_X.dot(UNIT_X), DELTA);
		assertEquals("UNIT_Y.dot(UNIT_Y) = 1", 1, UNIT_Y.dot(UNIT_Y), DELTA);
		
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1.dot(v1) = magnitude(v1)^2", 
			             v1s[i].magnitude()*v1s[i].magnitude(), 
			             v1s[i].dot(v1s[i]), 
			             DELTA);
			assertEquals("v2.dot(v2) = magnitude(v2)^2", 
			             v2s[i].magnitude()*v2s[i].magnitude(), 
			             v2s[i].dot(v2s[i]),
			             DELTA);	
		}
		
		/* Verify zero scalar product. */
		assertEquals("ONE.dot(ZERO) = 0", 0, ONE.dot(ZERO), DELTA);
		assertEquals("UNIT_X.dot(ZERO) = 0", 0, UNIT_X.dot(ZERO), DELTA);
		assertEquals("UNIT_Y.dot(ZERO) = 0", 0, UNIT_Y.dot(ZERO), DELTA);
		
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1.dot(ZERO) = 0", 0, v1s[i].dot(ZERO), DELTA);
			assertEquals("v2.dot(ZERO) = 0", 0, v2s[i].dot(ZERO), DELTA);
		}
	}

	@Test
	public void testDivide() {
		assertEquals("UNIT_X.normalized = UNIT_X", UNIT_X, UNIT_X.normalized());
		assertEquals("UNIT_Y.normalized = UNIT_Y", UNIT_Y, UNIT_Y.normalized());
		assertEquals("ONE.normalized = [√2/2,√2/2]", rectangular(ROOT2/2, ROOT2/2), ONE.normalized());
		
		/* Ensure zero vector doesn't normalize. */
		try {
			ZERO.normalized();
			fail("ZERO normalization.");
		} catch (UnsupportedOperationException e) {
		}
		
		for (int i = 0; i < BATTERY_SIZE; i++) {
        	assertEquals("|v1.normalized| = 1", 1, v1s[i].normalized().magnitude(), DELTA);
        	assertEquals("|v2.normalized| = 1", 1, v2s[i].normalized().magnitude(), DELTA);
		}
	}

	@Test
	public void testRepresentations() {
		fail("Not implemented.");
	}

	@Test
	public void testOrthants() {
		assertEquals("ZERO.quadrant = null", null, ZERO.quadrant());
		assertEquals("UNIT_X.quadrant = null", null, UNIT_X.quadrant());
		assertEquals("UNIT_Y.quadrant = null", null, UNIT_Y.quadrant());
		assertEquals("ONE.quadrant = PP", Quadrant.PP, ONE.quadrant());
	}

	@Test
	public void testFunctions() {
		/* Test magnitudes. */
		assertEquals("|ZERO| = 0", 0, ZERO.magnitude(), DELTA);
		assertEquals("|ONE| = √2", (float) Math.sqrt(2), ONE.magnitude(), DELTA);
		assertEquals("|UNIT_X| = 1", 1, UNIT_X.magnitude(), DELTA);
		assertEquals("|UNIT_Y| = 1", 1, UNIT_Y.magnitude(), DELTA);
		assertEquals("|r[√2, √2]| = 2", 2, rectangular((float) Math.sqrt(2), 
		                                               (float) Math.sqrt(2)).magnitude(), DELTA);
		
		/* Test opposites. */
		assertEquals("-ZERO = ZERO", ZERO, ZERO.opposite());
		assertEquals("-ONE = r[-1, -1]", rectangular(-1, -1), ONE.opposite());
		assertEquals("-UNIT_X = r[-1, 0]", rectangular(-1, 0), UNIT_X.opposite());
		assertEquals("-UNIT_Y = r[0, -1]", rectangular(0, -1), UNIT_Y.opposite());
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("-v1 + v1 = ZERO", ZERO, v1s[i].add(v1s[i].opposite()));
			assertEquals("-v2 + v2 = ZERO", ZERO, v2s[i].add(v2s[i].opposite()));
		}
		
	}
}
