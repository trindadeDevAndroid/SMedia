package com.smedia.app;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import java.io.*;
import java.io.File;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import com.google.android.material.switchmaterial.SwitchMaterial;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;;

public class PublishActivity extends AppCompatActivity {
	
	public final int REQ_CD_HR = 101;
	
	 FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	 FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	 boolean superior = false;
	 HashMap<String, Object> map = new HashMap<>();
	 String zuid, path;
	 double posToDel = 0;
	 HashMap<String, Object> map_attrs = new HashMap<>();
	 HashMap<String, Object> map2 = new HashMap<>();
	
	 ArrayList<HashMap<String, Object>> listmap_images = new ArrayList<>();
	 ArrayList<HashMap<String, Object>> listmap_images_url = new ArrayList<>();
	 ArrayList<HashMap<String, Object>> listmap_created = new ArrayList<>();
	
	 LinearLayout linear_background1;
	 LinearLayout start;
	 LinearLayout linear1;
	 LinearLayout end;
	 LinearLayout publication_text_info_background;
	 LinearLayout publication_image_info_background;
	 LinearLayout linear2;
	 LinearLayout linear3;
	 Button button1;
	 LinearLayout linear_top2;
	 TextInputLayout textinputlayout1;
	 TextView title2;
	 TextView lenght_100;
	 TextInputEditText text;
	 LinearLayout linear_top1;
	 GridView gridview1;
	 TextView title1;
	 Button pickImage;
	 TextView textview1;
	 //SwitchCompat switch1;
	 TextInputLayout textinputlayout2;
	 TextInputLayout textinputlayout3;
	 TextInputLayout textinputlayout4;
	 TextInputEditText rotation;
	 TextInputEditText width;
	 TextInputEditText height;
	
	 FirebaseAuth auth;
	 OnCompleteListener<AuthResult> _auth_create_user_listener;
	 OnCompleteListener<AuthResult> _auth_sign_in_listener;
	 OnCompleteListener<Void> _auth_reset_password_listener;
	 OnCompleteListener<Void> auth_updateEmailListener;
	 OnCompleteListener<Void> auth_updatePasswordListener;
	 OnCompleteListener<Void> auth_emailVerificationSentListener;
	 OnCompleteListener<Void> auth_deleteUserListener;
	 OnCompleteListener<Void> auth_updateProfileListener;
	 OnCompleteListener<AuthResult> auth_phoneAuthListener;
	 OnCompleteListener<AuthResult> auth_googleSignInListener;
	
	 DatabaseReference SMediaPublications = _firebase.getReference("SMediaPublications");
	 ChildEventListener _SMediaPublications_child_listener;
	 StorageReference SMediaPublications_st = _firebase_storage.getReference("SMediaPublications_st");
	 OnCompleteListener<Uri> _SMediaPublications_st_upload_success_listener;
	 OnSuccessListener<FileDownloadTask.TaskSnapshot> _SMediaPublications_st_download_success_listener;
	 OnSuccessListener _SMediaPublications_st_delete_success_listener;
	 OnProgressListener _SMediaPublications_st_upload_progress_listener;
	 OnProgressListener _SMediaPublications_st_download_progress_listener;
	 OnFailureListener _SMediaPublications_st_failure_listener;
	
	 Intent hr = new Intent(Intent.ACTION_GET_CONTENT);
	 AlertDialog.Builder dialog;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.publish);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	 void initialize(Bundle _savedInstanceState) {
		linear_background1 = findViewById(R.id.linear_background1);
		//start = findViewById(R.id.start);
		linear1 = findViewById(R.id.linear1);
		//end = findViewById(R.id.end);
		publication_text_info_background = findViewById(R.id.publication_text_info_background);
		publication_image_info_background = findViewById(R.id.publication_image_info_background);
		button1 = findViewById(R.id.button1);
		linear_top2 = findViewById(R.id.linear_top2);
		textinputlayout1 = findViewById(R.id.textinputlayout1);
		title2 = findViewById(R.id.title2);
		lenght_100 = findViewById(R.id.lenght_100);
		text = findViewById(R.id.text);
		linear_top1 = findViewById(R.id.linear_top1);
		gridview1 = findViewById(R.id.gridview1);
		title1 = findViewById(R.id.title1);
		pickImage = findViewById(R.id.pickImage);
	//	textview1 = findViewById(R.id.textview1);
	//	switch1 = findViewById(R.id.switch1);
		auth = FirebaseAuth.getInstance();
		hr.setType("image/*");
		hr.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		dialog = new AlertDialog.Builder(this);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				try{
					if (text.getText().toString().equals("")) {
						textinputlayout1.setError("Digite algum texto");
					}
					else {
						if (superior) {
							textinputlayout1.setError("Texto maior que 100");
						}
						else {
							zuid = SMediaPublications.push().getKey();
							map2 = new HashMap<>();
							map2.put("inApp", "SMedia Java App");
							listmap_created.add(map2);
							map_attrs = new HashMap<>();
							map_attrs.put("created", listmap_created);
							map = new HashMap<>();
							map.put("postText", text.getText().toString());
							map.put("posterUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
							map.put("postUid", zuid);
							map.put("postImages", new Gson().toJson(listmap_images_url));
							if (new Gson().toJson(listmap_images_url).equals("[]")) {
								map.put("postImage", "null");
								map.put("postType", "text");
							}
							else {
								map.put("postImage", listmap_images_url.get((int)0).get("postImage").toString());
								map.put("postType", "image");
							}
							map.put("postLikers", "[]");
							map.put("postLikes", "0");
							map.put("postComment", "[]");
							map.put("postAttrs", new Gson().toJson(map_attrs));
							SMediaPublications.child(zuid).updateChildren(map);
							finish();
						}
					}
				}catch(Exception e){
					SMediaUtils.showMessage(getApplicationContext(), e.toString());
				}
			}
		});
		
		text.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				try{
					if (_charSeq.length() > 100) {
						superior = true;
						lenght_100.setTextColor(0xFFB71C1C);
					}
					else {
						superior = false;
					}
					textinputlayout1.setErrorEnabled(false);
					lenght_100.setText(String.valueOf((long)(_charSeq.length())) + "/100");
				}catch(Exception e){
					SMediaUtils.showMessage(getApplicationContext(), e.toString());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		pickImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				try{
					Intent hr_image = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); startActivityForResult(hr_image, REQ_CD_HR);
				}catch(Exception e){
					SMediaUtils.showMessage(getApplicationContext(), e.toString());
				}
			}
		});
		
		_SMediaPublications_child_listener = new ChildEventListener() {
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
		SMediaPublications.addChildEventListener(_SMediaPublications_child_listener);
		
		_SMediaPublications_st_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_SMediaPublications_st_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_SMediaPublications_st_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("postImage", _downloadUrl);
					listmap_images_url.add(_item);
				}
				
				SMediaUtils.showMessage(getApplicationContext(), "imagem enviada!");
				_telegramLoaderDialog(false);
				listmap_images.remove((int)(posToDel));
			}
		};
		
		_SMediaPublications_st_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_SMediaPublications_st_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_SMediaPublications_st_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				
			}
		};
		
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
				
			}
		};
		
		_auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_auth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	
	 public void initializeLogic() {
		try{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { Window w = getWindow();  w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); };
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			textinputlayout1.setBoxCornerRadii((float)25, (float)25, (float)25, (float)25);
			textinputlayout2.setBoxCornerRadii((float)25, (float)25, (float)25, (float)25);
			textinputlayout3.setBoxCornerRadii((float)25, (float)25, (float)25, (float)25);
			textinputlayout4.setBoxCornerRadii((float)25, (float)25, (float)25, (float)25);
			linear3.setVisibility(View.GONE);
			_round(publication_text_info_background, 30);
			_round(publication_image_info_background, 30);
		}catch(Exception e){
			SMediaUtils.showMessage(getApplicationContext(), e.toString());
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_HR:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("path", _filePath.get((int)(0)));
					listmap_images.add(_item);
				}
				
				gridview1.setAdapter(new Gridview1Adapter(listmap_images));
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	public void _telegramLoaderDialog(final boolean _visibility) {
		if (_visibility) {
			if (coreprog == null){
				coreprog = new ProgressDialog(this);
				coreprog.setCancelable(false);
				coreprog.setCanceledOnTouchOutside(false);
				
				coreprog.requestWindowFeature(Window.FEATURE_NO_TITLE);  coreprog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
				
			}
			coreprog.show();
			coreprog.setContentView(R.layout.loading);
			
			
			LinearLayout linear2 = (LinearLayout)coreprog.findViewById(R.id.linear2);
			
			LinearLayout background = (LinearLayout)coreprog.findViewById(R.id.background);
			
			LinearLayout layout_progress = (LinearLayout)coreprog.findViewById(R.id.layout_progress);
			
			android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(); 
			gd.setColor(Color.parseColor("#E0E0E0")); /* color */
			gd.setCornerRadius(40); /* radius */
			gd.setStroke(0, Color.WHITE); /* stroke heigth and color */
			linear2.setBackground(gd);
			
			RadialProgressView progress = new RadialProgressView(this);
			layout_progress.addView(progress);
		}
		else {
			if (coreprog != null){
				coreprog.dismiss();
			}
		}
	}
	 ProgressDialog coreprog;
	{
	}
	
	
	
	public void gampiot_round_img(View item, Float radii){
			
			android.graphics.drawable.GradientDrawable gampiot_round_image = new android.graphics.drawable.GradientDrawable();
			gampiot_round_image.setColor(Color.TRANSPARENT);
			gampiot_round_image.setCornerRadius(radii);
			item.setClipToOutline(true);
			item.setBackground(gampiot_round_image);
	}
	{
	}
	
	
	 public void _Transitionmanger(final View _view, final String _transitionName) {
				_view.setTransitionName(_transitionName);
	}
	
	
	public void _ICC(final ImageView _img, final String _c1, final String _c2) {
		_img.setImageTintList(new android.content.res.ColorStateList(new int[][] {{-android.R.attr.state_pressed},{android.R.attr.state_pressed}},new int[]{Color.parseColor(_c1), Color.parseColor(_c2)}));
	}
	
	
	public void _round(final View _linear, final double _radious) {
		Drawable background = _linear.getBackground();
		if (background instanceof ColorDrawable) {
			    int backgroundColor = ((ColorDrawable) background).getColor();
			    Color cor = Color.valueOf(backgroundColor);
			    GradientDrawable drawable = new GradientDrawable();
			    drawable.setCornerRadius((int) _radious);
			    drawable.setColor(cor.toArgb());
			    _linear.setBackground(drawable);
		}
		
	}
	
	
	public class Gridview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Gridview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.publications_my, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final Button button1 = _view.findViewById(R.id.button1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(listmap_images.get((int)_position).get("path").toString(), 1024, 1024));
			textview1.setVisibility(View.GONE);
			_round(linear1, 30);
            gampiot_round_img(imageview1, 30f);
			button1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
							   _telegramLoaderDialog(true);
					SMediaPublications_st.child(Uri.parse(listmap_images.get((int)_position).get("path").toString()).getLastPathSegment()).putFile(Uri.fromFile(new File(listmap_images.get((int)_position).get("path").toString()))).addOnFailureListener(_SMediaPublications_st_failure_listener).addOnProgressListener(_SMediaPublications_st_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
						@Override
						public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
							return SMediaPublications_st.child(Uri.parse(listmap_images.get((int)_position).get("path").toString()).getLastPathSegment()).getDownloadUrl();
						}}).addOnCompleteListener(_SMediaPublications_st_upload_success_listener);
					posToDel = _position;
					}
			});
			
			return _view;
		}
	}
}