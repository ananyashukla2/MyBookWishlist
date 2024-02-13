// CMPUT 301 Assignment 1
// by Ananya Shukla

package com.example.mybookwishlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookArrayAdapter extends ArrayAdapter<Book> {
    private Context context;
    private ArrayList<Book> books;

    // Constructor for the adapter
    public BookArrayAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = new ArrayList<>(books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_main, parent, false);
        } else {
            view = convertView;
        }

        // Get the `Book` object located at this position in the list
        Book book = getItem(position);
        if (book != null) {
            TextView bookTitle = view.findViewById(R.id.book_title);
            TextView bookAuthor = view.findViewById(R.id.book_author);
            TextView bookGenre = view.findViewById(R.id.book_genre);
            TextView bookYear = view.findViewById(R.id.book_year);
            TextView bookStatus = view.findViewById(R.id.book_status);

            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookGenre.setText(book.getGenre());
            bookYear.setText(String.valueOf(book.getPublicationYear()));
            bookStatus.setText(book.isReadStatus() ? "Read" : "Unread");  // Display "Read" or "Unread" based on the read status of the book

        }

        return view;
    }
}


