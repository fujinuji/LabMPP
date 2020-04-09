package scs.ubb.mpp.repository;

import scs.ubb.mpp.entity.User;
import scs.ubb.mpp.repository.utils.ConnectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    @Override
    public List<User> getAll() {
        Statement statement = ConnectionUtils.getInstance().getStatement();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("address")
                ));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public User getById(Integer userId) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("SELECT * FROM user WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("address")
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(User entity) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("INSERT INTO user(name, password, email, address) VALUES(?, ?, ?, ?)");
            statement.setString(1, entity.getUserName());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getAddress());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, User entity) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("UPDATE user SET name = ?, password = ?, email = ?, address = ? WHERE id = ?");
            statement.setString(1, entity.getUserName());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getAddress());
            statement.setInt(5, integer);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(Integer integer) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("DELETE FROM user WHERE id=?");
            statement.setInt(1, integer);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public User loginUser(String name, String password) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("SELECT * FROM user WHERE name=? AND password=?");
            statement.setString(1, name);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            return new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("address")
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
