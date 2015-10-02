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

import nevik.autodiff.expr.Variable;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Patrick Lehner
 * @since 2015-10-02
 */
public class RealVariableTest {
	private RealVariable v1a, v1b, v2a, v2b;
	private List<RealVariable> vars;

	@Before
	public void setUp() throws Exception {
		v1a = new RealVariable();
		v1b = new RealVariable();
		v2a = new RealVariable("vari");
		v2b = new RealVariable("vari");
		vars = Arrays.asList(v1a, v1b, v2a, v2b);
	}

	@Test
	public void testGetVariables() throws Exception {
		final Set<Variable> v1aVariables = v1a.getVariables();
		assertTrue("Variable set must be singleton", v1aVariables.size() == 1);
		assertTrue("Variable set must contain instance", v1aVariables.contains(v1a));
		final Set<Variable> v2bVariables = v2b.getVariables();
		assertTrue("Variable set must be singleton", v2bVariables.size() == 1);
		assertTrue("Variable set must contain instance", v2bVariables.contains(v2b));
	}

	@Test
	public void testToString() throws Exception {
		for (final RealVariable varI : vars) {
			for (final RealVariable varJ : vars) {
				if (varI != varJ) {
					assertFalse("Different instances must have different string representations",
							varI.toString().equals(varJ.toString()));
				}
			}
		}
	}

	@Test
	public void testHashCode() throws Exception {
		for (final RealVariable varI : vars) {
			for (final RealVariable varJ : vars) {
				if (varI != varJ) {
					assertFalse("Different instances must have different hash codes",
							varI.hashCode() == varJ.hashCode());
				}
			}
		}
	}
}