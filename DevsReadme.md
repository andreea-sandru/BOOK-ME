
# What we have until now:

- universal account type: everyone can access the Manage Books Button in HomePage

  - we can change this later quickly but for now it's easier for testing
  

**- HomePage:** 
                 
- **Reserved Page**: this already has info in the database ("reserved_books") which contains all reserved books, along with reservedUserId
   
   - next: show only the books that the current user has reserved with a **RecyclerView**
           
    (or, easier: add to the userObject a list of reservedBooks: when he reserves a book: also add it to his own list 
    
    -> then show this list (path: users.userId.myReservedBooks) in Reserved Page; this way we don't need any filtering)

- **Available Page**: this has already all info; next: add filter option  
   
          
**- Manage Books:** 
  
  - this has only the **ADD BOOK** option
    
  - next: 
    
      - show all reserved books with a **RecyclerView**  ("reserved_books" in the db)
         
      - show all available books with another **RecyclerView** ("available_books" in the db)
      
      
      
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
                                    
