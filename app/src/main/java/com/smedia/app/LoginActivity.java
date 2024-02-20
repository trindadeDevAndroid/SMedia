package com.smedia.app;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.*;
import com.google.android.material.textfield.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class LoginActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private String app_name = "";
	private HashMap<String, Object> map = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> listmap_piot_accounts = new ArrayList<>();
	
	private LinearLayout linear1;
	private TextView textview1;
	private TextView textview2;
	private LinearLayout login_piot_bg;
	private ListView lv_1;
	private TextInputLayout textinputlayout1;
	private TextInputLayout textinputlayout2;
	private TextInputLayout textinputlayout3;
	private Button button1;
	private LinearLayout login_google_bg;
	private ImageView imageview1;
	private TextView textview3;
	private TextInputEditText name;
	private TextInputEditText email;
	private TextInputEditText password;
	private ImageView imageview2;
	private TextView textview4;
	
	private DatabaseReference SMediaUsers = _firebase.getReference("SMediaUsers");
	private ChildEventListener _SMediaUsers_child_listener;
	private FirebaseAuth auth;
	private OnCompleteListener<AuthResult> _auth_create_user_listener;
	private OnCompleteListener<AuthResult> _auth_sign_in_listener;
	private OnCompleteListener<Void> _auth_reset_password_listener;
	private OnCompleteListener<Void> auth_updateEmailListener;
	private OnCompleteListener<Void> auth_updatePasswordListener;
	private OnCompleteListener<Void> auth_emailVerificationSentListener;
	private OnCompleteListener<Void> auth_deleteUserListener;
	private OnCompleteListener<Void> auth_updateProfileListener;
	private OnCompleteListener<AuthResult> auth_phoneAuthListener;
	private OnCompleteListener<AuthResult> auth_googleSignInListener;
	
	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.login);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		login_piot_bg = findViewById(R.id.login_piot_bg);
		lv_1 = findViewById(R.id.lv_1);
		textinputlayout1 = findViewById(R.id.textinputlayout1);
		textinputlayout2 = findViewById(R.id.textinputlayout2);
		textinputlayout3 = findViewById(R.id.textinputlayout3);
		button1 = findViewById(R.id.button1);
		login_google_bg = findViewById(R.id.login_google_bg);
		imageview1 = findViewById(R.id.imageview1);
		textview3 = findViewById(R.id.textview3);
		name = findViewById(R.id.name);
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);
		imageview2 = findViewById(R.id.imageview2);
		textview4 = findViewById(R.id.textview4);
		auth = FirebaseAuth.getInstance();
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (email.getText().toString().equals("")) {
					textinputlayout1.setError("Digite seu email");
				}
				else {
					if (name.getText().toString().equals("")) {
						textinputlayout2.setError("Digite seu nome");
					}
					else {
						if (password.getText().toString().equals("")) {
							textinputlayout3.setError("Digite sua senha");
						}
						else {
							if (!(password.getText().toString().length() > 7)) {
								textinputlayout3.setError("Sua senha precisa ser maior que 7");
							}
							else {
								if (!email.getText().toString().trim().contains("@gmail.com")) {
									auth.createUserWithEmailAndPassword(email.getText().toString() + "@gmail.com".trim(), password.getText().toString()).addOnCompleteListener(LoginActivity.this, _auth_create_user_listener);
								}
								else {
									auth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString()).addOnCompleteListener(LoginActivity.this, _auth_create_user_listener);
								}
							}
						}
					}
				}
			}
		});
		
		name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				textinputlayout1.setErrorEnabled(false);
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		email.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				textinputlayout2.setErrorEnabled(false);
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				textinputlayout3.setErrorEnabled(false);
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_SMediaUsers_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		SMediaUsers.addChildEventListener(_SMediaUsers_child_listener);
		
		auth_updateEmailListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_updatePasswordListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_emailVerificationSentListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_deleteUserListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		auth_updateProfileListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		auth_googleSignInListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		_auth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				try{
					if (_success) {
						map = new HashMap<>();
						map.put("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
						map.put("userName", name.getText().toString());
						map.put("userPhoto", "default");
						map.put("userPassword", password.getText().toString());
						map.put("userUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
						map.put("userFollowers", "0");
						map.put("userAccountPrivacy", "public");
						map.put("userAttrs", "{\n  \"created\": [\n    {\n      \"inApp\": \"SMedia\"\n    }\n  ]\n}");
						map.put("Color", "red");
						SMediaUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
						intent.setClass(getApplicationContext(), HomeActivity.class);
						startActivity(intent);
					}
					else {
						auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(LoginActivity.this, _auth_sign_in_listener);
					}
				}catch(Exception e){
					SMediaUtils.showMessage(getApplicationContext(), e.toString());
				}
			}
		};
		
		_auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				try{
					intent.setClass(getApplicationContext(), HomeActivity.class);
					startActivity(intent);
				}catch(Exception e){
					SMediaUtils.showMessage(getApplicationContext(), e.toString());
				}
			}
		};
		
		_auth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	
	private void initializeLogic() {
		try{
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { Window w = getWindow();  w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); };
			login_piot_bg.setVisibility(View.GONE);
			login_google_bg.setVisibility(View.GONE);
			textinputlayout1.setBoxCornerRadii((float)25, (float)25, (float)25, (float)25);
			textinputlayout2.setBoxCornerRadii((float)25, (float)25, (float)25, (float)25);
			textinputlayout3.setBoxCornerRadii((float)25, (float)25, (float)25, (float)25);
		}catch(Exception e){
			SMediaUtils.showMessage(getApplicationContext(), e.toString());
		}
	}
}