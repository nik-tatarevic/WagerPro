CREATE TABLE wallets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    balance NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'RSD',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_balance CHECK (balance >= 0.00)
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    wallet_id BIGINT,
    user_id BIGINT NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id) ON DELETE SET NULL
);

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(30) NOT NULL,
    total_stake NUMERIC(10, 2) NOT NULL,
    total_odds NUMERIC(8, 2) NOT NULL,
    potential_payout NUMERIC(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ticket_items (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    match_id BIGINT NOT NULL,
    odd_id BIGINT NOT NULL,
    odd_value NUMERIC(5, 2) NOT NULL,
    market_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_item_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE
);

CREATE INDEX idx_wallets_user ON wallets(user_id);
CREATE INDEX idx_transactions_wallet ON transactions(wallet_id);
CREATE INDEX idx_transactions_user ON transactions(user_id);
CREATE INDEX idx_tickets_user ON tickets(user_id);
CREATE INDEX idx_ticket_items_ticket ON ticket_items(ticket_id);