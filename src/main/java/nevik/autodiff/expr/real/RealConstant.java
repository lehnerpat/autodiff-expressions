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

import nevik.autodiff.expr.Constant;
import nevik.autodiff.expr.real.visitor.VisitorRealExpression;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Constant real value.
 * <p/>
 * {@code RealConstant}s use value-based equality checking (i.e. two instances whose {@link #value}s are the same are
 * considered equal). However, this class also acts as a static instance manager for {@code RealConstant}s, and you
 * retrieve any instance via the static factory method {@link #reCons(double)}. This way you are guaranteed that, when
 * passing the same {@code value} to {@code reCons(double)}, it will return the same {@code RealConstant} instance.
 *
 * @author Patrick Lehner
 * @since 2015-10-02
 */
public class RealConstant extends RealExpression implements Constant<RealVariable> {
	// ===============================================================================================================
	// ====  Static fields and methods  ==============================================================================
	// ===============================================================================================================

	private static final int HASHCODE_PRIME_OFFSET = 6007;
	/**
	 * Value->Instance map used for instance management. Contains all {@code RealConstant} instances that have been
	 * constructed since this JVM instance has been started.
	 */
	private static final Map<Double, RealConstant> INSTANCE_MAP = new HashMap<>();
	/**
	 * Correctly typed empty set returned by {@link #getVariables()}. Cached in a variable to prevent method calls to
	 * {@link Collections#emptySet()} all the time.
	 */
	private static final Set<RealVariable> EMPTY_VARIABLE_SET = Collections.emptySet();
	/**
	 * Format string used to create the output of {@link #toString()}. Must handle exactly one {@code double} argument.
	 */
	private static final String FMT_STR = "ReCons{%f}";

	/**
	 * Constant representing the value {@code 0.0}, provided for convenience.
	 */
	public static final RealConstant ZERO = reCons(0.0);
	/**
	 * Constant representing the value {@code 1.0}, provided for convenience.
	 */
	public static final RealConstant ONE = reCons(1.0);

	/**
	 * Get the {@link RealConstant} instance representing the given {@code value}. If such an instance does not already
	 * exist, it is created, otherwise it is returned from the cache.
	 *
	 * @param value
	 * 		the {@code double} value to represent
	 * @return a {@code RealConstant} representing {@code value}
	 */
	public static RealConstant reCons(final double value) {
		RealConstant realConstant = INSTANCE_MAP.get(value);
		if (realConstant == null) {
			realConstant = new RealConstant(value);
			INSTANCE_MAP.put(value, realConstant);
		}
		return realConstant;
	}

	private static int computeHashCode(final double value) {
		final long temp = Double.doubleToLongBits(value);
		return HASHCODE_PRIME_OFFSET + (int) (temp ^ (temp >>> 32));
	}

	// ===============================================================================================================
	// ====  Instance fields and methods  ============================================================================
	// ===============================================================================================================

	/**
	 * The {@code double} value represented by this constant.
	 */
	public final double value;
	protected final int hashCode;

	private RealConstant(final double value) {
		super(Collections.singleton(RealConstant.class));
		this.value = value;
		this.hashCode = computeHashCode(this.value);
	}

	/**
	 * Get a set of variables on which this expression depends. Since a constant is a terminal expression that does not
	 * depend on any variables, this method returns an empty set.
	 *
	 * @return an immutable empty set
	 */
	@Override
	public Set<RealVariable> getVariables() {
		return EMPTY_VARIABLE_SET;
	}

	/**
	 * Get a String representation of this constant object. Note that this is not just the {@link #value} represented
	 * by this instance, but contains text to indicate it is a {@code RealConstant} instance.
	 *
	 * @return a String representation of this variable object
	 */
	@Override
	public String toString() {
		return String.format(FMT_STR, this.value);
	}

	@Override
	public boolean equals(final Object o) {
		return (this == o) || (o instanceof RealConstant && ((RealConstant) o).hashCode == this.hashCode);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public <ExprResultType, StateType> ExprResultType accept(
			final VisitorRealExpression<?, ExprResultType, StateType> visitor, final StateType state) {
		return visitor.visit(this, state);
	}
}
