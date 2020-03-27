package com.example.hwvalidationreg;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hwvalidationreg.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.TimeUnit;

import ru.tinkoff.decoro.Mask;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.PhoneNumberUnderscoreSlotsParser;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;


public class LoginFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = view.findViewById(R.id.btn_login);
        Button regButton = view.findViewById(R.id.btn_register);

        final TextInputLayout phoneTextInput = view.findViewById(R.id.phone_text_input);
        final TextInputEditText phoneEditText = view.findViewById(R.id.phone_edit_text);
        final TextInputLayout emailTextInput = view.findViewById(R.id.email_text_input);
        final TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);

        Slot[] phoneSlots = new UnderscoreDigitSlotsParser().parseSlots("+8 (___) ___ __ __");
        FormatWatcher phoneFormatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(phoneSlots));
        phoneFormatWatcher.installOn(phoneEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                if (!isPhoneValid(new SpannableStringBuilder(phoneFormatWatcher.getMask().toUnformattedString()))) {
                    phoneTextInput.setError("Неправильно введений номер!");
                } else {
                    phoneTextInput.setError(null);
                    count += 1;
                }

                if (!isEmailValid(emailEditText.getText())) {
                    if (emailEditText.getText().toString().contains("admin@gmail.com")){
                        emailTextInput.setError("Адреса зарезервована!");
                    }else {
                        emailTextInput.setError("Неправильна адреса!");
                    }
                } else {
                    emailTextInput.setError(null);
                    count += 1;
                }

                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError("Пароль має бути 8 символів!");
                } else {
                    passwordTextInput.setError(null);
                    count += 1;
                }

                if (count >= 3){
                    CommonUtils.showLoading(getActivity());
                    uploadData();
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Clear the error once more than 8 characters are typed.
        phoneEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPhoneValid(new SpannableStringBuilder(phoneFormatWatcher.getMask().toUnformattedString()))) {
                    phoneTextInput.setError(null); //Clear the error
                    //return true;
                }

                if (isEmailValid(emailEditText.getText())) {
                    emailTextInput.setError(null); //Clear the error
                    //return true;
                }

                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                    //return true;
                }

                return false;
            }
        });

        return view;
    }

    private boolean isPhoneValid(@NonNull Editable text){ return text != null && text.length() == 18; }

    private boolean isEmailValid(@NonNull Editable text){
        int i = 0;
        if (!text.toString().contains("admin@gmail.com")){
            i = 1;
        }
        return text != null && text.toString().contains("@") && i != 0;
    }

    private boolean isPasswordValid(@NonNull Editable text) { return text != null && text.length() >= 8; }

    public String uploadData(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    CommonUtils.hideLoading();
//                    ((NavigationHost)getActivity()).navigateTo(new SportNewsFragment(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        return null;
    }
}
