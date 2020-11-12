package com.servicepro.dao.interfaces;

import java.util.ArrayList;

import com.servicepro.entities.Review;

public interface ReviewDao extends Dao<Review> {

	ArrayList<Review> listUserReviewsAsReviewer(int userId);

	ArrayList<Review> listUserReviewsAsReviewee(int userId);

	ArrayList<Review> listUserReviewsAsAClientReviewee(int clientId);

	ArrayList<Review> listUserReviewsAsArtisanAsReviewee(int artisanId);

	ArrayList<Review> listUserReviewsAsClientAsReviewer(int clientId);

	ArrayList<Review> listUserReviewsAsArtisanAsReviewer(int artisanId);
	
	ArrayList<Review> getWorkReviews(int workId);
}
