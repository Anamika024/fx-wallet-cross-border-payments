create table transactions (
  id uuid primary key,
  wallet_id uuid not null references wallets(id),
  type varchar(30) not null,
  amount decimal(18,4) not null,
  currency_code varchar(3) not null,
  status varchar(20) not null,
  reference_id uuid,
  description varchar(255) not null,
  created_at timestamp not null
);
