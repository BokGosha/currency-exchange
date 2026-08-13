CREATE TABLE IF NOT EXISTS Currencies (
    ID       INTEGER PRIMARY KEY,
    Code     VARCHAR(3)  NOT NULL UNIQUE,
    FullName VARCHAR(20) NOT NULL,
    Sign     VARCHAR(3)  NOT NULL
);

CREATE TABLE IF NOT EXISTS ExchangeRates (
    ID               INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId   INTEGER        NOT NULL REFERENCES Currencies (ID),
    TargetCurrencyId INTEGER        NOT NULL REFERENCES Currencies (ID),
    Rate             DECIMAL(12, 6) NOT NULL CHECK (Rate > 0),
    UNIQUE (BaseCurrencyId, TargetCurrencyId),
    CHECK (BaseCurrencyId <> TargetCurrencyId)
);