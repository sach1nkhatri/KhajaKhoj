package com.example.khajakhoj.utils

import androidx.appcompat.widget.SearchView

object SearchManager {
    fun setupSearchView(searchView: SearchView, filterFunction: (String?) -> Unit) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterFunction(newText)
                return true
            }
        })
    }
}
