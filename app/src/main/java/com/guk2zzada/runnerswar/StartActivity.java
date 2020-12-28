package com.guk2zzada.runnerswar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.amazonaws.mobile.auth.core.StartupAuthErrorDetails;
import com.amazonaws.mobile.auth.core.StartupAuthResult;
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler;
import com.amazonaws.mobile.auth.core.signin.AuthException;
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider;
import com.amazonaws.mobile.auth.userpools.UserPoolSignInView;
import com.amazonaws.mobile.config.AWSConfiguration;

import com.amazonaws.mobile.auth.ui.SignInActivity;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeApplication();
        // application initialized
        setContentView(R.layout.activity_start);

        //aws login
        final IdentityManager identityManager =
                IdentityManager.getDefaultIdentityManager();

        identityManager.doStartupAuth(this,
                new StartupAuthResultHandler() {
                    @Override
                    public void onComplete(final StartupAuthResult authResults) {
                        if (authResults.isUserSignedIn()) {
                            final IdentityProvider provider =
                                    identityManager.getCurrentIdentityProvider();

                            // If the user was  signed in previously with a provider,
                            // indicate that to them with a toast.

                            goMain(StartActivity.this);
                            return;

                        } else {
                            // Either the user has never signed in with a provider before
                            // or refresh failed with a previously signed in provider.

                            // Optionally, you may want to check if refresh
                            // failed for the previously signed in provider.

                            final StartupAuthErrorDetails errors =
                                    authResults.getErrorDetails();

                            if (errors.didErrorOccurRefreshingProvider()) {
                                final AuthException providerAuthException =
                                        errors.getProviderRefreshException();

                            }

                            doSignIn(IdentityManager.getDefaultIdentityManager());
                            return;
                        }


                    }
                }, 2000);

    }

    //AWS intial
    private void initializeApplication() {

        AWSConfiguration awsConfiguration = new AWSConfiguration(getApplicationContext());

        // If IdentityManager is not created, create it
        if (IdentityManager.getDefaultIdentityManager() == null) {
            IdentityManager identityManager =
                    new IdentityManager(getApplicationContext(), awsConfiguration);
            IdentityManager.setDefaultIdentityManager(identityManager);
        }

        // Add Amazon Cognito User Pools as Identity Provider.
        IdentityManager.getDefaultIdentityManager().addSignInProvider(CognitoUserPoolsSignInProvider.class);
    }

    //aws do singin
    private void doSignIn(final IdentityManager identityManager) {

        identityManager.setUpToAuthenticate(
                StartActivity.this, new DefaultSignInResultHandler() {

                    @Override
                    public void onSuccess(Activity activity, IdentityProvider identityProvider) {
                        if (identityProvider != null) {

                            // Sign-in succeeded
                            // The identity provider name is available here using:
                            //     identityProvider.getDisplayName()

                        }

                        // On Success of SignIn go to your startup activity
                        activity.startActivity(new Intent(activity, StartActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }

                    @Override
                    public boolean onCancel(Activity activity) {
                        // Return false to prevent the user from dismissing
                        // the sign in screen by pressing back button.
                        // Return true to allow this.

                        return false;
                    }
                });


        // 이부분을 어떻게 조져야할듯한데
        // 내가보기엔 이부분이문제임

        AuthUIConfiguration config =
                new AuthUIConfiguration.Builder()
                        .userPools(true)
                        // .signInButton(FacebookButton.class)
                        // .signInButton(GoogleButton.class)
                        .logoResId(R.drawable.logo)
                        .build();

        Context context = StartActivity.this;
        SignInActivity.startSignInActivity(context, config);


        StartActivity.this.finish();
    }

    private void goMain(final Activity callingActivity) {
        callingActivity.startActivity(new Intent(callingActivity, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        callingActivity.finish();
    }
}
