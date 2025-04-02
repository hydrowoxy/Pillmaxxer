import { Redirect, Slot, Tabs } from "expo-router"
import { useAuth } from "@/context/auth-context"
import { StyleSheet } from "react-native"
import { Ionicons } from "@expo/vector-icons"
import { useReminder } from "../../context/reminder-context" // Import useReminder
import ReminderModal from "../../components/ReminderModal" // Import ReminderModal
import { Reminder } from "@/types/Types" // Import Reminder type
import BottomBar from "@/components/ui/nav-bar"

export default function AppLayout() {
  const { user } = useAuth()
  const {
    isReminderModalVisible,
    setReminderModalVisible,
    reminderModalContent,
  } = useReminder() // Get context values

  if (!user) {
    return <Redirect href="../auth/login" />
  }

  return (
    <>
      <Slot />
      <BottomBar />
      {isReminderModalVisible && ( // Render modal conditionally
        <ReminderModal
          isVisible={isReminderModalVisible}
          setIsVisible={setReminderModalVisible}
          reminder={reminderModalContent as Reminder}
        />
      )}
    </>
  )
}

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
})
