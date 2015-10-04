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
import nevik.autodiff.expr.real.RealExpression;
import nevik.autodiff.expr.real.RealVariable;
import nevik.autodiff.expr.real.visitor.VisitorRealExpressionPartialDifferentiator;
import nevik.autodiff.expr.real.visitor.VisitorRealExpressionPartialDifferentiator.PartialDiffParams;
import nevik.autodiff.expr.real.visitor.VisitorRealExpressionPrintingInfix;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nevik.autodiff.expr.real.RealConstant.reCons;
import static nevik.autodiff.expr.real.RealExprAddition.reAdd;
import static nevik.autodiff.expr.real.RealExprMultiplication.reMult;
import static nevik.autodiff.expr.real.RealExprNegation.reNeg;
import static nevik.autodiff.expr.real.RealExprReciprocal.reRecip;

/**
 * @author Patrick Lehner
 * @since 2015-10-03
 */
public class TestMain {
	public static void main(String[] args) {
		final RealVariable x = new RealVariable("x");

		final Map<RealVariable, String> varMap =
				Stream.of(x).collect(Collectors.toMap(Function.identity(), v -> v.name));
		final RealConstant c = reCons(1.0);
		final RealExprAddition a = reAdd(x, reCons(2.0));
		final RealExprMultiplication m = reMult(reCons(4.0), x);
		final RealExpression e = reAdd(x, reNeg(x), reMult(reCons(-4), x), reRecip(x));

		new VisitorRealExpressionPrintingInfix(new VisitorRealExpressionPartialDifferentiator(a, new PartialDiffParams(x)).evaluate(), varMap).evaluate();
		System.out.println();
		new VisitorRealExpressionPrintingInfix(new VisitorRealExpressionPartialDifferentiator(m, new PartialDiffParams(x)).evaluate(), varMap).evaluate();
		System.out.println();
		new VisitorRealExpressionPrintingInfix(new VisitorRealExpressionPartialDifferentiator(e, new PartialDiffParams(x)).evaluate(), varMap).evaluate();
	}}
