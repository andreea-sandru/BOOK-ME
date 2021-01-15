package com.example.bookme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.bookme.ObjectModels.BookObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BookPage extends AppCompatActivity  {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;

    ImageView mImage;
    private TextView mName, mAuthor, mYear, mStatus, mCategory;
    Button  bookMeButton;

    private String book_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_page);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));

        // extragem book id-ul trimis din homepage
        book_id = getIntent().getExtras().get("book_id").toString();

        mYear = (TextView) findViewById(R.id.bookPageYear);
        mCategory = (TextView) findViewById(R.id.bookPageCategory);
        mAuthor = (TextView) findViewById(R.id.bookPageAuthor);
        mName = (TextView) findViewById(R.id.bookPageName);
        mStatus = (TextView) findViewById(R.id.bookPageStatus);
        bookMeButton = (Button) findViewById(R.id.bookButton);
        mImage = (ImageView) findViewById(R.id.bookPageImage);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // accesam direct informatiile cartii
        databaseReference = mFirebaseDatabase.getReference().child("available_books").child(book_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BookObject book = dataSnapshot.getValue(BookObject.class);
                Glide.with(getApplicationContext()).load(book.getImageUri()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(mImage);
                mName.setText(book.getBookName());
                mYear.setText(book.getBookYear());
                mAuthor.setText(book.getBookAuthor());
                mCategory.setText(book.getBookCategory());
                mStatus.setText(book.getStatus());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bookMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // updatam cartea, o adaugam in reserved_books si apoi o stergem din available_books la intoarecea in HomePage
                    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("available_books").child(book_id);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BookObject updated_book = dataSnapshot.getValue(BookObject.class);
                            updated_book.setAvailable(false);
                            updated_book.setReservedUserId(userId);
                            DatabaseReference ref_new  = FirebaseDatabase.getInstance().getReference("reserved_books").child(book_id);
                            ref_new.setValue(updated_book).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                // afisare mesaj succes: cartea este acum in lista de reserved_books
                                                Toast.makeText(getApplicationContext(), "Book Reserved Successfully ", Toast.LENGTH_LONG).show();
                                                Intent home_page = new Intent(getApplicationContext(), HomePage.class);
                                                home_page.putExtra("book_delete_id", book_id);
                                                startActivity(home_page);
                                            }
                                        }
                                    }
                            );
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }
        });

    }
}