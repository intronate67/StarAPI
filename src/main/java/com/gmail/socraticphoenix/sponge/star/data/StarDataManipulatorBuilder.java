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
import com.gmail.socraticphoenix.plasma.file.cif.cifc.io.CIFCReader;
import com.gmail.socraticphoenix.plasma.file.cif.io.CIFException;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.common.data.util.DataUtil;

import java.util.Optional;

public class StarDataManipulatorBuilder implements DataManipulatorBuilder<StarData, ImmutableStarData> {

    @Override
    public StarData create() {
        return new StarData(new CIFCConfiguration());
    }

    @Override
    public Optional<StarData> createFrom(DataHolder dataHolder) {
        return null; //I don't know what to do here
    }

    @Override
    public Optional<StarData> build(DataView container) throws InvalidDataException {
        try {
            String config = DataUtil.getData(container, StarKeys.STAR_DATA, String.class);
            CIFCConfiguration starData = CIFCReader.read(config);
            return Optional.of(new StarData(starData));
        } catch (CIFException e) {
            throw new InvalidDataException("CIFC Data was invalid: ".concat(e.toString()));
        }
    }

}
