/*
 * Copyright Matt Palmer 2009-2012, All rights reserved.
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

package net.byteseek.automata.regex;

import java.util.List;

import net.byteseek.automata.Automata;

/**
 * An interface for classes which build automata that implement regular expressions,
 * wrapped in {@link Automata} objects.
 *
 * @param <T> The type of object associated with a match of the regular expression.
 * @param <S> The type of object used to build a transition from.
 * 
 * @author Matt Palmer
 */
public interface RegexBuilder<T, S> {

    public Automata<T> buildTransitionAutomata(S source, boolean inverted);
    
    /**
     * Builds an automata by joining the list of automata passed in
     * as a sequence.
     *
     * @param sequenceStates A list of automata to join into a sequence.
     * @return An automata which is a sequence of all the automata passed in.
     */
    public Automata<T> buildSequenceAutomata(final List<Automata<T>> sequenceStates);


    /**
     * Builds an automata by joining the list of automata passed in
     * as a set of alternatives.
     *
     * @param alternateStates A list of automata to join into a set of alternatives.
     * @return An automata which allows for a set of alternatives.
     */
    public Automata<T> buildAlternativesAutomata(final List<Automata<T>> alternateStates);


    /**
     * Builds an automata which repeats the automata passed in from a minimum
     * number of repeats up to a maximum number of repeats.
     *
     * @param minRepeat The minimum number of repeats for the automata.
     * @param maxRepeat The maximum number of repeats for the automata.
     * @param repeatedAutomata The automata to repeat.
     * @return An automata which repeats from min to max times.
     */
    public Automata<T> buildMinToMaxAutomata(final int minRepeat, final int maxRepeat, final Automata<T> repeatedAutomata);


    /**
     * Builds an automata which repeats from zero to many times.
     *
     * @param zeroToManyStates The automata to repeat from zero to many times.
     * @return An automata which repeats from zero to many times.
     */
    public Automata<T> buildZeroToManyAutomata(final Automata<T> zeroToManyStates);


    /**
     * Builds an automata which repeats from one to many times.
     *
     * @param oneToManyStates 
     * @return An automata which repeats from one to many times.
     */
    public Automata<T> buildOneToManyAutomata(final Automata<T> oneToManyStates);


    /**
     * Builds an automata which repeats from a minimum number of times to many.
     *
     * @param minRepeat The minimum number of repeats.
     * @param repeatedAutomata The automata which can repeat from min to many times.
     * @return An automata which repeats from a minimum number of times to many.
     */
    public Automata<T> buildMinToManyAutomata(final int minRepeat, final Automata<T> repeatedAutomata);


    /**
     * Builds an automata which repeats a given automata a defined number of times.
     *
     * @param repeatNumber The number of times to repeat the automata.
     * @param repeatedAutomta The automata to repeat.
     * @return An automata which repeats the automata passed in a defined number of times.
     */
    public Automata<T> buildRepeatedAutomata(final int repeatNumber, final Automata<T> repeatedAutomta);


    /**
     * Builds an automata consisting of a defined number of optional automata.
     *
     * @param numberOptional The number of optional automata to repeat.
     * @param optionalState The automata to create repeat optionally.
     * @return An automata which optionally repeats the automata passed in a defined number of times.
     */
    public Automata<T> buildRepeatedOptionalAutomata(final int numberOptional, final Automata<T> optionalState);


    /**
     * Builds an automata which is optional.
     *
     * @param optionalStates The automata to make optional.
     * @return An automata which is an optional version of the automata passed in.
     */
    public Automata<T> buildOptionalAutomata(final Automata<T> optionalStates);
}
