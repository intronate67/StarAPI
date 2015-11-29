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
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.common.data.value.mutable.SpongeValue;

public class StarData extends AbstractSingleData<CIFCConfiguration, StarData, ImmutableStarData> {

    protected StarData(CIFCConfiguration value) {
        super(StarData.class, value, StarKeys.STAR_DATA);
    }

    @Override
    protected Value<?> getValueGetter() {
        return this.data();
    }

    @Override
    public StarData copy() {
        return new StarData(this.getValue());
    }

    @Override
    public ImmutableStarData asImmutable() {
        return new ImmutableStarData(this.getValue());
    }

    @Override
    public int compareTo(StarData o) {
        return this.data().get().size() - o.data().get().size();
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer().set(StarKeys.STAR_DATA.getQuery(), this.getValue());
    }

    public Value<CIFCConfiguration> data() {
        return new SpongeValue<>(StarKeys.STAR_DATA, new CIFCConfiguration(), this.getValue());
    }

}
