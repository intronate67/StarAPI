/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 socraticphoenix@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Socratic_Phoenix (socraticphoenix@gmail.com)
 */
package com.gmail.socraticphoenix.sponge.star.chat.condition;

import com.gmail.socraticphoenix.sponge.star.chat.arguments.StarArgumentKeyValue;

public class Condition {
    private String key;
    private Verifier verifier;

    public Condition(String key, Verifier verifier) {
        this.key = key;
        this.verifier = verifier;
    }

    public String getKey() {
        return key;
    }

    public Verifier getVerifier() {
        return verifier;
    }

    public VerificationResult verify(StarArgumentKeyValue keyValue) {
        if(keyValue.getKey().equals(this.key)) {
            return this.verifier.verify(keyValue);
        } else {
            return VerificationResult.failure("Internal error: Condition did not match key");
        }
    }

    public boolean canVerify(StarArgumentKeyValue keyValue) {
        return keyValue.getKey().equals(this.key);
    }

}