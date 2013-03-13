package se.vgregion.portal.iframe.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-28 11:19
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class LoginField implements Serializable {
    private static final long serialVersionUID = -8278668379674747318L;

    private boolean use;
    private boolean nameField;
    private boolean passwordField;
    private boolean dynamicField;
    private boolean extraField;
    private String fieldName;
    private String fieldValue;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public boolean isDynamicField() {
        return dynamicField;
    }

    public void setDynamicField(boolean dynamicField) {
        this.dynamicField = dynamicField;
    }

    public boolean isExtraField() {
        return extraField;
    }

    public void setExtraField(boolean extraField) {
        this.extraField = extraField;
    }

    public boolean isNameField() {
        return nameField;
    }

    public void setNameField(boolean nameField) {
        this.nameField = nameField;
    }

    public boolean isPasswordField() {
        return passwordField;
    }

    public void setPasswordField(boolean passwordField) {
        this.passwordField = passwordField;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }
}
