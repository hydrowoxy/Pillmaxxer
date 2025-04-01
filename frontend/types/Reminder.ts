export type Reminder = {
    userId: string;
    date: string; // Date in YYYY-MM-DD format
    scheduledDose: ScheduledDose[],
}

export type ScheduledDose = {
    timeOfDay: string; // Time in HH:mm format
    medicationId: string; 
    medicationName: string; 
    dosage: string; // Dosage information
    instructions: string;
    prescriptionId: string; // ID of the prescription associated with this scheduled dose
}