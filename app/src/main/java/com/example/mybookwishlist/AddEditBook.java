// CMPUT 301 Assignment 1
// by Ananya Shukla

package com.example.mybookwishlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddEditBook extends DialogFragment {

    interface AddBookDialogListener {
        void addBook(Book book);
        void deleteBook(Book book);
    }

    private AddBookDialogListener listener;
    private Book currentBook = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddBookDialogListener) {
            listener = (AddBookDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddBookDialogListener");
        }
    }

    // To create a new instance of the fragment with a book object
    public static AddEditBook newInstance(Book book) {
        AddEditBook fragment = new AddEditBook();
        Bundle args = new Bundle();
        if (book != null) {
            args.putSerializable("book", book);
            fragment.currentBook = book;  // Save the book for editing
        }
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_book, null);

        EditText editTitle = view.findViewById(R.id.edit_text_title);
        EditText editAuthor = view.findViewById(R.id.edit_text_author);
        EditText editGenre = view.findViewById(R.id.edit_text_genre);
        EditText editYear = view.findViewById(R.id.edit_text_year);
        final RadioGroup statusGroup = view.findViewById(R.id.edit_text_status);
        Button deleteButton = view.findViewById(R.id.delete_button);

        if (currentBook != null) {
            editTitle.setText(currentBook.getTitle());
            editAuthor.setText(currentBook.getAuthor());
            editGenre.setText(currentBook.getGenre());
            editYear.setText(String.valueOf(currentBook.getPublicationYear()));
            ((RadioButton) view.findViewById(currentBook.isReadStatus() ? R.id.status_read : R.id.status_unread)).setChecked(true);
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(v -> {  // Set delete button functionality
                listener.deleteBook(currentBook);
                dismiss();
            });
        } else {
            deleteButton.setVisibility(View.GONE);  // Hide delete button when adding a new book
        }

        deleteButton.setOnClickListener(v -> {
            if (currentBook != null) {
                listener.deleteBook(currentBook);
                dismiss();
            }
        });

        builder.setView(view)
                .setTitle(currentBook != null ? "Edit Book" : "Add Book")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    String title = editTitle.getText().toString();
                    String author = editAuthor.getText().toString();
                    String genre = editGenre.getText().toString();
                    String yearStr = editYear.getText().toString();
                    boolean status = statusGroup.getCheckedRadioButtonId() == R.id.status_read;

                    if (!validateInput(title, author, genre, yearStr)) {  // Validate input before saving
                        Toast.makeText(getContext(), "Please correct the input fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int year = Integer.parseInt(yearStr);
                    if (currentBook == null) {
                        currentBook = new Book(title, author, genre, year, status);
                    } else {
                        currentBook.setTitle(title);
                        currentBook.setAuthor(author);
                        currentBook.setGenre(genre);
                        currentBook.setPublicationYear(year);
                        currentBook.setReadStatus(status);
                    }

                    listener.addBook(currentBook);
                });


        return builder.create();
    }

    private boolean validateInput(String title, String author, String genre, String yearStr) {
        if (TextUtils.isEmpty(title) || title.length() > 50) return false;
        if (TextUtils.isEmpty(author) || author.length() > 30) return false;
        if (TextUtils.isEmpty(genre)) return false;
        if (TextUtils.isEmpty(yearStr) || yearStr.length() != 4) return false;
        try {
            int year = Integer.parseInt(yearStr);
            if (year < 1000 || year > 9999) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
