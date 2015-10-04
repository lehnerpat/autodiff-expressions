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

import com.sun.xml.internal.ws.wsdl.writer.document.ParamType;
import nevik.autodiff.expr.real.RealConstant;
import nevik.autodiff.expr.real.RealExprAddition;
import nevik.autodiff.expr.real.RealExprMultiplication;
import nevik.autodiff.expr.real.RealExpression;
import nevik.autodiff.expr.real.RealSuperExpression;
import nevik.autodiff.expr.real.RealVariable;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Patrick Lehner
 * @since 2015-10-04
 */
public class VisitorRealExpressionPrintingInfix extends AbstractVisitorRealExpression<Map<RealVariable, String>, Void> {
	private static final Set<Class<? extends RealExpression>> SUPPORTED_TYPES = Collections.unmodifiableSet(new HashSet<>(
			Arrays.asList(RealConstant.class, RealVariable.class, RealExprAddition.class, RealExprMultiplication.class)));

	protected ArrayDeque<String> stack;

	public VisitorRealExpressionPrintingInfix(final Map<RealVariable, String> params, final RealExpression rootExpression) {
		super(SUPPORTED_TYPES, params, rootExpression);
	}

	@Override
	protected Void doEvaluation() {
		if (this.params == null && this.rootExpression.usedTypes.contains(RealVariable.class))
			throw new IllegalArgumentException("Cannot print expression containing variables without a name map (param)");
		stack = new ArrayDeque<>();
		this.rootExpression.accept(this);
		System.out.println(stack.pop());
		return null;
	}

	protected void visitRecursively(final RealSuperExpression superExpression) {
		for (final RealExpression subexpression : superExpression.subexpressions) {
			subexpression.accept(this);
		}
	}

	@Override
	public void visit(final RealConstant realConstant) {
		stack.push(String.valueOf(realConstant.value));
	}

	@Override
	public void visit(final RealVariable realVariable) {
		stack.push(this.params.get(realVariable));
	}

	@Override
	public void visit(final RealExprAddition realExprAddition) {
		visitRecursively(realExprAddition);
		String s = stack.pop() + ")";
		for (int i = 1; i < realExprAddition.subexpressions.size(); i++) {
			s = stack.pop() + " + " + s;
		}
		stack.push("(" + s);
	}

	@Override
	public void visit(final RealExprMultiplication realExprMultiplication) {
		visitRecursively(realExprMultiplication);
		String s = stack.pop() + ")";
		for (int i = 1; i < realExprMultiplication.subexpressions.size(); i++) {
			s = stack.pop() + " * " + s;
		}
		stack.push("(" + s);
	}
}
