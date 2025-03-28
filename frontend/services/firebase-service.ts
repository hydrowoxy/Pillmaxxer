/**
 * Firebase authentication service module.
 * Provides methods for user authentication and session management.
 */

import { 
    signInWithEmailAndPassword,
    signOut,
    User,
    UserCredential,
  } from 'firebase/auth';
  import { auth } from './firebase-config';
  
  /**
   * User response structure from Firebase Authentication
   * @interface
   */
  export interface FirebaseUserResponse {
    user: User;
  }
  
  export const getCurrentUser = async () => {
    try {
      return new Promise((resolve) => {
        const unsubscribe = auth.onAuthStateChanged((user) => {
          unsubscribe();
          resolve(user ? { user } : null);
        });
      });
    } catch (error) {
      console.error("[error getting user] ==>", error);
      return null;
    }
  };
  
  export async function login(
    email: string, 
    password: string
  ): Promise<FirebaseUserResponse | undefined> {
    try {
      const userCredential: UserCredential = await signInWithEmailAndPassword(
        auth, 
        email, 
        password
      );
      return { user: userCredential.user };
    } catch (e) {
      console.error("[error logging in] ==>", e);
      throw e;
    }
  }

  export async function logout(): Promise<void> {
    try {
      await signOut(auth);
    } catch (e) {
      console.error("[error logging out] ==>", e);
      throw e;
    }
  }