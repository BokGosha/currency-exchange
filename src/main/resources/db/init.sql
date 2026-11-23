CREATE TABLE IF NOT EXISTS currencies (
    id        integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code      varchar(3)  NOT NULL UNIQUE,
    full_name varchar(40) NOT NULL,
    sign      varchar(3)  NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange_rates (
    id                 integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    base_currency_id   integer        NOT NULL REFERENCES currencies (id),
    target_currency_id integer        NOT NULL REFERENCES currencies (id),
    rate               decimal(12, 6) NOT NULL CHECK (rate > 0),
    UNIQUE (base_currency_id, target_currency_id),
    CHECK (base_currency_id <> target_currency_id)
);