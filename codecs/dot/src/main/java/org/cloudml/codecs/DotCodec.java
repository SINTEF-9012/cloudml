/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.codecs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.NamedElement;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;

public class DotCodec implements Codec {

    @Override
    public NamedElement load(InputStream content) {
        throw new UnsupportedOperationException("The DOT codec does not support extracting CloudML model from DOT file.");
    }

    @Override
    public void save(NamedElement model, OutputStream content) {
        failIfInvalid(content);
        Visitable visitable = failIfInvalid(model);
        final DotPrinter printer = new DotPrinter();
        visitable.accept(new Visitor(printer));
        try {
            content.write(printer.getDotText().getBytes());
       
        } catch (IOException ex) {
            throw new RuntimeException("Unable to write the DOC code into the given output stream", ex);
        }
    }

    private void failIfInvalid(OutputStream content) {
        if (content == null) {
            throw new IllegalArgumentException("'null' is not a valid output stream");
        }
    }

    private Visitable failIfInvalid(NamedElement model) {
        if (model == null) {
            throw new IllegalArgumentException("'null' cannot be converted as a DOT file");
        }
        if (!(model instanceof Visitable)) {
            throw new IllegalArgumentException("Only vistable model element can be converted to DOT (found '" + model.getClass().getName() + "')");
        }
        return (Visitable) model;
    }

}
