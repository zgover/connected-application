// Zachary Gover
// JAV1 - 1608
// BookListings

package com.example.zachary.connectedapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BookListings extends Object {

	/**
	 * MARK: Pubic Properties
	 */

	private ArrayList<Book> books;
	private ListView listView;
	private Context context;
	protected ProgressDialog progress;

	/**
	 * MARK: Class Initializer
	 */

	public BookListings(Context con, ListView lv) {
		this.context = con;
		this.listView = lv;

		// Setup progress dialog
		progress = new ProgressDialog(con);
		progress.setTitle("Please Wait...");
		progress.setMessage("Fetching Google Books...");
	}

	/**
	 * MARK: Custom Methods
	 */

	public void search(String query) {
		// Clear current book listings
		if (books != null && !books.isEmpty()) {
			books.clear();
		}

		// Initialize the book listing fetch
		FetchAsyncTask task = new FetchAsyncTask();
		String encodedQuery = URLEncoder.encode(query);
		task.execute("https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery);
	}

	public void setupLayout() {
		// Check if result is empty
		if (books.isEmpty()) {
			showToast("No results for that search term");
		}

		// Set the custom adapter for the GridView
		listView.setAdapter(
			new ListImgAdapter(
				this.context, books,
				R.layout.book_list_listing
			)
		);
	}

	/**
	 * MARK: API Methods
	 */

	private ArrayList<Book> parseBooks(String json) {
		ArrayList<Book> listings = new ArrayList<>();

		if (json != null) {
			try {
				// Parse the string into json
				JSONObject parsedJSON = new JSONObject(json);
				JSONArray items = parsedJSON.getJSONArray("items");

				// Loop through the parsed json and create a book for each listing
				for (int i = 0; i < items.length(); i++) {
					Book newBook = new Book();
					JSONObject objListing = items
							.getJSONObject(i)
							.getJSONObject("volumeInfo");

					// Get and set the values for the book
					String title      = "Not Available";
					String author     = "Not Available";
					String publisher  = "Not Available";
					String pubDate    = "Not Available";
					String desc       = "Not Available";
					String imgUrl     = "http://bit.ly/25r9rNg";

					// Verify the json has each key before fetching it
					if (objListing.has("title")) {
						title = objListing.getString("title");
					}

					if (objListing.has("publisher")) {
						publisher = objListing.getString("publisher");
					}

					if (objListing.has("publishedDate")) {
						pubDate = objListing.getString("publishedDate");
					}

					if (objListing.has("description")) {
						desc = objListing.getString("description");
					}

					if (objListing.has("imageLinks") &&
							objListing.getJSONObject("imageLinks").has("thumbnail")) {
						imgUrl = objListing.getJSONObject("imageLinks")
									 .getString("thumbnail");
					}

					if (objListing.has("authors")) {
						// Build a readable list of authors
						StringBuilder authorBuilder = new StringBuilder();
						JSONArray authors = objListing.getJSONArray("authors");

						for (int a = 0; a < authors.length(); a++) {
							if (a == 0) {
								authorBuilder.append(authors.get(a));
							} else {
								authorBuilder.append(", " + authors.get(a));
							}
						}

						author = authorBuilder.toString();
					}

					newBook.setTitle(title);
					newBook.setAuthor(author);
					newBook.setPublisher(publisher);
					newBook.setPubDate(pubDate);
					newBook.setDescription(desc);
					newBook.setImgUrl(imgUrl);

					listings.add(newBook);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return listings;
	}

	private class FetchAsyncTask extends AsyncTask<String, String, ArrayList<Book>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Present ProgressDialog
			progress.show();
		}

		@Override
		protected ArrayList<Book> doInBackground(String... params) {
			ArrayList<Book> response = null;
			HttpURLConnection con = null;
			InputStream stream = null;

			try {
				// Define the requirement for the request
				URL url = new URL(params[0]);
				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Accept", "application/json");
				con.setRequestProperty("charset", "utf-8");

				int reCode = con.getResponseCode();

				if (reCode >= 400 && reCode <= 499) {
					throw new Exception(
						"Bad Request! \n"+
							"Response Code: " + reCode + "\n"+
							"Request URL: " + url + "\n" +
							"Error Stream: " + con.getErrorStream()
					);
				}

				stream = con.getInputStream();
				String json = IOUtils.toString(stream);
				response = parseBooks(json);

				return response;

			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				// Close the connection
				if (con != null) {
					IOUtils.close(con);
				}

				// Close the stream
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return response;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(ArrayList<Book> response) {
			super.onPostExecute(response);

			// Hide the progress dialog and parse the books
			progress.hide();
			books = response;
			setupLayout();
		}
	}

	/**
	 * MARK: Getter and Setters
	 */

	public ArrayList<Book> getListings() {
		return this.books;
	}

	/**
	 * MARK: Notification Methods
	 */

	public void showToast(String message) {
		Toast toast = Toast.makeText(
			this.context, message, Toast.LENGTH_LONG
		);

		toast.show();
	}

}
