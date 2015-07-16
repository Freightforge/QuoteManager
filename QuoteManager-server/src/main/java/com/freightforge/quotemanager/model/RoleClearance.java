package com.freightforge.quotemanager.model;

import java.io.Serializable;

/**
 * Represent the link between a role and authority.
 */
public class RoleClearance implements Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    /** The authority. */
    private Authority authority;

    /** The role. */
    private Role role;

    public RoleClearance() {
    }

    public RoleClearance(Authority authority, Role role) {
        this.authority = authority;
        this.role = role;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleClearance that = (RoleClearance) o;

        if (authority != null ? !authority.equals(that.authority) : that.authority != null) return false;
        return !(role != null ? !role.equals(that.role) : that.role != null);

    }

    @Override
    public int hashCode() {
        int result = authority != null ? authority.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RoleClearance{" +
            "authority=" + authority +
            ", role=" + role +
            '}';
    }
}
