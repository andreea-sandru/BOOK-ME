package com.example.bookme;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.bookme.ObjectModels.BookObject;
import com.example.bookme.ObjectModels.UserObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BookPage extends AppCompatActivity  {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference, dbRef;
    public String estimatedTime = "";
    ImageView mImage;
    private TextView mName, mAuthor, mYear, mStatus, mCategory, reservedUsername, reservedEstimated, timePassed;
    Button  bookMeButton;
    LinearLayout info1, info2, info3;

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
        timePassed = (TextView) findViewById(R.id.timePassed);
        bookMeButton = (Button) findViewById(R.id.bookButton);
        mImage = (ImageView) findViewById(R.id.bookPageImage);
        info1 = (LinearLayout) findViewById(R.id.bookInfo1);
        info2 = (LinearLayout) findViewById(R.id.bookInfo2);
        info3 = (LinearLayout) findViewById(R.id.bookInfo3);
        reservedEstimated = (TextView) findViewById(R.id.reservedEstimated);
        reservedUsername = (TextView) findViewById(R.id.reservedUsername);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // accesam direct informatiile cartii
        databaseReference = mFirebaseDatabase.getReference().child("all_books").child(book_id);
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

                // daca este rezervata cartea => afisam si info legate de rezervare
                if(!book.isAvailable()) {
                   info1.setVisibility(View.VISIBLE);
                   info2.setVisibility(View.VISIBLE);
                   info3.setVisibility(View.VISIBLE);
                   reservedEstimated.setText(book.getEstimatedTime());
                   reservedUsername.setText(book.getReservedUsername());

                    String reservedDate = book.getReservedDate();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String nowDate = dateFormat.format(date).toString();
                    // Function Call
                    String time = findDifference(reservedDate, nowDate);
                    timePassed.setText(time);
                    bookMeButton.setText("Notify when available");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bookMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // verificam daca este availble si daca da => o putem rezerva
                dbRef = FirebaseDatabase.getInstance().getReference().child("all_books").child(book_id);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        BookObject book = dataSnapshot.getValue(BookObject.class);
                        if(book.isAvailable()) {
                            raisePopUp();
                        }
                        else {
                            // extragere id user curent
                            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            DatabaseReference ref2  = FirebaseDatabase.getInstance().getReference("all_books").child(book_id);
                            ref2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // citire info carte din baza de date
                                    BookObject book = dataSnapshot.getValue(BookObject.class);
                                    HashMap usersList = book.getNotifyUserIds();
                                    usersList.put(userId, userId);
                                    // actualizare book
                                    book.setNotifyUserIds(usersList);

                                    // facem iar o referinta catre carte
                                    DatabaseReference ref3  = FirebaseDatabase.getInstance().getReference("all_books").child(book_id);
                                    // update carte in baza de date
                                    ref3.child("notifyUserIds").setValue(usersList).addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        // afisam mesaj de succes
                                                        Toast.makeText(getApplicationContext(), "Book Updated Successfully ", Toast.LENGTH_LONG).show();
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
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void raisePopUp() {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estimatedTime = editText.getText().toString();

                // updatam cartea in lista all_books
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("all_books").child(book_id);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final BookObject updated_book = dataSnapshot.getValue(BookObject.class);

                        if(updated_book.isAvailable()) {

                            updated_book.setEstimatedTime(estimatedTime);
                            updated_book.setAvailable(false);
                            updated_book.setReservedUserId(userId);
                            // extragere nume user curent:
                            DatabaseReference ref_new  = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            ref_new.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserObject user = snapshot.getValue(UserObject.class);
                                    updated_book.setReservedUsername(user.getUserFullName());

                                    // salvare data rezervare carte ca string
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    Date date = new Date();
                                    String dateString = dateFormat.format(date).toString();
                                    updated_book.setReservedDate(dateString);

                                    // updatare info carte in baza de date
                                    DatabaseReference ref_new  = FirebaseDatabase.getInstance().getReference("all_books").child(book_id);
                                    ref_new.setValue(updated_book).addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        dialogBuilder.dismiss();
                                                        // afisare mesaj succes: cartea este acum in lista de reserved_books
                                                        Toast.makeText(getApplicationContext(), "Book Reserved Successfully ", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else {
                            dialogBuilder.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }


    static String findDifference(String start_date, String end_date)  {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();

            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;

            return difference_In_Days + "days, "
                    + difference_In_Hours
                    + " h, "
                    + difference_In_Minutes
                    + " m, "
                    + difference_In_Seconds
                    + " s";
        }

        catch (ParseException e) {

            e.printStackTrace();
            return "errorrr";
        }
    }

}