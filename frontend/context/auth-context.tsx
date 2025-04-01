/**
 * Authentication context module providing global auth state and methods.
 */

import React, { createContext, useContext, useEffect, useState } from "react";
import { User, onAuthStateChanged } from "firebase/auth";
import { login, logout } from "@/services/firebase-service";
import { registerPatient } from "@/api/general";
import { auth } from "@/services/firebase-config";
import { Patient } from "@/types/Patient"; // Import your Patient type

/**
 * Authentication context interface defining available methods and state
 * for managing user authentication throughout the application.
 * @interface
 */
interface AuthContextType {
  signIn: (email: string, password: string) => Promise<User | undefined>;
  signOut: () => void;
  user: User | null;
  /** Loading state for authentication operations */
  isLoading: boolean;
  userId: string | null; // Store userId directly
  setUserId: (userId: string | null) => void;
  registerPatient: (patientData: any) => Promise<Patient | null>;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

/**
 * Custom hook to access authentication context
 * @throws {Error} If used outside of AuthProvider
 */
export function useAuth(): AuthContextType {
  const value = useContext(AuthContext);

  if (process.env.NODE_ENV !== "production") {
    if (!value) {
      throw new Error("useAuth must be wrapped in a <AuthProvider />");
    }
  }

  return value;
}

/**
 * SessionProvider component that manages authentication state
 */
export function AuthProvider(props: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [userId, setUserId] = useState<string | null>(null); // Store userId directly

  /**
   * Sets up Firebase authentication state listener
   * Automatically updates user state on auth changes
   */
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setUser(user);
      if (user) {
        setUserId(user.uid); // Set userId when user logs in
      } else {
        setUserId(null); // Clear userId when user logs out
      }
      setIsLoading(false);
    });

    // Cleanup subscription on unmount
    return () => unsubscribe();
  }, []);

  /**
   * Handles user sign-in process
   */
  const handleSignIn = async (email: string, password: string) => {
    try {
      const response = await login(email, password);
      if (response && response.user) {
        return response.user;
      }
      return undefined;
    } catch (error) {
      console.error("[handleSignIn error] ==>", error);
      return undefined;
    }
  };

  /**
   * Handles user sign-out process
   * Clears local user state after successful logout
   */
  const handleSignOut = async () => {
    try {
      await logout();
      setUser(null);
      setUserId(null); // Clear userId on logout
    } catch (error) {
      console.error("[handleSignOut error] ==>", error);
    }
  };

  const handleRegisterPatient = async (patientData: any) => {
    try {
      const patient: Patient = await registerPatient(patientData);
      setUserId(patient.userId); // Set userId after registration
      return patient;
    } catch (error) {
      console.error("[handleRegisterPatient error] ==>", error);
      return null;
    }
  };

  return (
    <AuthContext.Provider
      value={{
        signIn: handleSignIn,
        signOut: handleSignOut,
        user,
        isLoading,
        userId,
        setUserId,
        registerPatient: handleRegisterPatient,
      }}
    >
      {props.children}
    </AuthContext.Provider>
  );
}