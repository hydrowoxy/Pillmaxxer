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

export type MedicationDose = {
	medicationId: string;
	medicationName: string;
	dosage: string;
	instructions: string;
	prescriptionId: string;
}

export type Patient = {
	userId: string;
	firstName: string;
	lastName: string;
	email: string;
	phoneNumber: string;
	deviceToken: string[];
};

export type MedicationSchedule = {
	userId: string;
	dailySchedules: DailySchedule[];
}

export type DailySchedule = {
	date: string; // Date in YYYY-MM-DD format
	scheduledDoses: ScheduledDose[]; // Array of scheduled doses for this date
}

export type Prescription = {
	medicationName: string;
	dosage: string;
	startDate: string; // Date in YYYY-MM-DD format
	endDate: string; // Date in YYYY-MM-DD format
	instructions: string;
	quantity: string;
	frequency: string;
};