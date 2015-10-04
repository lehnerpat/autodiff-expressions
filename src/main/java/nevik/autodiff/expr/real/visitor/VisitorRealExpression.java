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

/**
 * @author Patrick Lehner
 * @since 2015-10-04
 */
public interface VisitorRealExpression<ResultType> {
	ResultType evaluate();
	Set<Class<? extends RealExpression>> getSupportedTypes();
	void visit(RealConstant realConstant);
	void visit(RealVariable realVariable);
	void visit(RealExprAddition realExprAddition);
	void visit(RealExprMultiplication realExprMultiplication);
	void visit(RealExprNegation realExprNegation);
	void visit(RealExprReciprocal realExprReciprocal);
}
