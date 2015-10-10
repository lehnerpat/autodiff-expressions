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
import nevik.autodiff.expr.real.visitor.VisitorRealExpressionSimplifier.SimplifierDiffParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nevik.autodiff.expr.real.RealConstant.ZERO;
import static nevik.autodiff.expr.real.RealConstant.reCons;
import static nevik.autodiff.expr.real.RealExprAddition.reAdd;
import static nevik.autodiff.expr.real.RealExprMultiplication.reMult;
import static nevik.autodiff.expr.real.RealExprNegation.reNeg;
import static nevik.autodiff.expr.real.RealExprReciprocal.reRecip;

/**
 * @author Patrick Lehner
 * @since 2015-10-04
 */
public class VisitorRealExpressionSimplifier
		extends AbstractVisitorRealExpression<SimplifierDiffParams, RealExpression, RealExpression, Void> {
	public static final class SimplifierDiffParams {
		public SimplifierDiffParams() {
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

	public static RealExpression simplify(final RealExpression rootExpression) {
		return new VisitorRealExpressionSimplifier(Objects.requireNonNull(rootExpression), null).evaluate();
	}

	public VisitorRealExpressionSimplifier(final RealExpression rootExpression, final SimplifierDiffParams params) {
		super(SUPPORTED_TYPES, rootExpression, params);
	}

	@Override
	protected RealExpression doEvaluation() {
		//		if (this.params == null) {
		//			throw new IllegalArgumentException("Cannot start derivation without parameter object");
		//		}

		return this.rootExpression.accept(this, null);
	}

	@Override
	public RealExpression visit(final RealConstant realConstant, final Void state) {
		// constant cannot be simplified further
		return realConstant;
	}

	@Override
	public RealExpression visit(final RealVariable realVariable, final Void state) {
		// variable cannot be simplified further
		return realVariable;
	}

	@Override
	public RealExpression visit(final RealExprAddition realExprAddition, final Void state) {
		final int subexprCount = realExprAddition.subexpressions.size();
		final List<RealExpression> simplifiedSummands =
				realExprAddition.subexpressions.stream().map(expr -> expr.accept(this, null)).flatMap(
						expr -> (expr instanceof RealExprAddition) ?
								((RealExprAddition) expr).subexpressions.stream() :
								Stream.of(expr)).collect(Collectors.toList());

		if (subexprCount == 1) {
			// if there's only one summand, we can't simplify further at this level
			return simplifiedSummands.get(0);
		}

		final ArrayList<RealExpression> newSummands = new ArrayList<>(subexprCount);
		final Map<RealExpression, Integer> subexprCounts = new HashMap<>();
		double constantVal = 0;

		for (RealExpression subexpression : simplifiedSummands) {
			int sign = 1;
			if (subexpression instanceof RealExprNegation) {
				subexpression = ((RealExprNegation) subexpression).subexpressions.get(0);
				sign = -1;
			}
			if (subexpression instanceof RealConstant) {
				// collect up all constants
				constantVal += sign * ((RealConstant) subexpression).value;
			} else {
				subexprCounts.put(subexpression, subexprCounts.getOrDefault(subexpression, 0) + sign);
			}
		}

		if (constantVal != 0 || subexprCounts.isEmpty()) {
			newSummands.add(reCons(constantVal));
		}
		for (final Entry<RealExpression, Integer> entry : subexprCounts.entrySet()) {
			final Integer factor = entry.getValue();
			if (factor != 0) {
				if (factor == 1) {
					newSummands.add(entry.getKey());
				} else if (factor == -1) {
					newSummands.add(reNeg(entry.getKey()));
				} else {
					newSummands.add(reMult(reCons(factor), entry.getKey()));
				}
			}
		}

		if (newSummands.isEmpty()) {
			return ZERO;
		} else if (newSummands.size() == 1) {
			return newSummands.get(0);
		} else {
			return reAdd(newSummands); // reAdd sorts the summands
		}
	}

	@Override
	public RealExpression visit(final RealExprMultiplication realExprMultiplication, final Void state) {
		final int subexprCount = realExprMultiplication.subexpressions.size();
		final List<RealExpression> simplifiedFactors =
				realExprMultiplication.subexpressions.stream().map(expr -> expr.accept(this, null)).flatMap(
						expr -> (expr instanceof RealExprMultiplication) ?
								((RealExprMultiplication) expr).subexpressions.stream() :
								Stream.of(expr)).collect(Collectors.toList());

		if (subexprCount == 1) {
			// if there's only one summand, we can't simplify further at this level
			return simplifiedFactors.get(0);
		}

		final ArrayList<RealExpression> newFactors = new ArrayList<>(subexprCount);
		double constantVal = 1;

		for (final RealExpression subexpression : simplifiedFactors) {
			if (subexpression instanceof RealConstant) {
				// collect up all constants
				constantVal *= ((RealConstant) subexpression).value;
			} else {
				newFactors.add(subexpression);
			}
		}

		if (constantVal == 0) {
			return ZERO;
		}

		if (constantVal != 1 || newFactors.isEmpty()) {
			newFactors.add(reCons(constantVal));
		}

		if (newFactors.isEmpty()) {
			return ZERO;
		} else if (newFactors.size() == 1) {
			return newFactors.get(0);
		} else {
			return reMult(newFactors); // reMult sorts the factors
		}
	}

	@Override
	public RealExpression visit(final RealExprNegation realExprNegation, final Void state) {
		final RealExpression simplifiedSubexpr = realExprNegation.subexpressions.get(0).accept(this, null);
		if (simplifiedSubexpr instanceof RealExprNegation) {
			// merge together double-negation
			return ((RealExprNegation) simplifiedSubexpr).subexpressions.get(0);
		} else if (simplifiedSubexpr instanceof RealConstant) {
			// negate constant value
			return reCons(-((RealConstant) simplifiedSubexpr).value);
		} else {
			return reNeg(simplifiedSubexpr);
		}
	}

	@Override
	public RealExpression visit(final RealExprReciprocal realExprReciprocal, final Void state) {
		final RealExpression simplifiedSubexpr = realExprReciprocal.subexpressions.get(0).accept(this, null);
		if (simplifiedSubexpr instanceof RealExprReciprocal) {
			// merge together double-reciprocal
			return ((RealExprReciprocal) simplifiedSubexpr).subexpressions.get(0);
		} else if (simplifiedSubexpr instanceof RealConstant) {
			// compute reciprocal of constant value
			return reCons(1.0 / ((RealConstant) simplifiedSubexpr).value);
		} else {
			return reRecip(simplifiedSubexpr);
		}
	}
}
