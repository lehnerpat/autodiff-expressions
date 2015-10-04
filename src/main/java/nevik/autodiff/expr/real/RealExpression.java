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

import nevik.autodiff.expr.Expression;
import nevik.autodiff.expr.real.visitor.VisitorRealExpression;
import nevik.autodiff.util.CachedHashCode;

import java.util.Set;

/**
 * Abstract base class for real-valued expression (corresponding to {@code double} in Java).
 *
 * @author Patrick Lehner
 * @since 2015-10-02
 */
public abstract class RealExpression implements Expression, CachedHashCode {
	// Implementation note: This class does not provide a common implementation of Expression.getVariables(), since the
	// most reasonable way of doing so would be to pass the variable set to the constructor of this class
	// (RealExpression) -- however, RealVariable *cannot* pass this set, since it would contain only the RealVariable
	// instance itself, and a constructor cannot pass `this` to its super constructor. We therefore defer a common
	// implementation of getVariables() for all non-terminal expressions to RealSuperExpression instead.

	public final Set<Class<? extends RealExpression>> usedTypes;

	protected RealExpression(final Set<Class<? extends RealExpression>> usedTypes) {
		this.usedTypes = usedTypes;
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(final Object obj);

	public abstract <ExprResultType, StateType> ExprResultType accept(
			VisitorRealExpression<?, ExprResultType, StateType> visitor, StateType state);
}
