// Zachary Gover
// JAV1 - 1608
// ListImgAdapter

package com.example.zachary.connectedapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

public class ListImgAdapter extends BaseAdapter {

	/**
	 * MARK: Global Properties
	 */

	private Context context;
	private ArrayList<Book> books;
	private LayoutInflater inflater;
	private ListImgItem viewHolder;
	private int layoutId;

	/**
	 * MARK: Default Methods
	 */

	public ListImgAdapter(Context applicationContext, ArrayList<Book> bookList, int layoutId) {
		this.context = applicationContext;
		this.books = bookList;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		return books.size();
	}

	@Override
	public Object getItem(int i) {
		return books.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			// inflate the layout
			inflater = ((Activity) this.context).getLayoutInflater();
			convertView = inflater.inflate(layoutId, null);

			// Setup view holder if the current view is null
			this.viewHolder = new ListImgItem();
			this.viewHolder.bookListTitle = (TextView) convertView.findViewById(
				R.id.bookListTitle
			);
			this.viewHolder.bookListAuthor = (TextView) convertView.findViewById(
				R.id.bookListAuthor
			);
			this.viewHolder.bookListImg = (SmartImageView) convertView.findViewById(
				R.id.bookListImg
			);

			convertView.setTag(this.viewHolder);

		} else {
			// Set the current view holder if we are not null
			this.viewHolder = (ListImgItem) convertView.getTag();
		}

		// Set the values for each view holder item
		Book book = books.get(position);

		// Return if that index is null
		if (book.equals(null)) { return convertView; }

		this.viewHolder.bookListTitle.setText(book.getTitle());
		this.viewHolder.bookListAuthor.setText(book.getAuthor());
		this.viewHolder.bookListImg.setImageUrl(book.getImgUrl());

		return convertView;
	}

	/**
	 * MARK: Nested Classes
	 */

	// View Holder
	static class ListImgItem {
		TextView bookListTitle;
		TextView bookListAuthor;
		SmartImageView bookListImg;
	}
}
