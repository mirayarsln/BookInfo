package com.example.bookinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> bookList;
    private Context context;
    private BookAdapterListener listener;
    public interface BookAdapterListener {
        void onBooksUpdated(List<Book> updatedBooks);
    }
    public BookAdapter(Context context, List<Book> bookList, BookAdapterListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }
    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bookTitle.setText(book.getTitle());

        holder.itemView.setOnClickListener(v -> showEditSummaryDialog(position));
    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
    private void showEditSummaryDialog(int position) {
        Book book = bookList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_summary, null);
        builder.setView(dialogView);
        TextView titleText = dialogView.findViewById(R.id.titleText);
        TextView summaryInput = dialogView.findViewById(R.id.summaryInput);
        titleText.setText(book.getTitle());
        summaryInput.setText(book.getSummary());
        builder.setPositiveButton("Kaydet", (dialog, which) -> {
            String newSummary = summaryInput.getText().toString().trim();

            if (!newSummary.isEmpty()) {
                bookList.get(position).setSummary(newSummary);
                notifyItemChanged(position);
                listener.onBooksUpdated(bookList);
                Toast.makeText(context, "Özet güncellendi!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Özet boş olamaz!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("İptal", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle;
        public BookViewHolder(View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
        }
    }
}
