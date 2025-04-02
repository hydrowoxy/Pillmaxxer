import { AuthProvider } from "@/context/auth-context"
import { ReminderProvider } from "@/context/reminder-context"
import { ScheduleProvider } from "@/context/schedule-context"
import { Slot } from "expo-router"

export default function Root() {
  // Set up the auth context and render our layout inside of it.
  return (
    <ScheduleProvider>
      <AuthProvider>
        <ReminderProvider>
  
            {/* 
              Slot renders child routes dynamically
              This includes both (app) and (auth) group routes
            */}
            <Slot />
        
        </ReminderProvider>
      </AuthProvider>
    </ScheduleProvider>
  )
}
