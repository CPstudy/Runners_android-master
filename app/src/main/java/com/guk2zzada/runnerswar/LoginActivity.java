package com.guk2zzada.runnerswar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
//aws identitymanager ,userpool import
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInActivity;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider;
//aws login
import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.amazonaws.mobile.auth.core.StartupAuthErrorDetails;
import com.amazonaws.mobile.auth.core.StartupAuthResult;
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler;
import com.amazonaws.mobile.auth.core.signin.AuthException;
//aws 내장 ui

public class LoginActivity extends AppCompatActivity {

    LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
    Animation animSlideUp, animSlideDown, animSlideInTop, animSlideOutTop;

    Button btnJoin, btnLogin;
    EditText edtID, edtPW;
    ImageView imgLogo;
    View layForm;

    Intent intent1, intent2;

    int activity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeApplication();
        // application initialized

        setContentView(R.layout.activity_login);


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
                            Toast.makeText(
                                    LoginActivity.this, String.format("Signed in with %s",
                                            provider.getDisplayName()), Toast.LENGTH_LONG).show();
                            goMain(LoginActivity.this);
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


        //aws login

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        intent1 = new Intent(getBaseContext(), MainActivity.class);
        intent2 = new Intent(getBaseContext(), JoinActivity.class);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        edtID = (EditText) findViewById(R.id.edtID);
        edtPW = (EditText) findViewById(R.id.edtPW);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        layForm = findViewById(R.id.layForm);

        // AsyncTask 실행
        // loginAsyncTask.execute("");

        animSlideUp = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_bottom_delay);
        animSlideDown = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_out_bottom);
        animSlideInTop = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_top);
        animSlideOutTop = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_out_top);
        animSlideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 로그인 폼 애니메이션이 끝나면 수행
                switch (activity) {
                    case 0:
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        finish();
                        break;

                    case 1:
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        //로그인 버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = 0;
                imgLogo.startAnimation(animSlideOutTop);
                layForm.startAnimation(animSlideDown);
                GlobalVar.id = edtID.getText().toString();
            }
        });

        //회원가입 버튼
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = 1;
                imgLogo.startAnimation(animSlideOutTop);
                layForm.startAnimation(animSlideDown);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            layForm.startAnimation(animSlideUp);
            imgLogo.startAnimation(animSlideInTop);
        }
    }

    private static class LoginAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 수행 전
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            // 수행 중 뷰 작업
        }

        @Override
        protected String doInBackground(String... strings) {
            // 수행 >> 끝나면 종료
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 종료
        }
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
                LoginActivity.this, new DefaultSignInResultHandler() {

                    @Override
                    public void onSuccess(Activity activity, IdentityProvider identityProvider) {
                        if (identityProvider != null) {

                            // Sign-in succeeded
                            // The identity provider name is available here using:
                            //     identityProvider.getDisplayName()

                        }

                        // On Success of SignIn go to your startup activity
                        activity.startActivity(new Intent(activity, LoginActivity.class)
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

        Context context = LoginActivity.this;
        SignInActivity.startSignInActivity(context, config);
        LoginActivity.this.finish();
    }

    private void goMain(final Activity callingActivity) {
        callingActivity.startActivity(new Intent(callingActivity, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        callingActivity.finish();
    }

}