// Zachary Gover
// JAV1 - 1608
// MainActivity

package com.example.zachary.connectedapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.loopj.android.image.SmartImageView;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

	/**
	 * MARK: Global Properties
	 */

	public BookListings bookListings;

	/**
	 * MARK: View Connections
	 */

	// Master/List View
	public EditText searchField;
	public ListView searchResults;

	// Detail View
	public ScrollView scrollView;
	public SmartImageView bookImg;
	public TextView bookTitle;
	public TextView bookAuthor;
	public TextView bookPublisher;
	public TextView bookPubDate;
	public TextView bookDescription;

	/**
	 * MARK: Default Methods
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set default properties and view connections
		searchField      = (EditText) findViewById(R.id.searchField);
		searchResults    = (ListView) findViewById(R.id.searchResults);
		scrollView       = (ScrollView) findViewById(R.id.scrollView);
		bookImg          = (SmartImageView) findViewById(R.id.bookImg);
		bookTitle        = (TextView) findViewById(R.id.bookTitle);
		bookAuthor       = (TextView) findViewById(R.id.bookAuthor);
		bookPublisher    = (TextView) findViewById(R.id.bookPublisher);
		bookPubDate      = (TextView) findViewById(R.id.bookPubDate);
		bookDescription  = (TextView) findViewById(R.id.bookDescription);

		bookListings = new BookListings(this, searchResults);

		searchField.setOnEditorActionListener(this);
		searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				updateDetailView(bookListings.getListings().get(position));
			}
		});

		resetView();

		if (!NetworkConnectivity.online(this)) {
			// Notify the user is we're not connected on start
			showToast("No network connectivity");
		}
	}

	@Override
	public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			hideKeyBoard();
			performSearch();
			return true;
		}
		return false;
	}

	/**
	 * MARK: Event Listeners
	 */

	public void searchBooks(View view) {
		performSearch();
	}

	/**
	 * MARK: Custom Methods
	 */

	private void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		// Verify that the keyboard is open
		if(imm.isAcceptingText()) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	public void performSearch() {
		String currentValue = searchField.getText().toString().trim();

		// Make sure the user is connected and entered a valid search term
		if (currentValue.equals("")) {
			showToast("Please enter a search term");
		} else if (NetworkConnectivity.online(this)) {
			bookListings.search(currentValue);
		} else {
			showToast("No network connectivity");
		}
	}

	public void resetView() {
		// Remove default values from view connections
		bookImg.setImageResource(R.mipmap.ic_launcher);
		bookTitle.setText("");
		bookAuthor.setText("");
		bookPublisher.setText("");
		bookPubDate.setText("");
		bookDescription.setText("");
	}

	public void updateDetailView(Book book) {
		// Set the value for the detail view
		bookImg.setImageUrl(book.getImgUrl());
		bookTitle.setText(book.getTitle());
		bookAuthor.setText(book.getAuthor());
		bookPublisher.setText(book.getPublisher());
		bookPubDate.setText(book.getPubDate());
		bookDescription.setText(book.getDescription());

		// Scroll the scrollView to the top
		scrollView.pageScroll(ScrollView.FOCUS_UP);
	}

	/**
	 * MARK: Notification Methods
	 */

	public void showToast(String message) {
		Toast toast = Toast.makeText(
			getApplicationContext(), message, Toast.LENGTH_LONG
		);

		toast.show();
	}
}
