package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.ShortcutSettings;
/**
 * {@code AddressBook} that keeps track of its own history.
 * @author {damithc}-reused
 *     adapted from <a href="https://github.com/se-edu/addressbook-level4">ab4</a>.
 */
public class TrackedAddressBook extends AddressBook {

    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;

    /**
     * Contructor
     * @param initialState ReadOnlyAddressBook that new Command produces
     */
    public TrackedAddressBook(ReadOnlyAddressBook initialState, ShortcutSettings shortcutSettings) {
        super(initialState, shortcutSettings);

        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState, shortcutSettings.getCopy()));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code AddressBook} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();

        ShortcutSettings shortcutSettingsCopy = getShortcutSettings().getCopy();
        addressBookStateList.add(new AddressBook(this, shortcutSettingsCopy));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state.
     */
    public void undo() {
        if (!hasHistory()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        ShortcutSettings previousShortcutSettings = addressBookStateList.get(currentStatePointer)
                .getShortcutSettings();
        this.getShortcutSettings().setShortcutSettings(previousShortcutSettings);
        //updating the real pointer. Problem : might be reassigning to wrong
        resetData(addressBookStateList.get(currentStatePointer), getShortcutSettings());
        //updating tracked copies
    }

    /**
     * Restores the address book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        ShortcutSettings previousShortcutSettings = addressBookStateList.get(currentStatePointer)
                        .getShortcutSettings();
        this.getShortcutSettings().setShortcutSettings(previousShortcutSettings);
        resetData(addressBookStateList.get(currentStatePointer), getShortcutSettings());
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean hasHistory() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TrackedAddressBook)) {
            return false;
        }

        TrackedAddressBook otherVersionedAddressBook = (TrackedAddressBook) other;

        // state check
        return super.equals(otherVersionedAddressBook)
                && addressBookStateList.equals(otherVersionedAddressBook.addressBookStateList)
                && currentStatePointer == otherVersionedAddressBook.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of addressBookState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of addressBookState list, unable to redo.");
        }
    }
}
