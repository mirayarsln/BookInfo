package com.example.bookinfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookAdapter.BookAdapterListener {

    RecyclerView recyclerView;
    BookAdapter bookAdapter;
    List<Book> bookList;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("BookInfo", MODE_PRIVATE);

        bookList = loadBooksFromSharedPreferences();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, bookList, this);
        recyclerView.setAdapter(bookAdapter);

        findViewById(R.id.addBookButton).setOnClickListener(v -> showAddBookDialog());
    }

    private void showAddBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_book, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        EditText bookTitleInput = dialogView.findViewById(R.id.bookTitleInput);
        EditText bookAboutInput = dialogView.findViewById(R.id.bookAboutInput);


        builder.setPositiveButton("Ekle", (dialog, which) -> {
            String bookTitle = bookTitleInput.getText().toString().trim();
            String bookAbout = bookAboutInput.getText().toString().trim();

            if (!bookTitle.isEmpty() && !bookAbout.isEmpty()) {
                Book newBook = new Book(bookTitle, bookAbout);
                bookList.add(newBook);
                saveBooksToSharedPreferences();
                bookAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Kitap eklendi!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kitap adı ve özet boş olamaz!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("İptal", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void saveBooksToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putInt("book_count", bookList.size());
        for (int i = 0; i < bookList.size(); i++) {
            editor.putString("book_title_" + i, bookList.get(i).getTitle());
            editor.putString("book_summary_" + i, bookList.get(i).getSummary());
        }

        editor.apply();
    }

    private List<Book> loadBooksFromSharedPreferences() {
        int bookCount = sharedPreferences.getInt("book_count", 0);
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < bookCount; i++) {
            String title = sharedPreferences.getString("book_title_" + i, null);
            String summary = sharedPreferences.getString("book_summary_" + i, null);

            if (title != null && summary != null) {
                books.add(new Book(title, summary));
            }
        }

        return books;
    }

    @Override
    public void onBooksUpdated(List<Book> updatedBooks) {
        bookList = updatedBooks;
        saveBooksToSharedPreferences();
    }
}
