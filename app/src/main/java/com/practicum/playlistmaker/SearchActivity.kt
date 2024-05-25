package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {

    private var searchTextValue = SEARCH_TEXT_VALUE
    private lateinit var editTextSearch: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBackButton = findViewById<ImageView>(R.id.searchBackButton)
        editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        val imageViewSearchClear = findViewById<ImageView>(R.id.imageViewSearchClear)

        searchBackButton.setOnClickListener {
            finish()
        }

        imageViewSearchClear.setOnClickListener {
            editTextSearch.setText("")
            hideKeyBoard()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imageViewSearchClear.visibility = imageViewSearchClearVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchTextValue = s?.toString() ?: ""
            }

        }

        editTextSearch.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextValue = savedInstanceState.getString(SEARCH_TEXT_KEY, SEARCH_TEXT_VALUE)
        editTextSearch.setText(searchTextValue)
    }

    private fun imageViewSearchClearVisibility(text: CharSequence?): Int {
        return if (text.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    companion object {
        const val SEARCH_TEXT_VALUE = ""
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
    }
}