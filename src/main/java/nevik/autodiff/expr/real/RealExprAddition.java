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

import java.util.Arrays;
import java.util.List;

/**
 * N-ary addition expression, i.e. sum of 1 or more terms.
 *
 * @author Patrick Lehner
 * @since 2015-10-03
 */
public class RealExprAddition extends RealSuperExpression {
	// ===============================================================================================================
	// ====  Static fields and methods  ==============================================================================
	// ===============================================================================================================
	private static final int HASHCODE_PRIME_OFFSET = 59971;

	/**
	 * Create a new addition expression containing the given list of sub-expressions in the given order.
	 *
	 * @param subexpressions
	 * 		list of sub-expressions contained in this addition expression; must be non-{@code null}, must not be empty,
	 * 		and must not contain any {@code null} elements
	 * @throws java.lang.NullPointerException
	 * 		if {@code subexpressions} is {@code null}
	 * @throws java.lang.IllegalArgumentException
	 * 		if {@code subexpressions} is empty or contains a {@code null} entry
	 */
	public static RealExprAddition reAdd(final List<RealExpression> subexpressions) {
		return new RealExprAddition(subexpressions);
	}

	/**
	 * Create a new addition expression containing the given sub-expressions in the given order.
	 *
	 * @param subexpressions
	 * 		one or more sub-expressions contained in this addition expression; must be non-{@code null} (if passed as
	 * 		array), must not be empty, and must not contain any {@code null} elements
	 * @throws java.lang.NullPointerException
	 * 		if {@code subexpressions} is {@code null}
	 * @throws java.lang.IllegalArgumentException
	 * 		if {@code subexpressions} is empty or contains a {@code null} entry
	 */
	public static RealExprAddition reAdd(final RealExpression... subexpressions) {
		return new RealExprAddition(Arrays.asList(subexpressions));
	}

	// ===============================================================================================================
	// ====  Instance fields and methods  ============================================================================
	// ===============================================================================================================

	protected final int hashCode;

	/**
	 * Create a new addition expression containing the given list of sub-expressions in the given order.
	 *
	 * @param subexpressions
	 * 		list of sub-expressions contained in this addition expression; must be non-{@code null}, must not be empty,
	 * 		and must not contain any {@code null} elements
	 * @throws java.lang.NullPointerException
	 * 		if {@code subexpressions} is {@code null}
	 * @throws java.lang.IllegalArgumentException
	 * 		if {@code subexpressions} is empty or contains a {@code null} entry
	 */
	public RealExprAddition(final List<RealExpression> subexpressions) {
		super(immutableCopy(argCheck(subexpressions)));
		this.hashCode = HASHCODE_PRIME_OFFSET + super.hashCode;
	}

	@Override
	public boolean equals(final Object o) {
		return this == o || (o instanceof RealExprAddition && ((RealExprAddition) o).hashCode == this.hashCode);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}
}
