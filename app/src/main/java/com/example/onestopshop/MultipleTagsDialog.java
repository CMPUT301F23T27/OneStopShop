package com.example.onestopshop;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * This is a dialog for adding tags to multiple items
 */
public class MultipleTagsDialog extends Dialog {
    EditText tagInputEditText;
    Button createButton;
    TagsController tagsController;
    public MultipleTagsDialog(@NonNull Context context, ArrayList<String> selectedItemIds) {
        super(context);
        setContentView(R.layout.dialog_multipletags);
        tagInputEditText = findViewById(R.id.editTextTagInput);
        createButton = findViewById(R.id.buttonCreate);
        tagsController = new TagsController();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tagInputEditText.getText() != null && !TextUtils.isEmpty(tagInputEditText.getText().toString())){
                    //upload tags for all items
                    for(String id : selectedItemIds) {
                        tagsController.uploadTag(id, tagInputEditText.getText().toString());
                    }
                    dismiss();
                }

            }
        });
    }
}
