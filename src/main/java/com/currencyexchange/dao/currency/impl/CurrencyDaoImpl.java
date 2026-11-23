package com.currencyexchange.dao.currency.impl;

import com.currencyexchange.dao.currency.CurrencyDao;
import com.currencyexchange.db.HikariDataSourceFactory;
import com.currencyexchange.exception.DataAccessException;
import com.currencyexchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {

    private static final String FIND_ALL_QUERY = """
        SELECT id, code, full_name, sign
        FROM currencies
        """;

    private static final String CREATE_QUERY = """
        INSERT INTO currencies (code, full_name, sign)
        VALUES (?, ?, ?)
        RETURNING id, code, full_name, sign
        """;

    private static final String FIND_BY_CODE = """
        SELECT id, code, full_name, sign
        FROM currencies
        WHERE code = ?
        """;

    @Override
    public List<Currency> findAll() {
        try (Connection connection = HikariDataSourceFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                Currency currency = mapResultSetToCurrency(resultSet);
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException exception) {
            throw new DataAccessException("Ошибка базы данных при получении списка валют");
        }
    }

    @Override
    public Currency create(Currency currency) {
        try (Connection connection = HikariDataSourceFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY)) {

            preparedStatement.setString(1, currency.code());
            preparedStatement.setString(2, currency.name());
            preparedStatement.setString(3, currency.sign());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return mapResultSetToCurrency(resultSet);
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Ошибка базы данных при сохранении валюты");
        }
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        try (Connection connection = HikariDataSourceFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {

            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToCurrency(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Ошибка базы данных при поиске валюты с кодом");
        }

        return Optional.empty();
    }

    private Currency mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
