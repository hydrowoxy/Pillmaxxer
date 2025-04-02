export type Reminder = {
    userId: string;
    date: string; // Date in YYYY-MM-DD format
    scheduledDose: ScheduledDose,
}

export type ScheduledDose = {
    timeOfDay: string; // Time in HH:mm format
    medications: MedicationDose[]; // Array of medications for this scheduled dose
}

export type ReminderResponse = {
    reminder: Reminder, 
    pollInterval: number, // Polling interval in milliseconds
}

interface MedicationDose {
    medicationId: string;
    medicationName: string;
    dosage: string;
    instructions: string;
    prescriptionId: string;
  }