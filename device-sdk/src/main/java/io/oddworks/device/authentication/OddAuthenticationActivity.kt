package io.oddworks.device.authentication

import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import io.oddworks.device.R
import io.oddworks.device.model.OddViewer
import io.oddworks.device.model.common.OddResourceType
import io.oddworks.device.request.OddRequest
import io.oddworks.device.request.RxOddCall
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OddAuthenticationActivity : AccountAuthenticatorActivity() {
    lateinit var authTokenType: String
    lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_odd_authentication)
        accountManager = AccountManager.get(baseContext)

        val accountEmail = intent.getStringExtra(ARG_ACCOUNT_EMAIL)
        authTokenType = intent.getStringExtra(ARG_AUTH_TYPE) ?: OddAuthGeneral.AUTH_TOKEN_TYPE_ODDWORKS_DEVICE

        if (accountEmail != null) {
            (findViewById(R.id.account_email) as TextView).text = accountEmail
        }

        findViewById(R.id.email_sign_in_button).setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val email = (findViewById(R.id.account_email) as TextView).text.toString()
        val password = (findViewById(R.id.account_password) as TextView).text.toString()
        val accountType = intent.getStringExtra(ARG_ACCOUNT_TYPE)

        RxOddCall
                .observableFrom<OddViewer> {
                    Log.d(TAG, "Started authenticating")
                    OddRequest.Builder(baseContext, OddResourceType.VIEWER)
                            .login(email, password)
                            .build()
                            .enqueueRequest(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "login successful")

                    finishLogin(accountType, password, it)
                },{
                    Log.d(TAG, "login failed", it)
                    Toast.makeText(baseContext, it.message, Toast.LENGTH_SHORT).show()
                })
    }

    private fun finishLogin(accountType: String, password: String,  viewer: OddViewer) {
        val passwordData = password.toByteArray()
        val hashedPassword = Base64.encodeToString(passwordData, Base64.DEFAULT)
        val account = Account(viewer.id, accountType)
        if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d(TAG, "finishLogin > addAccountExplicitly")

            val extras = Bundle()
            extras.putStringArray("entitlements", viewer.entitlements.toTypedArray())

            accountManager.addAccountExplicitly(account, hashedPassword, extras)
            accountManager.setAuthToken(account, authTokenType, viewer.jwt)
        } else {
            Log.d(TAG, "finishLogin > setPassword")
            accountManager.setPassword(account, hashedPassword)
        }

        // TODO - do we need to put things in the bundle?
        val data = Bundle()
        val result = Intent()
        result.putExtras(data)
        setAccountAuthenticatorResult(data)
        setResult(RESULT_OK, result)
        finish()
    }

    companion object {
        private val TAG = OddAuthenticationActivity::class.java.simpleName
        val ARG_ACCOUNT_TYPE = "${OddAuthenticationActivity::class.java.name}.ARG_ACCOUNT_TYPE"
        val ARG_AUTH_TYPE = "${OddAuthenticationActivity::class.java.name}.ARG_AUTH_TYPE"
        val ARG_ACCOUNT_EMAIL = "${OddAuthenticationActivity::class.java.name}.ARG_ACCOUNT_EMAIL"
        val ARG_IS_ADDING_NEW_ACCOUNT = "${OddAuthenticationActivity::class.java.name}.ARG_IS_ADDING_NEW_ACCOUNT"
    }
}