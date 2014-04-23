/**
 * Copyright 2010-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.mojo.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * Permet d'avoir les fichiers de proprietes tries.
 *
 * @author ruchaud <ruchaud@codelutin.com>
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class SortedProperties extends Properties {

    private static final long serialVersionUID = -1147150444452577558L;

    /**
     * l'encoding a utiliser pour lire et ecrire le properties.
     */
    protected String encoding;

    /**
     * un drapeau pour savoir s'il faut enlever l'entete generere
     */
    protected boolean removeHeader;

    public SortedProperties(String encoding) {
        this(encoding, true);
    }

    public SortedProperties(String encoding, boolean removeHeader) {
        this.encoding = encoding;
        this.removeHeader = removeHeader;
    }

    public SortedProperties(Properties defaults) {
        super(defaults);
    }

    @Override
    public Enumeration<Object> keys() {
        List<Object> objects = Collections.list(super.keys());
        Vector<Object> result;
        try {
            // Attention, si les clef ne sont pas des string, ca ne marchera pas
            List<String> list = toGenericList(objects, String.class);
            Collections.sort(list);
            result = new Vector<Object>(list);
        } catch (IllegalArgumentException e) {
            // keys are not string !!!
            // can not sort keys
            result = new Vector<Object>(objects);
        }
        return result.elements();
    }

    /**
     * Charge le properties a partir d'un fichier.
     *
     * @param src
     *            le fichier src a charger en utilisant l'encoding declare
     * @return l'instance du properties
     * @throws java.io.IOException
     *             if any io pb
     */
    public SortedProperties load(File src) throws IOException {
        FileInputStream reader = new FileInputStream(src);
        try {
            load(reader);
        } finally {
            reader.close();
        }
        return this;
    }

    /**
     * Sauvegarde le properties dans un fichier, sans commentaire et en utilisant l'encoding declare.
     *
     * @param dst
     *            the fichier de destination
     * @throws java.io.IOException
     *             if any io pb
     */
    public void store(File dst) throws IOException {
        OutputStream writer = new FileOutputStream(dst);
        try {
            store(writer, null);
        } finally {
            writer.close();
        }
    }

    /**
     * Permet de convertir une liste non typee, en une liste typee.
     * <p/>
     * La liste en entree en juste bien castee.
     * <p/>
     * On effectue une verification sur le typage des elements de la liste.
     * <p/>
     * Note : <b>Aucune liste n'est creee, ni recopiee</b>
     *
     * @param <O>
     *            le type des objets de la liste
     * @param list
     *            la liste a convertir
     * @param type
     *            le type des elements de la liste
     * @return la liste typee
     * @throws IllegalArgumentException
     *             si un element de la liste en entree n'est pas en adequation avec le type voulue.
     */
    @SuppressWarnings({ "unchecked" })
    static public <O> List<O> toGenericList(List<?> list, Class<O> type) throws IllegalArgumentException {
        if (list.isEmpty()) {
            return (List<O>) list;
        }
        for (Object o : list) {
            if (!type.isAssignableFrom(o.getClass())) {
                throw new IllegalArgumentException("can not cast List with object of type " + o.getClass() + " to "
                        + type + " type!");
            }
        }
        return (List<O>) list;
    }

}
