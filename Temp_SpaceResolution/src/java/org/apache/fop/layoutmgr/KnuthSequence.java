/*
 * Copyright 2004-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.layoutmgr;

import java.util.ArrayList;

/**
 * Represents a list of Knuth elements.
 */
public class KnuthSequence extends ArrayList {
    /** Number of elements to ignore at the beginning of the list. */ 
    public int ignoreAtStart = 0;
    /** Number of elements to ignore at the end of the list. */
    public int ignoreAtEnd = 0;
    // Is this an inline or a block sequence?
    private boolean isInlineSequence = false;

    /**
     * Creates a new and empty list.
     */
    public KnuthSequence() {
        super();
    }

    /**
     * Creates a new and empty list, and sets isInlineSequence.
     */
    public KnuthSequence(boolean isInlineSequence) {
        super();
        this.isInlineSequence = isInlineSequence;
    }

    /**
     * Marks the start of the sequence.
     */
    public void startSequence() {
    }

    /**
     * Finalizes a Knuth sequence.
     * @return a finalized sequence.
     */
    public KnuthSequence endSequence() {
        return endSequence(null);
    }
    
    /**
     * Finalizes a Knuth sequence.
     * @param breakPosition a Position instance for the last penalty (may be null)
     * @return a finalized sequence.
     */
    public KnuthSequence endSequence(Position breakPosition) {
        // remove glue and penalty item at the end of the paragraph
        while (this.size() > ignoreAtStart
               && !((KnuthElement)this.get(this.size() - 1)).isBox()) {
            this.remove(this.size() - 1);
        }
        if (this.size() > ignoreAtStart) {
            // add the elements representing the space at the end of the last line
            // and the forced break
            this.add(new KnuthPenalty(0, KnuthElement.INFINITE, false, null, false));
            this.add(new KnuthGlue(0, 10000000, 0, null, false));
            this.add(new KnuthPenalty(0, -KnuthElement.INFINITE, false, breakPosition, false));
            ignoreAtEnd = 3;
            return this;
        } else {
            this.clear();
            return null;
        }
    }

    public KnuthElement getLast() {
        int idx = size();
        if (idx == 0) {
            return null; 
        }
        return (KnuthElement) get(idx - 1);
    }

    public KnuthElement removeLast() {
        int idx = size();
        if (idx == 0) {
            return null; 
        }
        return (KnuthElement) remove(idx - 1);
    }

    public KnuthElement getElement(int index) {
        return (KnuthElement) get(index);
    }

    /**
     * Is this an inline or a block sequence?
     * @return true if this is an inline sequence
     */
    public boolean isInlineSequence() {
        return isInlineSequence;
    }
}