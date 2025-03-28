import { Redirect, Slot } from "expo-router";
import { useAuth } from "@/context/auth-context";

/**
 * AppLayout serves as the root authentication wrapper for the main app routes (the tabs).
 * It ensures:
 * 1. Protected routes are only accessible to authenticated users
 * 3. Unauthenticated users are redirected to sign-in
 */
export default function AppLayout() {
  const { user } = useAuth();

  if (!user) {
    return <Redirect href="../auth/login" />;
  } 
  return <Slot />;
}