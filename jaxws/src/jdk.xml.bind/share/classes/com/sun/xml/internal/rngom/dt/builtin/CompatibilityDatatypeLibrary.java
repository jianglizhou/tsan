/*
 * Copyright (c) 2005, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * Copyright (C) 2004-2011
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sun.xml.internal.rngom.dt.builtin;

import org.relaxng.datatype.Datatype;
import org.relaxng.datatype.DatatypeBuilder;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.DatatypeLibrary;
import org.relaxng.datatype.DatatypeLibraryFactory;
import com.sun.xml.internal.rngom.xml.util.WellKnownNamespaces;

class CompatibilityDatatypeLibrary implements DatatypeLibrary {
    private final DatatypeLibraryFactory factory;
    private DatatypeLibrary xsdDatatypeLibrary = null;

    CompatibilityDatatypeLibrary(DatatypeLibraryFactory factory) {
        this.factory = factory;
    }

    public DatatypeBuilder createDatatypeBuilder(String type)
        throws DatatypeException {
        if (type.equals("ID")
            || type.equals("IDREF")
            || type.equals("IDREFS")) {
            if (xsdDatatypeLibrary == null) {
                xsdDatatypeLibrary =
                    factory.createDatatypeLibrary(
                        WellKnownNamespaces.XML_SCHEMA_DATATYPES);
                if (xsdDatatypeLibrary == null)
                    throw new DatatypeException();
            }
            return xsdDatatypeLibrary.createDatatypeBuilder(type);
        }
        throw new DatatypeException();
    }

    public Datatype createDatatype(String type) throws DatatypeException {
        return createDatatypeBuilder(type).createDatatype();
    }
}