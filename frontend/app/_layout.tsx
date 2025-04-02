import { AuthProvider } from "@/context/auth-context"
import { ReminderProvider } from "@/context/reminder-context"
import { Slot } from "expo-router"

export default function Root() {

  return (
    <AuthProvider>
      <ReminderProvider>
        <Slot />
      </ReminderProvider>
    </AuthProvider>
  )
}
