
# What we have until now:

**Universal account type**

  - we can change this later quickly but for now it's easier for testing
  

**HomePage:** 
   
- **All Books Page**

- **Reserved (by me) Page** 

- **Available Books Page**

- **Saved Books Page**: not implemented yet

- i added filtering by categories

- next: filtering by name (especially in book management page)


**BookPage:**

- this has almost everything except Notifications functionality

          
**Manage Books Page:** 
  
  - this has only the **ADD BOOK** option
    
  - next: search books by name and implement **DELETE BOOK** functionality
  
  
 What have changed:
 
 - now we have a single list called "all_books"
 
 - and to extract specific books we use filtering
 
 
 **So we still need:**
 
 - notifications
 
 - book management: search books by name + delete books
 
 - separate account types
 
 - favourites page (optional)
 
 - better documentation
 
      
# Useful info about Firebase:
      
- to work with Firebase Realtime Database, you first need a reference to it:
      
 - DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reserved_books").child(book_id);
      
   (this is a reference to a specific book id in the "reserved_books" list)
       
  - then, to add an *object* at that reference you need to use setValue:
        
   **reference.setValue(object).addOnCompleteListener(..);**
   
  - if you want to access some data in Firebase you also need a reference and a listener
  
     which can be a one time listener (when you want to acces the data only once)
   
     or a continuous listener (that listens for changes in the database and updates the existing data)
   
   - single time listener:
   
     DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("available_books").child(book_id);
     
     ref.addListenerForSingleValueEvent(new ValueEventListener() {
        
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        
        BookObject book = dataSnapshot.getValue(BookObject.class); 
        });
        
        
  - continuous listener:
  
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("available_books").child(book_id);
       
    ref.addValueEventListener(new ValueEventListener() {
       
       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             BookObject book = dataSnapshot.getValue(BookObject.class);
             // book va fi mereu actualizat cu ultimele modificari din database
                
      });
                                    
