package com.graytsar.wlnupdates.ui.login

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.graytsar.wlnupdates.*
import com.graytsar.wlnupdates.databinding.FragmentLoginBinding


class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModelLogin by viewModels<ViewModelLogin>()

    private lateinit var editUsername:EditText
    private lateinit var editPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.includeToolbarLogin.toolbarLogin
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        val navController = NavHostFragment.findNavController(this)
        NavigationUI.setupActionBarWithNavController(this.context as MainActivity, navController)

        editUsername = binding.editUsername
        editPassword = binding.editPassword
        binding.buttonSignIn.setOnClickListener {
            onClickSignIn(it)
        }

        binding.buttonSignIn.setOnClickListener {
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()

            viewModelLogin.getLogin(username, password)
        }

        viewModelLogin.responseLogin.observe(viewLifecycleOwner) {
            if(!it.error){
                Toast.makeText(requireActivity(), "Success", Toast.LENGTH_LONG).show()

                val sp = requireContext().getSharedPreferences(keyPreferenceCredentials, Context.MODE_PRIVATE)
                val editor = sp.edit()
                editor.putString(keyPreferenceUsername, editUsername.text.toString())
                editor.putString(keyPreferencePassword, editPassword.text.toString())
                editor.apply()

                //viewModelLogin.getTest()
                navController.popBackStack()
            }
        }

        viewModelLogin.isLoading.observe(viewLifecycleOwner) {
            binding.progressBarLogin.visibility = if(it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModelLogin.errorResponseLogin.observe(viewLifecycleOwner) {
            showDialog(getString(R.string.alert_dialog_title_error), it.message)
        }

        viewModelLogin.failureResponse.observe(viewLifecycleOwner) {
            showDialog(getString(R.string.alert_dialog_title_failure), it.message)
        }

        viewModelLogin.errorServerLogin.observe(viewLifecycleOwner) {
            showDialog(getString(R.string.alert_dialog_title_error), it.code().toString())
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sp = requireContext().getSharedPreferences(keyPreferenceCredentials, Context.MODE_PRIVATE)

        val username = sp.getString(keyPreferenceUsername, null)
        val password = sp.getString(keyPreferencePassword, null)

        if(username != null && password != null) {
            editUsername.setText(username, TextView.BufferType.EDITABLE)
            editPassword.setText(password, TextView.BufferType.EDITABLE)
        }
    }

    private fun showDialog(title:String, message:String?){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.alert_dialog_ok), null)
            .show()
    }

    fun onClickSignIn(view: View) {
        val username = editUsername.text.toString()
        val password = editPassword.text.toString()

        if(username.isNullOrEmpty() || username.length < 5) {
            showDialog("Error", "Username must be at least 5 characters long.")
            return
        }

        if(!password.isNullOrEmpty() || password.length < 8) {
            showDialog("Error", "Password must be at least 8 characters long.")
            return
        }

        viewModelLogin.getLogin(username, password)
    }
}