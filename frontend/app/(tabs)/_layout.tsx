import { Redirect, Tabs } from "expo-router";
import { useAuth } from "@/context/auth-context";
import { StyleSheet } from "react-native";
import { Ionicons } from "@expo/vector-icons";

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
  return (
    <Tabs
      screenOptions={{
        tabBarShowLabel: false, // Hide tab labels
        tabBarStyle: styles.tabBarStyle, // Apply custom tab bar styles
        tabBarItemStyle: { justifyContent: "center" }, // Center tab icons vertically
        animation: 'shift', // Set animation type
      }}
    >
      <Tabs.Screen
        name="index" // Route for the home screen
        options={{
          title: "Home", // Title for accessibility
          tabBarIcon: ({ color, size }) => ( // Tab bar icon component
            <Ionicons name="home" size={size} color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="schedule" // Route for the features screen
        options={{
          title: "Schedule", // Title for accessibility
          tabBarIcon: ({ color, size }) => ( // Tab bar icon component
            <Ionicons name="grid" size={size} color={color} />
          ),
          headerShown: false, // Hide header
        }}
      />
      <Tabs.Screen
        name="profile" // Route for the profile screen
        options={{
          title: "Profile", // Title for accessibility
          tabBarIcon: ({ color, size }) => ( // Tab bar icon component
            <Ionicons name="person" size={size} color={color} />
          ),
          headerShown: false, // Hide header
        }}
      />
      <Tabs.Screen
        name="scan-image" // Route for the medical resume screen
        options={{
          title: "Scan Image", // Title for accessibility
          tabBarIcon: ({ color, size }) => ( // Tab bar icon component
            <Ionicons name="document-text" size={size} color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="form-input" // Route for the medical resume screen
        options={{
          title: "Form Input", // Title for accessibility
          tabBarIcon: ({ color, size }) => ( // Tab bar icon component
            <Ionicons name="document-text" size={size} color={color} />
          ),
        }}
      />
    </Tabs>
  );
};

const styles = StyleSheet.create({
  tabBarStyle: {
    position: "absolute",
    bottom: "4%",
    marginHorizontal: "10%", 
    width: "80%", 
    height: "6%", 
    backgroundColor: "#ffffff",
    borderRadius: 100, 
    flexDirection: "row",
    justifyContent: "space-between", 
    alignItems: "center", 
  },
});