CREATE TABLE address (
id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 street1 TEXT DEFAULT NULL,
 street2 TEXT NOT NULL,
 city TEXT NOT NULL,
 zip INTEGER NOT NULL,
 country TEXT NOT NULL
);

CREATE TABLE doctor (
id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 lastname TEXT NOT NULL,
 firstname TEXT NOT NULL,
 speciality TEXT NOT NULL,
 email TEXT NOT NULL,
 pwd TEXT NOT NULL,
 pwd_salt TEXT NOT NULL,
 description TEXT,
 is_under_agreement INTEGER DEFAULT NULL,
 is_health_insurance_card INTEGER DEFAULT NULL,
 is_third_party_payment INTEGER DEFAULT NULL,
 address_id INTEGER NOT NULL,
 last_login TEXT,
 FOREIGN KEY(address_id) REFERENCES address(id)
);

CREATE TABLE availability (
doctor_id INTEGER NOT NULL,
 date TEXT NOT NULL,
 time TEXT NOT NULL,
 PRIMARY KEY (doctor_id,date,time),
 FOREIGN KEY(doctor_id) REFERENCES doctor(id)
);

CREATE TABLE education (
doctor_id INTEGER NOT NULL,
 year TEXT NOT NULL,
 degree TEXT NOT NULL,
 PRIMARY KEY (doctor_id,year,degree),
 FOREIGN KEY(doctor_id) REFERENCES doctor(id)
);

CREATE TABLE experience (
doctor_id INTEGER NOT NULL,
 year TEXT NOT NULL,
 description TEXT NOT NULL,
 PRIMARY KEY (doctor_id,year,description),
 FOREIGN KEY(doctor_id) REFERENCES doctor(id)
);

CREATE TABLE language (
doctor_id INTEGER NOT NULL,
 language TEXT NOT NULL,
 PRIMARY KEY (doctor_id,language),
 FOREIGN KEY(doctor_id) REFERENCES doctor(id)
);

CREATE TABLE patient (
id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 lastname TEXT NOT NULL,
 firstname TEXT NOT NULL,
 birthdate TEXT NOT NULL,
 email TEXT NOT NULL,
 pwd TEXT NOT NULL,
 pwd_salt TEXT NOT NULL,
 insurance_number TEXT NOT NULL UNIQUE,
 address_id INTEGER NOT NULL,
 last_login TEXT,
 FOREIGN KEY(address_id) REFERENCES address(id)
);

CREATE TABLE payment_option (
doctor_id INTEGER NOT NULL,
 payment_option TEXT NOT NULL,
 PRIMARY KEY (doctor_id,payment_option),
 FOREIGN KEY(doctor_id) REFERENCES doctor(id)
);

CREATE TABLE reason (
id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 doctor_id INTEGER NOT NULL,
 description TEXT NOT NULL,
 FOREIGN KEY(doctor_id) REFERENCES doctor(id)
);

CREATE TABLE booking (
 patient_id INTEGER NOT NULL,
 doctor_id INTEGER NOT NULL,
 reason_id INTEGER NOT NULL,
 date TEXT NOT NULL,
 time TEXT NOT NULL,
 booking_date TEXT NOT NULL,
 PRIMARY KEY (patient_id,doctor_id,date,time),
 FOREIGN KEY(patient_id) REFERENCES patient(id),
 FOREIGN KEY(doctor_id) REFERENCES doctor(id),
 FOREIGN KEY(reason_id) REFERENCES reason(id)
);