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

package net.domesdaybook.automata.trie;

import java.util.Collection;
import net.domesdaybook.automata.StateFactory;
import net.domesdaybook.automata.TransitionFactory;
import net.domesdaybook.matcher.sequence.SequenceMatcher;

/**
 * An implementation of {@link Trie} formed of {@link SequenceMatcher}s.
 * It extends the {@link AbstractTrie} class.
 * 
 * @author Matt Palmer
 * @see <a href="http://en.wikipedia.org/wiki/Trie">Trie structures</a>
 */
public final class SequenceMatcherTrie extends AbstractTrie<SequenceMatcher> {


    /**
     * Constructs a SequenceMatcherTrie with no sequences added to it initially.
     * using the default {@link net.domesdaybook.automata.StateFactory}
     * , {@link net.domesdaybook.automata.base.BaseStateFactory}, and the default
     * {@link net.domesdaybook.automata.TransitionFactory}, {@link net.domesdaybook.automata.base.ByteMatcherTransitionFactory}.
     */
    public SequenceMatcherTrie() {
        this(null, null, null);
    }
    
    
    /**
     * Constructs a Trie using the default {@link net.domesdaybook.automata.StateFactory}
     * , {@link net.domesdaybook.automata.base.BaseStateFactory}, and the default
     * {@link net.domesdaybook.automata.TransitionFactory}, {@link net.domesdaybook.automata.base.ByteMatcherTransitionFactory}.
     * 
     * @param sequences A collection of SequenceMatchers to add to the Trie.
     */
    public SequenceMatcherTrie(final Collection<? extends SequenceMatcher> sequences) {
        this(sequences, null, null);
    }        
    
    
    /**
     * Constructs a Trie using the supplied {@link net.domesdaybook.automata.StateFactory}
     * and the default {@link net.domesdaybook.automata.TransitionFactory}, {@link net.domesdaybook.automata.base.ByteMatcherTransitionFactory}.
     * 
     * @param stateFactory The StateFactory to use to create States for the Trie.
     */
    public SequenceMatcherTrie(final StateFactory<SequenceMatcher> stateFactory) {
        this(null, stateFactory, null);
    }
    
 
    /**
     * Constructs a Trie using the default {@link net.domesdaybook.automata.StateFactory}
     * , {@link net.domesdaybook.automata.base.BaseStateFactory}, and the supplied
     * {@link net.domesdaybook.automata.TransitionFactory}.
     * 
     * @param transitionFactory The TransitionFactory to use to create Transitions for the Trie.
     */
    public SequenceMatcherTrie(final TransitionFactory<SequenceMatcher> transitionFactory) {
        this(null, null, transitionFactory);
    }
    
    
    /**
     * Constructs a Trie using the supplied {@link net.domesdaybook.automata.StateFactory}
     * and the default {@link net.domesdaybook.automata.TransitionFactory}, {@link net.domesdaybook.automata.base.ByteMatcherTransitionFactory}.
     * 
     * @param sequences The initial collection of SequenceMatchers to add to the Trie.
     * @param stateFactory The StateFactory to use to create States for the Trie.
     */
    public SequenceMatcherTrie(final Collection<? extends SequenceMatcher> sequences,
                               final StateFactory<SequenceMatcher> stateFactory) {
        this(sequences, stateFactory, null);
    }    
    
    
    /**
     * Constructs a Trie using the default {@link net.domesdaybook.automata.StateFactory}
     * , {@link net.domesdaybook.automata.base.BaseStateFactory}, and the supplied
     * {@link net.domesdaybook.automata.TransitionFactory}.
     * 
     * @param sequences The initial collection of SequenceMatchers to add to the Trie.
     * @param transitionFactory The TransitionFactory to use to create Transitions for the Trie.
     */
    public SequenceMatcherTrie(final Collection<? extends SequenceMatcher> sequences,
                              final TransitionFactory<SequenceMatcher> transitionFactory) {
        this(sequences, null, transitionFactory);
    }
        
    
    /**
     * Constructs a Trie using the supplied {@link net.domesdaybook.automata.StateFactory}
     * and {@link net.domesdaybook.automata.TransitionFactory}.
     * 
     * @param stateFactory The StateFactory to use to create States for the Trie.
     * @param transitionFactory The TransitionFactory to use to create Transitions for the Trie.
     */
    public SequenceMatcherTrie(final StateFactory<SequenceMatcher> stateFactory, 
                               final TransitionFactory<SequenceMatcher> transitionFactory) {
        this(null, stateFactory, transitionFactory);
    }
    
    
    /**
     * Constructs a Trie using the supplied {@link net.domesdaybook.automata.StateFactory}
     * and {@link net.domesdaybook.automata.TransitionFactory}.
     * 
     * @param sequences The initial collection of SequenceMatchers to add to the Trie.
     * @param stateFactory The StateFactory to use to create States for the Trie.
     * @param transitionFactory The TransitionFactory to use to create Transitions for the Trie.
     */
    public SequenceMatcherTrie(final Collection<? extends SequenceMatcher> sequences, 
                               final StateFactory<SequenceMatcher> stateFactory, 
                               final TransitionFactory<SequenceMatcher> transitionFactory) {
        super(stateFactory, transitionFactory);
        if (sequences != null) {
            addAll(sequences);
        }
    }    
    

    /**
     * Returns the length of a {@link SequenceMatcher}.
     * 
     * @param sequence The SequenceMatcher to return the length of.
     * @return int the length of the SequenceMatcher.
     */
    @Override
    protected int getSequenceLength(final SequenceMatcher sequence) {
        return sequence.length();
    }

    
    /**
     * Returns an array of bytes for the values of bytes that exist at a 
     * given position in the SequenceMatcher.
     * 
     * @param sequence The SequenceMatcher to get the byte values for.
     * @param position The position in the SequenceMatcher to get the byte values.
     * @return A byte array containing the bytes which exist in the SequenceMatcher at the given position.
     */
    @Override
    protected byte[] getBytesForPosition(final SequenceMatcher sequence, final int position) {
        return sequence.getMatcherForPosition(position).getMatchingBytes();
    }
    
}
