import os
import sys
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, KeepTogether, HRFlowable
)
from reportlab.pdfgen import canvas

class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_decorations(num_pages)
            super().showPage()
        super().save()

    def draw_page_decorations(self, page_count):
        self.saveState()
        self.setFont("Helvetica", 9)
        self.setFillColor(colors.HexColor("#555555"))
        
        # Header (Only on page 2 and later)
        if self._pageNumber > 1:
            self.drawString(54, 750, "MyLogBook - Android App Viva Exam Defense & Complete Code Guide")
            self.setStrokeColor(colors.HexColor("#DDDDDD"))
            self.setLineWidth(0.5)
            self.line(54, 742, 558, 742)
            
        # Footer (All pages)
        page_str = f"Page {self._pageNumber} of {page_count}"
        self.drawRightString(558, 36, page_str)
        self.drawString(54, 36, "CSE489: Mobile Application Development | East West University")
        self.setStrokeColor(colors.HexColor("#DDDDDD"))
        self.setLineWidth(0.5)
        self.line(54, 48, 558, 48)
        
        self.restoreState()

def create_pdf(filename):
    doc = SimpleDocTemplate(
        filename,
        pagesize=letter,
        leftMargin=54,
        rightMargin=54,
        topMargin=54,
        bottomMargin=54
    )
    
    styles = getSampleStyleSheet()
    
    # Custom Palette
    PRIMARY = colors.HexColor("#003366")      # Deep Navy
    SECONDARY = colors.HexColor("#0052D4")    # Vibrant Blue
    ACCENT = colors.HexColor("#D9534F")       # Red accent
    DARK_TEXT = colors.HexColor("#222222")    # Main Text
    LIGHT_BG = colors.HexColor("#F8F9FA")     # Code/Table background
    BORDER_COLOR = colors.HexColor("#E2E8F0") # Soft Border
    HIGHLIGHT_BG = colors.HexColor("#FEF3C7") # Warm Yellow Highlight
    SUCCESS_BG = colors.HexColor("#ECFDF5")   # Soft Green Highlight

    # Styles
    title_style = ParagraphStyle(
        'DocTitle',
        parent=styles['Title'],
        fontName='Helvetica-Bold',
        fontSize=22,
        leading=26,
        textColor=PRIMARY,
        alignment=0,
        spaceAfter=4
    )
    
    subtitle_style = ParagraphStyle(
        'DocSubTitle',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=11,
        leading=15,
        textColor=SECONDARY,
        spaceAfter=12
    )

    h1_style = ParagraphStyle(
        'H1',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=14,
        leading=18,
        textColor=PRIMARY,
        spaceBefore=12,
        spaceAfter=6,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'H2',
        parent=styles['Heading2'],
        fontName='Helvetica-Bold',
        fontSize=11,
        leading=15,
        textColor=SECONDARY,
        spaceBefore=8,
        spaceAfter=4,
        keepWithNext=True
    )

    h3_style = ParagraphStyle(
        'H3',
        parent=styles['Heading3'],
        fontName='Helvetica-Bold',
        fontSize=10,
        leading=13,
        textColor=colors.HexColor("#1A202C"),
        spaceBefore=6,
        spaceAfter=3,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'Body',
        parent=styles['BodyText'],
        fontName='Helvetica',
        fontSize=9,
        leading=13,
        textColor=DARK_TEXT,
        spaceAfter=5
    )

    bullet_style = ParagraphStyle(
        'Bullet',
        parent=body_style,
        leftIndent=12,
        firstLineIndent=-8,
        spaceAfter=3
    )

    code_style = ParagraphStyle(
        'CodeStyle',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=8,
        leading=10.5,
        textColor=colors.HexColor("#1E293B"),
        backColor=LIGHT_BG,
        borderColor=BORDER_COLOR,
        borderWidth=0.5,
        borderPadding=5,
        spaceBefore=3,
        spaceAfter=5
    )

    badge_style = ParagraphStyle(
        'Badge',
        parent=styles['Normal'],
        fontName='Courier-Bold',
        fontSize=8,
        leading=10,
        textColor=colors.HexColor("#0F766E")
    )

    story = []

    # Title Header Banner
    story.append(Paragraph("📱 MyLogBook - Comprehensive Viva Defense & Code Guide", title_style))
    story.append(Paragraph("<b>Course</b>: CSE489 Mobile Application Development &nbsp;|&nbsp; <b>Institution</b>: East West University<br/><b>Student ID</b>: 2023-3-60-051 &nbsp;|&nbsp; <b>Tech Stack</b>: Java, Android SDK, SQLite, XML", subtitle_style))
    story.append(HRFlowable(width="100%", thickness=1.5, color=PRIMARY, spaceAfter=10))

    # --- SECTION 1: ARCHITECTURE & OVERVIEW ---
    story.append(Paragraph("1. High-Level Architecture & Execution Flow", h1_style))
    story.append(Paragraph(
        "<b>MyLogBook</b> is a multi-user contact management application built with <b>Java</b>, <b>SQLite</b>, and <b>Android SDK</b>. "
        "It features user registration, authentication, session persistence via <code>SharedPreferences</code>, "
        "and multi-user isolated CRUD contact operations.",
        body_style
    ))
    
    story.append(Paragraph("<b>App Execution Sequence:</b>", h2_style))
    story.append(Paragraph("1. <b>Manifest Initialization</b>: Operating System opens <code>AndroidManifest.xml</code> and starts <b>SignUp_Activity</b> (launcher activity).", bullet_style))
    story.append(Paragraph("2. <b>Auto-Login Check</b>: In <code>onCreate()</code> of <b>SignUp_Activity</b>, <code>SharedPreferences</code> ('UserPrefs') is read. If <code>REM-LOGIN</code> is true, it immediately routes to <b>AddressBook_Activity</b>.", bullet_style))
    story.append(Paragraph("3. <b>User Registration & DB Insert</b>: Unregistered users enter details in <b>SignUp_Activity</b>. Upon validation, <b>EventDB</b> (SQLite) inserts the user into the <code>users</code> table.", bullet_style))
    story.append(Paragraph("4. <b>Login Verification</b>: Registered users log in through <b>LoginPage_Activity</b>, which queries <b>EventDB</b> (`getUser`) and compares passwords.", bullet_style))
    story.append(Paragraph("5. <b>List View Rendering</b>: <b>AddressBook_Activity</b> queries contacts for `owner_id = currentUserId` and passes them to <b>ActivityAdapter</b> to render inside `lvAddressBook` using <b>row_contact.xml</b>.", bullet_style))
    story.append(Paragraph("6. <b>CRUD Actions</b>: Single-click on contact opens <b>AddressDetails_Activity</b> for editing. Long-press triggers an <code>AlertDialog</code> to delete from SQLite.", bullet_style))

    story.append(Spacer(1, 6))

    # --- SECTION 2: XML FILES & LAYOUTS ---
    story.append(Paragraph("2. XML UI Layouts & Manifest Reference", h1_style))
    
    xml_table_data = [
        [Paragraph("<b>XML File Path</b>", h3_style), Paragraph("<b>Type</b>", h3_style), Paragraph("<b>Components & Purpose</b>", h3_style)],
        [
            Paragraph("<code>AndroidManifest.xml</code>", badge_style),
            Paragraph("Manifest", body_style),
            Paragraph("Declares all 4 activities. Sets <code>SignUp_Activity</code> with <code>MAIN</code> and <code>LAUNCHER</code> intent-filter.", body_style)
        ],
        [
            Paragraph("<code>activity_sign_up.xml</code>", badge_style),
            Paragraph("Layout", body_style),
            Paragraph("User registration UI. Includes <code>EditText</code>s for User ID, Name, Email, Phone, Passwords, Checkboxes, and Action Buttons.", body_style)
        ],
        [
            Paragraph("<code>activity_login_page.xml</code>", badge_style),
            Paragraph("Layout", body_style),
            Paragraph("Sign-in UI. Contains User ID & Password fields, <code>cbRememberUser</code>, <code>cbRememberLogin</code>, Exit, and Sign In buttons.", body_style)
        ],
        [
            Paragraph("<code>activity_address_book.xml</code>", badge_style),
            Paragraph("Layout", body_style),
            Paragraph("Main contact list screen. Houses <code>ListView</code> (<code>lvAddressBook</code>), Log Out button, and Add New Contact button.", body_style)
        ],
        [
            Paragraph("<code>activity_address_details.xml</code>", badge_style),
            Paragraph("Layout", body_style),
            Paragraph("Contact entry/edit form. Features clickable profile <code>ImageView</code>, fields for contact details, Save and Cancel buttons.", body_style)
        ],
        [
            Paragraph("<code>row_contact.xml</code>", badge_style),
            Paragraph("Layout", body_style),
            Paragraph("Single row template inside <code>ListView</code>. Contains <code>ivProfileIcon</code>, <code>tvName</code>, <code>tvPhone</code>, and <code>tvDob</code>.", body_style)
        ]
    ]

    t_xml = Table(xml_table_data, colWidths=[130, 55, 319])
    t_xml.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor("#E2E8F0")),
        ('GRID', (0,0), (-1,-1), 0.5, BORDER_COLOR),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
        ('TOPPADDING', (0,0), (-1,-1), 4),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t_xml)

    story.append(Spacer(1, 6))

    # --- SECTION 3: VIVA DEFENSE - PURPOSE & USE OF CONCEPTS ---
    story.append(Paragraph("3. Viva Exam Master Guide: Purpose & Use of Every Concept", h1_style))
    story.append(Paragraph(
        "<b>This section directly answers the core viva question: 'What is the USE of X in your code and WHY did you use it?'</b>",
        ParagraphStyle('WarnText', parent=body_style, textColor=colors.HexColor("#B45309"), fontName='Helvetica-Bold')
    ))

    viva_uses = [
        ("SharedPreferences ('UserPrefs')",
         "<b>Use:</b> Lightweight key-value XML storage saved on disk.<br/>"
         "<b>Why we used it:</b> Stores session flags (`REM-LOGIN`, `REM-USER`) and `LAST-USER-ID` so users don't have to log in every time the app opens."),

        ("SQLiteOpenHelper (EventDB)",
         "<b>Use:</b> Abstract helper class managing SQLite database creation (`onCreate`) and version migration (`onUpgrade`).<br/>"
         "<b>Why we used it:</b> Handles local relational database persistence (`EventDB.db`), isolating user contacts with foreign key `owner_id`."),

        ("ContentValues",
         "<b>Use:</b> A key-value map object tailored for SQLite table operations.<br/>"
         "<b>Why we used it:</b> Used in `insertUser()`, `insertContact()`, and `updateContact()` to map column names to values before executing SQL queries safely."),

        ("Cursor",
         "<b>Use:</b> A pointer object that iterates over query results returned by `db.rawQuery()` or `SELECT` statement.<br/>"
         "<b>Why we used it:</b> Used in `loadContacts()` (`cursor.moveToNext()`) to read contact columns from SQLite and build Java `Contact` objects."),

        ("CONFLICT_REPLACE",
         "<b>Use:</b> An SQL conflict resolution strategy.<br/>"
         "<b>Why we used it:</b> When `insertContact()` or `insertUser()` encounters an existing primary key, SQLite replaces the existing row instead of throwing a crash exception."),

        ("ArrayAdapter & ActivityAdapter",
         "<b>Use:</b> Adapter pattern connecting an array list of data objects (`ArrayList<Contact>`) to a visual list container (`ListView`).<br/>"
         "<b>Why we used it:</b> `ActivityAdapter` overrides `getView()` to populate custom row template (`row_contact.xml`) with dynamic data."),

        ("LayoutInflater",
         "<b>Use:</b> Converts an XML layout file into an active view object hierarchy in Java memory.<br/>"
         "<b>Why we used it:</b> Inside `getView()`, `inflater.inflate(R.layout.row_contact, parent, false)` instantiates the XML row design for each list item."),

        ("convertView (View Recycling)",
         "<b>Use:</b> Recycles view objects that have scrolled off-screen.<br/>"
         "<b>Why we used it:</b> Checking `if (convertView == null)` avoids inflating new XML layouts repeatedly, conserving system memory and ensuring smooth scrolling performance."),

        ("notifyDataSetChanged()",
         "<b>Use:</b> Triggers the `ListView` and `Adapter` to redraw the list UI.<br/>"
         "<b>Why we used it:</b> Called after adding, updating, or deleting a contact so changes appear instantly on screen without restarting the activity."),

        ("Intent & Extra Parameters",
         "<b>Use:</b> Messaging object used to transition between screens (`startActivity`) and pass payload data (`putExtra` / `getExtra`).<br/>"
         "<b>Why we used it:</b> Passes logged-in `USER-ID` between activities and passes `CONTACT_EMAIL` to `AddressDetails_Activity` for editing."),

        ("finish() vs finishAffinity()",
         "<b>Use:</b> `finish()` closes the current activity. `finishAffinity()` closes the current activity AND all parent activities in the stack.<br/>"
         "<b>Why we used it:</b> `finishAffinity()` is called after login/signup so pressing the hardware Back button exits the app rather than going back to login."),

        ("onResume() Activity Lifecycle",
         "<b>Use:</b> Callback method invoked whenever an Activity comes into the foreground.<br/>"
         "<b>Why we used it:</b> In `AddressBook_Activity`, `onResume()` re-executes `loadContacts()` so newly added or edited contacts appear immediately upon returning."),

        ("AlertDialog.Builder",
         "<b>Use:</b> Builds a modal popup dialog requesting user confirmation.<br/>"
         "<b>Why we used it:</b> Displays a confirmation dialog on long-pressing a contact: 'Are you sure you want to delete [Name]?' to prevent accidental deletion."),

        ("ActivityResultLauncher & GetContent()",
         "<b>Use:</b> Modern API for launching system activities for result (image picking).<br/>"
         "<b>Why we used it:</b> Replaces deprecated `startActivityForResult()`, allowing users to select profile images safely from gallery."),

        ("takePersistableUriPermission()",
         "<b>Use:</b> Retains read access permission for a device image URI across device reboots.<br/>"
         "<b>Why we used it:</b> Ensures selected gallery image URIs can still be displayed when the app is reopened later."),

        ("etEmail.setEnabled(false)",
         "<b>Use:</b> Disables user interaction on an `EditText` field.<br/>"
         "<b>Why we used it:</b> Freezes the Email input during Contact Edit Mode because Email forms part of the SQLite composite primary key.")
    ]

    for title, desc in viva_uses:
        story.append(Paragraph(f"• <b>{title}</b>:<br/>{desc}", bullet_style))

    story.append(PageBreak())

    # --- SECTION 4: JAVA CLASSES & METHODS DETAILED BREAKDOWN ---
    story.append(Paragraph("4. Java Code, Classes & Method Breakdown", h1_style))

    # 4.1 Contact.java
    story.append(Paragraph("4.1 Data Model: Contact.java", h2_style))
    story.append(Paragraph("Data Transfer Object (DTO) encapsulating contact attributes.", body_style))
    contact_code = (
        "public class Contact {\n"
        "    String id, name, email, phone, dob, presentAddress, permanentAddress, imageUri;\n\n"
        "    public Contact(String id, String name, String email, String phone, String dob,\n"
        "                   String presentAddress, String permanentAddress, String imageUri) {\n"
        "        this.id = id; this.name = name; this.email = email; this.phone = phone;\n"
        "        this.dob = dob; this.presentAddress = presentAddress;\n"
        "        this.permanentAddress = permanentAddress; this.imageUri = imageUri;\n"
        "    }\n"
        "}"
    )
    story.append(Paragraph(contact_code.replace("\n", "<br/>").replace(" ", "&nbsp;"), code_style))

    story.append(Spacer(1, 4))

    # 4.2 EventDB.java
    story.append(Paragraph("4.2 Database Helper: EventDB.java", h2_style))
    story.append(Paragraph("<b>Extends</b>: <code>SQLiteOpenHelper</code> | DB Name: <code>EventDB.db</code> | Version: <code>3</code>", body_style))
    
    eventdb_methods = [
        ("EventDB(Context context)", "Constructor initializing super class with DB name 'EventDB.db' and version 3."),
        ("onCreate(SQLiteDatabase db)", "Runs `db.execSQL()` to create `users` (primary key `userId`) and `contacts` (composite primary key `email, owner_id`)."),
        ("onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)", "Drops existing tables on database version changes and calls `onCreate(db)`."),
        ("insertUser(userId, userName, email, phone, password)", "Packs credentials into `ContentValues` and calls `db.insertWithOnConflict(\"users\", null, cols, CONFLICT_REPLACE)`."),
        ("getUser(String userId)", "Executes `SELECT * FROM users WHERE userId=?` returning a query `Cursor`."),
        ("insertContact(ownerId, name, email, phone, dob, presentAddress, permanentAddress, imageUri)", "Inserts contact record mapped to `owner_id` into `contacts` table using `CONFLICT_REPLACE`."),
        ("updateContact(ownerId, name, email, phone, dob, presentAddress, permanentAddress, imageUri)", "Updates existing contact matching `email=? AND owner_id=?` using `db.update(...)`."),
        ("deleteContact(ownerId, email)", "Deletes contact matching `email=? AND owner_id=?` using `db.delete(...)`."),
        ("getAllContacts(ownerId)", "Executes `SELECT * FROM contacts WHERE owner_id=?` returning `Cursor` of contacts for current user.")
    ]

    for m_name, m_desc in eventdb_methods:
        story.append(Paragraph(f"• <b><code>{m_name}</code></b>:<br/>{m_desc}", bullet_style))

    story.append(Spacer(1, 4))

    # 4.3 ActivityAdapter.java
    story.append(Paragraph("4.3 Custom Adapter: ActivityAdapter.java", h2_style))
    story.append(Paragraph("<b>Extends</b>: <code>ArrayAdapter&lt;Contact&gt;</code>", body_style))
    
    adapter_code = (
        "@Override\n"
        "public View getView(int position, View convertView, ViewGroup parent) {\n"
        "    if (convertView == null) {\n"
        "        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);\n"
        "        convertView = inflater.inflate(R.layout.row_contact, parent, false);\n"
        "    }\n"
        "    TextView tvName = convertView.findViewById(R.id.tvName);\n"
        "    TextView tvPhone = convertView.findViewById(R.id.tvPhone);\n"
        "    TextView tvDob = convertView.findViewById(R.id.tvDob);\n"
        "    ImageView ivProfile = convertView.findViewById(R.id.ivProfileIcon);\n\n"
        "    Contact c = values.get(position);\n"
        "    tvName.setText(c.name);\n"
        "    tvPhone.setText(c.phone);\n"
        "    tvDob.setText(c.dob);\n\n"
        "    if (c.imageUri != null) {\n"
        "        ivProfile.setImageURI(Uri.parse(c.imageUri));\n"
        "        ivProfile.setPadding(0, 0, 0, 0);\n"
        "        ivProfile.setImageTintList(null);\n"
        "    } else {\n"
        "        ivProfile.setImageResource(R.drawable.ic_person);\n"
        "        ivProfile.setImageTintList(ColorStateList.valueOf(0xFF0052D4));\n"
        "    }\n"
        "    return convertView;\n"
        "}"
    )
    story.append(Paragraph(adapter_code.replace("\n", "<br/>").replace(" ", "&nbsp;"), code_style))

    story.append(Spacer(1, 4))

    # 4.4 SignUp_Activity.java
    story.append(Paragraph("4.4 Registration Activity: SignUp_Activity.java", h2_style))
    signup_methods = [
        ("onCreate(Bundle savedInstanceState)", "Checks `UserPrefs` SharedPreferences. If `REM-LOGIN` is true, auto-redirects directly to `AddressBook_Activity`. Otherwise inflates UI and sets click listeners."),
        ("accessFieldsData()", "Validates input constraints (User ID 4-6 digits, Username 4-20 chars, regex email match, Phone 11-14 digits, Password match min 6 chars). Inserts user into SQLite via `db.insertUser()`, updates `SharedPreferences`, and calls `finishAffinity()`."),
        ("isValidEmail(String email)", "Uses `android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()`.")
    ]
    for m_name, m_desc in signup_methods:
        story.append(Paragraph(f"• <b><code>{m_name}</code></b>:<br/>{m_desc}", bullet_style))

    story.append(PageBreak())

    # 4.5 LoginPage_Activity.java
    story.append(Paragraph("4.5 Authentication Activity: LoginPage_Activity.java", h2_style))
    login_methods = [
        ("onCreate(Bundle savedInstanceState)", "Pre-fills User ID if `REM-USER` was previously saved in preferences. Binds button listeners for Exit, Sign In (Go), and Register."),
        ("accessFieldsData()", "Validates inputs, queries SQLite (`db.getUser()`), verifies password match, updates `SharedPreferences` session, and routes to `AddressBook_Activity` with `USER-ID` extra.")
    ]
    for m_name, m_desc in login_methods:
        story.append(Paragraph(f"• <b><code>{m_name}</code></b>:<br/>{m_desc}", bullet_style))

    story.append(Spacer(1, 4))

    # 4.6 AddressBook_Activity.java
    story.append(Paragraph("4.6 Dashboard Activity: AddressBook_Activity.java", h2_style))
    addressbook_methods = [
        ("onCreate(Bundle savedInstanceState)", "Retrieves `USER-ID`, attaches `ActivityAdapter` to `lvAddressBook`, and configures Log Out button (resets `REM-LOGIN` flag)."),
        ("setOnItemClickListener(...)", "Item click handler: Extracts contact email and opens `AddressDetails_Activity` passing `CONTACT_EMAIL` for editing."),
        ("setOnItemLongClickListener(...)", "Item long-click handler: Shows `AlertDialog` asking confirmation. If confirmed, deletes contact via `db.deleteContact()`, removes item from array, and calls `adapter.notifyDataSetChanged()`."),
        ("onResume() & loadContacts()", "Callback executed when returning to screen. Clears array, queries SQLite for current user's contacts (`db.getAllContacts(currentUserId)`), populates array, and calls `notifyDataSetChanged()`.")
    ]
    for m_name, m_desc in addressbook_methods:
        story.append(Paragraph(f"• <b><code>{m_name}</code></b>:<br/>{m_desc}", bullet_style))

    story.append(Spacer(1, 4))

    # 4.7 AddressDetails_Activity.java
    story.append(Paragraph("4.7 Contact Form Activity: AddressDetails_Activity.java", h2_style))
    details_methods = [
        ("imagePickerLauncher", "Registers `ActivityResultContracts.GetContent()` launcher to open gallery (`image/*`) and invokes `takePersistableUriPermission()`."),
        ("onCreate(Bundle savedInstanceState)", "Binds UI elements. If intent extra `CONTACT_EMAIL` exists, calls `loadContactData(existingEmail)`."),
        ("loadContactData(String email)", "Fetches contact details from SQLite, populates inputs, sets image URI, and calls `etEmail.setEnabled(false)` to freeze Primary Key editing."),
        ("accessFieldsData()", "Validates input lengths and email format. Executes `db.updateContact()` if editing, or `db.insertContact()` if new, shows Toast, and finishes activity.")
    ]
    for m_name, m_desc in details_methods:
        story.append(Paragraph(f"• <b><code>{m_name}</code></b>:<br/>{m_desc}", bullet_style))

    story.append(Spacer(1, 10))

    # --- SECTION 5: TOP VIVA EXAM QUESTIONS & SAMPLE ANSWERS ---
    story.append(Paragraph("5. Top 10 Viva Exam Questions & Model Answers", h1_style))

    viva_qna = [
        ("Q1: What is the main entry point of your Android App?",
         "Answer: `SignUp_Activity` is declared as the launch activity in `AndroidManifest.xml` with `<intent-filter>` containing `MAIN` and `LAUNCHER`."),

        ("Q2: How does the application implement user session persistence?",
         "Answer: Through `SharedPreferences` ('UserPrefs'). `REM-LOGIN` boolean remembers if a user is logged in (auto-bypassing login on app start), while `REM-USER` remembers the last entered User ID."),

        ("Q3: How does the app ensure multi-tenant security/privacy between different users?",
         "Answer: The `contacts` table in SQLite includes an `owner_id` column matching the logged-in user's `userId`. Queries use `WHERE owner_id=?` so users only retrieve their own contacts."),

        ("Q4: Explain the difference between SQLite insert and update in your code.",
         "Answer: `insertContact()` creates a new row using `CONFLICT_REPLACE`. `updateContact()` modifies an existing row matching `WHERE email=? AND owner_id=?` without changing the primary key email."),

        ("Q5: Why do we call notifyDataSetChanged() on the Adapter?",
         "Answer: It notifies the ListView that underlying `ArrayList<Contact>` data has changed (add/edit/delete), forcing the ListView to redraw its child views immediately."),

        ("Q6: Why is convertView checked for null inside ActivityAdapter.getView()?",
         "Answer: `convertView` recycling reuses row views that scroll off-screen. Checking `if (convertView == null)` prevents repeatedly inflating `row_contact.xml`, saving memory."),

        ("Q7: What is the purpose of finishAffinity() in SignUp and Login activities?",
         "Answer: It closes all activities in the task stack so pressing the hardware Back button on the Address Book screen exits the app instead of returning to login/signup."),

        ("Q8: Why is etEmail.setEnabled(false) called when editing a contact?",
         "Answer: Email forms part of the composite primary key in the SQLite database (`email, owner_id`). Disabling the field prevents altering the identifier used to locate the record."),

        ("Q9: What is the purpose of Cursor.moveToNext() in database methods?",
         "Answer: It advances the database cursor to the next row in the query result set. It returns `true` if a row exists, driving the `while` loop to extract column data."),

        ("Q10: How does image selection work in AddressDetails_Activity?",
         "Answer: Uses `ActivityResultLauncher` with `GetContent()` contract to pick an image URI from gallery, displays it via `ivProfile.setImageURI(uri)`, and calls `takePersistableUriPermission`.")
    ]

    for q, a in viva_qna:
        story.append(Paragraph(f"<b>{q}</b>", h3_style))
        story.append(Paragraph(a, body_style))

    doc.build(story, canvasmaker=NumberedCanvas)

if __name__ == '__main__':
    output_pdf = r"f:\EWU\CSE489\Lab\MyLogBook\MyLogBook_Code_Summary_Guide.pdf"
    create_pdf(output_pdf)
    print(f"PDF successfully updated at: {output_pdf}")
