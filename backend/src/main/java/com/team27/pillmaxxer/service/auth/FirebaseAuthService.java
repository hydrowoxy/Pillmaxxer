package com.team27.pillmaxxer.service.auth;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import lombok.extern.java.Log;

@Service
@Log
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public String createFirebaseUser(String email, String password) throws FirebaseAuthException {
        log.info("Creating a user record in firebase.. " + email);
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisabled(false);

        log.info("Created user record in firebase.. " + request);
        try {
            UserRecord userRecord = firebaseAuth.createUser(request);
            log.info("Created user record in firebase.. " + userRecord.getUid());
            return userRecord.getUid();
        } catch (FirebaseAuthException e) {
            log.severe("Error creating user record in firebase.. " + e.getMessage());
            throw e;
        }
        // UserRecord userRecord = firebaseAuth.createUser(request);
        // log.info("Created user record in firebase.. " + userRecord.getUid());
        // return userRecord.getUid();
    }

    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(idToken);
    }

    public void deleteUser(String uid) throws FirebaseAuthException {
        firebaseAuth.deleteUser(uid);
    }

    public UserRecord getUser(String uid) throws FirebaseAuthException {
        return firebaseAuth.getUser(uid);
    }
}
