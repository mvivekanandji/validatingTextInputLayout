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

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mvivekanandji.validatingtextinputlayout.databinding.ActivityMainBinding;

import java.util.List;

/**
 * <h1>Demo Activity to show the usage of  {@link ValidatingTextInputLayout} and
 * {@link TextInputLayoutValidator}
 *
 * @author vivekanand
 * @version 0.1.0
 */
public class MainActivity extends AppCompatActivity implements TextInputLayoutValidator.ValidatorListener {

    private ActivityMainBinding rootBinding;
    private TextInputLayoutValidator inputLayoutValidator;
    private boolean isFirstTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(rootBinding.getRoot());

        inputLayoutValidator = new TextInputLayoutValidator(rootBinding.getRoot(), this);
        isFirstTry = true;

        rootBinding.mbLogin.setOnClickListener(v -> {
            if (isFirstTry)
                inputLayoutValidator.validate();

            isFirstTry = false;

            if (inputLayoutValidator.isValid()) {
                Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onValidateErrors(List<ValidatingTextInputLayout> errorLayoutList, List<TextInputLayoutValidator.ValidationError> validationErrorList) {

    }

    @Override
    public void onError(ValidatingTextInputLayout inputLayout, TextInputLayoutValidator.ValidationError validationError, boolean isErrorOnValidate) {
        rootBinding.mbLogin.setEnabled(false);
        inputLayout.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.danger)));
        inputLayout.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.danger)));

    }

    @Override
    public void onErrorResolved(ValidatingTextInputLayout inputLayout) {
        inputLayout.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
        inputLayout.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryVariant)));

    }

    @Override
    public void onSuccess() {
        rootBinding.mbLogin.setEnabled(true);
    }
}