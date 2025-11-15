package it.unibo.deathnote.api.impl;

import it.unibo.deathnote.api.DeathNote;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements the DeathNote interface.
 */
public final class DeathNoteImplementation implements DeathNote {

    private final Map<String, Death> deaths;
    private String lastWrittenName;

    /**
     * A new Death Note, with no name written on it yet.
     */
    public DeathNoteImplementation() {
        deaths = new LinkedHashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRule(int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()) {
           throw new IllegalArgumentException("ruleNumber out of range: " + ruleNumber);
        }
        return RULES.get(ruleNumber - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(final String name){
        Objects.requireNonNull(name);
        this.lastWrittenName = name;
        deaths.put(name, new Death());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDeathCause(final String cause) {
        return updateDeath(
            cause,
            new DeathTransformer() {
                @Override
                public Death call(final Death input) {
                    return input.writeCause(cause);
                }
            }
        );
    }

    /**
     * {@inheritDoc}
     * nearly to writeCause but with details
     */
    @Override
    public boolean writeDetails(final String details) {
        return updateDeath(
            details,
            new DeathTransformer() {
                @Override
                public Death call(final Death input) {
                    return input.writeDetails(details);
                }
            }
        );
    }

    @Override
    public String getDeathCause(final String name) {
        return getDeath(name).cause;
    }

    @Override
    public String getDeathDetails(final String name) {
        return getDeath(name).details;
    }

    @Override
    public boolean isNameWritten(final String name) {
        return deaths.containsKey(name);
    }

    private Death getDeath(final String name) {
        final var death = deaths.get(name);
        if (death == null) {
            throw new IllegalArgumentException(name + " has never been written in this notebook");
        }
        return death;
    }

    private boolean updateDeath(final String update, final DeathTransformer operation) {
        if (lastWrittenName == null || update == null) {
            throw new IllegalStateException("Status not completed fro writing" + lastWrittenName + update);
        }
        final var prec = deaths.get(lastWrittenName);
        final var updated = operation.call(prec);
        if (prec.equals(updated)) {
            return false;
        } else {
            deaths.put(lastWrittenName, updated);
        }
        return true;
    }

    @FunctionalInterface
    private interface DeathTransformer {
        Death call(Death input);
    }

    private static final class Death {
        private static final String DEFAULT_CAUSE = "heart attack";
        private static final byte VALID_CAUSE_TIMEOUT = 40;
        private static final short VALID_DETAILS_TIMEOUT = 6000 + VALID_CAUSE_TIMEOUT;
        private final String cause;
        private final String details;
        private final long timeOfDeath;

        // This object is immutable, so we can cache the hash
        private int hash;

        private Death(final String cause, final String details) {
            this.cause = cause;
            this.details = details;
            timeOfDeath = System.currentTimeMillis();
        }

        Death() {
            this(DEFAULT_CAUSE, "");
        }

        Death writeCause(final String deathCause) {
            return System.currentTimeMillis() < timeOfDeath + VALID_CAUSE_TIMEOUT
                ? new Death(deathCause, this.details)
                : this;
        }

        Death writeDetails(final String deathDetails) {
            return System.currentTimeMillis() < timeOfDeath + VALID_DETAILS_TIMEOUT
                ? new Death(this.cause, deathDetails)
                : this;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Death other)) {
                return false;
            }
            return Objects.equals(cause, other.cause)
                && Objects.equals(details, other.details)
                && timeOfDeath == other.timeOfDeath;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = Objects.hash(cause, details, timeOfDeath);
            }
            return hash;
        }
    }
}