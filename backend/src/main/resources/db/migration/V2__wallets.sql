create table wallets (
  id uuid primary key,
  user_id uuid not null references users(id),
  currency_code varchar(3) not null,
  balance decimal(18,4) not null,
  is_default boolean not null,
  status varchar(20) not null,
  created_at timestamp not null,
  unique(user_id, currency_code)
);
