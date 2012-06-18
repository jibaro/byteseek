/*
 * Copyright Matt Palmer 2012, All rights reserved.
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
package net.domesdaybook.parser.regex;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.domesdaybook.parser.ParseException;
import net.domesdaybook.parser.ParseTree;
import net.domesdaybook.parser.ParseTreeType;
import net.domesdaybook.parser.ParseTreeUtils;
import net.domesdaybook.util.bytes.ByteUtilities;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;

/**
 * @author matt
 *
 */
public class AntlrParseTreeAdaptor extends CommonTreeAdaptor {

  public static final String TYPE_ERROR = "Parse tree type id [&d] with description [%s] is not supported by the parser.";
  
	@Override
	public Object create(Token payload) {
		switch (payload.getType()) {
		
			case regularExpressionParser.BYTE: {
				return new HexByteAdaptor(payload, ParseTreeType.BYTE);
			}
			
			case regularExpressionParser.ALL_BITMASK: {
				return new AllBitmaskAdaptor(payload, ParseTreeType.ALL_BITMASK);
			}
			
			case regularExpressionParser.ANY_BITMASK: {
				return new AnyBitmaskAdaptor(payload, ParseTreeType.ANY_BITMASK);
			}
			
			case regularExpressionParser.ANY: {
				return new ParseTreeAdaptor(payload, ParseTreeType.ANY);
			}
			
			case regularExpressionParser.CASE_INSENSITIVE_STRING: {
				return new QuotedTextAdaptor(payload, ParseTreeType.CASE_INSENSITIVE_STRING);
			}

			case regularExpressionParser.CASE_SENSITIVE_STRING: {
				return new QuotedTextAdaptor(payload, ParseTreeType.CASE_SENSITIVE_STRING);
			}
			
			case regularExpressionParser.SET: {
				return new SetAdaptor(payload, ParseTreeType.SET);
			}
			
			case regularExpressionParser.INVERTED_SET: {
				return new InvertedSetAdaptor(payload, ParseTreeType.INVERTED_SET);
			}
			
			case regularExpressionParser.RANGE: {
				return new RangeAdaptor(payload, ParseTreeType.RANGE);
			}
			
			case regularExpressionParser.NUMBER: {
			  return new IntegerAdaptor(payload, ParseTreeType.INTEGER);
			}
			
			default:
				return new ParseTreeAdaptor(payload, null);
		}
	}

	
	private static class ParseTreeAdaptor extends CommonTree implements ParseTree {

		private final ParseTreeType nodeType;
		
		private ParseTreeAdaptor(Token payload, ParseTreeType nodeType) {
			super(payload);
			this.nodeType = nodeType;
		}
		
		@Override
		public ParseTreeType getParseTreeType() {
			return nodeType;
		}

		@Override
		public byte getByteValue() throws ParseException {
			throw new ParseException("Not supported");
		}

		@Override
		public int getIntValue() throws ParseException {
			throw new ParseException("Not supported");
		}

		@Override
		public long getLongValue() throws ParseException {
			throw new ParseException("Not supported");
		}

		@Override
		public String getTextValue() throws ParseException {
			throw new ParseException("Not supported");
		}

		@Override
		public Collection<Byte> getByteSetValue() throws ParseException {
			throw new ParseException("Not supported");
		}
		
		@SuppressWarnings("unchecked") // We know that all tree nodes are ParseTree implementations.
		@Override
		public List<ParseTree> getChildren() {
			return super.getChildren();
		}
		
	}
	
	public static class HexByteAdaptor extends ParseTreeAdaptor {
		
		public HexByteAdaptor(Token payload, ParseTreeType type) {
			super(payload, type);
		}
		
		@Override
		public byte getByteValue() throws ParseException {
			return ParseTreeUtils.parseHexByte(getText());
		}
		
	}
	
	public static class AnyBitmaskAdaptor extends ParseTreeAdaptor {
		
		public AnyBitmaskAdaptor(Token payload, ParseTreeType type) {
			super(payload, type);
		}
		
		@Override
    public byte getByteValue() throws ParseException {
		//TODO: what is the type of the node under bitmask?  Is it a byte node?
      return ParseTreeUtils.parseHexByte(getChild(0).getText());
    }		
		
		@Override
		public Collection<Byte> getByteSetValue() throws ParseException {
		//TODO: what is the type of the node under bitmask?  Is it a byte node?
			final byte byteValue = ParseTreeUtils.parseHexByte(getChild(0).getText());
			return ByteUtilities.getBytesMatchingAnyBitMask(byteValue);
		}
	}	
	
	public static class AllBitmaskAdaptor extends ParseTreeAdaptor {
		
		public AllBitmaskAdaptor(Token payload, ParseTreeType type) {
			super(payload, type);
		}
		
		@Override
		public byte getByteValue() throws ParseException {
		  //TODO: what is the type of the node under bitmask?  Is it a byte node?
		  return ParseTreeUtils.parseHexByte(getChild(0).getText());
		}
		
		@Override
		public Collection<Byte> getByteSetValue() throws ParseException {
		  //TODO: what is the type of the node under bitmask?  Is it a byte node?
			final byte byteValue = ParseTreeUtils.parseHexByte(getChild(0).getText());
			return ByteUtilities.getBytesMatchingAllBitMask(byteValue);
		}
	}		
	
	public static class QuotedTextAdaptor extends ParseTreeAdaptor {
		
		public QuotedTextAdaptor(Token payload, ParseTreeType type) {
			super(payload, type);
		}
		
		@Override
		public String getTextValue() throws ParseException {
			return ParseTreeUtils.unquoteString(getText());
		}
	}
	
	public static class RangeAdaptor extends ParseTreeAdaptor {
		
		public RangeAdaptor(Token payload, ParseTreeType type) {
			super(payload, type);
		}
		
		@Override
		public Collection<Byte> getByteSetValue() throws ParseException {
			final ParseTree firstChild = (ParseTree) getChild(0);
			final ParseTree secondChild = (ParseTree) getChild(1);
			int minValue, maxValue;
			if (firstChild.getParseTreeType() == ParseTreeType.BYTE) {
				minValue = firstChild.getByteValue() & 0xFF;
				maxValue = secondChild.getByteValue() & 0xFF;
			} else if (firstChild.getParseTreeType() == ParseTreeType.CASE_SENSITIVE_STRING) {
				final String firstTextValue = firstChild.getTextValue();
				if (firstTextValue.length() != 1) {
					throw new ParseException("Only a single character is allowed for range values:" +
											  firstTextValue);
				}
				final String secondTextValue = secondChild.getTextValue();
				if (secondTextValue.length() != 1) {
					throw new ParseException("Only a single character is allowed for range values:" +
											 secondTextValue);
				}
				minValue = firstTextValue.charAt(0);  
				maxValue = secondTextValue.charAt(0); 
			} else {
				throw new ParseException("Only bytes and case sensitive strings are allowed for ranges.");
			}
			if (minValue > maxValue) {
				final int tempSwap = minValue;
				minValue = maxValue;
				maxValue = tempSwap;
			}
			if (minValue < 0 || maxValue > 255) {
				throw new ParseException("Only range values from 0 to 255 are allowed.");
			}
			return buildRange(minValue, maxValue);
		}	
		
		private List<Byte> buildRange(final int from, final int to) {
			final int rangeLength = from - to + 1;
			final List<Byte> byteRange = new ArrayList<Byte>(rangeLength);
			for (int rangeValue = from; rangeValue <= to; rangeValue++) {
				byteRange.add((byte) rangeValue);
			}
			return byteRange;
		}
		
	}
	
	public static class SetAdaptor extends ParseTreeAdaptor {
		
		public SetAdaptor(Token payload, ParseTreeType type) {
			super(payload, type);
		}
		
		@Override
		public Set<Byte> getByteSetValue() throws ParseException {
	    final Set<Byte> setValues = new LinkedHashSet<Byte>(320);
	    for (final ParseTree child : getChildren()) {
	      switch (child.getParseTreeType().getId()) {

          case ParseTreeType.BYTE_ID: {
  	        setValues.add(child.getByteValue());
  	        break;
  	      }
  	      
  	      case ParseTreeType.RANGE_ID:
  	      case ParseTreeType.ALL_BITMASK_ID:
  	      case ParseTreeType.ANY_BITMASK_ID:
  	      case ParseTreeType.INVERTED_SET_ID:
  	      case ParseTreeType.SET_ID:
  	      {
  	        setValues.addAll(child.getByteSetValue());
  	        break;
  	      }     
  
  	      case ParseTreeType.CASE_SENSITIVE_STRING_ID: {
  	        try {
  	          final byte[] utf8Value = child.getTextValue().getBytes("US-ASCII");
  	          ByteUtilities.addAll(utf8Value, setValues);
  	        } catch (UnsupportedEncodingException e) {
  	          throw new ParseException(e);
  	        }
  	        break;
  	      }
  
  	      case ParseTreeType.CASE_INSENSITIVE_STRING_ID: {
  	        final String stringValue = child.getTextValue();
  	        for (int charIndex = 0; charIndex < stringValue.length(); charIndex++) {
  	          final char charAt = stringValue.charAt(charIndex);
  	          if (charAt >= 'a' && charAt <= 'z') {
  	            setValues.add((byte) Character.toUpperCase(charAt));
  
  	          } else if (charAt >= 'A' && charAt <= 'A') {
  	            setValues.add((byte) Character.toLowerCase(charAt));
  	          }
  	          setValues.add((byte) charAt);
  	        }
  	        break;
  	      }
  
  	      default: {
  	        final ParseTreeType type = child.getParseTreeType();
  	        final String message = String.format(TYPE_ERROR, type.getId(), type.getDescription());
  	        throw new ParseException(message);
  	      }
	      }
	    }
	    return setValues;
	  }		
	}		
	
  public static class InvertedSetAdaptor extends SetAdaptor {
    
    public InvertedSetAdaptor(Token payload, ParseTreeType type) {
      super(payload, type);
    }
    
    @Override
    public Set<Byte> getByteSetValue() throws ParseException {
      return ByteUtilities.invertedSet(super.getByteSetValue());
    }
  }
	
  public static class IntegerAdaptor extends ParseTreeAdaptor {
    
    public IntegerAdaptor(Token payload, ParseTreeType type) {
      super(payload, type);
    }
    
    @Override
    public int getIntValue() throws ParseException {
      try {
        return Integer.parseInt(getText());
      } catch (NumberFormatException nfe) {
        throw new ParseException("Could not parse value into an integer:" + getText());
      }
    }
  }
	
}

