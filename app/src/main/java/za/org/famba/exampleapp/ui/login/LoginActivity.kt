package za.org.famba.exampleapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService
import com.huawei.hms.support.feature.result.CommonConstant
import za.org.famba.exampleapp.MainActivity
import za.org.famba.exampleapp.databinding.ActivityLoginBinding
import za.org.famba.exampleapp.utils.SessionManager
import android.graphics.Color

import cc.cloudist.acplibrary.ACProgressConstant

import cc.cloudist.acplibrary.ACProgressFlower


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authService: AccountAuthService
    private lateinit var authParam: AccountAuthParams
    private val REQUEST_CODE_SIGN_IN = 1000
    private val TAG = "Account"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            silentSignInByHwId()
        }
    }

    private fun silentSignInByHwId() {
        val dialog = ACProgressFlower.Builder(this)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("Sing in ...")
            .fadeColor(Color.DKGRAY).build()
        dialog.show()

        authParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .createParams()

        authService = AccountAuthManager.getService(this, authParam)

        val task = authService!!.silentSignIn()
        task.addOnSuccessListener { account ->
            dialog.dismiss()
            dealWithResultOfSignIn(account)
        }


        task.addOnFailureListener { e ->
            dialog.dismiss()
            if (e is ApiException) {
                val apiException = e
                val signInIntent = authService!!.signInIntent

                Log.i(TAG, e.message!!)
                dealWithFailure(signInIntent)
            }
        }
    }

    private fun dealWithFailure(signInIntent: Intent) {
        /*var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
                    if (authAccountTask.isSuccessful) {
                        Log.d(TAG, "Sign In successful")

                        var authAccount = authAccountTask.result
                        dealWithResultOfSignIn(authAccount)
                    } else {
                        Snackbar.make( binding.root, "Sign in failed", Snackbar.LENGTH_LONG).show()
                    }
                }
            }

        resultLauncher.launch(signInIntent)*/
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }

    private fun dealWithResultOfSignIn(account: AuthAccount?) {
        val bundle = Bundle()
        bundle.putString(CommonConstant.KEY_DISPLAY_NAME, account!!.displayName)
        bundle.putString(CommonConstant.KEY_EMAIL, account!!.email)
        bundle.putString(CommonConstant.KEY_PHOTO_URI, account!!.avatarUriString)

        val openMain = Intent(this, MainActivity::class.java)
        openMain.putExtras(bundle)
        startActivity(openMain)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                val authAccount = authAccountTask.result
                dealWithResultOfSignIn(authAccount)
            } else {
                TODO("Add fail dialog")
            }
        }
    }

}