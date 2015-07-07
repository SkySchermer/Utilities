package schermer.math.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static schermer.math.Vector3f.ONE;
import static schermer.math.Vector3f.UNIT_X;
import static schermer.math.Vector3f.UNIT_Y;
import static schermer.math.Vector3f.UNIT_Z;
import static schermer.math.Vector3f.ZERO;
import static schermer.math.Vector3f.rectangular;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import schermer.math.Quaternion;
import schermer.math.Vector2f;
import schermer.math.Vector3f;

public class Vector3fTest {

	public static final float DELTA = 0.0001f;
	public static final float π = (float) Math.PI;
	public static final float ROOT2 = (float) Math.sqrt(2);
	public static final float ROOT3 = (float) Math.sqrt(3);
	public static final int BATTERY_SIZE = 200;
	public static final float MAX_FLOAT = 10e18f;
	private Vector3f v;
	private Vector3f[] v1s;
	private Vector3f[] v2s;

	@Before
	public void setUp() throws Exception {
		Vector3f.setFloatingPointPrecision(DELTA);

		Random r = new Random();
		v1s = new Vector3f[BATTERY_SIZE];
		v2s = new Vector3f[BATTERY_SIZE];
		for (int i = 0; i < BATTERY_SIZE; i++) {
			v1s[i] = rectangular((r.nextFloat() - 0.5f) * MAX_FLOAT,
								 (r.nextFloat() - 0.5f) * MAX_FLOAT,
								 (r.nextFloat() - 0.5f) * MAX_FLOAT);
			v2s[i] = rectangular((r.nextFloat() - 0.5f) * MAX_FLOAT,
								 (r.nextFloat() - 0.5f) * MAX_FLOAT,
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
		v = rectangular(0, 0, 0);
		assertEquals("r[0, 0, 0].x", 0, v.getX(), DELTA);
		assertEquals("r[0, 0, 0].y", 0, v.getY(), DELTA);
		assertEquals("r[0, 0, 0].z", 0, v.getZ(), DELTA);

		v = rectangular(1, 1, 1);
		assertEquals("r[1, 1, 1].x", 1, v.getX(), DELTA);
		assertEquals("r[1, 1, 1].y", 1, v.getY(), DELTA);
		assertEquals("r[1, 1, 1].y", 1, v.getZ(), DELTA);

		v = rectangular(-1, -1, -1);
		assertEquals("r[-1, -1, -1].x", -1, v.getX(), DELTA);
		assertEquals("r[-1, -1, -1].y", -1, v.getY(), DELTA);
		assertEquals("r[-1, -1, -1].y", -1, v.getZ(), DELTA);

		v = rectangular(0.0001f, -0.0001f,-0.0001f);
		assertEquals("r[0.0001f, -0.0001f, -0.0001f].x", 0.0001, v.getX(), DELTA);
		assertEquals("r[0.0001f, -0.0001f, -0.0001f].y", -0.0001, v.getY(), DELTA);
		assertEquals("r[0.0001f, -0.0001f, -0.0001f].y", -0.0001, v.getZ(), DELTA);

		v = rectangular(-π, -10e35f, -10e-35f);
		assertEquals("r[-π, -10e35f, -10e-35f].x", -π, v.getX(), DELTA);
		assertEquals("r[-π, -10e35f, -10e-35f].y", -10e35f, v.getY(), DELTA);
		assertEquals("r[-π, -10e35f, -10e-35f].y", -10e-35f, v.getZ(), DELTA);



		/* Test polar coordinate constructor. */

	}

	@Test
	public void testEquals() {
		/* Verify Reflexivity */
		assertEquals("ZERO.equals(ZERO)", ZERO, ZERO);
		assertEquals("ONE.equals(ONE)", ONE, ONE);
		assertEquals("UNIT_X.equals(UNIT_X)", UNIT_X, UNIT_X);
		assertEquals("UNIT_Y.equals(UNIT_Y)", UNIT_Y, UNIT_Y);

		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("V.equals(V)", v1s[i], v1s[i]);
			assertEquals("V.equals(V)", v2s[i], v2s[i]);
		}


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


		assertEquals("r[10, 1e-24f, 12].equals(r[10, 1e-24f, 12])",
					 rectangular(10, 1e-24f, 12),
					 rectangular(10, 1e-24f, 12));

	}

	@Test
	public void testAddSubtract() {
		/* Verify Commutativity */
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1 + v2 = v2 + v1", v1s[i].add(v2s[i]), v2s[i].add(v1s[i]));
		}


		assertEquals("ZERO + ZERO", ZERO.add(ZERO), ZERO);
		assertEquals("ONE + ONE", ONE.add(ONE), rectangular(2, 2, 2));
		assertEquals("UNIT_X + UNIT_X", UNIT_X.add(UNIT_X), rectangular(2, 0, 0));
		assertEquals("UNIT_Y + UNIT_Y", UNIT_Y.add(UNIT_Y), rectangular(0, 2, 0));
		assertEquals("UNIT_Z + UNIT_Z", UNIT_Z.add(UNIT_Z), rectangular(0, 0, 2));


		assertEquals("ZERO + ONE", ONE, ZERO.add(ONE));
		assertEquals("ONE + ZERO", ONE, ONE.add(ZERO));

		assertEquals("ZERO + UNIT_X", UNIT_X, ZERO.add(UNIT_X));
		assertEquals("UNIT_X + ZERO", UNIT_X, UNIT_X.add(ZERO));

		assertEquals("ZERO + UNIT_Y", UNIT_Y, ZERO.add(UNIT_Y));
		assertEquals("UNIT_Y + ZERO", UNIT_Y, UNIT_Y.add(ZERO));

		assertEquals("ONE + UNIT_X", rectangular(2, 1, 1), ONE.add(UNIT_X));
		assertEquals("UNIT_X + ONE", rectangular(2, 1, 1), UNIT_X.add(ONE));

		assertEquals("ONE + UNIT_Y", rectangular(1, 2, 1), ONE.add(UNIT_Y));
		assertEquals("UNIT_Y + ONE", rectangular(1, 2, 1), UNIT_Y.add(ONE));
		
		assertEquals("ONE + UNIT_Z", rectangular(1, 1, 2), ONE.add(UNIT_Z));
		assertEquals("UNIT_Z + ONE", rectangular(1, 1, 2), UNIT_Z.add(ONE));

		assertEquals("UNIT_X + UNIT_Y + UNIT_Z", ONE, UNIT_X.add(UNIT_Y).add(UNIT_Z));
		assertEquals("UNIT_Y + UNIT_X + UNIT_Z", ONE, UNIT_Y.add(UNIT_X).add(UNIT_Z));

		assertEquals("ZERO + 1", ONE, ZERO.add(1));
		assertEquals("ONE + 1", rectangular(2, 2, 2), ONE.add(1));

		assertEquals("ZERO - ZERO", ZERO, ZERO.subtract(ZERO));
		assertEquals("ONE - ZERO", ONE, ONE.subtract(ZERO));
		assertEquals("UNIT_X - ZERO", UNIT_X, UNIT_X.subtract(ZERO));
		assertEquals("UNIT_Y - ZERO", UNIT_Y, UNIT_Y.subtract(ZERO));
		assertEquals("UNIT_Z - ZERO", UNIT_Z, UNIT_Z.subtract(ZERO));
		assertEquals("ONE - UNIT_X", UNIT_Y.add(UNIT_Z), ONE.subtract(UNIT_X));
		assertEquals("ONE - UNIT_Y", UNIT_X.add(UNIT_Z), ONE.subtract(UNIT_Y));
		assertEquals("ONE - UNIT_Z", UNIT_Y.add(UNIT_X), ONE.subtract(UNIT_Z));

		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("ZERO - v1 = -(v1)", v1s[i].multiply(-1), ZERO.subtract(v1s[i]));
			assertEquals("ZERO - v2 = -(v2)", v2s[i].multiply(-1), ZERO.subtract(v2s[i]));
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
		assertEquals("UNIT_Z.dot(ZERO) = ZERO.dot(UNIT_Y)", UNIT_Z.dot(ZERO), ZERO.dot(UNIT_Z), DELTA);
		
		assertEquals("UNIT_X.dot(ONE) = ONE.dot(UNIT_X)", UNIT_X.dot(ONE), ONE.dot(UNIT_X), DELTA);
		assertEquals("UNIT_Y.dot(ONE) = ONE.dot(UNIT_Y)", UNIT_Y.dot(ONE), ONE.dot(UNIT_Y), DELTA);
		assertEquals("UNIT_Z.dot(ONE) = ONE.dot(UNIT_Z)", UNIT_Z.dot(ONE), ONE.dot(UNIT_Z), DELTA);
		
		assertEquals("UNIT_X.dot(UNIT_Y) = UNIT_Y.dot(UNIT_X)", UNIT_X.dot(UNIT_Y), UNIT_Y.dot(UNIT_X), DELTA);
		assertEquals("UNIT_X.dot(UNIT_Z) = UNIT_Z.dot(UNIT_X)", UNIT_X.dot(UNIT_Z), UNIT_Z.dot(UNIT_X), DELTA);
		assertEquals("UNIT_Z.dot(UNIT_Y) = UNIT_Y.dot(UNIT_Z)", UNIT_Z.dot(UNIT_Y), UNIT_Y.dot(UNIT_Z), DELTA);
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1.dot(v2) = v2.dot(v1)", 
			             v1s[i].dot(v2s[i]), 
			             v2s[i].dot(v1s[i]), 
			             DELTA);
		}
		
		/* Verify self scalar product. */
		assertEquals("ZERO.dot(ZERO) = 0", 0, ZERO.dot(ZERO), DELTA);
		assertEquals("ONE.dot(ONE) = 3", 3, ONE.dot(ONE), DELTA);
		assertEquals("UNIT_X.dot(UNIT_X) = 1", 1, UNIT_X.dot(UNIT_X), DELTA);
		assertEquals("UNIT_Y.dot(UNIT_Y) = 1", 1, UNIT_Y.dot(UNIT_Y), DELTA);
		assertEquals("UNIT_Z.dot(UNIT_Z) = 1", 1, UNIT_Z.dot(UNIT_Z), DELTA);
		
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
		assertEquals("UNIT_Z.dot(ZERO) = 0", 0, UNIT_Z.dot(ZERO), DELTA);
		
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1.dot(ZERO) = 0", 0, v1s[i].dot(ZERO), DELTA);
			assertEquals("v2.dot(ZERO) = 0", 0, v2s[i].dot(ZERO), DELTA);
		}
	}

	@Test
	public void testDivide() {
		assertEquals("UNIT_X.normalized = UNIT_X", UNIT_X, UNIT_X.normalized());
		assertEquals("UNIT_Y.normalized = UNIT_Y", UNIT_Y, UNIT_Y.normalized());
		assertEquals("UNIT_Z.normalized = UNIT_Z", UNIT_Z, UNIT_Z.normalized());
		assertEquals("ONE.normalized = [√3/3,√3/3,√3/3]", rectangular(ROOT3/3, ROOT3/3, ROOT3/3), ONE.normalized());
		
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
	public void testCross() {
		
		assertEquals("UNIT_X x UNIT_Y = UNIT_Z", UNIT_Z, UNIT_X.cross(UNIT_Y));
		assertEquals("UNIT_Y x UNIT_X = -UNIT_Z", UNIT_Z.multiply(-1), UNIT_Y.cross(UNIT_X));
		
		assertEquals("UNIT_X x UNIT_Z = UNIT_Y", UNIT_Y, UNIT_X.cross(UNIT_Z));
		assertEquals("UNIT_Z x UNIT_X = -UNIT_Y", UNIT_Y.multiply(-1), UNIT_Z.cross(UNIT_X));
		
		assertEquals("UNIT_Y x UNIT_Z = UNIT_X", UNIT_X, UNIT_Y.cross(UNIT_Z));
		assertEquals("UNIT_Z x UNIT_Y = -UNIT_X", UNIT_X.multiply(-1), UNIT_Z.cross(UNIT_Y));
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("v1 x v2 = - (v2 x v1)", v1s[i].cross(v2s[i]), v2s[i].cross(v1s[i]).opposite());
		}
		
	}
	
	@Test
	public void testRotate() {
		assertEquals("UNIT_X -> UNIT_Y", UNIT_Y, UNIT_X.rotated(UNIT_Z, π/2));
		assertEquals("UNIT_X -> UNIT_Z", UNIT_Z, UNIT_X.rotated(UNIT_Y, -π/2));
		
		assertEquals("UNIT_Y -> UNIT_X", UNIT_X, UNIT_Y.rotated(UNIT_Z, -π/2));
		assertEquals("UNIT_Y -> UNIT_Z", UNIT_Z, UNIT_Y.rotated(UNIT_X, π/2));
		
		assertEquals("UNIT_Z -> UNIT_X", UNIT_X, UNIT_Z.rotated(UNIT_Y, π/2));
		assertEquals("UNIT_Z -> UNIT_Y", UNIT_Y, UNIT_Z.rotated(UNIT_X, -π/2));
		
		assertEquals("UNIT_X -> UNIT_Y", UNIT_Y, UNIT_X.rotated(Quaternion.fromRotationVector(UNIT_X).multiply(π/2)));
		assertEquals("UNIT_X -> UNIT_Z", UNIT_Z, UNIT_X.rotated(UNIT_Y, -π/2));
		
		assertEquals("UNIT_Y -> UNIT_X", UNIT_X, UNIT_Y.rotated(UNIT_Z, -π/2));
		assertEquals("UNIT_Y -> UNIT_Z", UNIT_Z, UNIT_Y.rotated(UNIT_X, π/2));
		
		assertEquals("UNIT_Z -> UNIT_X", UNIT_X, UNIT_Z.rotated(UNIT_Y, π/2));
		assertEquals("UNIT_Z -> UNIT_Y", UNIT_Y, UNIT_Z.rotated(UNIT_X, -π/2));
	}
	
	@Test
	public void testRepresentations() {
		fail("Not implemented.");
	}

	@Test
	public void testOrthants() {
	}

	@Test
	public void testFunctions() {
		/* Test magnitudes. */
		assertEquals("|ZERO| = 0", 0, ZERO.magnitude(), DELTA);
		assertEquals("|ONE| = √3", ROOT3, ONE.magnitude(), DELTA);
		assertEquals("|UNIT_X| = 1", 1, UNIT_X.magnitude(), DELTA);
		assertEquals("|UNIT_Y| = 1", 1, UNIT_Y.magnitude(), DELTA);
		assertEquals("|UNIT_Z| = 1", 1, UNIT_Y.magnitude(), DELTA);
		assertEquals("|r[√3, √3, √3]| = 3", 3, rectangular(ROOT3, 
		                                                   ROOT3,
		                                                   ROOT3).magnitude(), DELTA);
		
		/* Test opposites. */
		assertEquals("-ZERO = ZERO", ZERO, ZERO.opposite());
		assertEquals("-ONE = r[-1, -1]", rectangular(-1, -1, -1), ONE.opposite());
		assertEquals("-UNIT_X = r[-1, 0, 0]", rectangular(-1, 0, 0), UNIT_X.opposite());
		assertEquals("-UNIT_Y = r[0, -1, 0]", rectangular(0, -1, 0), UNIT_Y.opposite());
		assertEquals("-UNIT_Z = r[0, 0, -1]", rectangular(0, 0, -1), UNIT_Z.opposite());
		for (int i = 0; i < BATTERY_SIZE; i++) {
			assertEquals("-v1 + v1 = ZERO", ZERO, v1s[i].add(v1s[i].opposite()));
			assertEquals("-v2 + v2 = ZERO", ZERO, v2s[i].add(v2s[i].opposite()));
		}
	}
}
