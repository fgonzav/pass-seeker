package com.pass.seeker.password.repository;

import java.util.Optional;

public interface PasswordRepository {

    Optional<String> getLastUsed();


}
