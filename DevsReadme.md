
# What we have until now:

- universal account type: everyone can access the Manage Books Button in HomePage

  - we can change this later quickly but for now it's easier for testing
  

**- HomePage:** 
                 
   - *Reserved Page*: this already has info in the database ("reserved_books") which contains all books that are reserved, along with the reservedUserId
   
          - next: show only the books that the current user has reserved
           
           (or, easier: add to the userObject a list of reservedBooks: when he reserves a book: add it to the list -> then show this list in the RecyclerView in Reserved Page)

   - *Available Page*: this has already all info; next: add filter option  
   
          
  **- Manage Books:** 
  
    - this has only the *ADD BOOK* option
    
    - next: 
    
        - show all reserved books with a **RecyclerView**  ("reserved_books" in the db)
         
        - show all available books with another **RecyclerView** ("available_books" in the db)
