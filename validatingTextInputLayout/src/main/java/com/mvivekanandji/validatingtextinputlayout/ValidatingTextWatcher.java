package com.mvivekanandji.validatingtextinputlayout;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

class ValidatingTextWatcher implements TextWatcher {

    interface OnStateChangedListener {
        void onError(ValidatingTextInputLayout textInputLayout, TextInputLayoutValidator.ValidationError validationError);

        void onSuccess(ValidatingTextInputLayout textInputLayout);
    }

    private final ValidatingTextInputLayout textInputLayout;
    private final OnStateChangedListener onStateChangedListener;

    public ValidatingTextWatcher(@NonNull final ValidatingTextInputLayout textInputLayout,
                                 @NonNull final OnStateChangedListener onStateChangedListener) {
        this.textInputLayout = textInputLayout;
        this.onStateChangedListener = onStateChangedListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (textInputLayout.isRequired() && TextUtils.isEmpty(s)) {
            if (onStateChangedListener != null)
                onStateChangedListener.onError(textInputLayout, TextInputLayoutValidator.ValidationError.REQUIRED);
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(textInputLayout.getRequiredErrorText());

        } else if (textInputLayout.isMinLengthSet() && s.length() < textInputLayout.getMinLength()) {
            if (onStateChangedListener != null)
                onStateChangedListener.onError(textInputLayout, TextInputLayoutValidator.ValidationError.MIN_LENGTH);
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(textInputLayout.getMinLengthErrorText());

        } else if (textInputLayout.isMaxLengthSet() && s.length() > textInputLayout.getMaxLength()) {
            if (onStateChangedListener != null)
                onStateChangedListener.onError(textInputLayout, TextInputLayoutValidator.ValidationError.MAX_LENGTH);
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(textInputLayout.getMaxLengthErrorText());

        } else if (textInputLayout.isValidationRegexSet() && !s.toString().matches(textInputLayout.getValidationRegex())) {
            if (onStateChangedListener != null)
                onStateChangedListener.onError(textInputLayout, TextInputLayoutValidator.ValidationError.REGEX);
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(textInputLayout.getValidationRegexErrorText());

        } else if (textInputLayout.isValidationTypeSet() && !s.toString().matches(textInputLayout.getValidationTypeRegex())) {
            if (onStateChangedListener != null)
                onStateChangedListener.onError(textInputLayout, TextInputLayoutValidator.ValidationError.VALIDATION_TYPE);
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(textInputLayout.getValidationTypeErrorText());

        } else {
            if (onStateChangedListener != null)
                onStateChangedListener.onSuccess(textInputLayout);
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
    }

}
