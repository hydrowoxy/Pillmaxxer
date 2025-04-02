import { post } from "./fetch"

const API_URL = process.env.EXPO_PUBLIC_API_URL ?? "http://localhost:8080"

export const postDrugInteractions = async (userId: string) => {
    const url = `${API_URL}/api/patients/${userId}/schedules/drug-interactions/check`

    const response = await post({
        url,
        body: null, // Backend fetches schedule by userId
    })

    return response
}
