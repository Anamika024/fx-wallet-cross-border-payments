create table transfers (
  id uuid primary key,
  sender_wallet_id uuid not null references wallets(id),
  receiver_wallet_id uuid references wallets(id),
  receiver_email varchar(255),
  external_destination varchar(255),
  source_currency varchar(3) not null,
  target_currency varchar(3) not null,
  source_amount decimal(18,4) not null,
  target_amount decimal(18,4) not null,
  exchange_rate decimal(18,6) not null,
  fee decimal(18,4) not null,
  state varchar(20) not null,
  fraud_flagged boolean not null,
  created_at timestamp not null
);

create table fraud_alerts (
  id uuid primary key,
  transfer_id uuid not null references transfers(id),
  user_id uuid not null references users(id),
  rule_triggered varchar(100) not null,
  reviewed_by uuid,
  resolution varchar(20) not null,
  created_at timestamp not null
);
