// CMPUT 301 Assignment 1
// by Ananya Shukla

package com.example.mybookwishlist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddEditBook.AddBookDialogListener {
    private ArrayList<Book> dataList;  // List to hold book data
    private ListView bookList;  // ListView to display books
    private BookArrayAdapter bookAdapter;  // Adapter for the ListView
    private TextView tvBookSummary;  // TextView to display summary of books

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing
        dataList = new ArrayList<>();
        bookList = findViewById(R.id.book_list);
        bookAdapter = new BookArrayAdapter(this, dataList);
        bookList.setAdapter(bookAdapter);
        tvBookSummary = findViewById(R.id.tv_book_summary);

        // Setting up the FloatingActionButton for adding new books
        FloatingActionButton fab = findViewById(R.id.button_add_book);
        fab.setOnClickListener(v -> {
            AddEditBook editFragment = AddEditBook.newInstance(null);
            editFragment.show(getSupportFragmentManager(), "Add/Edit Book");
        });

        // Setting up item click listener for the ListView to edit selected books
        bookList.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = bookAdapter.getItem(position);
            AddEditBook editFragment = AddEditBook.newInstance(selectedBook);
            editFragment.show(getSupportFragmentManager(), "Edit Book");
        });

        updateBookSummary();
    }

    @Override
    public void addBook(Book book) {
        boolean found = false;
        for (int i = 0; i < dataList.size(); i++) {  // Checking if the book already exists to update it
            if (dataList.get(i).getTitle().equals(book.getTitle())) {
                dataList.set(i, book);
                found = true;
                break;
            }
        }
        if (!found) {  // Adding new book if not found in the list
            dataList.add(book);
        }
        bookAdapter.notifyDataSetChanged();
        updateBookSummary();
    }

    @Override
    public void deleteBook(Book book) {  // Removing the book from the list
        dataList.remove(book);
        bookAdapter.notifyDataSetChanged();
        updateBookSummary();
    }

    private void updateBookSummary() {  // Calculating total books and the number of books read
        int totalBooks = dataList.size();
        int readBooksCount = 0;
        for (Book book : dataList) {
            if (book.isReadStatus()) {
                readBooksCount++;
            }
        }

        String summaryText = "Total Books: " + totalBooks + " | Read: " + readBooksCount;
        tvBookSummary.setText(summaryText);
    }
}
