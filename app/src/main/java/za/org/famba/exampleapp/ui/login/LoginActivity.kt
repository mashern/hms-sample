package za.org.famba.exampleapp.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService
import com.huawei.hms.support.feature.result.CommonConstant
import za.org.famba.exampleapp.databinding.ActivityLoginBinding
import za.org.famba.exampleapp.utils.SessionManager
import kotlin.math.sin

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
        authParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .createParams()

        authService = AccountAuthManager.getService(this, authParam)

        val task = authService!!.silentSignIn()
        task.addOnSuccessListener { account ->
            dealWithResultOfSignIn(account)
        }


        task.addOnFailureListener { e ->
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
        Log.i(TAG, "display name:" + account!!.displayName);
        Log.i(TAG, "photo uri string:" + account!!.avatarUriString);
        Log.i(TAG, "photo uri:" + account!!.avatarUri);
        Log.i(TAG, "email:" + account.email);
        Log.i(TAG, "openid:" + account.openId);
        Log.i(TAG, "unionid:" + account.unionId);

        SessionManager.setString(CommonConstant.KEY_DISPLAY_NAME, account!!.displayName)
        SessionManager.setString(CommonConstant.KEY_EMAIL, account!!.email)
        SessionManager.setString("photo", account!!.avatarUriString)
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