package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.medicine.Company;
import seedu.address.model.medicine.Email;
import seedu.address.model.medicine.Name;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.medicine.Quantity;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Medicine objects.
 */
public class MedicineBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_QUANTITY = "332";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_COMPANY = "GlaxoSmithKline";

    private Name name;
    private Quantity quantity;
    private Email email;
    private Company company;
    private Set<Tag> tags;

    public MedicineBuilder() {
        name = new Name(DEFAULT_NAME);
        quantity = new Quantity(DEFAULT_QUANTITY);
        email = new Email(DEFAULT_EMAIL);
        company = new Company(DEFAULT_COMPANY);
        tags = new HashSet<>();
    }

    /**
     * Initializes the MedicineBuilder with the data of {@code medicineToCopy}.
     */
    public MedicineBuilder(Medicine medicineToCopy) {
        name = medicineToCopy.getName();
        quantity = medicineToCopy.getQuantity();
        email = medicineToCopy.getEmail();
        company = medicineToCopy.getCompany();
        tags = new HashSet<>(medicineToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Medicine} that we are building.
     */
    public MedicineBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Medicine} that we are building.
     */
    public MedicineBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Company} of the {@code Medicine} that we are building.
     */
    public MedicineBuilder withCompany(String company) {
        this.company = new Company(company);
        return this;
    }

    /**
     * Sets the {@code Quantity} of the {@code Medicine} that we are building.
     */
    public MedicineBuilder withQuantity(String quantity) {
        this.quantity = new Quantity(quantity);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Medicine} that we are building.
     */
    public MedicineBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Medicine build() {
        return new Medicine(name, quantity, email, company, tags);
    }

}