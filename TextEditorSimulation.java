import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class TextEditorSimulation {

    // Helper Action class representing an operation
    static class Action {
        enum Type { INSERT, DELETE }

        Type type;
        String text; // inserted or deleted text

        Action(Type type, String text) {
            this.type = type;
            this.text = text;
        }
    }

    private final StringBuilder content = new StringBuilder();
    private final Deque<Action> undoStack = new ArrayDeque<>();
    private final Deque<Action> redoStack = new ArrayDeque<>();

    public void insert(String text) {
        if (text == null || text.isEmpty()) return;
        content.append(text);
        undoStack.push(new Action(Action.Type.INSERT, text));
        redoStack.clear();
        System.out.println("-> Inserted: \"" + text + "\"");
    }

    public void delete(int count) {
        if (count <= 0) {
            System.out.println("Delete count must be positive.");
            return;
        }
        if (content.length() == 0) {
            System.out.println("Document is already empty. Nothing to delete.");
            return;
        }

        int actualCount = Math.min(count, content.length());
        int start = content.length() - actualCount;
        String deleted = content.substring(start);
        content.delete(start, content.length());

        undoStack.push(new Action(Action.Type.DELETE, deleted));
        redoStack.clear();
        System.out.println("-> Deleted last " + actualCount + " character(s): \"" + deleted + "\"");
    }

    public boolean undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo! (Undo stack is empty)");
            return false;
        }

        Action action = undoStack.pop();
        if (action.type == Action.Type.INSERT) {
            int start = content.length() - action.text.length();
            content.delete(start, content.length());
            redoStack.push(action);
            System.out.println("-> Undid INSERT: Removed \"" + action.text + "\"");
        } else if (action.type == Action.Type.DELETE) {
            content.append(action.text);
            redoStack.push(action);
            System.out.println("-> Undid DELETE: Restored \"" + action.text + "\"");
        }
        return true;
    }

    public boolean redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo! (Redo stack is empty)");
            return false;
        }

        Action action = redoStack.pop();
        if (action.type == Action.Type.INSERT) {
            content.append(action.text);
            undoStack.push(action);
            System.out.println("-> Redid INSERT: Re-added \"" + action.text + "\"");
        } else if (action.type == Action.Type.DELETE) {
            int start = content.length() - action.text.length();
            content.delete(start, content.length());
            undoStack.push(action);
            System.out.println("-> Redid DELETE: Removed \"" + action.text + "\"");
        }
        return true;
    }

    public void display() {
        System.out.println("--------------------------------------------------");
        System.out.println("Current Document: \"" + content.toString() + "\"");
        System.out.println("Undo Stack Depth: " + undoStack.size());
        System.out.println("Redo Stack Depth: " + redoStack.size());
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) {
        TextEditorSimulation editor = new TextEditorSimulation();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("   UNDO-REDO TEXT EDITOR SIMULATION (Java)        ");
        System.out.println("==================================================");
        System.out.println("Commands:");
        System.out.println("  1 <text>  : Insert text (e.g. 1 Hello )");
        System.out.println("  2 <count> : Delete last N characters (e.g. 2 3)");
        System.out.println("  3         : Undo");
        System.out.println("  4         : Redo");
        System.out.println("  5         : Display current text");
        System.out.println("  6 / exit  : Exit");
        System.out.println("==================================================");

        while (true) {
            System.out.print("\nEditor > ");
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0].toLowerCase();

            if (cmd.equals("6") || cmd.equals("exit") || cmd.equals("quit")) {
                System.out.println("Exiting Text Editor. Goodbye!");
                break;
            }

            switch (cmd) {
                case "1":
                    if (parts.length < 2) {
                        System.out.println("Usage: 1 <text to insert>");
                    } else {
                        editor.insert(parts[1]);
                        editor.display();
                    }
                    break;
                case "2":
                    if (parts.length < 2) {
                        System.out.println("Usage: 2 <number of characters>");
                    } else {
                        try {
                            int count = Integer.parseInt(parts[1]);
                            editor.delete(count);
                            editor.display();
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid integer for delete count.");
                        }
                    }
                    break;
                case "3":
                    editor.undo();
                    editor.display();
                    break;
                case "4":
                    editor.redo();
                    editor.display();
                    break;
                case "5":
                    editor.display();
                    break;
                default:
                    System.out.println("Unknown command. Choices: 1 (Insert), 2 (Delete), 3 (Undo), 4 (Redo), 5 (Display), 6 (Exit)");
                    break;
            }
        }

        scanner.close();
    }
}
