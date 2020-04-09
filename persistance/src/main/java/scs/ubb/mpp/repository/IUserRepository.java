package scs.ubb.mpp.repository;


import scs.ubb.mpp.entity.User;

public interface IUserRepository extends Repository<User, Integer> {
    User loginUser(String name, String password);
}
