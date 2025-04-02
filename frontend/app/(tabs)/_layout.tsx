import { Redirect, Tabs } from "expo-router";
import { useAuth } from "@/context/auth-context";
import { StyleSheet } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useReminder } from "../../context/reminder-context"; // Import useReminder
import ReminderModal from '../../components/ReminderModal'; // Import ReminderModal
import { Reminder } from '@/types/Reminder'; // Import Reminder type

export default function AppLayout() {
  const { user } = useAuth();
  const { isReminderModalVisible, setReminderModalVisible, reminderModalContent } = useReminder(); // Get context values

  if (!user) {
    return <Redirect href="../auth/login" />;
  }

  return (
    <>
      <Tabs
        screenOptions={{
          tabBarShowLabel: false,
          tabBarStyle: styles.tabBarStyle,
          tabBarItemStyle: { justifyContent: "center" },
          animation: 'shift',
        }}
      >
        <Tabs.Screen
          name="index"
          options={{
            title: "Home",
            tabBarIcon: ({ color, size }) => (
              <Ionicons name="home" size={size} color={color} />
            ),
          }}
        />
        <Tabs.Screen
          name="medication-schedule"
          options={{
            title: "Schedule",
            tabBarIcon: ({ color, size }) => (
              <Ionicons name="grid" size={size} color={color} />
            ),
            headerShown: false,
          }}
        />
        <Tabs.Screen
          name="profile"
          options={{
            title: "Profile",
            tabBarIcon: ({ color, size }) => (
              <Ionicons name="person" size={size} color={color} />
            ),
            headerShown: false,
          }}
        />
        <Tabs.Screen
          name="scan"
          options={{
            title: "Scan Image",
            tabBarIcon: ({ color, size }) => (
              <Ionicons name="document-text" size={size} color={color} />
            ),
          }}
        />
        <Tabs.Screen
          name="prescription-form"
          options={{
            title: "Form Input",
            tabBarIcon: ({ color, size }) => (
              <Ionicons name="document-text" size={size} color={color} />
            ),
          }}
        />
      </Tabs>
      {isReminderModalVisible && ( // Render modal conditionally
        <ReminderModal
          isVisible={isReminderModalVisible}
          setIsVisible={setReminderModalVisible}
          reminder={reminderModalContent as Reminder}
        />
      )}
    </>
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