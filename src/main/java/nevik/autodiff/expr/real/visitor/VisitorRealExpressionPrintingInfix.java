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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Patrick Lehner
 * @since 2015-10-04
 */
public class VisitorRealExpressionPrintingInfix extends AbstractVisitorRealExpression<Map<RealVariable, String>, Void, Void, Void> {
	private static final Set<Class<? extends RealExpression>> SUPPORTED_TYPES = Collections.unmodifiableSet(new HashSet<>(
			Arrays.asList(RealConstant.class, RealVariable.class, RealExprAddition.class, RealExprMultiplication.class)));

	public VisitorRealExpressionPrintingInfix(final Map<RealVariable, String> params, final RealExpression rootExpression) {
		super(SUPPORTED_TYPES, params, rootExpression);
	}

	@Override
	protected Void doEvaluation() {
		if (this.params == null && this.rootExpression.usedTypes.contains(RealVariable.class))
			throw new IllegalArgumentException("Cannot print expression containing variables without a name map (param)");
		this.rootExpression.accept(this, null);
		return null;
	}

	@Override
	public Void visit(final RealConstant realConstant, final Void state) {
		System.out.print(realConstant.value);
		return null;
	}

	@Override
	public Void visit(final RealVariable realVariable, final Void state) {
		System.out.print(this.params.get(realVariable));
		return null;
	}

	@Override
	public Void visit(final RealExprAddition realExprAddition, final Void state) {
		final Iterator<RealExpression> iterator = realExprAddition.subexpressions.iterator();
		System.out.print("(");
		iterator.next().accept(this, null);
		while (iterator.hasNext()) {
			System.out.print(" + ");
			iterator.next().accept(this, null);
		}
		System.out.print(")");
		return null;
	}

	@Override
	public Void visit(final RealExprMultiplication realExprMultiplication, final Void state) {
		final Iterator<RealExpression> iterator = realExprMultiplication.subexpressions.iterator();
		System.out.print("(");
		iterator.next().accept(this, null);
		while (iterator.hasNext()) {
			System.out.print(" * ");
			iterator.next().accept(this, null);
		}
		System.out.print(")");
		return null;
	}
}
