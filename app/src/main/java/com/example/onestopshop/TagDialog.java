package com.example.onestopshop;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;


public class TagDialog extends Dialog {
    Context context;
    ChipGroup addFragmentTags;
    List<String> selectedTags;
    List<String> existingTags;
    ChipGroup selectedTagsChipGroup;
    ChipGroup existingTagsChipGroup;
    EditText tagInputEditText;
    Button  createButton;
    TagsController tagsController;
    Button doneBtn;

    public TagDialog(@NonNull Context context, List<String> selectedTags, List<String> existingTags, ChipGroup addFragmentTags) {
        super(context);
        this.context = context;
        this.addFragmentTags = addFragmentTags;
        this.selectedTags = selectedTags;
        this.existingTags = existingTags;
        initDialog();
    }

    private void initDialog(){
        setContentView(R.layout.dialog_tag);
        selectedTagsChipGroup = findViewById(R.id.chipGroupSelectedTags);
        existingTagsChipGroup = findViewById(R.id.chipGroupExistingTags);
        tagInputEditText = findViewById(R.id.editTextTagInput);
        createButton = findViewById(R.id.buttonCreate);
        doneBtn = findViewById(R.id.buttonDone);
        tagsController = new TagsController();


        // Set up click listeners
        createButton.setOnClickListener(v -> onCreateButtonClick());
        doneBtn.setOnClickListener(v -> {dismiss();});

        // Populate chip groups with existing and selected tags
        populateChipGroup(existingTagsChipGroup, existingTags, false);
        populateChipGroup(selectedTagsChipGroup, selectedTags, true);

    }

    private void onCreateButtonClick() {
        String newTag = tagInputEditText.getText().toString().trim();
        if (!newTag.isEmpty()) {
            if(!isExistingTag(newTag) ){

                // Clear the input field
                tagInputEditText.setText("");

                // Update the chip group
                addToSelectedTags(newTag);
            }
            else{
                Toast.makeText(getContext(), "Tag already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addToSelectedTags(String newTag) {
        if (!newTag.isEmpty()) {
            if(!isSelectedTag(newTag)){
                // Add the new tag to the selected tags
                selectedTags.add(newTag);

                // Clear the input field
                tagInputEditText.setText("");

                //add to ChipGroup
                Chip chip = new Chip(context, null, R.style.TagsChipStyle);
                chip.setText(newTag);
                chip.setOnClickListener(v -> onExistingTagClick(newTag));
                styleTagChipDialog(chip);
                selectedTagsChipGroup.addView(chip);



                //add to ChipGroup for addfragment
                Chip chipAdd = new Chip(context, null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action);
                chipAdd.setText(newTag);
                styleTagChipFragment(chipAdd);
                addFragmentTags.addView(chipAdd);
                chipAdd.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFragmentTags.removeView(chipAdd);
                        selectedTags.remove(newTag);
                    }
                });
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedTagsChipGroup.removeView(chip);
                        selectedTags.remove(newTag);
                        addFragmentTags.removeView(chipAdd);
                    }
                });
            }
            else{
                Toast.makeText(getContext(), "Tag already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void populateChipGroup(ChipGroup chipGroup, List<String> tags, boolean isSelectedTags) {
        for (String tag : tags) {
            Chip chip = new Chip(context, null, R.style.ChipStyle);
            chip.setText(tag);
            if(isSelectedTags) {
                styleTagChipDialog(chip);
            }
            else{
                chip.setOnClickListener(v -> onExistingTagClick(tag));
            }
            chipGroup.addView(chip);
        }
    }

    private void onExistingTagClick(String tag) {

        addToSelectedTags(tag);
    }

    public boolean isExistingTag(String tag) {
        for(String existingTag: existingTags) {
            if(tag == existingTag){
                return true;
            }
        }
            return false;
    }
    public boolean isSelectedTag(String tag) {
        for(String existingTag: selectedTags) {
            if(tag.equals(existingTag)){
                return true;
            }
        }
        return false;
    }
    private void styleTagChipDialog(Chip chip) {
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
        chip.setTextColor(Color.WHITE);
        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#BD2AC0")));

    }
    private void styleTagChipFragment(Chip chip) {
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
    }


}
