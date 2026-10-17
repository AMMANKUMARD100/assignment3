"""
Undo-Redo Text Editor Simulation
---------------------------------
Features:
- Insert text
- Delete last N characters
- Stack-based undo() and redo() mechanisms
- Current buffer inspection and action history display
"""

from typing import List, Tuple

class TextEditor:
    def __init__(self):
        # Current editor text content
        self.content: str = ""
        # undo_stack stores actions as tuples: (action_type, payload)
        # action types: ("INSERT", text_appended) or ("DELETE", text_removed)
        self.undo_stack: List[Tuple[str, str]] = []
        # redo_stack stores undone actions that can be reapplied
        self.redo_stack: List[Tuple[str, str]] = []

    def insert(self, text: str) -> None:
        """Appends new text to the document and records it on undo stack."""
        if not text:
            return
        self.content += text
        self.undo_stack.append(("INSERT", text))
        # Clear redo stack upon performing a new forward operation
        self.redo_stack.clear()
        print(f"-> Inserted: '{text}'")

    def delete(self, count: int) -> None:
        """Deletes last `count` characters and records removed text on undo stack."""
        if count <= 0:
            print("Delete count must be positive.")
            return
        if not self.content:
            print("Document is already empty. Nothing to delete.")
            return

        # Cap count to length of content
        actual_count = min(count, len(self.content))
        removed_text = self.content[-actual_count:]
        self.content = self.content[:-actual_count]

        self.undo_stack.append(("DELETE", removed_text))
        self.redo_stack.clear()
        print(f"-> Deleted last {actual_count} character(s): '{removed_text}'")

    def undo(self) -> bool:
        """Reverts the most recent action using the undo stack."""
        if not self.undo_stack:
            print("Nothing to undo! (Undo stack is empty)")
            return False

        action_type, payload = self.undo_stack.pop()
        if action_type == "INSERT":
            # Undoing an insert means trimming off that text
            self.content = self.content[:-len(payload)]
            self.redo_stack.append(("INSERT", payload))
            print(f"-> Undid INSERT: Removed '{payload}'")
        elif action_type == "DELETE":
            # Undoing a delete means restoring that text
            self.content += payload
            self.redo_stack.append(("DELETE", payload))
            print(f"-> Undid DELETE: Restored '{payload}'")
        return True

    def redo(self) -> bool:
        """Re-applies the most recently undone action using the redo stack."""
        if not self.redo_stack:
            print("Nothing to redo! (Redo stack is empty)")
            return False

        action_type, payload = self.redo_stack.pop()
        if action_type == "INSERT":
            # Redoing an insert re-appends the text
            self.content += payload
            self.undo_stack.append(("INSERT", payload))
            print(f"-> Redid INSERT: Re-added '{payload}'")
        elif action_type == "DELETE":
            # Redoing a delete re-removes the text
            self.content = self.content[:-len(payload)]
            self.undo_stack.append(("DELETE", payload))
            print(f"-> Redid DELETE: Removed '{payload}'")
        return True

    def display(self) -> None:
        """Prints current document content and stack statuses."""
        print("-" * 50)
        print(f"Current Document: \"{self.content}\"")
        print(f"Undo Stack Depth: {len(self.undo_stack)}")
        print(f"Redo Stack Depth: {len(self.redo_stack)}")
        print("-" * 50)


def interactive_cli():
    editor = TextEditor()
    print("=" * 50)
    print("   UNDO-REDO TEXT EDITOR SIMULATION (Python)    ")
    print("=" * 50)
    print("Commands:")
    print("  1 <text>  : Insert text (e.g. 1 Hello )")
    print("  2 <count> : Delete last N characters (e.g. 2 3)")
    print("  3         : Undo")
    print("  4         : Redo")
    print("  5         : Display current text")
    print("  6 / exit  : Exit")
    print("=" * 50)

    while True:
        try:
            cmd_line = input("\nEditor > ").strip()
            if not cmd_line:
                continue

            parts = cmd_line.split(maxsplit=1)
            choice = parts[0].lower()

            if choice in ("6", "exit", "quit"):
                print("Exiting Text Editor. Goodbye!")
                break
            elif choice == "1":
                if len(parts) < 2:
                    print("Usage: 1 <text to insert>")
                else:
                    editor.insert(parts[1])
                    editor.display()
            elif choice == "2":
                if len(parts) < 2:
                    print("Usage: 2 <number of characters>")
                else:
                    try:
                        count = int(parts[1])
                        editor.delete(count)
                        editor.display()
                    except ValueError:
                        print("Please enter a valid number of characters.")
            elif choice == "3":
                editor.undo()
                editor.display()
            elif choice == "4":
                editor.redo()
                editor.display()
            elif choice == "5":
                editor.display()
            else:
                print("Unknown command. Choices: 1 (Insert), 2 (Delete), 3 (Undo), 4 (Redo), 5 (Display), 6 (Exit)")
        except (KeyboardInterrupt, EOFError):
            print("\nExiting Text Editor.")
            break

if __name__ == "__main__":
    interactive_cli()
