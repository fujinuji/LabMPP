package scs.ubb.mpp.repository;

import scs.ubb.mpp.entity.Reservation;
import scs.ubb.mpp.repository.utils.ConnectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository implements IReservationRepository {
    @Override
    public List<Reservation> getAll() {
        Statement statement = ConnectionUtils.getInstance().getStatement();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM reservation");
            List<Reservation> reservations = new ArrayList<>();

            while (resultSet.next()) {
                reservations.add(new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getString("user_name"),
                        resultSet.getInt("trip_id"),
                        resultSet.getInt("reserved_seats")
                ));
            }
            return reservations;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public Reservation getById(Integer reservationId) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("SELECT * FROM reservation WHERE id = ?");
            statement.setInt(1, reservationId);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return new Reservation(
                    resultSet.getInt("id"),
                    resultSet.getString("user_name"),
                    resultSet.getInt("trip_id"),
                    resultSet.getInt("reserved_seats")
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Reservation entity) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("INSERT INTO reservation(user_name, trip_id, reserved_seats) VALUES(?, ?, ?)");
            statement.setString(1, entity.getUserName());
            statement.setInt(2, entity.getTripId());
            statement.setInt(3, entity.getNumberOfSeats());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Reservation entity) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("UPDATE reservation SET user_name = ?, trip_id = ?, reserved_seats = ? WHERE id = ?");
            statement.setString(1, entity.getUserName());
            statement.setInt(2, entity.getTripId());
            statement.setInt(3, entity.getNumberOfSeats());
            statement.setInt(4, integer);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(Integer integer) {
        try {
            PreparedStatement statement = ConnectionUtils.getInstance().getConnection().prepareStatement("DELETE FROM reservation WHERE id=?");
            statement.setInt(1, integer);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
