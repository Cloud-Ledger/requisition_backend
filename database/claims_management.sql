
BEGIN;

CREATE  TABLE IF NOT EXISTS users
(
	user_id SERIAL NOT NULL,
	username VARCHAR(100) NOT NULL,
	password_hash CHAR(60) NOT NULL,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT unique_user UNIQUE (username),
	CONSTRAINT unique_email UNIQUE (email),
	CONSTRAINT user_pkey PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS roles
(
	role_id SERIAL NOT NULL,
	role_name VARCHAR(100) NOT NULL,
	description TEXT,
	CONSTRAINT unique_role UNIQUE (role_name),
	CONSTRAINT roles_pkey PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS locations
(
	location_id SERIAL NOT NULL,
	location_name VARCHAR(100) NOT NULL,
	CONSTRAINT unique_location UNIQUE (location_name),
	CONSTRAINT loc_id_pkey PRIMARY KEY (location_id)
);

CREATE TABLE IF NOT EXISTS user_locations
(
	user_id INT REFERENCES users(user_id),
	location_id INT REFERENCES locations(location_id),
	CONSTRAINT locuser_pkey PRIMARY KEY (user_id,location_id)
);

CREATE TABLE IF NOT EXISTS user_roles
(
	user_id INT REFERENCES users(user_id),
	role_id INT REFERENCES roles(role_id),
	CONSTRAINT user_role_pkey PRIMARY KEY (user_id,role_id)
);


CREATE TABLE IF NOT EXISTS med_practice
(
    med_practice_id SERIAL NOT NULL,
    location_id INT REFERENCES locations(location_id),
    pract_name VARCHAR (70) NOT NULL,
    contact_tel VARCHAR(15),
    contact_person VARCHAR(50),
    contact_person_cell VARCHAR(15),
    CONSTRAINT med_practice_pkey PRIMARY KEY (med_practice_id),
    CONSTRAINT unique_med_practice UNIQUE(med_practice_id,location_id)
);

CREATE TABLE IF NOT EXISTS referring_doc
(
    ref_doc_id SERIAL NOT NULL,
    ref_doc_name VARCHAR(70) NOT NULL,
    contact_tel VARCHAR(15),
    email VARCHAR(50),
    med_practice_id INT REFERENCES med_practice(med_practice_id),
    CONSTRAINT referring_doc_pkey PRIMARY KEY (ref_doc_id)
);

CREATE TABLE IF NOT EXISTS examining_doc
(
    exam_doc_id SERIAL NOT NULL,
    exam_doc_name VARCHAR(255) NOT NULL,
    med_practice_id INT REFERENCES med_practice(med_practice_id),
    CONSTRAINT examining_doc_pkey PRIMARY KEY (exam_doc_id)
);

CREATE TABLE IF NOT EXISTS medical_aid
(
    med_aid_id SERIAL NOT NULL,
    med_aid_name VARCHAR(50) NOT NULL,
    contact_tel VARCHAR(15),
    CONSTRAINT medical_aid_pkey PRIMARY KEY (med_aid_id)
);

CREATE TABLE IF NOT EXISTS med_aid_member
(
    med_aid_member_id SERIAL NOT NULL,
    med_aid_id INT REFERENCES medical_aid(med_aid_id),
    membership_num VARCHAR(30) NOT NULL,
    is_principal_member INT NOT NULL DEFAULT 0,
    suffix_num INT,
    employer VARCHAR(70),
    employer_tel VARCHAR(15),
    CONSTRAINT med_aid_member_pkey PRIMARY KEY (med_aid_member_id),
    CONSTRAINT unique_member_number UNIQUE (med_aid_member_id, membership_num)
);

CREATE TABLE IF NOT EXISTS examination
(
    exam_id SERIAL NOT NULL,
    code VARCHAR(30) NOT NULL,
    description VARCHAR(255),
    tariff numeric(10, 2),
    CONSTRAINT examination_pkey PRIMARY KEY (exam_id)
);

CREATE TABLE IF NOT EXISTS patient
(
    patient_id SERIAL NOT NULL,
    patient_name VARCHAR(70) NOT NULL,
    date_of_birth date,
    member_suffix VARCHAR(5),
    addr1 VARCHAR(50),
    addr2 VARCHAR(50),
    addr3 VARCHAR(50),
    contact_tel VARCHAR(15),
    contact_cell VARCHAR(15),
    ref_doc_id INT REFERENCES referring_doc(ref_doc_id),
    med_aid_member_id INT REFERENCES med_aid_member(med_aid_member_id),
    CONSTRAINT patient_pkey PRIMARY KEY (patient_id)
);

-- Create ENUM object types for use in the claims table.
CREATE TYPE payment_options AS ENUM('cash', 'co-payment','fully-covered');
CREATE TYPE status AS ENUM('submitted', 'approved','partially-approved','rejected');

CREATE TABLE IF NOT EXISTS claim
(
    claim_id SERIAL NOT NULL,
    patient_id INT REFERENCES patient(patient_id),
    med_practice_id INT REFERENCES med_practice(med_practice_id),
    med_aid_id INT REFERENCES medical_aid(med_aid_id),
    date_of_submission date,
    payment_method payment_options,
    amnt_claimed numeric(10, 2),
    current_status status,
    comments text,
    examining_doctor_id INT REFERENCES examining_doc(exam_doc_id),
    CONSTRAINT claim_pkey PRIMARY KEY (claim_id)
);

CREATE TABLE IF NOT EXISTS claim_examination
(
    claim_id INT REFERENCES claim(claim_id),
    exam_id INT REFERENCES examination(exam_id),
    date_of_exam date,
    CONSTRAINT claim_examination_pkey PRIMARY KEY (claim_id, exam_id)
);

CREATE TABLE IF NOT EXISTS dignosis_report
(
    id SERIAL NOT NULL,
    med_practice_id INT REFERENCES med_practice(med_practice_id),
    patient_id INT REFERENCES patient(patient_id),
    ref_doc_id INT REFERENCES referring_doc(ref_doc_id),
    exam_id INT REFERENCES examination(exam_id),
    report_date date,
    diagnosis text,
    notes_on_exam_results text,
    CONSTRAINT dignosis_report_pkey PRIMARY KEY (id)
);

END;


