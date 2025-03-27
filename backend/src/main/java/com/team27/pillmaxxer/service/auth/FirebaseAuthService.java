package com.team27.pillmaxxer.service.auth;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

@Service
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public String createFirebaseUser(String email, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisabled(false);

        UserRecord userRecord = firebaseAuth.createUser(request);
        return userRecord.getUid();
    }

    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(idToken);
    }

    // Additional useful methods
    public void deleteUser(String uid) throws FirebaseAuthException {
        firebaseAuth.deleteUser(uid);
    }

    public UserRecord getUser(String uid) throws FirebaseAuthException {
        return firebaseAuth.getUser(uid);
    }
}
