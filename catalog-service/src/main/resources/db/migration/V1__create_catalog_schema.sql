CREATE TABLE sports (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE leagues (
    id BIGSERIAL PRIMARY KEY,
    sport_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_league_sport FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE,
    CONSTRAINT uk_league_name_country UNIQUE (name, country)
);

CREATE TABLE matches (
    id BIGSERIAL PRIMARY KEY,
    league_id BIGINT NOT NULL,
    home_team VARCHAR(100) NOT NULL,
    away_team VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'UPCOMING',
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_match_league FOREIGN KEY (league_id) REFERENCES leagues(id) ON DELETE CASCADE,
    CONSTRAINT chk_match_teams CHECK (home_team <> away_team)
);

CREATE TABLE market_definitions (
    id BIGSERIAL PRIMARY KEY,
    sport_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_market_definition_sport FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE,
    CONSTRAINT uk_sport_market_type UNIQUE (sport_id, type)
);

CREATE TABLE odds (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT NOT NULL,
    market_definition_id BIGINT NOT NULL,
    odd_value NUMERIC(8,3) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_odd_match FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE,
    CONSTRAINT fk_odd_market_definition FOREIGN KEY (market_definition_id) REFERENCES market_definitions(id) ON DELETE CASCADE,
    CONSTRAINT uk_match_market UNIQUE (match_id, market_definition_id),
    CONSTRAINT chk_odd_value CHECK (odd_value >= 1.01)
);

CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL DEFAULT 'MatchStatusChangedEvent',
    payload TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    processed BOOLEAN DEFAULT FALSE
);


CREATE INDEX idx_outbox_unprocessed ON outbox_events (processed, created_at) WHERE processed = FALSE;

CREATE INDEX idx_matches_status_start_time ON matches(status, start_time);
CREATE INDEX idx_matches_league ON matches(league_id);
CREATE INDEX idx_odds_match_active ON odds(match_id, is_active);
CREATE INDEX idx_market_definition_sport ON market_definitions(sport_id);