package com.androhome.neshm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androhome.neshm.model.UserModel

class SplashAcivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var userModel: UserModel = UserModel(this)
        if (userModel.loggedin) {
            startActivity(Intent(this@SplashAcivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplashAcivity, LoginActivity::class.java))
        }
    }
}
