package com.freightforge.quotemanager.model;

import java.io.Serializable;

/**
 * Represent the link between a company and authority.
 */
public class CompanyClearance implements Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    /** The authority. */
    private Authority authority;

    /** The company. */
    private Company company;

    public CompanyClearance() {
    }

    public CompanyClearance(Authority authority, Company company) {
        this.authority = authority;
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyClearance that = (CompanyClearance) o;

        if (authority != null ? !authority.equals(that.authority) : that.authority != null) return false;
        return !(company != null ? !company.equals(that.company) : that.company != null);

    }

    @Override
    public int hashCode() {
        int result = authority != null ? authority.hashCode() : 0;
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompanyClearance{" +
            "authority=" + authority +
            ", company=" + company +
            '}';
    }
}
