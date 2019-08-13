// BookController.aidl
package com.example.yangl.androidsample;

// Declare any non-default types here with import statements
import com.example.yangl.androidsample.Book;
interface BookController {
    List<Book> getBookList();
    void addBookInout(inout Book book);
}
