# Bob's Little Free Library 

Bob's Little Free Library is a book sharing app created as the term project for [CMPUT 301 - Intro to Software Engineering ](https://www.ualberta.ca/computing-science/undergraduate-studies/course-directory/courses/introduction-to-software-engineering.html) in Fall of 2020. The app is for a community of readers that want to upload, borrow, lend, and exchange each other's books with ease. Take a look at a short demo [here](https://drive.google.com/file/d/1G78mErhBzyJEwKd62OLz0UABxwjl0JgC/view?usp=sharing) where two users, Mari and Lucas, go through the process of adding, requesting, and then exchanging a book. This demo showcases the UI design and the basic features of the app, a comprehensive list of it's features can be found below.

### Signing Up & Logging in

### The Main Screens
After logging in, you will see the *Home* screen. The **Home** screen houses a few things, starting from the top:	
+ There is a *Search* button at the top left which will bring you to a search screen where you can search for books to request.	
+ There is a *Profile* button at the top right which will bring you to your own profile page.	
+ The *Latest Books* section contains a swipeable area that shows some of the latest books to be added by people. Your books can even appear here. Pressing on these books will bring you to a new page that contains information about that book.	
+ Underneath that, the *Notifications* section tells you how many notifications you have and pressing on that area will bring you to a notifications screen.	
+ Then, there is the Quick Scan button. Pressing it will bring up a scanner for you to scan book ISBNs. It is primarily used when exchanging books.	
+ Lastly, at the very bottom of the screen is the navigation menu. Pressing the buttons on this menu allows you to go to and from the other Main screens as well as the Home screen.	

The **Bookshelf** screen will display all the books that you have added, as well as any books you are currently borrowing.	
+ There is a *Filter* button at the top right which will allow you to apply filters to help you view your bookshelf in the way you want.	
+ There is a circular *Add* button at the bottom right, pressing it will bring you to a new screen where you can add a book.	
+ Once you have added a book or have successfully borrowed a book, it will appear in your bookshelf and pressing on it will bring you to a page that contains information about that book.	

The **Requests** screen is a place for you to keep track of all the requests that you have received for your books, as well as all the requests you have sent for other books.	
+ There is a *Filter* button at the top right which will allow you to apply filters to help you view your requests in the way you want.	
+ Underneath that, there are two tab buttons for you to press to switch between received requests and sent requests.	
+ Once you have a request or have sent a request, they will appear in this screen and pressing on them will bring you to a new page that contains information about that request.

### Adding a book
To add a book a user can either scan or enter the isbn from the back of the book, this will automatically get all the book's information like the title, author, and description. The user can also enter this information manually if they choose and if any they make any mistakes can easily go back and edit the book. They can then either select an existing image on their device, or take a new image to attach to this book. 

### Searching for a Book
When you click on the *Search* Button in the *Home* screen, you will be directed to the *Search* screen. The *Search* screen houses a list of all the books that are available to borrow and the books that have been requested to borrow. This screen allows you to do the following: 

Search For a Book: 
+ To Search for a book, use the search bar at the top of the screen (below *BACK* button and *FILTER* button).
+ In this search bar, you can search for a book by its title or a keyword in its description. 

Apply Filters: 
+ You can apply filters to filter out books that are either *Available* or *Requested* to borrow. 
+ To do so, click on the *FILTER* button at the top right of the *Search* screen, and this will display the filters that you can apply to the list of books. 
+ Select the *Available* chip to look for books that are Available, or select the *Requested* chip to look for books that have been requested to borrow. 
+ Not selecting a chip or deselecting a chip will display a list of both *Available* and *Requested* books. 
+ to hide the filter chips, you can click on the *HIDE* button which is now present instead of the *FILTER* button.

Search with Applied Filters: 
+ You can perform the above two tasks in combination i.e. you can apply filters and search for books within the applied filter. 

The list displays the title of the book, its author, its current status, and the owner of the book. You can click on any book in the list to view the book, request to borrow it, or edit its information if the book belongs to you. 

### Requesting and Exchanging a Book
To request a book a user needs to click on a book that is not theirs, either by searching or from the "Latest Books" section on the home page. If the book has not been accepted or borrowed yet, the user can scroll down to the bottom of the page and hit the "Request Book" button to request the book. For exchanging a book, either the owner of the book needs to accept the request or the borrower needs to request to return the book. After selecting a location and confirming, both parties will meet at the specified location with the book and both parties need to scan the book using the "Quick Scan" function on the home page in order to exchange the book and update the status of the book in the app.

