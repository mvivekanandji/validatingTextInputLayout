/*
 * Copyright 2021 Vivekanand Mishra.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mvivekanandji.validatingtextinputlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

/**
 * <h1>An advanced auto validating implementation of {@link TextInputLayout} to be used in
 * association with {@link TextInputLayoutValidator}</h1>
 *
 * @author vivekanand
 * @version 0.2.0
 */
public class ValidatingTextInputLayout extends TextInputLayout {

    private static final String REGEX_ALPHA = "\\p{Alpha}+";
    private static final String REGEX_ALPHA_WITH_SYMBOL = "[\\p{Alpha}\\p{Punct}]+";
    private static final String REGEX_NUMERIC = "\\p{Digit}+";
    private static final String REGEX_NUMERIC_WITH_SYMBOL = "[\\p{Digit}\\p{Punct}]+";
    private static final String REGEX_ALPHA_NUMERIC = "\\p{Alnum}+";
    private static final String REGEX_ALPHA_NUMERIC_WITH_SYMBOL = "\\p{Graph}+";
    private static final String REGEX_EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+ (\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String REGEX_IP = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.\n ([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public enum ValidationCheck {
        ON_TRIGGER,
        ALWAYS,
    }

    public enum ValidationType {
        NONE,
        ALPHA,
        ALPHA_WITH_SYMBOL,
        NUMERIC,
        NUMERIC_WITH_SYMBOL,
        ALPHA_NUMERIC,
        ALPHA_NUMERIC_WITH_SYMBOL,
        EMAIL,
        IP;
    }

    private boolean required;
    private String requiredErrorText;
    private int minLength;
    private String minLengthErrorText;
    private int maxLength;
    private String maxLengthErrorText;
    private String validationRegex;
    private String validationRegexErrorText;
    private ValidationType validationType;
    private String validationTypeErrorText;
    private boolean defaultValidated;
    private ValidationCheck validationCheck;

    public ValidatingTextInputLayout(@NonNull Context context) {
        super(context);
    }

    public ValidatingTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().
                obtainStyledAttributes(attrs, R.styleable.ValidatingTextInputLayout, 0, 0);

        required = typedArray.getBoolean(R.styleable.ValidatingTextInputLayout_required, false);
        if (typedArray.hasValue(R.styleable.ValidatingTextInputLayout_requiredErrorText))
            requiredErrorText = typedArray.getString(R.styleable.ValidatingTextInputLayout_requiredErrorText);
        else requiredErrorText = "Required*";

        minLength = typedArray.getInt(R.styleable.ValidatingTextInputLayout_minLength, -1);
        if (typedArray.hasValue(R.styleable.ValidatingTextInputLayout_minLengthErrorText))
            minLengthErrorText = typedArray.getString(R.styleable.ValidatingTextInputLayout_minLengthErrorText);
        else minLengthErrorText = "Minimum length should be greater than " + minLength;

        maxLength = typedArray.getInt(R.styleable.ValidatingTextInputLayout_maxLength, -1);
        if (typedArray.hasValue(R.styleable.ValidatingTextInputLayout_maxLengthErrorText))
            maxLengthErrorText = typedArray.getString(R.styleable.ValidatingTextInputLayout_maxLengthErrorText);
        else maxLengthErrorText = "Maximum length should be less than " + maxLength;

        if (typedArray.hasValue(R.styleable.ValidatingTextInputLayout_validationRegex))
            validationRegex = typedArray.getString(R.styleable.ValidatingTextInputLayout_validationRegex);
        else validationRegex = null;

        if (typedArray.hasValue(R.styleable.ValidatingTextInputLayout_validationRegexErrorText))
            validationRegexErrorText = typedArray.getString(R.styleable.ValidatingTextInputLayout_validationRegexErrorText);
        else validationRegexErrorText = "Regex Validation Failed!";

        validationType = ValidationType.values()[typedArray.getInt(R.styleable.ValidatingTextInputLayout_validationType, 0)];
        if (typedArray.hasValue(R.styleable.ValidatingTextInputLayout_validationTypeErrorText))
            validationTypeErrorText = typedArray.getString(R.styleable.ValidatingTextInputLayout_validationTypeErrorText);
        else validationTypeErrorText = validationType.toString() + " Validation Failed!";

        defaultValidated = typedArray.getBoolean(R.styleable.ValidatingTextInputLayout_defaultValidated, false);
        validationCheck = ValidationCheck.values()[typedArray.getInt(R.styleable.ValidatingTextInputLayout_validationCheck, 0)];

        typedArray.recycle();
    }

    public ValidatingTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getRequiredErrorText() {
        return requiredErrorText;
    }

    public void setRequiredErrorText(String requiredErrorText) {
        this.requiredErrorText = requiredErrorText;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public String getMinLengthErrorText() {
        return minLengthErrorText;
    }

    public void setMinLengthErrorText(String minLengthErrorText) {
        this.minLengthErrorText = minLengthErrorText;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getMaxLengthErrorText() {
        return maxLengthErrorText;
    }

    public void setMaxLengthErrorText(String maxLengthErrorText) {
        this.maxLengthErrorText = maxLengthErrorText;
    }

    public String getValidationRegex() {
        return validationRegex;
    }

    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    public String getValidationRegexErrorText() {
        return validationRegexErrorText;
    }

    public void setValidationRegexErrorText(String validationRegexErrorText) {
        this.validationRegexErrorText = validationRegexErrorText;
    }

    public ValidationType getValidationType() {
        return validationType;
    }

    public void setValidationType(ValidationType validationType) {
        this.validationType = validationType;
    }

    public String getValidationTypeErrorText() {
        return validationTypeErrorText;
    }

    public void setValidationTypeErrorText(String validationTypeErrorText) {
        this.validationTypeErrorText = validationTypeErrorText;
    }

    public boolean isDefaultValidated() {
        return defaultValidated;
    }

    public void setDefaultValidated(boolean defaultValidated) {
        this.defaultValidated = defaultValidated;
    }

    public ValidationCheck getValidationCheck() {
        return validationCheck;
    }

    public void setValidationCheck(ValidationCheck validationCheck) {
        this.validationCheck = validationCheck;
    }

    boolean isMinLengthSet() {
        return minLength != -1;
    }

    boolean isMaxLengthSet() {
        return maxLength != -1;
    }

    boolean isValidationRegexSet() {
        return validationRegex != null;
    }

    boolean isValidationTypeSet() {
        return validationType != ValidationType.NONE;
    }

    String getValidationTypeRegex() {
        String regex = "";

        switch (validationType) {
            case NONE:
                break;
            case ALPHA:
                regex = REGEX_ALPHA;
                break;
            case ALPHA_WITH_SYMBOL:
                regex = REGEX_ALPHA_WITH_SYMBOL;
                break;
            case NUMERIC:
                regex = REGEX_NUMERIC;
                break;
            case NUMERIC_WITH_SYMBOL:
                regex = REGEX_NUMERIC_WITH_SYMBOL;
                break;
            case ALPHA_NUMERIC:
                regex = REGEX_ALPHA_NUMERIC;
                break;
            case ALPHA_NUMERIC_WITH_SYMBOL:
                regex = REGEX_ALPHA_NUMERIC_WITH_SYMBOL;
                break;
            case EMAIL:
                regex = REGEX_EMAIL;
                break;
            case IP:
                regex = REGEX_IP;
                break;
        }

        return regex;
    }

    boolean isContinuousValidationRequired() {
        return validationCheck.equals(ValidationCheck.ALWAYS);
    }

    boolean isAnyValidationSet() {
        return (!isDefaultValidated()) || isRequired() || isMinLengthSet() || isMaxLengthSet() || isValidationRegexSet() | isValidationTypeSet();
    }
}
