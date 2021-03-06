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

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(rootBinding.getRoot());

        inputLayoutValidator = new TextInputLayoutValidator(rootBinding.getRoot(), this);

        rootBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputLayoutValidator.validate();
            }
        });
    }

    @Override
    public void onValidateErrors(List<ValidatingTextInputLayout> errorLayoutList, List<TextInputLayoutValidator.ValidationError> validationErrorList) {

    }

    @Override
    public void onError(ValidatingTextInputLayout inputLayout, TextInputLayoutValidator.ValidationError validationError) {
        Toast.makeText(this, "onError" + validationError.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResolved(ValidatingTextInputLayout inputLayout) {
        Toast.makeText(this, "Resolved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }
}