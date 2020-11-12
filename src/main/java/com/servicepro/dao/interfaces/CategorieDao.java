package com.servicepro.dao.interfaces;

import java.util.ArrayList;

import com.servicepro.entities.*;

public interface CategorieDao extends Dao<Categorie> {

	ArrayList<Categorie> listAllAvailableCategories();

	ArrayList<Categorie> listAllUnAvailableCategories();

	void deleteCategory(int id);
}
