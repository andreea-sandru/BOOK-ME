package com.example.bookme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.bookme.ObjectModels.BookObject;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;

public class AddBook extends AppCompatActivity {

    EditText nameEditText, authorEditText, editCategoryInfo, editYearInfo;
    ImageView  ShowImageView;
    Button ButtonUploadBook, ButtonChooseImage;

    // path-ul din Firebase Storage unde stocam imaginile
    String Storage_Path = "images/";
    // referinte storage si database
    StorageReference storageReference;
    FirebaseAuth mFirebaseAuth;

    Uri FilePathUri;
    private final int Image_Req_Code = 7;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));

        storageReference = FirebaseStorage.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        authorEditText= (EditText)  findViewById(R.id.authorEditText);
        editCategoryInfo= (EditText) findViewById(R.id.editCategoryInfo);
        editYearInfo = (EditText)  findViewById(R.id.editYearInfo);

        ButtonChooseImage = (Button) findViewById(R.id.ButtonChooseImage);
        ButtonUploadBook = (Button) findViewById(R.id.ButtonUploadBook);
        ShowImageView = (ImageView) findViewById(R.id.ShowImageView);

        progressDialog = new ProgressDialog(AddBook.this);

        // Click listener pentru choose image button
        ButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Req_Code);
            }
        });


        // Click listener pentru upload button
        ButtonUploadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadBookImageToFirebaseStorage();
            }
        });
    }

    public void UploadBookImageToFirebaseStorage() {
        final String bookName = nameEditText.getText().toString().trim();
        final String bookYear = editYearInfo.getText().toString().trim();
        final String bookCategory = editCategoryInfo.getText().toString().trim();
        final String bookAuthor = authorEditText.getText().toString().trim();

        if (bookName.isEmpty() || bookYear.isEmpty() || bookCategory.isEmpty() || FilePathUri == null || bookAuthor.isEmpty()) {
            Toast.makeText(AddBook.this, "Please insert all requested data!", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setTitle("Data is Uploading...");
            progressDialog.show();
            final StorageReference storageRef2 = storageReference.child(Storage_Path + FilePathUri.getLastPathSegment());
            storageRef2.putFile(FilePathUri).continueWithTask(new Continuation< UploadTask.TaskSnapshot, Task< Uri >>() {
                @Override
                public Task < Uri > then(@NonNull Task < UploadTask.TaskSnapshot > task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageRef2.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener< Uri >() {
                @Override
                public void onComplete(@NonNull Task < Uri > task) {
                    if (task.isSuccessful()) {
                        /* poza s-a uploadat => stop progressbar */
                        progressDialog.dismiss();

                        // uri-ul imaginii din storage
                        Uri downloadUri = task.getResult();
                        final String imageUri = downloadUri.toString();

                        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference(); // referinta catre baza de date
                        final String key = dbref.push().getKey(); // acesta va fi id-ul unic al cartii
                        final String userId = mFirebaseAuth.getCurrentUser().getUid(); //id-ul userului curent

                        // creare carte si apoi adaugare in baza de date
                        BookObject book = new BookObject(key, userId, imageUri, bookName, bookAuthor, bookCategory, bookYear);

                        dbref.child("all_books").child(key).setValue(book).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // afisare mesaj succes
                                            Toast.makeText(getApplicationContext(), "Book Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                            // daca s-a adaugat cartea cu succes => redirectare catre HomePage
                                            startActivity(new Intent(AddBook.this, HomePage.class));
                                        }
                                    }
                                }
                        );

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AddBook.this, "Could't load book, please try again", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddBook.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Req_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                ShowImageView.setImageBitmap(bitmap);
                ButtonChooseImage.setText("Image Selected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}