package io.oddworks.device.authentication

import android.accounts.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import io.oddworks.device.R
import io.oddworks.device.model.OddViewer
import io.oddworks.device.model.common.OddResourceType
import io.oddworks.device.request.OddRequest
import io.oddworks.device.request.RxOddCall
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OddAuthenticator(val context: Context) : AbstractAccountAuthenticator(context) {

    @Throws(NetworkErrorException::class)
    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        Log.d(TAG, "addAccount")

        val intent = Intent(context, OddAuthenticationActivity::class.java)
        intent.putExtra(OddAuthenticationActivity.ARG_ACCOUNT_TYPE, accountType)
        intent.putExtra(OddAuthenticationActivity.ARG_AUTH_TYPE, authTokenType)
        intent.putExtra(OddAuthenticationActivity.ARG_IS_ADDING_NEW_ACCOUNT, true)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    @Throws(NetworkErrorException::class)
    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle? {
        Log.d(TAG, "getAuthToken")

        if (account == null) {
            val result = Bundle()
            result.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_BAD_AUTHENTICATION)
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid account")
            return result
        }

        // Ensure the caller requested an authToken type that we support
        if (authTokenType != OddAuthGeneral.AUTH_TOKEN_TYPE_ODDWORKS_DEVICE) {
            val result = Bundle()
            result.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_BAD_AUTHENTICATION)
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType")
            return result
        }

        val accountManager = AccountManager.get(context)

        var authToken = accountManager.peekAuthToken(account, authTokenType)
        Log.d(TAG, "peekAuthToken: $authToken")

        // try again to authenticate the user
        if (authToken.isEmpty()) {
            val password = accountManager.getPassword(account)
            if (password != null) {
                val passwordData = password.toByteArray()
                val unhashedPassword = Base64.decode(passwordData, Base64.DEFAULT).toString()

                val viewer = RxOddCall
                        .observableFrom<OddViewer> {
                            Log.d(TAG, "reauthenticating")
                            OddRequest.Builder(context, OddResourceType.VIEWER)
                                    .login(account.name, unhashedPassword)
                                    .build()
                                    .enqueueRequest(it)
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toBlocking()
                        .singleOrDefault(null)
                if (viewer != null) {
                    authToken = viewer.jwt
                }
            }
        }

        if (!authToken.isEmpty()) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return result
        }

        val intent = Intent(context, OddAuthenticationActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(OddAuthenticationActivity.ARG_ACCOUNT_TYPE, account.type)
        intent.putExtra(OddAuthenticationActivity.ARG_AUTH_TYPE, authTokenType)
        intent.putExtra(OddAuthenticationActivity.ARG_ACCOUNT_EMAIL, account.name)
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun getAuthTokenLabel(authTokenType: String): String {
        if (authTokenType == OddAuthGeneral.AUTH_TOKEN_TYPE_ODDWORKS_DEVICE) {
            return context.getString(R.string.oddworks_account_label)
        }
        return "$authTokenType (Label)"
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        val result = Bundle()
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false)
        return result
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? {
        return null
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle? {
        return null
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle? {
        return null
    }

    companion object {
        private val TAG = OddAuthenticator::class.java.simpleName
    }
}
