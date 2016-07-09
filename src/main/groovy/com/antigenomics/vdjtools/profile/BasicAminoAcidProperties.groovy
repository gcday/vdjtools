/*
 * Copyright (c) 2015, Bolotin Dmitry, Chudakov Dmitry, Shugay Mikhail
 * (here and after addressed as Inventors)
 * All Rights Reserved
 *
 * Permission to use, copy, modify and distribute any part of this program for
 * educational, research and non-profit purposes, by non-profit institutions
 * only, without fee, and without a written agreement is hereby granted,
 * provided that the above copyright notice, this paragraph and the following
 * three paragraphs appear in all copies.
 *
 * Those desiring to incorporate this work into commercial products or use for
 * commercial purposes should contact the Inventors using one of the following
 * email addresses: chudakovdm@mail.ru, chudakovdm@gmail.com
 *
 * IN NO EVENT SHALL THE INVENTORS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 * ARISING OUT OF THE USE OF THIS SOFTWARE, EVEN IF THE INVENTORS HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE SOFTWARE PROVIDED HEREIN IS ON AN "AS IS" BASIS, AND THE INVENTORS HAS
 * NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 * MODIFICATIONS. THE INVENTORS MAKES NO REPRESENTATIONS AND EXTENDS NO
 * WARRANTIES OF ANY KIND, EITHER IMPLIED OR EXPRESS, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A
 * PARTICULAR PURPOSE, OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY
 * PATENT, TRADEMARK OR OTHER RIGHTS.
 */

package com.antigenomics.vdjtools.profile

import com.antigenomics.vdjtools.misc.CommonUtil

class BasicAminoAcidProperties {
    static final BasicAminoAcidProperties INSTANCE = new BasicAminoAcidProperties()

    private final AminoAcidProperty[] aminoAcidProperties

    private BasicAminoAcidProperties() {
        aminoAcidProperties = AminoAcidProperty.fromInput(CommonUtil.resourceStream("profile/aa_property_table.txt"))
    }

    List<String> getPropertyNames() {
        aminoAcidProperties.collect { it.name }
    }

    AminoAcidProperty[] getProperties(List<String> propertyNames) {
        if (propertyNames.isEmpty())
            return getProperties()

        propertyNames = propertyNames.collect { it.toLowerCase() }

        aminoAcidProperties.findAll { propertyNames.contains(it.name.toLowerCase()) } as AminoAcidProperty[]
    }

    AminoAcidProperty getProperty(String name) {
        name = name.toLowerCase()
        aminoAcidProperties.find { name.contains(it.name.toLowerCase()) }
    }

    AminoAcidProperty[] getProperties() {
        Arrays.copyOf(aminoAcidProperties, aminoAcidProperties.length)
    }
}
