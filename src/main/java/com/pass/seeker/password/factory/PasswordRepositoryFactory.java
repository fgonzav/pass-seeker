package com.pass.seeker.password.factory;

import com.pass.seeker.password.repository.*;

import java.util.Optional;

public class PasswordRepositoryFactory {

    public static PasswordRepository getInstance(){
        return Optional::empty;
    }
}
