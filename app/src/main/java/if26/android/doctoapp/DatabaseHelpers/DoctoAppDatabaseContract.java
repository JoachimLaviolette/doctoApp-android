package if26.android.doctoapp.DatabaseHelpers;

import android.provider.BaseColumns;

public final class DoctoAppDatabaseContract {
    // To prevent someone from accidentally instantiating the contract class
    private DoctoAppDatabaseContract() {}

    public static class Address implements BaseColumns {
        public static final String TABLE_NAME = "address";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_STREET1 = "street1";
        public static final String COLUMN_NAME_STREET2 = "street2";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_ZIP = "zip";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String[] TABLE_KEYS_INSERT = {
                COLUMN_NAME_STREET1,
                COLUMN_NAME_STREET2,
                COLUMN_NAME_CITY,
                COLUMN_NAME_ZIP,
                COLUMN_NAME_COUNTRY,
        };
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_STREET1,
                COLUMN_NAME_STREET2,
                COLUMN_NAME_CITY,
                COLUMN_NAME_ZIP,
                COLUMN_NAME_COUNTRY,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
                2,
                3,
                4,
                5,
        };
    }

    public static class Availability implements BaseColumns {
        public static final String TABLE_NAME = "availability";
        public static final String COLUMN_NAME_DOCTOR = "doctor_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_DOCTOR,
                COLUMN_NAME_DATE,
                COLUMN_NAME_TIME,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
                2,
        };
    }

    public static class Booking implements BaseColumns {
        public static final String TABLE_NAME = "booking";
        public static final String COLUMN_NAME_PATIENT = "patient_id";
        public static final String COLUMN_NAME_DOCTOR = "doctor_id";
        public static final String COLUMN_NAME_REASON = "reason_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_BOOKING_DATE = "booking_date";
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_PATIENT,
                COLUMN_NAME_DOCTOR,
                COLUMN_NAME_REASON,
                COLUMN_NAME_DATE,
                COLUMN_NAME_TIME,
                COLUMN_NAME_BOOKING_DATE,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
                2,
                3,
                4,
                5,
        };
    }

    public static class Patient implements BaseColumns {
        public static final String TABLE_NAME = "patient";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LASTNAME = "lastname";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_BIRTHDATE = "birthdate";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PWD = "pwd";
        public static final String COLUMN_NAME_PWD_SALT = "pwd_salt";
        public static final String COLUMN_NAME_INSURANCE_NUMBER = "insurance_number";
        public static final String COLUMN_NAME_ADDRESS = "address_id";
        public static final String COLUMN_NAME_LAST_LOGIN = "last_login";
        public static final String[] TABLE_KEYS_INSERT = {
                COLUMN_NAME_LASTNAME,
                COLUMN_NAME_FIRSTNAME,
                COLUMN_NAME_BIRTHDATE,
                COLUMN_NAME_EMAIL,
                COLUMN_NAME_PWD,
                COLUMN_NAME_PWD_SALT,
                COLUMN_NAME_INSURANCE_NUMBER,
                COLUMN_NAME_ADDRESS,
                COLUMN_NAME_LAST_LOGIN,
        };
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_LASTNAME,
                COLUMN_NAME_FIRSTNAME,
                COLUMN_NAME_BIRTHDATE,
                COLUMN_NAME_EMAIL,
                COLUMN_NAME_PWD,
                COLUMN_NAME_PWD_SALT,
                COLUMN_NAME_INSURANCE_NUMBER,
                COLUMN_NAME_ADDRESS,
                COLUMN_NAME_LAST_LOGIN,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
        };
    }

    public static class Doctor implements BaseColumns {
        public static final String TABLE_NAME = "doctor";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LASTNAME = "lastname";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_SPECIALITY = "speciality";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PWD = "pwd";
        public static final String COLUMN_NAME_PWD_SALT = "pwd_salt";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IS_UNDER_AGREEMENT = "is_under_agreement";
        public static final String COLUMN_NAME_IS_HEALTH_INSURANCE_CARD = "is_health_insurance_card";
        public static final String COLUMN_NAME_IS_THIRD_PARTY_PAYMENT = "is_third_party_payment";
        public static final String COLUMN_NAME_ADDRESS = "address_id";
        public static final String COLUMN_NAME_LAST_LOGIN = "last_login";
        public static final String[] TABLE_KEYS_INSERT = {
                COLUMN_NAME_LASTNAME,
                COLUMN_NAME_FIRSTNAME,
                COLUMN_NAME_SPECIALITY,
                COLUMN_NAME_EMAIL,
                COLUMN_NAME_PWD,
                COLUMN_NAME_PWD_SALT,
                COLUMN_NAME_DESCRIPTION,
                COLUMN_NAME_IS_UNDER_AGREEMENT,
                COLUMN_NAME_IS_HEALTH_INSURANCE_CARD,
                COLUMN_NAME_IS_THIRD_PARTY_PAYMENT,
                COLUMN_NAME_ADDRESS,
                COLUMN_NAME_LAST_LOGIN,
        };
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_LASTNAME,
                COLUMN_NAME_FIRSTNAME,
                COLUMN_NAME_SPECIALITY,
                COLUMN_NAME_EMAIL,
                COLUMN_NAME_PWD,
                COLUMN_NAME_PWD_SALT,
                COLUMN_NAME_DESCRIPTION,
                COLUMN_NAME_IS_UNDER_AGREEMENT,
                COLUMN_NAME_IS_HEALTH_INSURANCE_CARD,
                COLUMN_NAME_IS_THIRD_PARTY_PAYMENT,
                COLUMN_NAME_ADDRESS,
                COLUMN_NAME_LAST_LOGIN,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
                10,
                11,
                12,
        };
    }

    public static class Education implements BaseColumns {
        public static final String TABLE_NAME = "education";
        public static final String COLUMN_NAME_DOCTOR = "doctor_id";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_DEGREE = "degree";
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_DOCTOR,
                COLUMN_NAME_YEAR,
                COLUMN_NAME_DEGREE,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
                2,
        };
    }

    public static class Experience implements BaseColumns {
        public static final String TABLE_NAME = "experience";
        public static final String COLUMN_NAME_DOCTOR = "doctor_id";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_DOCTOR,
                COLUMN_NAME_YEAR,
                COLUMN_NAME_DESCRIPTION,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
                2,
        };
    }

    public static class Language implements BaseColumns {
        public static final String TABLE_NAME = "language";
        public static final String COLUMN_NAME_DOCTOR = "doctor_id";
        public static final String COLUMN_NAME_LANGUAGE = "language";
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_DOCTOR,
                COLUMN_NAME_LANGUAGE,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
        };
    }

    public static class PaymentOption implements BaseColumns {
        public static final String TABLE_NAME = "payment_option";
        public static final String COLUMN_NAME_DOCTOR = "doctor_id";
        public static final String COLUMN_NAME_PAYMENT_OPTION = "payment_option";
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_DOCTOR,
                COLUMN_NAME_PAYMENT_OPTION,
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
        };
    }

    public static class Reason implements BaseColumns {
        public static final String TABLE_NAME = "reason";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DOCTOR = "doctor_id";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String[] TABLE_KEYS_INSERT = {
                COLUMN_NAME_DOCTOR,
                COLUMN_NAME_DESCRIPTION
        };
        public static final String[] TABLE_KEYS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DOCTOR,
                COLUMN_NAME_DESCRIPTION
        };
        public static final int[] TABLE_COLUMNS_POSITIONS = {
                0,
                1,
                2,
        };
    }
}
