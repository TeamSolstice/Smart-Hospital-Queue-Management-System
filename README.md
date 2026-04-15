# Smart Hospital Queue Management System


## Requirements
------------
- Java JDK 21 or higher
- JavaFX SDK 25.0.2 (https://gluonhq.com/products/javafx/)
- Eclipse IDE
  
------------------------------------------------------------

## BEFORE YOU START 
----------------

- SET FXML AS SOURCE FOLDER
   - Right-click the "fxml" folder in the project
   - Build Path → Use as Source Folder
   (This is required. Without this procedure the app will crash)


------------------------------------------------------------

DEMO ACCOUNTS INFO
------------------

  PATIENTS
  --------
  - ID: P001  Password: john1234   (John Doe)
  - ID: P002  Password: jane1234   (Jane Doe)
  - ID: P003  Password: bruno1234  (Bruno Mars)
  - ID: P004  Password: iron1234   (Iron Man)
  - ID: P005  Password: thor1234   (Thor Odinson)
  - ID: P006  Password: boris1234  (Boris Mason)

  STAFF
  -----
  - ID: D123  Password: abcde12345  (Dr. Yu — Doctor)
  - ID: F123  Password: qwer1234    (David Robinson — Front Desk)

------------------------------------------------------------

DEMO FLOW
---------
### Step 1 — Test Login Errors
- Leave fields empty → click Patient Login → `"Please enter both Patient ID and password."`
- Enter wrong ID (e.g. `AAA`) → `"Patient ID not found: AAA"`
- Enter correct ID but wrong password (e.g. 'P001', 'AAA')→ `"ID and Password does not match."`
- Same with Staff Login fields for staff error messages

### Step 2 — Log in as Doctor
- ID: `D123` / Password: `abcde12345`
- Patient list loads automatically (all 6 patients visible)

**Consultation flow:**
1. Select `P003 - Bruno Mars` → click **Begin Consultation**
2. Fill in Notes and Chief Complaint
3. Fill in Diagnosis
4. Switch to ""Wrap Up" tab
5. Set Scheduling Priority → Emergency
6. Set Test Required → True → scan toggle buttons activate
7. Select MRI
8. Fill in Follow Up Instructions
9. Click "Send to Front Desk"
   - Bruno Mars disappears from the patient list 
   - Notification is set on Bruno Mars's Patient account

**Repeat for a Regular patient:**
1. Select `P004 - Iron Man`
2. Same flow → Priority: Regular → scan: ULTRASOUND
3. Click "Send to Front Desk"

### Step 3 — Log in as Front Desk 
- ID: `F123` / Password: `qwer1234`
- Two scan requests visible: **EMERGENCY Bruno Mars** at the top, **REGULAR Iron Man** below
  - This demonstrates PriorityQueue — Emergency always comes first regardless of submission order

**Booking:**
 Select Bruno Mars's request → click **Book Appointment**
   - Appointment is created and added to Bruno Mars's appointment list
   - Slot is marked unavailable
   - Request is removed from the queue

### Step 4 — Log in as Patient 
- ID: `P003` / Password: `bruno1234`
- Notification area shows: `"Tests ordered: [MRI] | Follow-up: ..."`
- Book button shows `"Appointment already booked by front desk"` → locked
- My Appointments shows the MRI booking — CONFIRMED

**Appointment actions:**
1. Select the appointment → click **Mark Completed** → status changes to COMPLETED
2. Try **Cancel Appointment** on the completed one → `"Completed appointments cannot be cancelled."`
3. Click **Remove** → appointment is removed from the list

**Patient self-booking (use P004 - Iron Man):**
- Notification shows ULTRASOUND referral
- Click **Book Appointment** → slot list loads filtered to ULTRASOUND facilities only
- Select a slot → click **Confirm** → appointment added to My Appointments
- Go back → Refresh → appointment visible in dashboard

### Step 5 — Refresh behavior
- On Patient dashboard, click Refresh to pick up any new notifications or appointment updates from Doctor/Front Desk
