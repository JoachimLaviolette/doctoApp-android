package if26.android.doctoapp.Models;

import java.io.Serializable;

public enum PaymentOption implements Serializable {
    CASH { public String toString() { return "Cash"; } },
    CREDIT_CARD { public String toString() { return "Credit card"; } },
    CHEQUE { public String toString() { return "Cheque"; } };

    /**
     * Get the PaymentOption value of the given string representation
     * @param poString The string representation of the payment option
     * @return The payment option value associated
     */
    public static PaymentOption GetValueOf(String poString) {
        switch (poString) {
            case "Cash": return CASH;
            case "Credit card": return CREDIT_CARD;
            case "Cheque": return CHEQUE;
            default: return null;
        }
    }
}
