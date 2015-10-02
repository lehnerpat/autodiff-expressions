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

package nevik.autodiff.util;

/**
 * Marker interface for classes whose instances cache their hash code, either upon initialization or upon first call of
 * {@link #hashCode()}. Specifically, any implementation of this interface must turn the {@code hashCode()} method into
 * a simple getter method that merely returns a value and runs no (complex) computations, guaranteeing a very fast
 * return time.
 * <p/>
 * This serves mostly as information to the programmer, but might be used in specialized algorithms to use optimized
 * behavior.
 *
 * @author Patrick Lehner
 * @since 2015-10-02
 */
public interface CachedHashCode {
	@Override
	int hashCode();
}
