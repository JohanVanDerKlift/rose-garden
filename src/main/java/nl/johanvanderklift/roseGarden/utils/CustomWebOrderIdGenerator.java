package nl.johanvanderklift.roseGarden.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class CustomWebOrderIdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        Integer year = LocalDate.now().getYear();
        String prefix = "ORD";
        try {
            Connection connection = sharedSessionContractImplementor.getJdbcConnectionAccess().obtainConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select count(id) as Id from web_order");

            if (rs.next()) {
                int id = rs.getInt(1) + 10000;
                String generatedId = year + "-" + prefix + "-" + id;
                return generatedId;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
