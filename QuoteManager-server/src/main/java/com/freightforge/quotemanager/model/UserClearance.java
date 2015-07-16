package com.freightforge.quotemanager.model;

import java.io.Serializable;

/**
 * Represent the link between a user and authority.
 */
public class UserClearance implements Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    /** The authority. */
    private Authority authority;

    /** The user account. */
    private User user;

    public UserClearance() {
    }

    public UserClearance(Authority authority, User user) {
        this.authority = authority;
        this.user = user;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserClearance that = (UserClearance) o;

        if (authority != null ? !authority.equals(that.authority) : that.authority != null) return false;
        return !(user != null ? !user.equals(that.user) : that.user != null);

    }

    @Override
    public int hashCode() {
        int result = authority != null ? authority.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserClearance{" +
            "authority=" + authority +
            ", user=" + user +
            '}';
    }
}
