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

package nevik.autodiff.expr.real.visitor;

import nevik.autodiff.expr.real.RealVariable;
import org.junit.Before;
import org.junit.Test;

import static nevik.autodiff.expr.real.RealConstant.ONE;
import static nevik.autodiff.expr.real.RealConstant.reCons;
import static nevik.autodiff.expr.real.RealExprAddition.reAdd;
import static nevik.autodiff.expr.real.RealExprMultiplication.reMult;
import static nevik.autodiff.expr.real.RealExprNegation.reNeg;
import static nevik.autodiff.expr.real.visitor.VisitorRealExpressionSimplifier.simplify;
import static org.junit.Assert.assertEquals;

/**
 * @author Patrick Lehner
 * @since 2015-10-05
 */
public class VisitorRealExpressionSimplifierTest {
	private RealVariable x, y, z;

	@Before
	public void setUp() throws Exception {
		x = new RealVariable("x");
		y = new RealVariable("y");
		z = new RealVariable("z");
	}

	@Test
	public void testSimplifyTwoConstAddition() throws Exception {
		assertEquals(reCons(6), simplify(reAdd(reCons(2.5), reCons(3.5))));
	}

	@Test
	public void testSimplifyTwoConstMultiplication() throws Exception {
		assertEquals(reCons(-9.0), simplify(reMult(reCons(-4.5), reCons(2))));
	}

	@Test
	public void testSimplifyPureConstant() throws Exception {
		assertEquals(reCons(1.5), simplify(reCons(1.5)));
	}

	@Test
	public void testSimplifyPureVariable() throws Exception {
		assertEquals(x, simplify(x));
	}

	@Test
	public void testSimplifyUnpackUnaryAddition1Lvl() throws Exception {
		assertEquals(x, simplify(reAdd(x)));
		assertEquals(ONE, simplify(reAdd(ONE)));
	}

	@Test
	public void testSimplifyUnpackUnaryAddition2Lvl() throws Exception {
		assertEquals(x, simplify(reAdd(reAdd(x))));
		assertEquals(ONE, simplify(reAdd(reAdd(ONE))));
	}

	@Test
	public void testSimplifyUnpackUnaryMultiplication1Lvl() throws Exception {
		assertEquals(x, simplify(reMult(x)));
		assertEquals(ONE, simplify(reMult(ONE)));
	}

	@Test
	public void testSimplifyUnpackUnaryMultiplication2Lvl() throws Exception {
		assertEquals(x, simplify(reMult(reMult(x))));
		assertEquals(ONE, simplify(reMult(reMult(ONE))));
	}

	@Test
	public void testSimplifyUnpackDoubleNegation() throws Exception {
		assertEquals(x, simplify(reNeg(reNeg(x))));
		assertEquals(ONE, simplify(reNeg(reNeg(ONE))));
		assertEquals(reNeg(x), simplify(reNeg(reNeg(reNeg(x)))));
		assertEquals(reCons(-1), simplify(reNeg(reNeg(reNeg(ONE)))));
	}

	@Test
	public void testSimplifyCombineRepeatedAddition() throws Exception {
		assertEquals(reMult(reCons(2), x), simplify(reAdd(x, x)));
	}

	@Test
	public void testSimplifyUnpackNestedAddition1Lvl() throws Exception {
		assertEquals(reAdd(x, y, z), simplify(reAdd(x, reAdd(y, z))));
		assertEquals(reAdd(x, y, z), simplify(reAdd(reAdd(x, y), z)));
	}

	@Test
	public void testSimplifyUnpackNestedMultiplication1Lvl() throws Exception {
		assertEquals(reMult(x, y, z), simplify(reMult(x, reMult(y, z))));
		assertEquals(reMult(x, y, z), simplify(reMult(reMult(x, y), z)));
	}
}