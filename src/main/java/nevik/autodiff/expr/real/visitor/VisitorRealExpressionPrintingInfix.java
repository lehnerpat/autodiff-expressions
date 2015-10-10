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
import nevik.autodiff.expr.real.visitor.VisitorRealExpressionPrintingInfix.PrintingInfixParams;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Patrick Lehner
 * @since 2015-10-04
 */
public class VisitorRealExpressionPrintingInfix
		extends AbstractVisitorRealExpression<PrintingInfixParams, Void, Void, Void> {
	public static final class PrintingInfixParams {
		private final Map<RealVariable, String> varNames;
		private final PrintStream output;

		public PrintingInfixParams(final Map<RealVariable, String> varNames, final PrintStream output) {
			this.varNames = varNames;
			this.output = output;
		}
	}

	private static final Set<Class<? extends RealExpression>> SUPPORTED_TYPES = //
			Collections.unmodifiableSet(new HashSet<>(Arrays.asList( //
					RealConstant.class, //
					RealVariable.class, //
					RealExprAddition.class, //
					RealExprMultiplication.class, //
					RealExprReciprocal.class, //
					RealExprNegation.class)));

	public static void printExpression(final RealExpression rootExpression, final PrintingInfixParams params) {
		new VisitorRealExpressionPrintingInfix(rootExpression, params).evaluate();
	}

	public static void printExpression(final RealExpression rootExpression, final Map<RealVariable, String> varNames,
			final PrintStream output) {
		printExpression(rootExpression, new PrintingInfixParams(varNames, output));
	}

	public static void printExpression(final RealExpression rootExpression, final Map<RealVariable, String> varNames) {
		printExpression(rootExpression, varNames, System.out);
	}

	public static void printExpression(final RealExpression rootExpression) {
		printExpression(rootExpression, null, System.out);
	}

	public VisitorRealExpressionPrintingInfix(final RealExpression rootExpression, final PrintingInfixParams params) {
		super(SUPPORTED_TYPES, rootExpression, params);
	}

	@Override
	protected Void doEvaluation() {
		if (this.params.varNames == null && this.rootExpression.usedTypes.contains(RealVariable.class)) {
			throw new IllegalArgumentException(
					"Cannot print expression containing variables without a name map (param)");
		}
		this.rootExpression.accept(this, null);
		return null;
	}

	@Override
	public Void visit(final RealConstant realConstant, final Void state) {
		this.params.output.print(realConstant.value);
		return null;
	}

	@Override
	public Void visit(final RealVariable realVariable, final Void state) {
		this.params.output.print(Objects.requireNonNull(this.params.varNames.get(realVariable)));
		return null;
	}

	@Override
	public Void visit(final RealExprAddition realExprAddition, final Void state) {
		final Iterator<RealExpression> iterator = realExprAddition.subexpressions.iterator();
		this.params.output.print("(");
		iterator.next().accept(this, null);
		while (iterator.hasNext()) {
			this.params.output.print(" + ");
			iterator.next().accept(this, null);
		}
		this.params.output.print(")");
		return null;
	}

	@Override
	public Void visit(final RealExprMultiplication realExprMultiplication, final Void state) {
		final Iterator<RealExpression> iterator = realExprMultiplication.subexpressions.iterator();
		this.params.output.print("(");
		iterator.next().accept(this, null);
		while (iterator.hasNext()) {
			this.params.output.print(" * ");
			iterator.next().accept(this, null);
		}
		this.params.output.print(")");
		return null;
	}

	@Override
	public Void visit(final RealExprNegation realExprNegation, final Void state) {
		this.params.output.print("-");
		realExprNegation.subexpressions.get(0).accept(this, null);
		return null;
	}

	@Override
	public Void visit(final RealExprReciprocal realExprReciprocal, final Void state) {
		this.params.output.print("(1/");
		realExprReciprocal.subexpressions.get(0).accept(this, null);
		this.params.output.print(")");
		return null;
	}
}
