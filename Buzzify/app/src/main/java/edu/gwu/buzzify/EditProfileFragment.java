/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package edu.gwu.buzzify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.ui.ParseLoginConfig;
import com.parse.ui.ParseLoginFragmentBase;
import com.parse.ui.ParseOnLoadingListener;
import com.parse.ui.ParseOnLoginSuccessListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import edu.gwu.buzzify.common.ParseUtils;

/**
 * Fragment for the user signup screen.
 */
public class EditProfileFragment extends ParseLoginFragmentBase implements OnClickListener {
  public static final String USERNAME = "com.parse.ui.ParseSignupFragment.USERNAME";
  public static final String PASSWORD = "com.parse.ui.ParseSignupFragment.PASSWORD";

  private boolean photoSentSuccess = false;
  private ParseUser user;
  private ParseFile userPhotoParseFile;
  private RadioGroup accountTypeRadioGroup;
  private CheckBox loginPreferenceCheckbox;
  private ImageView profilePhoto;
  private EditText usernameField;
  private EditText passwordField;
  private EditText confirmPasswordField;
  private EditText emailField;
  private EditText nameField;
  private Button editAccountButton;
  private ParseOnLoginSuccessListener onLoginSuccessListener;

  private ParseLoginConfig config;
  private int minPasswordLength;

  private static final String LOG_TAG = "ParseSignupFragment";
  private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;
  private static final String USER_OBJECT_NAME_FIELD = "name";
  private static final String USER_OBJECT_ACCOUNT_TYPE = "accountType";
  private static final String USER_OBJECT_AUTO_LOGIN_PREF = "autoLogin";
  private static final String USER_PHOTO_FILE = "userPhoto.bmp";
  private static final String USER_OBJECT_PHOTO = "userPhoto";
  private static final int CAMERA_REQUEST = 1000;

  public static EditProfileFragment newInstance(Bundle configOptions, String username, String password) {
    EditProfileFragment signupFragment = new EditProfileFragment();
    Bundle args = new Bundle(configOptions);
    args.putString(EditProfileFragment.USERNAME, username);
    args.putString(EditProfileFragment.PASSWORD, password);
    signupFragment.setArguments(args);
    return signupFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                           Bundle savedInstanceState) {

    Bundle args = getArguments();
    config = ParseLoginConfig.fromBundle(args, getActivity());

    minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;
    if (config.getParseSignupMinPasswordLength() != null) {
      minPasswordLength = config.getParseSignupMinPasswordLength();
    }

    String username = (String) args.getString(USERNAME);
    String password = (String) args.getString(PASSWORD);

    View v = inflater.inflate(R.layout.fragment_edit_profile,
        parent, false);
    ImageView appLogo = (ImageView) v.findViewById(R.id.app_logo);
    profilePhoto = (ImageView) v.findViewById(R.id.drawerCircleView);
    accountTypeRadioGroup = (RadioGroup) v.findViewById(R.id.radio_group_account_type);
    loginPreferenceCheckbox = (CheckBox) v.findViewById(R.id.checkbox_login_pref);
    usernameField = (EditText) v.findViewById(R.id.signup_username_input);
    passwordField = (EditText) v.findViewById(R.id.signup_password_input);
    confirmPasswordField = (EditText) v
        .findViewById(R.id.signup_confirm_password_input);
    emailField = (EditText) v.findViewById(R.id.signup_email_input);
    nameField = (EditText) v.findViewById(R.id.signup_name_input);
    editAccountButton = (Button) v.findViewById(R.id.create_account);

    /**
     * Sets the fields with user's previous profile data.
     */

    user = ParseUser.getCurrentUser();

    Picasso.with(getActivity()).load(ParseUtils.getUserProfilePhotoUrl()).into(profilePhoto);

    usernameField.setText(user.getUsername());
    emailField.setText(user.getEmail());

    if (user.getString(USER_OBJECT_AUTO_LOGIN_PREF).matches("true")) {
      loginPreferenceCheckbox.setChecked(true);
    } else {
      loginPreferenceCheckbox.setChecked(false);
    }

    // TODO: 12/6/15 Set default password text as filled characters...but actually empty or some dummy text
    passwordField.setText(password);
    confirmPasswordField.setText(password);

    nameField.setText(user.getString(USER_OBJECT_NAME_FIELD));

    if (appLogo != null && config.getAppLogo() != null) {
      appLogo.setImageResource(config.getAppLogo());
    }

    if (config.isParseLoginEmailAsUsername()) {
      usernameField.setHint(com.parse.ui.R.string.com_parse_ui_email_input_hint);
      usernameField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
      if (emailField != null) {
        emailField.setVisibility(View.GONE);
      }
    }

    if (config.getParseSignupSubmitButtonText() != null) {
      editAccountButton.setText(config.getParseSignupSubmitButtonText());
    }
    editAccountButton.setOnClickListener(this);

    profilePhoto.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        changeProfilePhoto();
      }
    });

    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof ParseOnLoginSuccessListener) {
      onLoginSuccessListener = (ParseOnLoginSuccessListener) activity;
    } else {
      throw new IllegalArgumentException(
          "Activity must implemement ParseOnLoginSuccessListener");
    }

    if (activity instanceof ParseOnLoadingListener) {
      onLoadingListener = (ParseOnLoadingListener) activity;
    } else {
      throw new IllegalArgumentException(
          "Activity must implemement ParseOnLoadingListener");
    }
  }

  public void changeProfilePhoto() {
    //launch the camera
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(cameraIntent,CAMERA_REQUEST);
  }

  @Override
  public void onClick(View v) {

    String username = usernameField.getText().toString();
    String password = passwordField.getText().toString();
    String passwordAgain = confirmPasswordField.getText().toString();

    String email = null;
    if (config.isParseLoginEmailAsUsername()) {
      email = usernameField.getText().toString();
    } else if (emailField != null) {
      email = emailField.getText().toString();
    }

    String name = null;
    if (nameField != null) {
      name = nameField.getText().toString();
    }

    if (username.length() == 0) {
      if (config.isParseLoginEmailAsUsername()) {
        showToast(com.parse.ui.R.string.com_parse_ui_no_email_toast);
      } else {
        showToast(com.parse.ui.R.string.com_parse_ui_no_username_toast);
      }
    } else if (password.length() == 0) {
      showToast(com.parse.ui.R.string.com_parse_ui_no_password_toast);
    } else if (password.length() < minPasswordLength) {
      showToast(getResources().getQuantityString(
              com.parse.ui.R.plurals.com_parse_ui_password_too_short_toast,
              minPasswordLength, minPasswordLength));
    } else if (passwordAgain.length() == 0) {
      showToast(com.parse.ui.R.string.com_parse_ui_reenter_password_toast);
    } else if (!password.equals(passwordAgain)) {
      showToast(com.parse.ui.R.string.com_parse_ui_mismatch_confirm_password_toast);
      confirmPasswordField.selectAll();
      confirmPasswordField.requestFocus();
    } else if (email != null && email.length() == 0) {
      showToast(com.parse.ui.R.string.com_parse_ui_no_email_toast);
    } else if (name != null && name.length() == 0) {
      showToast(com.parse.ui.R.string.com_parse_ui_no_name_toast);
    } else {

//      user = new ParseUser();

      // Set standard fields
      user.setUsername(username);

      if (!password.isEmpty()) {
        user.setPassword(password);
      }

      user.setEmail(email);

      // Set additional custom fields only if the user filled it out
      if (name.length() != 0) {
        user.put(USER_OBJECT_NAME_FIELD, name);
      }

      //Set account type

//      String accountType;
//      int selectedAccountRadioId = accountTypeRadioGroup.getCheckedRadioButtonId();
//
//      if (selectedAccountRadioId == com.parse.ui.R.id.radio_bartender_user) {
//        accountType = "bartender";
//      } else if (selectedAccountRadioId == com.parse.ui.R.id.radio_dj_user) {
//        accountType = "dj";
//      } else {
//        accountType = "standard";
//      }

//      user.put(USER_OBJECT_ACCOUNT_TYPE, accountType);

      //Set login preference

      if (loginPreferenceCheckbox.isChecked()) {
        user.put(USER_OBJECT_AUTO_LOGIN_PREF, "true");
      } else {
        user.put(USER_OBJECT_AUTO_LOGIN_PREF, "false");
      }

      //send data to parse
      sendUserDataToParse();

    }
  }

  private boolean userDataIsValid(String username, String name, String email, String password, String passwordAgain) {
    boolean dataOk = false;

    if (usernameNameAndEmailIsValid(username, name, email) && passwordIsValid(password, passwordAgain)) {
      dataOk = true;
    }

    return dataOk;
  }

  private boolean usernameNameAndEmailIsValid(String username, String name, String email) {
    boolean dataOk = false;

    if (username.length() == 0) {
      if (config.isParseLoginEmailAsUsername()) {
        showToast(com.parse.ui.R.string.com_parse_ui_no_email_toast);
      } else {
        showToast(com.parse.ui.R.string.com_parse_ui_no_username_toast);
      }
    }else if (email != null && email.length() == 0) {
      showToast(com.parse.ui.R.string.com_parse_ui_no_email_toast);
    } else if (name != null && name.length() == 0) {
      showToast(com.parse.ui.R.string.com_parse_ui_no_name_toast);
    } else {
      dataOk = true;
    }

    return dataOk;
  }

  private boolean passwordIsValid(String password, String passwordAgain) {
    boolean dataOk = false;

    if (password.length() < minPasswordLength) {
      showToast(getResources().getQuantityString(
              com.parse.ui.R.plurals.com_parse_ui_password_too_short_toast,
              minPasswordLength, minPasswordLength));
    } else if (!password.equals(passwordAgain)) {
      showToast(com.parse.ui.R.string.com_parse_ui_mismatch_confirm_password_toast);
      confirmPasswordField.selectAll();
      confirmPasswordField.requestFocus();
    } else {
      dataOk = true;
    }

    return dataOk;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data){
    if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
      saveUserProfilePhoto(data);

    } else {
      Log.e(LOG_TAG, "Problem getting image");
    }
  }


  private void sendUserDataToParse() {

    loadingStart();

    if (userPhotoParseFile == null) {
      editUser();
    } else {

      //send photo to parse
      userPhotoParseFile.saveInBackground(new SaveCallback() {
        @Override
        public void done(ParseException e) {
          user.put(USER_OBJECT_PHOTO, userPhotoParseFile);
          editUser();
        }
      });
    }
  }

  private void saveUserProfilePhoto(Intent data) {
    Bitmap userPhotoBitmap = (Bitmap) data.getExtras().get("data");

    //Bitmap to byte array code taken from http://stackoverflow.com/questions/13758560/android-bitmap-to-byte-array-and-back-skimagedecoderfactory-returned-null

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    userPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] userPhotoByteArray = stream.toByteArray();

    userPhotoParseFile = new ParseFile(USER_PHOTO_FILE, userPhotoByteArray);

    //Update photo in edit profile
    profilePhoto.setImageBitmap(userPhotoBitmap);

  }

  private void editUser() {
    user.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (isActivityDestroyed()) {
          return;
        }

        if (e == null) {
          loadingFinish();
          editSuccess();
        } else {
          loadingFinish();
          if (e != null) {
            debugLog(getString(com.parse.ui.R.string.com_parse_ui_login_warning_parse_signup_failed) +
                    e.toString());
            switch (e.getCode()) {
              case ParseException.INVALID_EMAIL_ADDRESS:
                showToast(com.parse.ui.R.string.com_parse_ui_invalid_email_toast);
                break;
              case ParseException.USERNAME_TAKEN:
                showToast(com.parse.ui.R.string.com_parse_ui_username_taken_toast);
                break;
              case ParseException.EMAIL_TAKEN:
                showToast(com.parse.ui.R.string.com_parse_ui_email_taken_toast);
                break;
              default:
                showToast(com.parse.ui.R.string.com_parse_ui_signup_failed_unknown_toast);
            }
          }
        }
      }
    });
  }

  @Override
  protected String getLogTag() {
    return LOG_TAG;
  }

  private void editSuccess() {
    onLoginSuccessListener.onLoginSuccess();
  }
}
