# Implementation Plan - Project Cleanup

The goal is to remove unnecessary files and redundant code to keep the project clean and focused on the Address Book functionality.

## Proposed Changes

### [Component] File Cleanup

#### [DELETE] [MainActivity.java](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/java/com/example/lab22023_3_60_051/MainActivity.java)
- This is an empty activity template that is not used.

#### [DELETE] [activity_main.xml](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/res/layout/activity_main.xml)
- The layout for the unused `MainActivity`.

#### [DELETE] [activity_exam_practice.xml](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/res/layout/activity_exam_practice.xml)
- A redundant layout file with no associated code.

### [Component] Manifest Cleanup

#### [MODIFY] [AndroidManifest.xml](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/AndroidManifest.xml)
- Remove the registration for `.MainActivity`.
- Remove the registration for `.Exam_Practice`.

### [Component] Database Cleanup

#### [MODIFY] [EventDB.java](file:///F:/EWU/CSE489/Lab/Lab22023360051/app/src/main/java/com/example/lab22023_3_60_051/EventDB.java)
- Remove code that creates and drops the `events` table.
- Remove unused methods: `insertEvent`, `updateEvent`, `deleteEvent`, and `selectEvents`.

## Verification Plan

### Automated Tests
- Ensure the project builds successfully after deletions.

### Manual Verification
- Run the app to confirm it still starts correctly from `SignUp_Activity` and that all Address Book features remain functional.
