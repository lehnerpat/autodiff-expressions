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

import nevik.autodiff.expr.real.RealConstant;
import nevik.autodiff.expr.real.RealExprAddition;
import nevik.autodiff.expr.real.RealExprMultiplication;
import nevik.autodiff.expr.real.RealExprNegation;
import nevik.autodiff.expr.real.RealExprReciprocal;
import nevik.autodiff.expr.real.RealExpression;
import nevik.autodiff.expr.real.RealVariable;
import nevik.autodiff.expr.real.visitor.VisitorRealExpressionPartialDifferentiator.PartialDiffParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static nevik.autodiff.expr.real.RealConstant.ONE;
import static nevik.autodiff.expr.real.RealConstant.ZERO;
import static nevik.autodiff.expr.real.RealExprAddition.reAdd;
import static nevik.autodiff.expr.real.RealExprMultiplication.reMult;
import static nevik.autodiff.expr.real.RealExprNegation.reNeg;
import static nevik.autodiff.expr.real.RealExprReciprocal.reRecip;

/**
 * @author Patrick Lehner
 * @since 2015-10-04
 */
public class VisitorRealExpressionPartialDifferentiator
		extends AbstractVisitorRealExpression<PartialDiffParams, RealExpression, RealExpression, Void> {
	public static final class PartialDiffParams {
		public final RealVariable dx;

		public PartialDiffParams(final RealVariable dx) {
			this.dx = dx;
		}
	}

	private static final Set<Class<? extends RealExpression>> SUPPORTED_TYPES = //
			Collections.unmodifiableSet(new HashSet<>(Arrays.asList( //
					RealConstant.class, //
					RealVariable.class, //
					RealExprAddition.class, //
					RealExprMultiplication.class, //
					RealExprNegation.class, //
					RealExprReciprocal.class)));

	public VisitorRealExpressionPartialDifferentiator(final RealExpression rootExpression,
			final PartialDiffParams params) {
		super(SUPPORTED_TYPES, rootExpression, params);
	}

	@Override
	protected RealExpression doEvaluation() {
		if (this.params == null) {
			throw new IllegalArgumentException("Cannot start derivation without parameter object");
		}

		// if the root expression does not contain the variable with respect to which we are deriving, the result is 0
		if (!this.rootExpression.getVariables().contains(this.params.dx)) {
			return ZERO;
		}

		return this.rootExpression.accept(this, null);
	}

	@Override
	public RealExpression visit(final RealConstant realConstant, final Void state) {
		// derivative of a constant is always zero
		return ZERO;
	}

	@Override
	public RealExpression visit(final RealVariable realVariable, final Void state) {
		// partial derivative of a variable is 1 iff it is the variable we are deriving for (dx);
		// any other variable is constant w.r.t. dx, so the result is zero
		return (realVariable == this.params.dx) ? ONE : ZERO;
	}

	@Override
	public RealExpression visit(final RealExprAddition realExprAddition, final Void state) {
		// the derivative of a sum is the sum of the derivatives of the summands
		final ArrayList<RealExpression> newSummands = new ArrayList<>(realExprAddition.subexpressions.size());
		for (final RealExpression subexpression : realExprAddition.subexpressions) {
			final RealExpression expr = subexpression.accept(this, null);
			if (expr != ZERO) {
				newSummands.add(expr);
			}
		}

		// to avoid unnecessary complexity, we specially handle the cases of zero and one summands
		if (newSummands.isEmpty()) {
			return ZERO;
		} else if (newSummands.size() == 1) {
			return newSummands.get(0);
		} else {
			return reAdd(newSummands);
		}
	}

	@Override
	public RealExpression visit(final RealExprMultiplication realExprMultiplication, final Void state) {
		final int subexSize = realExprMultiplication.subexpressions.size();
		final ArrayList<RealExpression> newSummands = new ArrayList<>(subexSize);
		final ArrayList<RealExpression> factors = new ArrayList<>(subexSize);
		for (int i = 0; i < subexSize; i++) {
			final RealExpression subexDer = realExprMultiplication.subexpressions.get(i).accept(this, null);
			if (subexDer != ZERO) {
				factors.clear();
				factors.addAll(realExprMultiplication.subexpressions.subList(0, i));
				if (subexDer != ONE || subexSize == 1) {
					factors.add(subexDer); // (d/dx) * f_i
				}
				factors.addAll(realExprMultiplication.subexpressions.subList(i + 1, subexSize));
				newSummands.add(factors.size() > 1 ? reMult(factors) : factors.get(0));
			}
		}

		// to avoid unnecessary complexity, we specially handle the cases of zero and one summands
		if (newSummands.isEmpty()) {
			return ZERO;
		} else if (newSummands.size() == 1) {
			return newSummands.get(0);
		} else {
			return reAdd(newSummands);
		}
	}

	@Override
	public RealExpression visit(final RealExprNegation realExprNegation, final Void state) {
		// negation is just like a factor of -1, so we just negate the derivative of the subexpression;
		// to avoid unnecessary complexity, we specially handle the case of zero
		final RealExpression subexpression = realExprNegation.subexpressions.get(0);
		return subexpression == ZERO ? ZERO : reNeg(subexpression.accept(this, null));
	}

	@Override
	public RealExpression visit(final RealExprReciprocal realExprReciprocal, final Void state) {
		// reciprocal rule: (d/dx) (1/f) = - (1/(f*f)) * (d/dx) f
		final RealExpression subexpression = realExprReciprocal.subexpressions.get(0);
		final RealExpression subexDer = subexpression.accept(this, null);
		if (subexDer == ZERO) {
			return ZERO;
		}
		if (subexDer == ONE) {
			return reNeg(reRecip(reMult(subexpression, subexpression)));
		}
		return reNeg(reMult(reRecip(reMult(subexpression, subexpression)), subexDer));
	}
}
