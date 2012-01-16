/*
 * Copyright Matt Palmer 2011, All rights reserved.
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

package net.domesdaybook.reader.windowcache;

import net.domesdaybook.reader.Window;
import net.domesdaybook.reader.windowcache.WindowCache.WindowObserver;

/**
 *
 * @author Matt Palmer
 */

public final class TwoLevelCache extends AbstractCache implements WindowObserver {

    /**
     * 
     * @param primaryCache
     * @param secondaryCache
     * @return
     */
    public static TwoLevelCache create(final WindowCache primaryCache, final WindowCache secondaryCache) {
        final TwoLevelCache twoLevels = new TwoLevelCache(primaryCache, secondaryCache);
        primaryCache.subscribe(twoLevels);
        secondaryCache.subscribe(twoLevels);
        return twoLevels;
    }
    
    private final WindowCache primaryCache;
    private final WindowCache secondaryCache;

    private TwoLevelCache(final WindowCache primaryCache, final WindowCache secondaryCache) {
        this.primaryCache = primaryCache;
        this.secondaryCache = secondaryCache;
    }
    
    
    /**
     * 
     * @param position
     * @return
     */
    @Override
    public Window getWindow(final long position) {
        Window window = primaryCache.getWindow(position);
        if (window == null) {
            window = secondaryCache.getWindow(position);
            if (window != null) {
                addWindow(window);
            }
        }
        return window;
    }

    
    /**
     * 
     * @param window
     */
    @Override
    public void addWindow(final Window window) {
        primaryCache.addWindow(window);
    }

    
    /**
     * 
     */
    @Override
    public void clear() {
        primaryCache.clear();
        secondaryCache.clear();
    }

    
    /**
     * 
     * @param window
     * 
     */
    @Override
    public void windowFree(final Window window, final WindowCache fromCache) {
        if (fromCache == primaryCache) {
            secondaryCache.addWindow(window);
        } else if (fromCache == secondaryCache) {
            notifyWindowFree(window, fromCache);
        }
    }
    
    
    /**
     * 
     * @return
     */
    public WindowCache getPrimaryCache() {
        return primaryCache;
    }
    
    
    /**
     * 
     * @return
     */
    public WindowCache getSecondaryCache() {
        return secondaryCache;
    }
    
    
}