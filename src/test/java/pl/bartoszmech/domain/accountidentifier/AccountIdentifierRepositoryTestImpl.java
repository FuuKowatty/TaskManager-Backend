package pl.bartoszmech.domain.accountidentifier;

import java.util.concurrent.ConcurrentHashMap;

public class AccountIdentifierRepositoryTestImpl {
    ConcurrentHashMap<String, User> database = new ConcurrentHashMap<>();
}
