==========================================
  Smart Hospital Queue Management System 
==========================================

REQUIREMENTS
------------
- Java JDK 21 or higher
- JavaFX SDK 25.0.2 (https://gluonhq.com/products/javafx/)
- Eclipse IDE
  
------------------------------------------------------------

BEFORE YOU START 
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
  ID: P001  Password: john1234   (John Doe)
  ID: P002  Password: jane1234   (Jane Doe)
  ID: P003  Password: bruno1234  (Bruno Mars)
  ID: P004  Password: iron1234   (Iron Man)
  ID: P005  Password: thor1234   (Thor Odinson)
  ID: P006  Password: boris1234  (Boris Mason)

  STAFF
  -----
  ID: D123  Password: abcde12345  (Dr. Yu — Doctor)
  ID: F123  Password: qwer1234    (David Robinson — Front Desk)

------------------------------------------------------------

DEMO FLOW
---------
0. On launch, 3 login windows open side by side (Login-1, Login-2, Login-3)
   — use one for Patient, one for Doctor, one for Front Desk simultaneously
   
1. Log in as Doctor → select a patient → begin consultation
   → fill in complaint & diagnosis → go to Wrap Up tab
   → select scan type & priority → Send to Front Desk

2. Log in as Front Desk → view incoming scan requests
   → select a request → Book Appointment

3. Log in as Patient (e.g. P001) → check notification from doctor
   → Book Appointment → select available slot → Confirm

------------------------------------------------------------
