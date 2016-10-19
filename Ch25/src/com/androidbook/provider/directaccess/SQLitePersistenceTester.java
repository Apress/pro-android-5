package com.androidbook.provider.directaccess;

import java.util.List;

import android.content.Context;

import com.androidbook.provider.BaseTester;
import com.androidbook.provider.IReportBack;
import com.androidbook.provider.services.Book;
import com.androidbook.provider.services.IBookPS;
import com.androidbook.provider.services.Services;

/**
 * This is a tester class that implements the driver methods
 * to invoke and test persistence using SQLite
 *  
 * Implemented methods are:
 * addBook
 * removeBook
 * showBooks
 * getCount
 *
 * uses Persistence Services pattern to call these methods
 * 
 * Menu: R.menu.test_book_persistence_menu
 * 
 * @See Services.PersistenceServices
 * 
 */
public class SQLitePersistenceTester extends BaseTester 
{
	private static String tag = "SQLitePersistenceTester";
	private IBookPS bookPersistenceService = Services.PersistenceServices.bookps; 
	SQLitePersistenceTester(Context ctx, IReportBack target) {
		super(ctx, target,tag);
	}
	
	//Add a book whose id is one larger than the books 
	//in the database
	public void addBook()
	{
		Book book = Book.createAMockBook();
		int bookid = bookPersistenceService.saveBook(book);
		reportString(String.format("Inserted a book %s whose generated id now is %s"
				,book.getName()
				,bookid));
	}
	//Delete the last book
	public void removeBook()
	{
		List<Book> bookList = 
			bookPersistenceService.getAllBooks();
		if( bookList.size() <= 0)
		{
			reportString("There are no books that can be deleted");
			return;
		}
		reportString(String.format("There are %s books. First one will be deleted", bookList.size()));
		
		Book b = bookList.get(0);
		bookPersistenceService.deleteBook(b.getId());
		reportString(String.format("Book with id:%s successfully deleted", b.getId()));
	}
	
	//write the list of books so far to the screen
	public void showBooks()
	{
		List<Book> bookList = 
			bookPersistenceService.getAllBooks();
		reportString(String.format("Number of books:%s", bookList.size()));
		for(Book b: bookList) {
			reportString(String.format("id:%s name:%s author:%s isbn:%s"
					,b.getId()
					,b.getName()
					,b.getAuthor()
					,b.getIsbn()));
		}
	}
	
	//COunt the number of books in the database
	private int getCount()
	{
		List<Book> bookList = 
			bookPersistenceService.getAllBooks();
		return bookList.size();
	}
}
