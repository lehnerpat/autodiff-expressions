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

package nevik.autodiff.expr.real;

import nevik.autodiff.expr.Variable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class for non-terminal real-valued expressions, i.e. expressions that contain one or more
 * sub-expressions.
 *
 * @author Patrick Lehner
 * @since 2015-10-02
 */
public abstract class RealSuperExpression extends RealExpression {
	public final List<RealExpression> subexpressions;
	protected final Set<Variable> variables;

	/**
	 * Create a new super-expression containing the given list of sub-expressions in the given order. It is the caller's
	 * responsibility to ensure that a) {@code subexpressions} is not {@code null}, b) {@code subexpressions} is not
	 * empty (i.e. contains at least one element), and c) {@code subexpressions} contains no {@code null} entries.
	 *
	 * @param subexpressions list of sub-expressions contained in this super-expression
	 */
	protected RealSuperExpression(final List<RealExpression> subexpressions) {
		this.subexpressions = subexpressions;
		final HashSet<Variable> variables = new HashSet<>();
		for (final RealExpression subexpression : subexpressions) {
			variables.addAll(subexpression.getVariables());
		}
		this.variables = Collections.unmodifiableSet(variables);
	}

	@Override
	public Set<Variable> getVariables() {
		return this.variables;
	}
}
