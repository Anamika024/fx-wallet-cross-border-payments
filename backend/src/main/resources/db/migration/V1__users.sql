create table users (
  id uuid primary key,
  email varchar(255) not null unique,
  password_hash varchar(255) not null,
  full_name varchar(100) not null,
  role varchar(20) not null,
  is_verified boolean not null,
  otp_code varchar(10),
  refresh_token varchar(255),
  totp_secret varchar(255),
  created_at timestamp not null
);
