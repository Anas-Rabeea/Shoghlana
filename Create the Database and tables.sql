-- CREATE DATABASE shoghlana;
use shoghlana;
-- CREATE TABLE app_user (
--     id CHAR(36) PRIMARY KEY,
--     username VARCHAR(255) UNIQUE NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     email VARCHAR(255) UNIQUE,
--     phone VARCHAR(50) UNIQUE,
--     email_verified TINYINT(1) DEFAULT 0,
--     phone_verified TINYINT(1) DEFAULT 0,
--     app_auth_provider ENUM('EMAIL','GOOGLE','FACEBOOK','PHONE') NOT NULL,
--     provider_id VARCHAR(255),
--     enabled TINYINT(1) DEFAULT 1
-- );
-- CREATE TABLE roles (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(50) UNIQUE NOT NULL
-- );
-- INSERT INTO roles (name) VALUES
-- ('CUSTOMER'),
-- ('WORKER'),
-- ('ADMIN'),
-- ('DEVELOPER'),
-- ('TESTER');

-- CREATE TABLE user_roles (
-- 	app_user_id CHAR(36) NOT NULL,
--     role_id BIGINT,
--     
--     PRIMARY KEY (app_user_id, role_id),
--     
--     FOREIGN KEY (app_user_id) REFERENCES app_user(id),
--     FOREIGN KEY (role_id) REFERENCES roles(id)
-- );

-- -- every user is treated as customer = app_user + user_profile
-- CREATE TABLE user_profile (
-- 	id CHAR(36) NOT NULL,
--     full_name VARCHAR(255),
--     city VARCHAR(100),
--     profile_image_path VARCHAR(500),

--     FOREIGN KEY (id) REFERENCES app_user(id)
-- );
-- --  Only Created if user choose WORKER then user will has (app_user + user_profile + worker)
-- CREATE TABLE workers (
--     id CHAR(36) PRIMARY KEY,
--     profession VARCHAR(255),
--     description TEXT,
--     rating DECIMAL(2,1),
--     completed_jobs INT DEFAULT 0,

--     FOREIGN KEY (id) REFERENCES user_profile(id)
-- );

-- CREATE TABLE categories (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(255) NOT NULL -- سباك و كهربائي و نجار ...
-- );

CREATE TABLE specializaitons (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT,
    name VARCHAR(255) NOT NULL, -- سباك و كهربائي و نجار ...,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
CREATE TABLE categories_specializations (
	category_id BIGINT,
    specialization_id BIGINT ,
    PRIMARY KEY (category_id,specialization_id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
	FOREIGN KEY (specialization_id) REFERENCES specializaitons(id)
);
CREATE TABLE job (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id CHAR(36), -- user_profile id
    category_id BIGINT,
     title VARCHAR(255),
    description TEXT,
    budget_min DECIMAL(10,2),
    budget_max DECIMAL(10,2),
    city VARCHAR(100),
	-- date will be create by @CreatedBy  (Auditing) >> Can also be added as user from @CreatedBy comes from Authentication Token
    -- where the user id in the token is another thing from the customer_id
    status ENUM('OPEN','ASSIGNED','COMPLETED','CANCELLED') DEFAULT 'OPEN',
    FOREIGN KEY (customer_id) REFERENCES user_profile(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ; 
-- Worker will submit this to customer to offer doing this job
CREATE TABLE job_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    worker_id CHAR(36) NOT NULL,

    offered_price DECIMAL(10,2),
    status ENUM('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',

    FOREIGN KEY (job_id) REFERENCES job(id),
    FOREIGN KEY (worker_id) REFERENCES workers(id)
);
-- send notification to the user either be worker (for a job is offered) , or a customer (when worker send job_applications request)
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    title VARCHAR(255),
    message TEXT,
    is_read TINYINT(1) DEFAULT 0,

    FOREIGN KEY (user_id) REFERENCES user_profile(id)
);

CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    customer_id CHAR(36) NOT NULL,
    worker_id CHAR(36) NOT NULL,

    rating INT CHECK (rating BETWEEN 1 AND 5),
    details TEXT,

    FOREIGN KEY (job_id) REFERENCES job(id),
    FOREIGN KEY (customer_id) REFERENCES user_profile(id),
    FOREIGN KEY (worker_id) REFERENCES workers(id)
);

-- CREATE TABLE admin_logs (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     admin_id CHAR(36) NOT NULL,
--     action VARCHAR(255),

--     FOREIGN KEY (admin_id) REFERENCES app_user(id)
-- );

