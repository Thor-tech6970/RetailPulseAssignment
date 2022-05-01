package com.example.retailpulseassignment.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.retailpulseassignment.R
import com.example.retailpulseassignment.sharedPref.SharedPref
import com.example.retailpulseassignment.databinding.ActivityMainBinding
import com.example.retailpulseassignment.interfaces.MainActivityInterface
import com.example.retailpulseassignment.presenters.MainActivityPresenter

class MainActivity : AppCompatActivity() , MainActivityInterface.View {

    private lateinit var binding : ActivityMainBinding
    private var selectedUser : String = "Shyam"
    private lateinit var presenter : MainActivityPresenter
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        presenter = MainActivityPresenter(this)
        sharedPref = SharedPref(this)

        binding.ivUser2.setOnClickListener(View.OnClickListener {
            selectedUser = "Ram"
            binding.ivUser2.setImageDrawable(getDrawable(R.drawable.selected_user))
            binding.ivUser1.setImageDrawable(getDrawable(R.drawable.unselected_user))
        })

        binding.ivUser1.setOnClickListener(View.OnClickListener {
            selectedUser = "Shyam"
            binding.ivUser1.setImageDrawable(getDrawable(R.drawable.selected_user))
            binding.ivUser2.setImageDrawable(getDrawable(R.drawable.unselected_user))
        })

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            validateCredentials(binding.etPassword);
        })

    }

    private fun validateCredentials(etPassword: EditText) {

        if(etPassword.text.toString().equals("")){
            blank("Password is required")
        }else{
            presenter.doLogin(etPassword.text.toString())
        }

    }

    override fun onSuccess(message: String) {
      Toast.makeText(this , message , Toast.LENGTH_SHORT).show()
      sharedPref.setUser(selectedUser)
        val intent : Intent = Intent(this , StoreListActivity::class.java)
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        binding.etPassword.requestFocus()
        binding.etPassword.setError(message)
    }

    override fun blank(message: String) {
        binding.etPassword.requestFocus()
        binding.etPassword.setError(message)
    }
}