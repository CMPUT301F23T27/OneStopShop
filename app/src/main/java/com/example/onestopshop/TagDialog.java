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

/**
 * Custom dialog for managing tags using chips.
 */
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

    /**
     * Constructs a TagDialog.
     *
     * @param context          The context in which the dialog is created.
     * @param selectedTags     The list of selected tags.
     * @param existingTags     The list of existing tags.
     * @param addFragmentTags  The ChipGroup in the add fragment.
     */
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

    /**
     * Adds a new tag to the selected tags and updates the chip groups.
     *
     * @param newTag The new tag to be added.
     */
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

    /**
     * Populates a ChipGroup with tags.
     *
     * @param chipGroup     The ChipGroup to populate.
     * @param tags          The list of tags to populate the ChipGroup with.
     * @param isSelectedTags Indicates whether the tags are selected tags.
     */
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

    /**
     * Checks if a tag already exists in the list of existing tags.
     *
     * @param tag The tag to check for existence.
     * @return True if the tag already exists; otherwise, false.
     */
    public boolean isExistingTag(String tag) {
        for(String existingTag: existingTags) {
            if(tag == existingTag){
                return true;
            }
        }
            return false;
    }

    /**
     * Checks if a tag is already selected.
     *
     * @param tag The tag to check for selection.
     * @return True if the tag is already selected; otherwise, false.
     */
    public boolean isSelectedTag(String tag) {
        for(String existingTag: selectedTags) {
            if(tag.equals(existingTag)){
                return true;
            }
        }
        return false;
    }

    /**
     * Styles a Chip for the dialog by setting properties like clickability, text color, and background color.
     *
     * @param chip The Chip to style for the dialog.
     */
    private void styleTagChipDialog(Chip chip) {
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
        chip.setTextColor(Color.WHITE);
        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#BD2AC0")));

    }

    /**
     * Styles a Chip for the add fragment by setting properties like visibility, clickability, and text color.
     *
     * @param chip The Chip to style for the add fragment.
     */
    private void styleTagChipFragment(Chip chip) {
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
    }


}
