package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var searchData: String = SEARCH_DEF
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val searchTextField = findViewById<EditText>(R.id.search_field_edittext)
        val errorPlaceholderLayout = findViewById<LinearLayout>(R.id.error_placeholder_layout)
        val errorImage = findViewById<ImageView>(R.id.error_placeholder_image)
        val errorText = findViewById<TextView>(R.id.error_text)
        val errorRefreshButton = findViewById<Button>(R.id.refresh_button)
        if (savedInstanceState != null) {
            searchData = savedInstanceState.getString(SEARCH_VALUE, SEARCH_DEF)
        }
        var lastSearch = searchData
        searchTextField.setText(searchData)
        val clearButton = findViewById<ImageView>(R.id.clear_button)
        val searchFieldTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    clearButton.visibility = View.GONE
                }
                else{
                    clearButton.visibility = View.VISIBLE
                    searchData = s.toString()
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }
        val trackListRecyclerView = findViewById<RecyclerView>(R.id.track_list_recyclerview)
        val trackListAdapter = TrackListAdapter(tracks)
        trackListRecyclerView.adapter = trackListAdapter
        searchTextField.addTextChangedListener(searchFieldTextWatcher)
        clearButton.setOnClickListener {
            searchTextField.setText("")
            tracks.clear()
            trackListAdapter.notifyDataSetChanged()
            currentFocus?.let {
                val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
        fun iTunesSearch(query: String) {
            if (query.isNotEmpty()) {
                iTunesService.search(query).enqueue(object : Callback<ITunesResponse> {
                    override fun onResponse(call: Call<ITunesResponse>,
                                            response: Response<ITunesResponse>
                    ) {
                        if (response.isSuccessful) {
                            val results = response.body()?.results
                            if(results!= null){
                                tracks.clear()
                                if (results.isEmpty()) {
                                    trackListAdapter.notifyDataSetChanged()
                                    trackListRecyclerView.visibility = View.GONE
                                    errorPlaceholderLayout.visibility = View.VISIBLE
                                    errorImage.setImageResource(R.drawable.no_search_results_placeholder)
                                    errorText.text = resources.getText(R.string.no_search_results)
                                    errorRefreshButton.visibility = View.GONE
                                } else {
                                    tracks.addAll(response.body()?.results!!)
                                    trackListAdapter.notifyDataSetChanged()
                                    errorPlaceholderLayout.visibility = View.GONE
                                    trackListRecyclerView.visibility = View.VISIBLE
                                }
                            }
                        } else {
                            lastSearch = query
                            trackListRecyclerView.visibility = View.GONE
                            errorRefreshButton.visibility = View.VISIBLE
                            errorPlaceholderLayout.visibility = View.VISIBLE
                            errorImage.setImageResource(R.drawable.no_connection_placeholder)
                            errorText.text = resources.getText(R.string.connection_problem)
                        }
                    }
                    override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                        lastSearch = query
                        trackListRecyclerView.visibility = View.GONE
                        errorRefreshButton.visibility = View.VISIBLE
                        errorPlaceholderLayout.visibility = View.VISIBLE
                        errorImage.setImageResource(R.drawable.no_connection_placeholder)
                        errorText.text = resources.getText(R.string.connection_problem)
                    }

                })
            }
        }
        searchTextField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesSearch(searchData)
            }
            false
        }
        errorRefreshButton.setOnClickListener {
            iTunesSearch(lastSearch)
        }

        backButton.setOnClickListener {
            finish()
        }

    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchData)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchData = savedInstanceState.getString(SEARCH_VALUE, SEARCH_DEF)
    }
    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val SEARCH_DEF = ""
    }
}