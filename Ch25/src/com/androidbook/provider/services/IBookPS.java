package com.androidbook.provider.services;

import java.util.List;

import com.androidbook.provider.services.impl.sqlite.BaseEntitySQLiteMetaData;

//Book related services
//Any unexpected error will throw SQLException
public interface IBookPS {
	public int saveBook(Book book);
	public Book getBook(int bookid);
	public void updateBook(Book book);
	public void deleteBook(int bookid);
	public List<Book> getAllBooks();
}
