package com.example.pengtok;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pengtok.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private String splash_background;

    private ImageView profile;
    private Uri imageUrl; //업로드할 때 이미지 받음.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        splash_background = mFirebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        profile = findViewById(R.id.signUpActivity_imageView_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM); //모든 이벤트가 모여서 다른 곳으로 이동하게 해줌.

            }
        });

        email = findViewById(R.id.signUpActivity_edittext_email);
        name = findViewById(R.id.signUpActivity_edittext_name);
        password = findViewById(R.id.signUpActivity_edittext_password);
        signup = findViewById(R.id.signUpActivity_button_signup);
        signup.setBackgroundColor(Color.parseColor(splash_background));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString() == null || TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }else if(name.getText().toString() == null || TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }else if(password.getText().toString() == null || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(imageUrl != null) {
                    Toast.makeText(getApplicationContext(), "프로필 사진이 지정한 사진으로 저장되었습니다.", Toast.LENGTH_LONG).show();
                    onBackPressed();

                }else {
                    Toast.makeText(getApplicationContext(),"프로필 사진은 기본 이미지로 저장되었습니다.",Toast.LENGTH_LONG).show();
                    imageUrl = Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.loading_icon);
                    onBackPressed();
                }


                //회원가입
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) { //회원가입 완료
                                //회원가입 후 사진을 올리고 데이터베이스로 넘어가는 3단 구조
                                final String uid = task.getResult().getUser().getUid();

                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        //파일저장된 경로를 보내주는 코드

                                        UserModel userModel = new UserModel();
                                        userModel.userName = name.getText().toString();
                                        //Log.d("userName", name.getText().toString());
                                        userModel.profileImageUrl = task.getResult().getStorage().getDownloadUrl().toString();;
                                        //Log.d("ImageUrl", task.getResult().getStorage().getDownloadUrl().toString());

                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                                    }

                                });
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData()); //가운데 뷰를 바꿈.
            imageUrl = data.getData(); //이미지 경로 원본


        }
    }
}