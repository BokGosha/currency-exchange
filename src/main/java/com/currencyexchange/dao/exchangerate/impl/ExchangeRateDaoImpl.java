package com.currencyexchange.dao.exchangerate.impl;

import com.currencyexchange.dao.exchangerate.ExchangeRateDao;
import com.currencyexchange.db.HikariDataSourceFactory;
import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.exception.DataAccessException;
import com.currencyexchange.model.Currency;
import com.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {

    private static final String FIND_ALL_QUERY = """
        SELECT
            er.id        AS id,
            bc.id        AS base_currency_id,
            bc.full_name AS base_currency_full_name,
            bc.code      AS base_currency_code,
            bc.sign      AS base_currency_sign,
            tc.id        AS target_currency_id,
            tc.full_name AS target_currency_full_name,
            tc.code      AS target_currency_code,
            tc.sign      AS target_currency_sign,
            er.rate      AS rate
        FROM exchange_rates AS er
        INNER JOIN currencies AS bc ON (er.base_currency_id=bc.id)
        INNER JOIN currencies AS tc ON (er.target_currency_id=tc.id)
        """;

    private static final String FIND_BY_CODE_PAIR_QUERY = """
        SELECT
            er.id        AS id,
            bc.id        AS base_currency_id,
            bc.full_name AS base_currency_full_name,
            bc.code      AS base_currency_code,
            bc.sign      AS base_currency_sign,
            tc.id        AS target_currency_id,
            tc.full_name AS target_currency_full_name,
            tc.code      AS target_currency_code,
            tc.sign      AS target_currency_sign,
            er.rate      AS rate
        FROM exchange_rates AS er
        INNER JOIN currencies AS bc ON (er.base_currency_id=bc.id)
        INNER JOIN currencies AS tc ON (er.target_currency_id=tc.id)
        WHERE bc.code = ? AND tc.code = ?
        """;

    private static final String CREATE_QUERY = """
        INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
        VALUES (?, ?, ?)
        RETURNING id
        """;

    private static final String UPDATE_QUERY = """
        UPDATE exchange_rates
        SET rate = ?
        WHERE id = ?
        """;

    @Override
    public List<ExchangeRate> findAll() {
        try (Connection connection = HikariDataSourceFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                ExchangeRate exchangeRate = mapResultSetToExchangeRate(resultSet);
                exchangeRates.add(exchangeRate);
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка базы данных при получении списка обменных курсов");
        }
    }

    @Override
    public Optional<ExchangeRate> findByCodePair(CurrencyCodePair currencyCodePair) {
        try (Connection connection = HikariDataSourceFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_PAIR_QUERY)) {

            preparedStatement.setString(1, currencyCodePair.baseCurrencyCode());
            preparedStatement.setString(2, currencyCodePair.targetCurrencyCode());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToExchangeRate(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка базы данных при получении обменного курса");
        }

        return Optional.empty();
    }

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) {
        try (Connection connection = HikariDataSourceFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY)) {

            preparedStatement.setInt(1, exchangeRate.baseCurrency().id());
            preparedStatement.setInt(2, exchangeRate.targetCurrency().id());
            preparedStatement.setBigDecimal(3, exchangeRate.rate());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return new ExchangeRate(
                        resultSet.getInt("id"),
                        exchangeRate.baseCurrency(),
                        exchangeRate.targetCurrency(),
                        exchangeRate.rate()
                );
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Ошибка базы данных при создании обменного курса");
        }
    }

    @Override
    public ExchangeRate update(ExchangeRate exchangeRate, BigDecimal newRate) {
        try (Connection connection = HikariDataSourceFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {

            preparedStatement.setBigDecimal(1, newRate);
            preparedStatement.setInt(2, exchangeRate.id());

            preparedStatement.executeUpdate();

            return new ExchangeRate(
                    exchangeRate.id(),
                    exchangeRate.baseCurrency(),
                    exchangeRate.targetCurrency(),
                    newRate
            );
        } catch (SQLException exception) {
            throw new DataAccessException("Ошибка базы данных при обновлении обменного курса");
        }
    }

    private ExchangeRate mapResultSetToExchangeRate(ResultSet resultSet) throws SQLException {
        Currency baseCurrency = new Currency(
                resultSet.getInt("base_currency_id"),
                resultSet.getString("base_currency_code"),
                resultSet.getString("base_currency_full_name"),
                resultSet.getString("base_currency_sign")
        );

        Currency targetCurrency = new Currency(
                resultSet.getInt("target_currency_id"),
                resultSet.getString("target_currency_code"),
                resultSet.getString("target_currency_full_name"),
                resultSet.getString("target_currency_sign")
        );

        return new ExchangeRate(
                resultSet.getInt("id"),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal("rate")
        );
    }
}
