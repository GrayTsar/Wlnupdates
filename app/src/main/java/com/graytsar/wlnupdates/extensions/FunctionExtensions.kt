package com.graytsar.wlnupdates.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FunctionExtensions {

    @ExperimentalCoroutinesApi
    fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
        val query = MutableStateFlow<String>("")

        setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { newText ->
                    query.value = newText
                }
                return true
            }

        })
        return query
    }

    @ExperimentalCoroutinesApi
    fun EditText.getQueryTextChangeStateFlow(): StateFlow<String> {
        val query = MutableStateFlow<String>("")

        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let { newText ->
                    query.value = newText
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        return query
    }
}