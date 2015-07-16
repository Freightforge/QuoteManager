package com.freightforge.quotemanager.model;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

public class RolesOwner extends AbstractAuditingEntity implements Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    /** Set of roles. */
    protected Set<Role> roles = new HashSet<Role>();

    /**
     * Gets the roles.
     *
     * @return the roles
     */
    public Collection<Role> getRoles() {
        return roles;
    }

    /**
     * Sets the roles.
     *
     * @param roles
     *            the new roles
     */
    public void setRoles(final Collection<Role> roles) {
        this.roles = sortRoles(roles);
    }

    /**
     * Adds the roles.
     *
     * @param roles
     *            the roles
     */
    public void addRoles(final Role... roles) {
        if (this.roles == null) {
            this.roles = new HashSet<Role>();
        }

        this.roles.addAll(Arrays.asList(roles));
    }

    /**
     * Removes the roles.
     *
     * @param roles
     *            the roles
     */
    public void removeRoles(final Role... roles) {
        if (this.roles != null) {
            this.roles.removeAll(Arrays.asList(roles));
        }
    }
    /**
     * Sort roles.
     *
     * @param roles
     *            the roles
     * @return the sorted set
     */
    protected static SortedSet<Role> sortRoles(final Collection<? extends Role> roles) {
        Assert.notNull(roles, "Cannot pass a null Role collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getRoles() contract and SEC-717)
        final SortedSet<Role> sortedRoles = new TreeSet<Role>(new RoleComparator());

        for (final Role role : roles) {
            Assert.notNull(role, "Role list cannot contain any null elements");
            sortedRoles.add(role);
        }

        return sortedRoles;
    }

    /**
     * The Class RoleComparator.
     */
    public static class RoleComparator implements Comparator<Role>, Serializable {

        /** Serialization version. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(final Role g1, final Role g2) {
            // Neither should ever be null as each entry is checked before
            // adding it to the set.
            // If the role is null, it is a custom role and should
            // precede others.
            if (g2.getName() == null) {
                return -1;
            }

            if (g1.getName() == null) {
                return 1;
            }

            return g1.getName().compareTo(g2.getName());
        }
    }
}
