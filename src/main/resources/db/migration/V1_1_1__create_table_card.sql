CREATE TABLE card (
                         card_number VARCHAR(19) NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         balance DECIMAL(15,2) NOT NULL,
                         version BIGINT NOT NULL DEFAULT 0,
                         PRIMARY KEY (card_number)
);

CREATE INDEX idx_cartoes_numero ON card (card_number);
