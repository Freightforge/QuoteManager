package com.freightforge.quotemanager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * The company.
 */
@Document(collection = "COMPANY")
public class Company extends RolesOwner implements Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 0, max = 100)
    private String name;

    private Integer didrokId;

    private Company parent;

    private Boolean enabled = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDidrokId() {
        return didrokId;
    }

    public void setDidrokId(Integer didrokId) {
        this.didrokId = didrokId;
    }

    public Company getParent() {
        return parent;
    }

    public void setParent(Company parent) {
        this.parent = parent;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (id != null ? !id.equals(company.id) : company.id != null) return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;
        return !(didrokId != null ? !didrokId.equals(company.didrokId) : company.didrokId != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (didrokId != null ? didrokId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + id +
            ", didrokId=" + didrokId +
            ", name='" + name + '\'' +
            '}';
    }
}
