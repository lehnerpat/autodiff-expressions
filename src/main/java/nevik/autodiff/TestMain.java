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

package nevik.autodiff;

import nevik.autodiff.expr.real.RealConstant;
import nevik.autodiff.expr.real.RealExprAddition;
import nevik.autodiff.expr.real.RealExprMultiplication;
import nevik.autodiff.expr.real.visitor.VisitorRealExpressionPrintingInfix;

import static nevik.autodiff.expr.real.RealConstant.reCons;
import static nevik.autodiff.expr.real.RealExprAddition.reAdd;
import static nevik.autodiff.expr.real.RealExprMultiplication.reMult;

/**
 * @author Patrick Lehner
 * @since 2015-10-03
 */
public class TestMain {
	public static void main(String[] args) {
		final RealConstant c = reCons(1.0);
		final RealExprAddition a = reAdd(c, reCons(2.0));
		final RealExprMultiplication m = reMult(reCons(4.0), reCons(-4));

		new VisitorRealExpressionPrintingInfix(null, a).evaluate();
	}}
