package com.favorapp.api.user;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
//CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {

	User findById(int id);

	User findByEmail(String email);

	boolean existsByEmail (String email);




}
