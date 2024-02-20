package com.smedia.app;

import android.animation.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.*;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PerfilFragmentActivity extends Fragment {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private FloatingActionButton _fab;
	private HashMap<String, Object> userAttrs = new HashMap<>();
	private boolean myVerified = false;
	private boolean myAdm = false;
	
	private ArrayList<HashMap<String, Object>> listmap_my_publications = new ArrayList<>();
	
	private LinearLayout linear_background;
	private LinearLayout start;
	private LinearLayout activity_background;
	private LinearLayout end;
	private LinearLayout linear_perfil;
	private LinearLayout linear_basic_info;
	private GridView gridview1;
	private LinearLayout perfil;
	private TextView name;
	private TextView name2;
	private LinearLayout linear2;
	private TextView short_name;
	private LinearLayout linear_verified;
	private LinearLayout linear_adm;
	private TextView textview1;
	private TextView textview3;
	private LinearLayout linear_followers;
	private LinearLayout linear_publications;
	private ImageView image_1;
	private TextView followers_count;
	private ImageView image_2;
	private TextView publications_count;
    private LinearLayout opens;
	
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
	
	private DatabaseReference SMediaPublications = _firebase.getReference("SMediaPublications");
	private ChildEventListener _SMediaPublications_child_listener;
	private Intent intent = new Intent();
	private AlertDialog.Builder d;
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.perfil_fragment, _container, false);
		initialize(_savedInstanceState, _view);
		FirebaseApp.initializeApp(getContext());
		initializeLogic();
		return _view;
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
		_fab = _view.findViewById(R.id._fab);
		opens=_view.findViewById(R.id.opens);
		linear_background = _view.findViewById(R.id.linear_background);
		//start = _view.findViewById(R.id.start);
		activity_background = _view.findViewById(R.id.activity_background);
		end = _view.findViewById(R.id.end);
		linear_perfil = _view.findViewById(R.id.linear_perfil);
		linear_basic_info = _view.findViewById(R.id.linear_basic_info);
		gridview1 = _view.findViewById(R.id.gridview1);
		perfil = _view.findViewById(R.id.perfil);
		name = _view.findViewById(R.id.name);
		name2 = _view.findViewById(R.id.name2);
		linear2 = _view.findViewById(R.id.linear2);
		short_name = _view.findViewById(R.id.short_name);
		linear_verified = _view.findViewById(R.id.linear_verified);
		linear_adm = _view.findViewById(R.id.linear_adm);
		textview1 = _view.findViewById(R.id.textview1);
		textview3 = _view.findViewById(R.id.textview3);
		linear_followers = _view.findViewById(R.id.linear_followers);
		linear_publications = _view.findViewById(R.id.linear_publications);
		image_1 = _view.findViewById(R.id.image_1);
		followers_count = _view.findViewById(R.id.followers_count);
		image_2 = _view.findViewById(R.id.image_2);
		publications_count = _view.findViewById(R.id.publications_count);
		auth = FirebaseAuth.getInstance();
		d = new AlertDialog.Builder(getActivity());
		
        opens.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				try{
					Uri uri = Uri.parse("https://github.com/trindadeDevAndroid/SMedia"); // Substitua com o link do seu código
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
				}catch(Exception e){
					SMediaUtils.showMessage(getContext().getApplicationContext(), e.toString());
				}
			}
		});
        
        
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				try{
					_dialog_type_to_publish();
				}catch(Exception e){
					SMediaUtils.showMessage(getContext().getApplicationContext(), e.toString());
				}
			}
		});
		
		_SMediaUsers_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					name.setText(_childValue.get("userName").toString());
					name2.setText("@gmail.com".replace("gmail.com", "") + _childValue.get("userName").toString().trim().toLowerCase().toLowerCase().trim());
					followers_count.setText(_childValue.get("userFollowers").toString());
					_shortName(_childValue.get("userName").toString(), short_name);
					userAttrs = new Gson().fromJson(_childValue.get("userAttrs").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
					if (userAttrs.containsKey("verified")) {
						if (userAttrs.get("verified").toString().equals("true")) {
							myVerified = true;
						}
						else {
							myVerified = false;
							linear_verified.setVisibility(View.GONE);
						}
					}
					if (userAttrs.containsKey("adm")) {
						if (userAttrs.get("adm").toString().equals("true")) {
							myAdm = true;
						}
						else {
							myAdm = false;
							linear_adm.setVisibility(View.GONE);
						}
					}
				}
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
		
		_SMediaPublications_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				try{
					if (_childValue.get("posterUid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
						listmap_my_publications.add(_childValue);
						gridview1.setAdapter(new Gridview1Adapter(listmap_my_publications));
						publications_count.setText(String.valueOf((long)(listmap_my_publications.size())));
					}
				}catch(Exception e){
					SMediaUtils.showMessage(getContext().getApplicationContext(), e.toString());
				}
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
	
	private void initializeLogic() {
		_ICC(image_1, "#1D192B", "#FFFFFF");
		_ICC(image_2, "#1D192B", "#FFFFFF");
		_round(linear_perfil, 30);
		_round(perfil, 999);
		_round(linear_followers, 999);
		_round(linear_publications, 999);
		_round(linear_verified, 999);
		_round(linear_adm, 999);
	}
	
	public void _shortName(final String _name, final TextView _text) {
		shortName(_name, _text);
	}private void shortName(String name, TextView text){
			String str = name.substring((int)(0), (int)(1));
			text.setText(str.toUpperCase());
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
	
	
	public void _dialog_type_to_publish() {
		intent.setClass(getContext().getApplicationContext(), PublishActivity.class);
		startActivity(intent);
		/* Context act = getActivity();
final com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(act);
View view;
view = getActivity().getLayoutInflater().inflate(R.layout.publish_dialog, null);
dialog.setContentView(view);
dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
dialog.setCancelable(true);

LinearLayout linear_background = (LinearLayout) view.findViewById(R.id.linear_background);
LinearLayout img = (LinearLayout) view.findViewById(R.id.linear_type_image);
LinearLayout txt = (LinearLayout) view.findViewById(R.id.linear_type_text);
ImageView im1 = (ImageView) view.findViewById(R.id.imageview1);
ImageView im2 = (ImageView) view.findViewById(R.id.imageview2);
_ICC(im1, "#2c1515", "#2c1515");
_ICC(im2, "#2c1515", "#2c1515");
_round(img, 30);
_round(txt, 30);
img.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View _view) {
		    
	}
});
txt.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View _view) {
		   intent.setClass(getContext().getApplicationContext(), PublishActivity.class);
intent.putExtra("type", "txt");
startActivity(intent);
	}
});

dialog.show(); */
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
			LayoutInflater _inflater = getActivity().getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.publications_my, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final Button button1 = _view.findViewById(R.id.button1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			try{
				button1.setVisibility(View.GONE);
				_ICC(imageview1, "#1D192B", "#1D192B");
				_round(linear1, 30);
				linear1.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View _view) {
						Context c = getActivity();
						MaterialAlertDialogBuilder d = new MaterialAlertDialogBuilder(c);
						d.setTitle("Deletar publicação");
						d.setMessage("Deletar a publicação não é reversível.");
						d.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
										SMediaPublications.child(listmap_my_publications.get((int)_position).get("postUid").toString()).removeValue();
								listmap_my_publications.remove((int)(_position));
								}
						});
						d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
										 
								}
						});
						d.show();
						return true;
					}
				});
				imageview1.setImageResource(R.drawable.image_icon);
				textview1.setText("Publication");
			}catch(Exception e){
				SMediaUtils.showMessage(getContext().getApplicationContext(), e.toString());
			}
			
			return _view;
		}
	}
}