# PROG7313_PART2_CashaNova_FINAL3

# YOUTUBE PRESENTATION VIDEO LINK: https://youtu.be/fuGBYUh58es
# 1) Repository Strategy and Branching Decision

Our submission repository (PROG7313_PART2_CashaNova_FINAL3) is the third time that our group has attempted to complete part 2 of this assignment. This is not by coincidence; it is the work of two previous attempts at creating a repository that have not succeeded due to integration issues that failed to build successfully in the final product.
In our Part 1 and Part 2 development we worked in our own branches. However, when we went to merge and integrate the work of each group member, we encountered:
- Merge conflicts in various files
- Dependency issues (especially ViewModels, navigation and RoomDB)
- App crashes on run-time on integrated components
-  Effects of unstable and unusable code

After a few tries to resolve the issues, we felt the integration issues with multiple merges were not consistent and we would not have had a completed project in the required time.

# Current Repository Structure:
In order to submit an application that was fully functional, testable and rubric ready, we chose the following structure:
- Main Branch
This branch with the tested and full working version of the application lives.
It meets the assignment requirements and has no bugs
- Individual Member Branches
Members have contributed code to the repo in their own branches
These branches are transparent and public proof of member activity
# Our branches are based off the main branch therefore (the final project is present in each persons branch). Each person will add their own code files based off what they did through commits.

- Shared Development Branch
A common branch where all shared code files per each member are.
Also the repository has been set to public allowing the lecturer to:

- Review all branches
- Inspect commit histories
- Verify individual contributions

There will only be one commit on the main branch, and it is the final project on that branch.

This was done to:
- Avoid re-introducing fixes for merge conflicts
- Ensure the app is functional as planned for review

As a group, we had a vote and passed the resolution:
It is more academically correct to submit a working (tested) application to GitHub than an application with merge conflicts and a messed up commit history.

We explicitly acknowledge that:
This may also come with a GitHub penalty (±5%).
But we could get a lower overall mark if we submit a system that doesn't work

This project has been collaboratively worked on, as demonstrated by:
- Regular group discussions
- In-person collaboration sessions:
- On campus
- At the group leader's home where the full assignment was worked on
- Bug sessions, testing and user interface tweaks
- Discussion of design and integration

Each member has:
- Contributed independently
- Collaborated as a team to integrate and test
- Committed their work to the repository

All group members' work can be easily accessed
This repository, therefore, is functionally complete, and transparent

=======================================

# 2) UI Changes from Part 1 Mockups
The mockups created in Part 1 were to give a visual representation of the design, navigation and features of the Cashanova app. For Part 2, it was required to implement the mockups as an Android app in Android Studio using Kotlin, Jetpack Compose, RoomDB, ViewModels and navigation. So a few screens had to be re-designed to improve the user experience, reduce bugs, connect to the database and adhere to the Part 2 rubric. The team went for a functional, stable and testable app, rather than an app that looks exactly like the mockup. Some features of the mockup were not implemented as these will be in Part 3.

# Page-by-page differences
 Login Page
•	There is a simpler login page.
•	There are fewer graphics, gradients and special effects to improve the performance and remove any potential problems.
•	We retained the key function of the login process: entering login information, authentication and going to the dashboard.

Registration Page
•	In the mockup, we found a few fields with some information such as Full name, surname, phone number, date of birth, email and password fields.
•	In the final version, we included fields which are required to register and save user in RoomDB and flags are removed.
•	Additional fields for profile are reduced to minimum as it is not needed for Part 2.

Dashboard Page
•	The mockup contained complex graphs, expenditure graphs and visualisation of budget.
•	The actual dashboard is more user-friendly and has quick navigation.
•	All the complex graph and analyses were not implemented as they are better for Part 3.

Budget Page
•	The mockup had a good monthly budget page with budget limits per category, budget progress and budget goals.
•	The version at the end was modified to show the budget navigation and simple budget display.
•	A few of the progress bars remained to keep Part 2 simple.

Set Total Budget Goal Page
•	With sliders, income calculations, checkboxes, cards for budget overview.
•	These may not be in the final design.
•	These calculations have more complex financial calculations and will be created in Part 3.

Category Limits Page
•	Mockup had category limits with +/- and progress bars.
•	The actual implementation may not be as complicated for categories display/management.
•	Category limits as not yet implemented as Part 2 is focused on the key features.

Create Category Page
•	Icon selection and colour selection was included in the mockup.
•	Fortunately, this was done on the category create page.

Create Expense Entry Page
•	The mockups page design had category drop downs, date, amount, description and receipt upload.
•	The actual page contains the minimal details of the expense to be stored and viewed.
•	Upload receipt works for Part 2 but will be  more complex and appropriate in Part 3.

Recent Expenses Page
•	Mockup displays summaries of spending, monthly and category summaries.
•	Focuses mainly on showing expenses and the photo uploaded with it.
•	Search, summaries and export is postponed till Part 3.

Search / Filter Page
•	Mockup had text search, date range, category and amount range.
•	The final version may not contain all that was shown in the initial mockups but we implemented it.
•	The filter functionality is not developed in Part 2 and can be done in Part 3.

Bottom Navigation Bar
•	The mockup bottom navigation bar was designed with icons.
•	The final version has more Jetpack Compose suitable navigation.
•	Labels, icons and spacing may vary but the functionality remains the same: to be able to navigate to the main part of the app.


# User Flow (End-to-End Workflow of our CASHANOVA APP)

Application Launch
•	User launches the Cashanova application.
•	Checks if the user is logged in.
•	If not logged in → user is presented with the *Login Screen*.
•	If already logged in → user is taken to the *Dashboard*.

Login Screen
-	 User enters:
•	Username
•	Password

If details are correct:
-	User is logged in
-	Redirected to the *Dashboard*
-	If details are incorrect:
    An error message is displayed

  Option available:
   Register Screen if user does not have an account

Register Screen
User enters his/her details to create a new account
On successful registration:
•	Account is created
•	User is automatically logged in
•	Redirected to the *Dashboard*

---

Dashboard (Main Hub)
•	This is the initial screen when logging in
•	Gives user an overview of the application with quick actions.
•	Navigation hub of the application

  * User can move to:
•	Budget section
•	Expenses section
•	Search functionality
•	Create categories

---

Budget Section Flow
•	User selects *Budget*
•	Can view their budget overview

Set Total Budget Goal
•	Enters the monthly budget goal
•	This is their total budget

  Category Limits
•	View categories of expense
•	User can specify a limit for each category (food, transport, etc). MAINLY FOR PART 3

  Create Category
•	Can create new categories
•	Helps better organise their expenses

---

Expenses Section Flow
-	User selects *Expenses*
•	Can see a list of expenses
Create Expense
User can add an expense by entering:
•	Amount
•	Category
•	Description
•	Date (start-end)
•	*Expense is added and displayed on the list*
---

Search / Filter Flow
•	Click on *Search*
•	May enter keywords to search for expenses
•	Results are shown
•	Assists with finding transactions

---

# Navigation Experience
-  Bottom navigation bar is there.
- Allows access to:

-	Dashboard
•	Budget
•	Expenses
•	Search
---

Session Behaviour
If the user is logged in, they will remain logged in if they close the app
-	When the user logs back in:
•	User will be automatically returned to the Dashboard
User can choose to logout:
•	Session is cleared
•	User is taken back to the Login Screen

---

# In summary, a user can :
•	Open app → Login/Register
•	Access Dashboard

•	Navigate to:
•	Budget → create goals and restraints
•	Expenses → add and view expenses
•	Search → search for records
•	Navigate using navigation bar
