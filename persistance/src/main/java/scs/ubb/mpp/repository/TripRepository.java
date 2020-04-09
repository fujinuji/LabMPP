package scs.ubb.mpp.repository;

import scs.ubb.mpp.entity.Trip;
import scs.ubb.mpp.repository.utils.ConnectionUtils;
import scs.ubb.mpp.repository.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TripRepository implements ITripRepository {

    @Override
    public List<Trip> getAll() {
        Statement statement = ConnectionUtils.getInstance().getStatement();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM trip");
            List<Trip> trips = new ArrayList<>();

            while (resultSet.next()) {
                trips.add(new Trip(
                        resultSet.getInt("id"),
                        resultSet.getString("departure_town"),
                        resultSet.getString("arrival_town"),
                        LocalTime.parse(resultSet.getString("departure_time"), Utils.formatter),
                        LocalTime.parse(resultSet.getString("arrival_time"), Utils.formatter),
                        resultSet.getInt("available_seats")
                ));
            }
            return trips;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public Trip getById(Integer userId) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("SELECT * FROM trip WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return new Trip(
                    resultSet.getInt("id"),
                    resultSet.getString("departure_town"),
                    resultSet.getString("arrival_town"),
                    LocalTime.parse(resultSet.getString("departure_time"), Utils.formatter),
                    LocalTime.parse(resultSet.getString("arrival_time"), Utils.formatter),
                    resultSet.getInt("available_seats")
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Trip entity) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("INSERT INTO trip(departure_town, arrival_town, departure_time, arrival_time, available_seats) VALUES(?, ?, ?, ?, ?)");
            statement.setString(1, entity.getDepartureTown());
            statement.setString(2, entity.getArrivalTown());
            statement.setString(3, entity.getDepartureTime().format(Utils.formatter));
            statement.setString(4, entity.getArrivalTime().format(Utils.formatter));
            statement.setInt(5, entity.getAvailableSeats());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Trip entity) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("UPDATE trip SET departure_town = ?, arrival_town = ?, departure_time = ?, arrival_time = ?, available_seats = ? WHERE id = ?");
            statement.setString(1, entity.getDepartureTown());
            statement.setString(2, entity.getArrivalTown());
            statement.setString(3, entity.getDepartureTime().format(Utils.formatter));
            statement.setString(4, entity.getArrivalTime().format(Utils.formatter));
            statement.setInt(5, entity.getAvailableSeats());
            statement.setInt(6, integer);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(Integer integer) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("DELETE FROM trip WHERE id=?");
            statement.setInt(1, integer);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
