/*
 * Copyright Matt Palmer 2011-2012, All rights reserved.
 *
 * This code is licensed under a standard 3-clause BSD license:
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 *  * The names of its contributors may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.byteseek.io.reader;

/**
 * A Window is essentially a wrapper for a byte array containing bytes from the
 * {@link WindowReader} that creates it, at a specified position in the WindowReader.
 * Windows contain the position in the WindowReader they begin from, and how long the
 * Window is.
 * <p>
 * The byte array is not copied, which means that mutable state is directly
 * wrapped by this class, although the Window itself is immutable. The entire
 * reason for having a Window is to facilitate direct access to the underlying
 * byte arrays taken from a WindowReader in order to optimise read performance.
 * <p>
 * Note that the length of the Window may be less than the byte array backing
 * it, so only bytes up to the length of the Window will be from the actual
 * WindowReader. For example, the last Window read from a file will almost certainly
 * be shorter than the byte array that backs it.
 * 
 * @author Matt Palmer
 */
public final class Window {

	private final byte[] bytes;
	private final long windowPosition;
	private final int length;

	/**
	 * Constructs a Window using the byte array provided, recording the position
	 * in the WindowReader from which the bytes provided were read, and the length of
	 * the Window (which may be shorter than the length of the backing byte
	 * array).
	 * <p>
	 * The byte array is not copied, which means that mutable state is directly
	 * wrapped by this class, although the Window itself is immutable. The
	 * entire reason for having a Window is to facilitate direct access to the
	 * underlying byte arrays taken from a WindowReader in order to optimise read
	 * performance. = *
	 * 
	 * @param bytes
	 *            The byte array to wrap.
	 * @param windowPosition
	 *            The position at which the Window starts.
	 * @param length
	 *            An ending position of a slice of the array.
	 */
	public Window(final byte[] bytes, final long windowPosition,
			final int length) {
		if (bytes == null) {
			throw new IllegalArgumentException(
					"Null byte array passed in to Array.");
		}
		this.bytes = bytes; // a Window wraps a byte array - no defensive
							// copying should be allowed.
		this.windowPosition = windowPosition;
		this.length = length;
	}

	/**
	 * Gets a byte from the Window relative to the start of the Window (not
	 * relative to the start of the WindowReader). It simply returns the byte at the
	 * position in the byte array that backs the Window.
	 * <p>
	 * Note that no bounds checking is done by this method. It is possible to
	 * read bytes in the byte array which are beyond the length of the Window
	 * itself (since a Window can be shorter than the byte array which backs
	 * it).
	 * 
	 * @param position
	 *            The position in the Window to read a byte from.
	 * @return The byte at that position in the Window.
	 * @throws IndexOutOfBoundsException
	 *             if the position is less than zero, or past the end of the
	 *             byte array which backs this Window.
	 */
	public byte getByte(final int position) {
		return bytes[position];
	}

	/**
	 * Returns the array of bytes backing this Window. It does not clone or
	 * return a copy of the bytes, as the entire goal is performance. Hence, it
	 * is possible to abuse this. Clients should not alter the array returned by
	 * this method.
	 * 
	 * @return The byte array which backs this Window.
	 */
	public byte[] getArray() {
		return bytes; // a Window wraps a byte array - no defensive copying
						// should be allowed.
	}

	/**
	 * Returns the position in the WindowReader that this Window was read from.
	 * 
	 * @return The position in the WindowReader that this Window was read from.
	 */
	public long getWindowPosition() {
		return windowPosition;
	}

	/**
	 * Returns the final position in this window.  It is equivalent
	 * to the window position plus the length of the window, minus one.
	 *
	 * @return the last position in this window.
	 */
	public long getWindowEndPosition() {
		return windowPosition + length - 1;
	}

	/**
	 * Returns the starting position of the window after this one.  It is
	 * equivalent to the window position plus the length of this window.
	 *
	 * @return The starting position of the window after this one.
	 */
	public long getNextWindowPosition() {
		return windowPosition + length;
	}


	/**
	 * Returns the length of the Window. Note that this may be shorter than the
	 * length of the byte array which backs this Window.
	 * 
	 * @return The length of the Window.
	 */
	public int length() {
		return length;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[bytes: " + bytes + " bytes length: " + bytes.length + 
				                            " window length:" + length + " window pos:" + windowPosition + ']'; 
	}
}
