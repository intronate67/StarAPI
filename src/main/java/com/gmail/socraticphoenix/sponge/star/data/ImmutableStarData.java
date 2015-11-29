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
package com.gmail.socraticphoenix.sponge.star.data;

import com.gmail.socraticphoenix.plasma.file.cif.cifc.CIFCConfiguration;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.common.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;

public class ImmutableStarData extends AbstractImmutableSingleData<CIFCConfiguration, ImmutableStarData, StarData> {

    protected ImmutableStarData(CIFCConfiguration value) {
        super(ImmutableStarData.class, value, StarKeys.STAR_DATA);
    }

    @Override
    protected ImmutableValue<?> getValueGetter() {
        return null;
    }

    @Override
    public StarData asMutable() {
        return new StarData(this.getValue());
    }

    @Override
    public int compareTo(ImmutableStarData o) {
        return this.data().get().size() - o.data().get().size();
    }

    public ImmutableValue<CIFCConfiguration> data() {
        return new ImmutableSpongeValue<>(StarKeys.STAR_DATA, new CIFCConfiguration(), this.getValue());
    }

}
