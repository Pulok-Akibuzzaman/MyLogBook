# Walkthrough - Migrate Address Book to SQLite

I have successfully migrated your Address Book from `SharedPreferences` to a `SQLite` database using the `EventDB` helper. I also fixed the errors in `ActivityAdapter` by creating a proper data model and adapter structure.

## Changes Made

### 1. Database Layer
- **[EventDB.java](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/java/com/example/lab22023_3_60_051/EventDB.java)**: Added a `contacts` table and implemented `insertContact`, `updateContact`, `deleteContact`, and `getAllContacts` methods.

### 2. Data Model & UI Resources
- **[Contact.java](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/java/com/example/lab22023_3_60_051/Contact.java)**: Created a new POJO to hold contact data.
- **[row_contact.xml](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/res/layout/row_contact.xml)**: Added a simple layout for contact list items showing Name, Phone, and Date of Birth.

### 3. UI & Adapter Fixes
- **[ActivityAdapter.java](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/java/com/example/lab22023_3_60_051/ActivityAdapter.java)**: Created a functional adapter for `Contact` objects (replacing the broken `CustomListAdapter.java`).
- **[AddressDetails_Activity.java](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/java/com/example/lab22023_3_60_051/AddressDetails_Activity.java)**: Switched from `SharedPreferences` to `EventDB`. It now supports both adding new contacts and editing existing ones (triggered by passing an email in the Intent).
- **[AddressBook_Activity.java](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/java/com/example/lab22023_3_60_051/AddressBook_Activity.java)**: Updated to fetch the full contact list from the database and pass the contact's email to the details screen when an item is clicked.

## Verification Results

- **Data Persistence**: Contacts are now stored in `EventDB.db` and persist across app restarts.
- **CRUD Operations**: The app supports Inserting and Updating contacts.
- **Code Health**: All "Cannot resolve symbol" errors in the adapter have been resolved by using the correct data model and layout.

> [!TIP]
> To delete a contact, you can use the `db.deleteContact(email)` method in `EventDB.java`. I haven't added a delete button to the UI to keep it simple as requested, but the functionality is ready in the database layer.
