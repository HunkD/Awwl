@XmlSchema(
        elementFormDefault= XmlNsForm.UNQUALIFIED,
        attributeFormDefault = XmlNsForm.QUALIFIED,
        namespace = "http://schemas.android.com/apk/res/android",
        xmlns={
                @XmlNs(prefix="android",namespaceURI="http://schemas.android.com/apk/res/android")
        }
)
package com.hunkd.annotation.manifest.xml.elements;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;