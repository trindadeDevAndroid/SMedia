package com.smedia.app;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.*;
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
import androidx.core.widget.NestedScrollView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HomeFragmentActivity extends Fragment {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private HashMap<String, Object> map = new HashMap<>();
	private double position = 0;
	private boolean myPublicAccount = false;
	private double lenght = 0;
	private String myPhoto = "";
	private String myName = "";
	private HashMap<String, Object> mao = new HashMap<>();
	private String uidTemp = "";
	private double b = 0;
	private HashMap<String, Object> attrs = new HashMap<>();
	private double height = 0;
	private double width = 0;
	
	private ArrayList<HashMap<String, Object>> listmap_publications = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_users = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_likers = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_likedVideos = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_comments = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_images = new ArrayList<>();
	
	private LinearLayout linear_background;
	private LinearLayout start;
	private LinearLayout activity_background;
	private ListView listview_comment;
	private ImageView imageview1;
	private RecyclerView list_images;
	private ListView listview_publications;
	
	private DatabaseReference SMediaPublications = _firebase.getReference("SMediaPublications");
	private ChildEventListener _SMediaPublications_child_listener;
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
	
	private SharedPreferences sp;
	private AlertDialog.Builder dialog;
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.home_fragment, _container, false);
		initialize(_savedInstanceState, _view);
		FirebaseApp.initializeApp(getContext());
		initializeLogic();
		return _view;
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
		linear_background = _view.findViewById(R.id.linear_background);
		activity_background = _view.findViewById(R.id.activity_background);
		listview_comment = _view.findViewById(R.id.listview_comment);
		imageview1 = _view.findViewById(R.id.imageview1);
		list_images = _view.findViewById(R.id.list_images);
		listview_publications = _view.findViewById(R.id.listview_publications);
		auth = FirebaseAuth.getInstance();
		sp = getContext().getSharedPreferences("sp", Activity.MODE_PRIVATE);
		dialog = new AlertDialog.Builder(getActivity());
		
		_SMediaPublications_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				try{
					_telegramLoaderDialog(false);
					SMediaPublications.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot _dataSnapshot) {
							listmap_publications = new ArrayList<>();
							try {
								GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
								for (DataSnapshot _data : _dataSnapshot.getChildren()) {
									HashMap<String, Object> _map = _data.getValue(_ind);
									listmap_publications.add(_map);
								}
							}
							catch (Exception _e) {
								_e.printStackTrace();
							}
							listview_publications.setAdapter(new Listview_publicationsAdapter(listmap_publications));
							Collections.reverse(listmap_publications);
							((BaseAdapter)listview_publications.getAdapter()).notifyDataSetChanged();
						}
						@Override
						public void onCancelled(DatabaseError _databaseError) {
						}
					});
				}catch(Exception e){
					SMediaUtils.showMessage(getContext().getApplicationContext(), e.toString());
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				try{
					_telegramLoaderDialog(false);
					SMediaPublications.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot _dataSnapshot) {
							listmap_publications = new ArrayList<>();
							try {
								GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
								for (DataSnapshot _data : _dataSnapshot.getChildren()) {
									HashMap<String, Object> _map = _data.getValue(_ind);
									listmap_publications.add(_map);
								}
							}
							catch (Exception _e) {
								_e.printStackTrace();
							}
							listview_publications.setAdapter(new Listview_publicationsAdapter(listmap_publications));
							Collections.reverse(listmap_publications);
							((BaseAdapter)listview_publications.getAdapter()).notifyDataSetChanged();
						}
						@Override
						public void onCancelled(DatabaseError _databaseError) {
						}
					});
				}catch(Exception e){
					SMediaUtils.showMessage(getContext().getApplicationContext(), e.toString());
				}
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
		
		_SMediaUsers_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				try{
					if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
						_telegramLoaderDialog(false);
						if (_childValue.get("userAccountPrivacy").toString().equals("public")) {
							myPublicAccount = true;
						}
						else {
							myPublicAccount = false;
						}
					}
					else {
						
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
		_telegramLoaderDialog(true);
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
	
	
	public void _test(final View _view, final double _num1) {
		_view.getLayoutParams().height=(int)(_num1);
	}
	
	
	public void _shortName(final String _name, final TextView _text) {
		shortName(_name, _text);
	}private void shortName(String name, TextView text){
			String str = name.substring((int)(0), (int)(1));
			text.setText(str.toUpperCase());
	}
	
	
	public void _dialog_comment() {
		//Diálogo comentários
		    Context act = getActivity();
		final com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(act);
		View view;
		view = getActivity().getLayoutInflater().inflate(R.layout.comments_dialog, null);
		dialog.setContentView(view);
		//dialog.getWindow().setBackgroundResource(android.R.color.transparent);
		dialog.setCancelable(true);
		
		LinearLayout linear_background = (LinearLayout) view.findViewById(R.id.linear_background);
		ListView listview_comment = (ListView) view.findViewById(R.id.listview_comment);
		TextView t2 = (TextView) view.findViewById(R.id.textview2);
		com.google.android.material.textfield.TextInputEditText value = (com.google.android.material.textfield.TextInputEditText) view.findViewById(R.id.value);
		com.google.android.material.textfield.TextInputLayout valuelayout = (com.google.android.material.textfield.TextInputLayout) view.findViewById(R.id.textinputlayout1);
		ImageView s = (ImageView) view.findViewById(R.id.poster_image);
		TextView shortName = (TextView) view.findViewById(R.id.short_name);
		LinearLayout perfil = (LinearLayout) view.findViewById(R.id.perfil);
		ImageView send = (ImageView) view.findViewById(R.id.send);
		value.setFocusableInTouchMode(true);
		listview_comment.setAdapter(new Listview_commentAdapter(listmap_comments));
		((BaseAdapter)listview_comment.getAdapter()).notifyDataSetChanged();
		valuelayout.setBoxCornerRadii((float)25, (float)25, (float)25, (float)25);
		if (!(listmap_comments.size() == 0)) {
			t2.setVisibility(View.GONE);
		}
		_round(perfil, 999);
		if (myPhoto.equals("default")) {
			_shortName(myName, shortName);
			s.setVisibility(View.GONE);
		}
		else {
			Glide.with(getContext().getApplicationContext()).load(Uri.parse(myPhoto)).into(s);
			perfil.setVisibility(View.GONE);
		}
		send.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
						   mao = new HashMap<>();
				mao.put("commentText", value.getText().toString());
				mao.put("commentatorUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
				listmap_comments.add(mao);
				mao = new HashMap<>();
				mao.put("postComment", new Gson().toJson(listmap_comments));
				SMediaPublications.child(uidTemp).updateChildren(mao);
				listview_comment.setAdapter(new Listview_commentAdapter(listmap_comments));
				((BaseAdapter)listview_comment.getAdapter()).notifyDataSetChanged();
				}
		});
		
		dialog.show();
		//Diálogo comentários
	}
	
	
	public void _telegramLoaderDialog(final boolean _visibility) {
		Context c = getActivity();
		if (_visibility) {
			if (coreprog == null){
				coreprog = new ProgressDialog(c);
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
			
			RadialProgressView progress = new RadialProgressView(c);
			layout_progress.addView(progress);
		}
		else {
			if (coreprog != null){
				coreprog.dismiss();
			}
		}
	}
	private ProgressDialog coreprog;
	{
	} private void gampiot_round_img(View item, Float radii){
			
			android.graphics.drawable.GradientDrawable gampiot_round_image = new android.graphics.drawable.GradientDrawable();
			gampiot_round_image.setColor(Color.TRANSPARENT);
			gampiot_round_image.setCornerRadius(radii);
			item.setClipToOutline(true);
			item.setBackground(gampiot_round_image);
	}
	{
	}
	
	public class Listview_commentAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview_commentAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_view = _inflater.inflate(R.layout.comment, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final ImageView poster_image = _view.findViewById(R.id.poster_image);
			final LinearLayout perfil = _view.findViewById(R.id.perfil);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final TextView short_name = _view.findViewById(R.id.short_name);
			final TextView commentatorName = _view.findViewById(R.id.commentatorName);
			final TextView comment_value = _view.findViewById(R.id.comment_value);
			
			//listview de comentários no diálogo 
			    _round(perfil, 999);
			comment_value.setText(listmap_comments.get((int)_position).get("commentText").toString());
			SMediaUsers.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot _dataSnapshot) {
					listmap_users = new ArrayList<>();
					try {
						GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
						for (DataSnapshot _data : _dataSnapshot.getChildren()) {
							HashMap<String, Object> _map = _data.getValue(_ind);
							listmap_users.add(_map);
						}
					}
					catch (Exception _e) {
						_e.printStackTrace();
					}
					position = listmap_users.size() - 1;
					lenght = listmap_users.size();
					for(int _repeat118 = 0; _repeat118 < (int)(lenght); _repeat118++) {
						if (listmap_comments.get((int)_position).get("commentatorUid").toString().equals(listmap_users.get((int)position).get("userUid").toString())) {
							commentatorName.setText(listmap_users.get((int)position).get("userName").toString());
							_shortName(listmap_users.get((int)position).get("userName").toString(), short_name);
							Glide.with(getContext().getApplicationContext()).load(Uri.parse(listmap_users.get((int)position).get("userPhoto").toString())).into(poster_image);
							if (listmap_users.get((int)position).get("userPhoto").toString().equals("default")) {
								poster_image.setVisibility(View.GONE);
							}
							else {
								perfil.setVisibility(View.GONE);
							}
						}
						else {
							listmap_users.remove((int)(position));
						}
						position--;
					}
				}
				@Override
				public void onCancelled(DatabaseError _databaseError) {
				}
			});
			//listview de comentários no diálogo 
			
			return _view;
		}
	}
	
	public class List_imagesAdapter extends RecyclerView.Adapter<List_imagesAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public List_imagesAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getActivity().getLayoutInflater();
			View _v = _inflater.inflate(R.layout.corousel_image_view, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final ImageView img = _view.findViewById(R.id.img);
            
            
            try{
                Context ct = getActivity();
	Glide.with(ct).load(Uri.parse(_data.get((int)_position).get("postImage").toString())).into(img);
	gampiot_round_img(img, 40f);
	img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
					   Context c = getActivity();
			MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(c);
			View alertD = getLayoutInflater().inflate(R.layout.image_viewer, null);
			dialog.setView(alertD);
			
			final ImageView i1 = (ImageView) alertD.findViewById(R.id.imageview1);
			dialog.setTitle("Imagem");
                            Context ct = getActivity();
			Glide.with(ct).load(Uri.parse(_data.get((int)_position).get("postImage").toString())).into(i1);
			gampiot_round_img(i1, 30f);
			dialog.show();
			}
	});
}catch(Exception e){
	 
}
        
				
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
	}
	
	public class Listview_publicationsAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview_publicationsAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_view = _inflater.inflate(R.layout.publications, null);
			}
			
			final LinearLayout linear_background1 = _view.findViewById(R.id.linear_background1);
			final LinearLayout linear_background2 = _view.findViewById(R.id.linear_background2);
			final LinearLayout post_bottom1 = _view.findViewById(R.id.post_bottom1);
			final LinearLayout post_top = _view.findViewById(R.id.post_top);
			final LinearLayout post_bottom2 = _view.findViewById(R.id.post_bottom2);
			final LinearLayout linear_background_image = _view.findViewById(R.id.linear_background_image);
			final ImageView poster_image = _view.findViewById(R.id.poster_image);
			final LinearLayout perfil = _view.findViewById(R.id.perfil);
			final TextView poster_name = _view.findViewById(R.id.poster_name);
			final TextView short_name = _view.findViewById(R.id.short_name);
			final TextView post_text = _view.findViewById(R.id.post_text);
			final androidx.recyclerview.widget.RecyclerView list_images = _view.findViewById(R.id.list_images);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView likes_count = _view.findViewById(R.id.likes_count);
			final TextView coments_button = _view.findViewById(R.id.coments_button);
			
			try{
				Animation animation; animation = AnimationUtils.loadAnimation( getContext().getApplicationContext(), android.R.anim.fade_in );  animation.setDuration(1200); 
				
				linear_background1.startAnimation(animation); animation = null;
				post_text.setText(listmap_publications.get((int)_position).get("postText").toString());
				_round(perfil, 999);
				SMediaUsers.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						listmap_users = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								listmap_users.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						position = listmap_users.size() - 1;
						lenght = listmap_users.size();
						for(int _repeat364 = 0; _repeat364 < (int)(lenght); _repeat364++) {
							if (listmap_publications.get((int)_position).get("posterUid").toString().equals(listmap_users.get((int)position).get("userUid").toString())) {
								poster_name.setText(listmap_users.get((int)position).get("userName").toString());
								_shortName(listmap_users.get((int)position).get("userName").toString(), short_name);
								Glide.with(getContext().getApplicationContext()).load(Uri.parse(listmap_users.get((int)position).get("userPhoto").toString())).into(poster_image);
								if (listmap_users.get((int)position).get("userPhoto").toString().equals("default")) {
									poster_image.setVisibility(View.GONE);
								}
								else {
									perfil.setVisibility(View.GONE);
								}
							}
							else {
								listmap_users.remove((int)(position));
							}
							position--;
						}
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				if (listmap_publications.get((int)_position).get("postImages").toString().equals("[]")) {
					linear_background_image.setVisibility(View.GONE);
				}
				else {
					
				}
				//Comentários
				    coments_button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
								   uidTemp = listmap_publications.get((int)_position).get("postUid").toString();
						listmap_comments = new Gson().fromJson(listmap_publications.get((int)_position).get("postComment").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						_dialog_comment();
						}
				});
				//Comentários
				//Likes
				    likes_count.setText(_data.get((int)_position).get("postLikes").toString().concat(" Likes"));
				imageview1.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
								   ScaleAnimation fade_in = new ScaleAnimation(0.9f, 1f, 0.9f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.7f);
						fade_in.setDuration(300);
						fade_in.setFillAfter(true);
						imageview1.startAnimation(fade_in);
						listmap_likers = new Gson().fromJson(_data.get((int)_position).get("postLikers").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						if (sp.getString(_data.get((int)_position).get("postUid").toString().concat("_like"), "").equals("liked")) {
							if (!_data.get((int)_position).get("postLikers").toString().contains("")) {
								map = new HashMap<>();
								map.put("likerUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
								listmap_likers.add(map);
							}
							map = new HashMap<>();
							map.put("postLikes", String.valueOf((long)(Double.parseDouble(_data.get((int)_position).get("postLikes").toString()) - 1)));
							map.put("postLikers", new Gson().toJson(listmap_likers));
							SMediaPublications.child(_data.get((int)_position).get("postUid").toString()).updateChildren(map);
							sp.edit().putString(_data.get((int)_position).get("postUid").toString().concat("_like"), "").commit();
							likes_count.setText(_data.get((int)_position).get("postLikes").toString().concat("likes"));
							imageview1.setImageResource(R.drawable.like_1);
						}
						else {
							map = new HashMap<>();
							map.put("postLikes", String.valueOf((long)(Double.parseDouble(_data.get((int)_position).get("postLikes").toString()) + 1)));
							SMediaPublications.child(_data.get((int)_position).get("postUid").toString()).updateChildren(map);
							sp.edit().putString(_data.get((int)_position).get("postUid").toString().concat("_like"), "liked").commit();
							likes_count.setText(_data.get((int)_position).get("postLikes").toString().concat("likes"));
							imageview1.setImageResource(R.drawable.like_2);
						}
						}
				});
				if (sp.getString(_data.get((int)_position).get("postUid").toString().concat("_like"), "").equals("")) {
					imageview1.setImageResource(R.drawable.like_1);
				}
				else {
					imageview1.setImageResource(R.drawable.like_2);
				}
				//Likes
				listmap_images = new Gson().fromJson(listmap_publications.get((int)_position).get("postImages").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				list_images.setAdapter(new List_imagesAdapter(listmap_images));
				list_images.setLayoutManager(new LinearLayoutManager(getContext()));
				list_images.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
				if (listmap_publications.get((int)_position).containsKey("postAttrs")) {
					attrs = new Gson().fromJson(listmap_publications.get((int)_position).get("postAttrs").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
				}
				else {
					
				}
			}catch(Exception e){
				SMediaUtils.showMessage(getContext().getApplicationContext(), e.toString());
			}
			
			return _view;
		}
	}
}