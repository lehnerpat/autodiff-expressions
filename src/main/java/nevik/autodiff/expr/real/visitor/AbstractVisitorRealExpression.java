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

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Patrick Lehner
 * @since 2015-10-04
 */
public abstract class AbstractVisitorRealExpression<ParamType, ResultType>
		implements VisitorRealExpression<ResultType> {
	protected final Set<Class<? extends RealExpression>> supportedTypes;
	protected RealExpression rootExpression;
	protected ParamType params;

	protected AbstractVisitorRealExpression(final Set<Class<? extends RealExpression>> supportedTypes,
			final ParamType params, final RealExpression rootExpression) {
		this.params = params;
		this.supportedTypes = supportedTypes;
		this.rootExpression = rootExpression;
	}

	@Override
	public Set<Class<? extends RealExpression>> getSupportedTypes() {
		return this.supportedTypes;
	}

	@Override
	public ResultType evaluate() {
		if (!supportedTypes.containsAll(rootExpression.usedTypes)) {
			throw new IllegalArgumentException(String.format("This visitor does not support all expression types "
							+ "occurring in the given root expression.\n\t\tTypes in this root expression:   %s\n"
							+ "\t\tTypes supported by this visitor: %s\n\t\t=> Unsupported: %s",
					rootExpression.usedTypes.stream().map(Class::getSimpleName).sorted()
							.collect(Collectors.joining(", ")),
					this.supportedTypes.stream().map(Class::getSimpleName).sorted().collect(Collectors.joining(", ")),
					rootExpression.usedTypes.stream().filter(e -> !this.supportedTypes.contains(e))
							.map(Class::getSimpleName).sorted().collect(Collectors.joining(", "))));
		}
		return this.doEvaluation();
	}

	public ResultType evaluate(final RealExpression rootExpression) {
		this.setRootExpression(rootExpression);
		return this.evaluate();
	}

	public ResultType evaluate(final ParamType params) {
		this.setParams(params);
		return this.evaluate();
	}

	public ResultType evaluate(final ParamType params, final RealExpression rootExpression) {
		this.setParams(params);
		this.setRootExpression(rootExpression);
		return this.evaluate();
	}

	protected abstract ResultType doEvaluation();

	public void setRootExpression(final RealExpression rootExpression) {
		this.rootExpression = rootExpression;
	}

	public void setParams(final ParamType params) {
		this.params = params;
	}

	// ===============================================================================================================
	// ====  visit(...) method stubs  ================================================================================
	// ===============================================================================================================

	@Override
	public void visit(final RealConstant realConstant) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(final RealVariable realVariable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(final RealExprAddition realExprAddition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(final RealExprMultiplication realExprMultiplication) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(final RealExprNegation realExprNegation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(final RealExprReciprocal realExprReciprocal) {
		throw new UnsupportedOperationException();
	}
}
