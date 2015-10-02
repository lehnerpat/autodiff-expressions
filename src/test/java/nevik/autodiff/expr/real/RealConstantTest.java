/**
 * ISC License Terms (http://opensource.org/licenses/isc-license):
 *
 * Copyright (c) 2015, Patrick Lehner <lehner dot patrick at gmx dot de>
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN
 * AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

package nevik.autodiff.expr.real;

import org.junit.Before;
import org.junit.Test;

import static nevik.autodiff.expr.real.RealConstant.reCons;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Patrick Lehner
 * @since 2015-10-02
 */
public class RealConstantTest {
	private RealConstant c1a, c1b, c2a, c2b;

	@Before
	public void setUp() throws Exception {
		c1a = reCons(1.0);
		c1b = reCons(1.0);
		c2a = reCons(1e6 + 3);
		c2b = reCons(1e6 + 3);
	}

	@Test
	public void testGetVariables() throws Exception {
		assertTrue("Variable set must be empty", c1a.getVariables().isEmpty());
		assertTrue("Variable set must be empty", c2b.getVariables().isEmpty());
	}

	@Test
	public void testToString() throws Exception {
		assertTrue("String representations of equal-valued constants must be equal",
				c1a.toString().equals(c1b.toString()));
		assertTrue("String representations of equal-valued constants must be equal",
				c2a.toString().equals(c2b.toString()));
		assertFalse("String representations of differently valued constants must be different",
				c1a.toString().equals(c2a.toString()));
	}

	@Test
	public void testEquals() throws Exception {
		assertTrue("Equal-valued constants must be equal", c1a.equals(c1b));
		assertTrue("Equal-valued constants must be equal", c2a.equals(c2b));
		assertFalse("Differently valued constants must not be equal", c1a.equals(c2a));
		assertFalse("Differently valued constants must not be equal", c2b.equals(c1b));

		assertFalse("Constant cannot be equal to a string", c1a.equals(""));
		assertFalse("Constant cannot be equal to null", c1a.equals(null));

		assertTrue("Equality must be symmetric", c1b.equals(c1a));
		assertTrue("Equality must be symmetric", c2b.equals(c2a));
	}

	@Test
	public void testIdentity() throws Exception {
		assertTrue("Equal-valued constants must be identical", c1a == c1b);
		assertTrue("Equal-valued constants must be identical", c2a == c2b);
		assertFalse("Differently valued constants must not be identical", c1a == c2a);
	}

	@Test
	public void testHashCode() throws Exception {
		assertTrue("Equal-valued constants must be have identical hash code", c1a.hashCode() == c1b.hashCode());
		assertTrue("Equal-valued constants must be have identical hash code", c2a.hashCode() == c2b.hashCode());
		assertFalse("Differently valued constants must have different hash codes", c1a.hashCode() == c2a.hashCode());
	}
}