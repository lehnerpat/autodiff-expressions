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
import nevik.autodiff.expr.real.visitor.VisitorRealExpression;

import java.util.Collections;
import java.util.Set;

/**
 * Real-valued symbolic variable, with an optional name (which could be used, for example, in String representations of
 * an expression).
 * <p/>
 * {@code RealVariable}s use instance-identity for equality testing, i.e. different instances are never considered
 * equal (even if they have the same {@link #name}).
 *
 * @author Patrick Lehner
 * @since 2015-10-02
 */
public class RealVariable extends RealExpression implements Variable<RealVariable> {
	// ===============================================================================================================
	// ====  Static fields and methods  ==============================================================================
	// ===============================================================================================================

	/**
	 * Format string used to create the output of {@link #toString()}. Must handle exactly two arguments: a
	 * {@link String} and an {@code int} (the hash code).
	 */
	private static final String FMT_STR = "ReVar{'%s'@%08x}";

	// ===============================================================================================================
	// ====  Instance fields and methods  ============================================================================
	// ===============================================================================================================

	/**
	 * Name for this variable. Optional (may be {@code null}), and need not necessarily be used in String
	 * representations that contain this variable.
	 */
	public final String name;
	protected final Set<RealVariable> variables;
	protected final int hashCode;

	/**
	 * Create a new {@link RealVariable} with the given {@code name} (may be {@code null}).
	 *
	 * @param name
	 * 		name for this variable, used mostly in the code; optional, may be {@code null}
	 * @see #RealVariable()
	 */
	public RealVariable(final String name) {
		super(Collections.singleton(RealVariable.class));
		this.name = name;
		this.variables = Collections.singleton(this);
		this.hashCode = System.identityHashCode(this); // cache the hash of this object
	}

	/**
	 * Create a new {@link RealVariable} with no name (i.e. {@link #name} is {@code null}).
	 *
	 * @see #RealVariable(String)
	 */
	public RealVariable() {
		this(null);
	}

	/**
	 * Get a set of variables on which this expression depends. Since a variable is a terminal expression, this set is
	 * a singleton containing only this variable.
	 *
	 * @return an immutable singleton set containing this variable
	 */
	@Override
	public Set<RealVariable> getVariables() {
		return this.variables;
	}

	/**
	 * Get a String representation of this variable object. Note that this is not just the value of {@link #name}, but
	 * contains text to indicate it is a {@code RealVariable} instance.
	 *
	 * @return a String representation of this variable object
	 */
	@Override
	public String toString() {
		return String.format(FMT_STR, name, this.hashCode);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(final Object o) {
		return o == this;
	}

	@Override
	public <ExprResultType, StateType> ExprResultType accept(
			final VisitorRealExpression<?, ExprResultType, StateType> visitor, final StateType state) {
		return visitor.visit(this, state);
	}
}
