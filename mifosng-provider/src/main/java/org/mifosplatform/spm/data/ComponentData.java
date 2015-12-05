/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.spm.data;

public class ComponentData {

    private String key;
    private String text;
    private String description;
    private Integer sequenceNo;

    public ComponentData() {
        super();
    }

    public ComponentData(final String key, final String text,
                         final String description, final Integer sequenceNo) {
        super();
        this.key = key;
        this.text = text;
        this.description = description;
        this.sequenceNo = sequenceNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String title) {
        this.text = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
}
