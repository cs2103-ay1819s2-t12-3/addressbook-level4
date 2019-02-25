package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_MEDICINES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicine.Company;
import seedu.address.model.medicine.Expiry;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.medicine.Name;
import seedu.address.model.medicine.Quantity;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing medicine in the inventory.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the medicine identified "
            + "by the index number used in the displayed medicine list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_QUANTITY + "QUANTITY] "
            + "[" + PREFIX_EXPIRY + "EXPIRY] "
            + "[" + PREFIX_COMPANY + "COMPANY] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_QUANTITY + "91234567 "
            + PREFIX_EXPIRY + "johndoe@example.com";

    public static final String MESSAGE_EDIT_MEDICINE_SUCCESS = "Edited Medicine: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_MEDICINE = "This medicine already exists in the inventory.";

    private final Index index;
    private final EditMedicineDescriptor editMedicineDescriptor;

    /**
     * @param index of the medicine in the filtered medicine list to edit
     * @param editMedicineDescriptor details to edit the medicine with
     */
    public EditCommand(Index index, EditMedicineDescriptor editMedicineDescriptor) {
        requireNonNull(index);
        requireNonNull(editMedicineDescriptor);

        this.index = index;
        this.editMedicineDescriptor = new EditMedicineDescriptor(editMedicineDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Medicine> lastShownList = model.getFilteredMedicineList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEDICINE_DISPLAYED_INDEX);
        }

        Medicine medicineToEdit = lastShownList.get(index.getZeroBased());
        Medicine editedMedicine = createEditedMedicine(medicineToEdit, editMedicineDescriptor);

        if (!medicineToEdit.isSameMedicine(editedMedicine) && model.hasMedicine(editedMedicine)) {
            throw new CommandException(MESSAGE_DUPLICATE_MEDICINE);
        }

        model.setMedicine(medicineToEdit, editedMedicine);
        model.updateFilteredMedicineList(PREDICATE_SHOW_ALL_MEDICINES);
        model.commitInventory();
        return new CommandResult(String.format(MESSAGE_EDIT_MEDICINE_SUCCESS, editedMedicine));
    }

    /**
     * Creates and returns a {@code Medicine} with the details of {@code medicineToEdit}
     * edited with {@code editMedicineDescriptor}.
     */
    private static Medicine createEditedMedicine(Medicine medicineToEdit,
            EditMedicineDescriptor editMedicineDescriptor) {
        assert medicineToEdit != null;

        Name updatedName = editMedicineDescriptor.getName().orElse(medicineToEdit.getName());
        Quantity updatedQuantity = editMedicineDescriptor.getQuantity().orElse(medicineToEdit.getQuantity());
        Expiry updatedExpiry = editMedicineDescriptor.getExpiryDate().orElse(medicineToEdit.getExpiry());
        Company updatedCompany = editMedicineDescriptor.getCompany().orElse(medicineToEdit.getCompany());
        Set<Tag> updatedTags = editMedicineDescriptor.getTags().orElse(medicineToEdit.getTags());

        return new Medicine(updatedName, updatedQuantity, updatedExpiry, updatedCompany, updatedTags, Collections.emptyMap());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editMedicineDescriptor.equals(e.editMedicineDescriptor);
    }

    /**
     * Stores the details to edit the medicine with. Each non-empty field value will replace the
     * corresponding field value of the medicine.
     */
    public static class EditMedicineDescriptor {
        private Name name;
        private Quantity quantity;
        private Expiry expiry;
        private Company company;
        private Set<Tag> tags;

        public EditMedicineDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditMedicineDescriptor(EditMedicineDescriptor toCopy) {
            setName(toCopy.name);
            setQuantity(toCopy.quantity);
            setExpiryDate(toCopy.expiry);
            setCompany(toCopy.company);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, quantity, expiry, company, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setQuantity(Quantity quantity) {
            this.quantity = quantity;
        }

        public Optional<Quantity> getQuantity() {
            return Optional.ofNullable(quantity);
        }

        public void setExpiryDate(Expiry expiry) {
            this.expiry = expiry;
        }

        public Optional<Expiry> getExpiryDate() {
            return Optional.ofNullable(expiry);
        }

        public void setCompany(Company company) {
            this.company = company;
        }

        public Optional<Company> getCompany() {
            return Optional.ofNullable(company);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditMedicineDescriptor)) {
                return false;
            }

            // state check
            EditMedicineDescriptor e = (EditMedicineDescriptor) other;

            return getName().equals(e.getName())
                    && getQuantity().equals(e.getQuantity())
                    && getExpiryDate().equals(e.getExpiryDate())
                    && getCompany().equals(e.getCompany())
                    && getTags().equals(e.getTags());
        }
    }
}
