import {
  DailySchedule,
  DrugInteractionResponse,
  MedicationSchedule,
  Prescription,
  ReminderResponse,
} from "@/types/Types"
import { get } from "./fetch"

import axios from "axios"

const API_BASE_URL =
  process.env.EXPO_PUBLIC_API_URL ?? "http://localhost:8080/api" // for testing, necessary when env not loaded

export const getHealth = async () => {
  const data = await get({
    url: `${API_BASE_URL}/health`,
  })
  return data
}

// Configure axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 15000, // 15 second timeout
  headers: {
    "Content-Type": "application/json",
  },
})

/**
 * Registers a new patient with the backend including Expo push token
 */
export const registerPatient = async (patientData: {
  email: string
  password: string
  firstName: string
  lastName: string
  phoneNumber: string
  deviceToken: string[]
}) => {
  try {
    console.log(patientData)
    // Register patient with push token
    const response = await api.post("/patients/register", patientData)

    return response.data
  } catch (error) {
    throw error
  }
}

export const getPatientData = async (userId: string) => {
  console.log("Fetching patient data for userId:", userId)
  try {
    const response = await api.get(`/patients/${userId}`)
    return response.data
  } catch (error) {
    console.error("Error fetching patient data:", error)
    throw error
  }
}

export const getReminder = async (
  userId: string
): Promise<ReminderResponse> => {
  const data = await api.get(`/patients/${userId}/reminders`)
  const reminderData: ReminderResponse = data.data
  console.log("Fetched reminder data: ", reminderData)
  return reminderData
}

export const getMedicationSchedule = async (
  userId: string,
  date?: string // Optional date parameter
): Promise<DailySchedule> => {
  try {
    let url = `/patients/${userId}/schedules`
    if (date) {
      url += `?date=${date}`
    }
    const response = await api.get(url)
    const schedule = response.data as DailySchedule
    return schedule
  } catch (error: any) {
    console.error("Error fetching medication schedule:", error)
    throw error
  }
}

export const getMedicationScheduleForRange = async (
  userId: string,
  startDate: string,
  endDate: string
): Promise<MedicationSchedule> => {
  try {
    const url = `/patients/${userId}/schedules/${startDate}/${endDate}`
    const response = await api.get(url)
    const schedule = response.data as MedicationSchedule
    return schedule
  } catch (error: any) {
    console.error("Error fetching medication schedule for range:", error)
    throw error
  }
}

export const createPrescription = async (
  userId: string,
  prescriptionData: Prescription
): Promise<Prescription> => {
  try {
    const response = await api.post(
      `/patients/${userId}/prescriptions/create`,
      prescriptionData
    )
    const prescription = response.data as Prescription
    return prescription
  } catch (error: any) {
    console.error("Error creating prescription:", error)
    throw error
  }
}

export const createMedicationSchedule = async (
  userId: string
): Promise<MedicationSchedule> => {
  try {
    const response = await api.post(`/patients/${userId}/schedules/create`)
    const schedule = response.data as MedicationSchedule
    return schedule
  } catch (error: any) {
    console.error(
      `Error creating medication schedule for user ${userId}:`,
      error
    )
    throw error
  }
}

export const checkDrugInteractions = async (
  userId: string,
  schedule: MedicationSchedule
): Promise<DrugInteractionResponse> => {
  try {
    const response = await api.post(
      `/patients/${userId}/schedules/drug-interactions/check`,
      schedule
    )
    const drugInteractions = response.data as DrugInteractionResponse
    return drugInteractions
  } catch (error: any) {
    console.error("Error checking drug interactions:", error)
    throw error
  }
}
