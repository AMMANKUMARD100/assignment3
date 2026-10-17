# Undo-Redo Text Editor Simulation

A mini project simulating a text editor buffer supporting stack-based action recording with **Undo** and **Redo** capabilities.

## 📌 Features
- **Insert Action**: Appends text to document and pushes the operation to the `undo_stack`. Clears the `redo_stack`.
- **Delete Action**: Deletes the last $N$ characters and records the deleted substring onto the `undo_stack`. Clears the `redo_stack`.
- **Undo Feature (`undo()`)**:
  - Pops the last action from `undo_stack`.
  - Reverses the action (removes inserted text or restores deleted text).
  - Pushes the reversed action to `redo_stack`.
- **Redo Feature (`redo()`)**:
  - Pops the action from `redo_stack`.
  - Re-applies the action to the buffer.
  - Pushes the operation back to `undo_stack`.
- Implementations provided in both **Python** and **Java**.

---

## 🛠️ Data Structure & Stack Design
- `undo_stack`: LIFO stack storing executed actions `(action_type, payload)`.
- `redo_stack`: LIFO stack storing undone actions.
- Every new user action (Insert/Delete) invalidates and clears the `redo_stack`.

---

## 🚀 How to Run

### 1. Python (`text_editor.py`)
```bash
python text_editor.py
```

### 2. Java (`TextEditorSimulation.java`)
```bash
javac TextEditorSimulation.java
java TextEditorSimulation
```

---

## 💻 Sample Interaction
```text
==================================================
   UNDO-REDO TEXT EDITOR SIMULATION
==================================================
Commands:
  1 <text>  : Insert text (e.g. 1 Hello )
  2 <count> : Delete last N characters (e.g. 2 3)
  3         : Undo
  4         : Redo
  5         : Display current text
  6 / exit  : Exit
==================================================

Editor > 1 Hello World
-> Inserted: 'Hello World'
--------------------------------------------------
Current Document: "Hello World"
Undo Stack Depth: 1
Redo Stack Depth: 0
--------------------------------------------------

Editor > 2 5
-> Deleted last 5 character(s): 'World'
--------------------------------------------------
Current Document: "Hello "
Undo Stack Depth: 2
Redo Stack Depth: 0
--------------------------------------------------

Editor > 3
-> Undid DELETE: Restored 'World'
--------------------------------------------------
Current Document: "Hello World"
Undo Stack Depth: 1
Redo Stack Depth: 1
--------------------------------------------------

Editor > 4
-> Redid DELETE: Removed 'World'
--------------------------------------------------
Current Document: "Hello "
Undo Stack Depth: 2
Redo Stack Depth: 0
--------------------------------------------------
```
