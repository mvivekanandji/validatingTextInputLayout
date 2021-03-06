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

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>Class used to bootstrap the validation fields of {@link ValidatingTextInputLayout}
 *
 * @author vivekanand
 * @version 0.2.0
 */
public class TextInputLayoutValidator implements ValidatingTextWatcher.OnStateChangedListener {

    public interface ValidatorListener {
        void onValidateErrors(List<ValidatingTextInputLayout> errorLayoutList, List<ValidationError> validationErrorList);

        void onError(ValidatingTextInputLayout inputLayout, ValidationError validationError);

        void onErrorResolved(ValidatingTextInputLayout inputLayout);

        void onSuccess();
    }

    public enum ValidationError {
        REQUIRED,
        MIN_LENGTH,
        MAX_LENGTH,
        REGEX,
        VALIDATION_TYPE;
    }

    private final Map<ValidatingTextInputLayout, Pair> inputLayoutPairMap;
    private final ValidatorListener validatorListener;

    public TextInputLayoutValidator(@NonNull final ViewGroup viewGroup, @NonNull final ValidatorListener validatorListener) {
        inputLayoutPairMap = new HashMap<>();
        this.validatorListener = validatorListener;
        initLayoutList(viewGroup);
        attachListeners();
    }

    private void initLayoutList(@NonNull final ViewGroup viewGroup) {
        for (int i = 0, count = viewGroup.getChildCount(); i < count; i++) {
            View view = viewGroup.getChildAt(i);

            if (view instanceof ValidatingTextInputLayout)
                inputLayoutPairMap.put((ValidatingTextInputLayout) view, new Pair(!((ValidatingTextInputLayout) view).isAnyValidationSet()));
            else if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                initLayoutList((ViewGroup) view);
        }
    }

    public void validate() {
        List<ValidatingTextInputLayout> errorLayoutList = new ArrayList<>();
        List<ValidationError> validationErrorList = new ArrayList<>();

        for (Map.Entry<ValidatingTextInputLayout, Pair> entry : inputLayoutPairMap.entrySet()) {
            String text = entry.getKey().getEditText().getText().toString();

            if (!entry.getValue().isTextWatcherAttached())
                attachTextWatcher(entry.getKey(), entry.getValue());

            if (entry.getKey().isRequired() && TextUtils.isEmpty(text)) {
                entry.getKey().setError(entry.getKey().getRequiredErrorText());
                errorLayoutList.add(entry.getKey());
                validationErrorList.add(ValidationError.REQUIRED);

            } else if (entry.getKey().isMinLengthSet() && text.length() < entry.getKey().getMinLength()) {
                entry.getKey().setError(entry.getKey().getMinLengthErrorText());
                errorLayoutList.add(entry.getKey());
                validationErrorList.add(ValidationError.MIN_LENGTH);

            } else if (entry.getKey().isMaxLengthSet() && text.length() > entry.getKey().getMaxLength()) {
                entry.getKey().setError(entry.getKey().getMaxLengthErrorText());
                errorLayoutList.add(entry.getKey());
                validationErrorList.add(ValidationError.MAX_LENGTH);

            } else if (entry.getKey().isValidationRegexSet() && !text.matches(entry.getKey().getValidationRegex())) {
                entry.getKey().setError(entry.getKey().getValidationRegexErrorText());
                errorLayoutList.add(entry.getKey());
                validationErrorList.add(ValidationError.REGEX);

            } else if (entry.getKey().isValidationTypeSet() && !text.matches(entry.getKey().getValidationTypeRegex())) {
                entry.getKey().setError(entry.getKey().getValidationTypeErrorText());
                errorLayoutList.add(entry.getKey());
                validationErrorList.add(ValidationError.VALIDATION_TYPE);
            }
        }

        if (errorLayoutList.isEmpty()) {
            if (validatorListener != null)
                validatorListener.onSuccess();

        } else {
            if (validatorListener != null)
                validatorListener.onValidateErrors(errorLayoutList, validationErrorList);
        }
    }

    private void attachListeners() {
        for (Map.Entry<ValidatingTextInputLayout, Pair> entry : inputLayoutPairMap.entrySet()) {
            if (entry.getKey().isContinuousValidationRequired())
                attachTextWatcher(entry.getKey(), entry.getValue());
        }
    }

    @Override
    synchronized public void onError(ValidatingTextInputLayout textInputLayout, ValidationError validationError) {
        if (validatorListener != null)
            validatorListener.onError(textInputLayout, validationError);

        inputLayoutPairMap.get(textInputLayout).setErrorFree(false);
    }

    @Override
    synchronized public void onSuccess(ValidatingTextInputLayout textInputLayout) {
        if (validatorListener != null)
            validatorListener.onErrorResolved(textInputLayout);

        inputLayoutPairMap.get(textInputLayout).setErrorFree(true);

        for (Map.Entry<ValidatingTextInputLayout, Pair> entry : inputLayoutPairMap.entrySet())
            if (!entry.getValue().isErrorFree())
                return;

        if (validatorListener != null)
            validatorListener.onSuccess();
    }

    private void attachTextWatcher(ValidatingTextInputLayout inputLayout, Pair pair) {
        ValidatingTextWatcher textWatcher = new ValidatingTextWatcher(inputLayout, this);
        inputLayout.getEditText().addTextChangedListener(textWatcher);
        pair.setTextWatcher(textWatcher);
    }

    private static class Pair {
        private ValidatingTextWatcher textWatcher;
        private boolean isErrorFree;

        public Pair(boolean isErrorFree) {
            this.textWatcher = null;
            this.isErrorFree = isErrorFree;
        }

        public ValidatingTextWatcher getTextWatcher() {
            return textWatcher;
        }

        public void setTextWatcher(ValidatingTextWatcher textWatcher) {
            this.textWatcher = textWatcher;
        }

        public boolean isErrorFree() {
            return isErrorFree;
        }

        public void setErrorFree(boolean errorFree) {
            isErrorFree = errorFree;
        }

        public boolean isTextWatcherAttached() {
            return textWatcher != null;
        }
    }

}
