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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.byteseek.parser.ParseException;
import net.byteseek.parser.tree.ParseTree;
import net.byteseek.parser.tree.ParseTreeType;

import org.junit.Test;

public class StringNodeTest {

	@Test
	public final void testTextValues() {
		testNodes("");
		testNodes("a");
		testNodes("                                                                           ");
		testNodes("Oberon");
		testNodes("Titania");
		testNodes("I know a bank where the wild thyme grows,");
		testNodes(getAllCharsFromZeroTo255());
	}
	
	@Test
	public final void testNullEmptyValue() throws ParseException {
		ParseTree node = new StringNode(null);
		assertEquals("Node is a STRING", ParseTreeType.STRING, node.getParseTreeType());
		assertEquals("Node has empty value", "", node.getTextValue());
		
		node = new StringNode(null, ParseTreeType.STRING);
		assertEquals("Node is a STRING", ParseTreeType.STRING, node.getParseTreeType());
		assertEquals("Node has empty value", "", node.getTextValue());

		node = new StringNode(null, ParseTreeType.CASE_INSENSITIVE_STRING);
		assertEquals("Node is a CASE_INSENSITIVE_STRING", ParseTreeType.CASE_INSENSITIVE_STRING, node.getParseTreeType());
		assertEquals("Node has empty value", "", node.getTextValue());

		node = new StringNode("");
		assertEquals("Node is a STRING", ParseTreeType.STRING, node.getParseTreeType());
		assertEquals("Node has empty value", "", node.getTextValue());
		
		node = new StringNode("", ParseTreeType.STRING);
		assertEquals("Node is a STRING", ParseTreeType.STRING, node.getParseTreeType());
		assertEquals("Node has empty value", "", node.getTextValue());

		node = new StringNode("", ParseTreeType.CASE_INSENSITIVE_STRING);
		assertEquals("Node is a CASE_INSENSITIVE_STRING", ParseTreeType.CASE_INSENSITIVE_STRING, node.getParseTreeType());
		assertEquals("Node has empty value", "", node.getTextValue());
	}
	
	@SuppressWarnings("unused")
	@Test
	public final void testInvalidConstructorTypes() {
		for (ParseTreeType type : ParseTreeType.values()) {
			if (type != ParseTreeType.STRING && type != ParseTreeType.CASE_INSENSITIVE_STRING) {
				try {
					new StringNode("Bad type", type);
					fail("String node should not be constructed with anything other than STRING or CASE_INSENSITIVE_STRING.  Type was " + type);
				} catch (IllegalArgumentException expected) {
				}
			}
		}
	}
	
	private String getAllCharsFromZeroTo255() {
		StringBuilder builder = new StringBuilder(256);
		for (char c = 0; c < 256; c++) {
			builder.append(c);
		}
		return builder.toString();
	}
	
	private void testNodes(String value) {
		testNode("Default case sensitive: ",     new StringNode(value), true, value);
		testNode("Specified case sensitive: ",   new StringNode(value, ParseTreeType.STRING), true, value);
		testNode("Specified case insensitive: ", new StringNode(value, ParseTreeType.CASE_INSENSITIVE_STRING), false, value);
	}
	
	private void testNode(String description, StringNode node, boolean isCaseSensitive, String value) {
		testNodeAttributes(description + "(original value) ", node, isCaseSensitive, value);
	}
	
	private void testNodeAttributes(String description, StringNode node, boolean isCaseSensitive, String value) {
		
		ParseTreeType expectedType = isCaseSensitive? ParseTreeType.STRING : ParseTreeType.CASE_INSENSITIVE_STRING;
		assertEquals(description + "Node is case sensitive?: " + isCaseSensitive, expectedType, node.getParseTreeType());
		assertEquals(description + "Node agrees its case sensitive status:" + isCaseSensitive, isCaseSensitive, node.isCaseSensitive());
		
		try {
			assertEquals(description + "Node has correct string value: [" + value + ']', value, node.getTextValue());
		} catch (ParseException e) {
			fail(description + "Should not throw ParseException requesting text value.");
		}
		
		assertFalse(description + "Node is not inverted.", node.isValueInverted());
	
		try { 
			node.getByteValue();
			fail(description + "Expected a ParseException if asked for the byte value");
		} catch (ParseException allIsFine) {};
		
		try { 
			node.getIntValue();
			fail(description + "Expected a ParseException if asked for the int value");
		} catch (ParseException allIsFine) {};
		
		assertEquals(description + "Child list is empty", 0, node.getNumChildren());	
		assertTrue("toString contains class name", node.toString().contains(node.getClass().getSimpleName()));
	}

}
