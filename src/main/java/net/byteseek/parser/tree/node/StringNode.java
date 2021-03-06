/*
 * Copyright Matt Palmer 2012-2013, All rights reserved.
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

package net.byteseek.parser.tree.node;

import net.byteseek.parser.ParseException;
import net.byteseek.parser.tree.ParseTreeType;


/**
 * An immutable ParseTree node that has a string value.
 * <p>
 * The ParseTreeType is normally ParseTreeType.STRING, unless the node
 * should match case-insensitively, in which case the ParseTreeType 
 * will be ParseTreeType.CASE_INSENSITIVE_STRING.
 * <p>
 * StringNodes have no children, and can not be inverted.
 * 
 * @author Matt Palmer
 *
 */
public final class StringNode extends BaseNode {

  private String value;

  /**
   * Constructs a StringNode with the given String.
   * 
   * @param value The String for this StringNode.
   */
  public StringNode(final String value) {
    this(value, ParseTreeType.STRING);
  }
  
  
  /**
   * Constructs a StringNode with the given String and whether the node
   * should match case sensitively or not.  If the string passed in is null, then an empty
   * string will be used for the StringNode.
   * 
   * @param value The String for this StringNode.
   * @param type The ParseTreeType of the StringNode.  It can only be STRING or CASE_INSENSITIVE_STRING.
   * @throws IllegalArgumentException if type is not STRING or CASE_INSENSITIVE_STRING.
   */
  public StringNode(final String value, final ParseTreeType type) {
    super(type);
    if (type != ParseTreeType.STRING && type != ParseTreeType.CASE_INSENSITIVE_STRING) {
    	throw new IllegalArgumentException("A StringNode can only be of type STRING or CASE_INSENSITIVE_STRING. " + 
    										"The type passed in was [" + type + ']');
    }
    this.value = value == null? "" : value;
  }

  /**
   * Gets the text value of this StringNode.
   * 
   * @return String the String of this StringNode.
   */
  @Override
  public String getTextValue() throws ParseException {
    return value;
  }
  

  /**
   * Returns whether the string matches case sensitively or not.
   * 
   * @return boolean True if the string matches case-sensitively (ParseTreeType.STRING), 
   *                  False if the string is case-insensitive (ParseTreeType.CASE_INSENSITIVE_STRING).
   */
  public boolean isCaseSensitive() {
	  return getParseTreeType() == ParseTreeType.STRING;
  }
  
  @Override
  public String toString() {
	  return getClass().getSimpleName() + '[' + getParseTreeType() + ", value:" + value + ']';
  }

}
