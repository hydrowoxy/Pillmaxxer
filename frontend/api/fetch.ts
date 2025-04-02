import { getFileType } from "@/utils/files"
import * as FileSystem from "expo-file-system"

interface GetProps {
  url: string
  params?: any
  token?: string
}

export const get = async ({ url, params }: GetProps) => {
  const headers: Record<string, string> = { "Content-Type": "application/json" }

  if (params) {
    url += "?" + new URLSearchParams(params).toString()
  }

  const response = await fetch(url, {
    headers,
    method: "GET",
  })

  const data = await response.json()

  if (response.ok) {
    return data
  }
  throw data
}

interface PostProps {
  url: string
  body?: any
  token?: string
  formData?: boolean
}

export const post = async ({ url, body }: PostProps) => {
  const headers: Record<string, string> = { "Content-Type": "application/json" }
  const requestBody = JSON.stringify(body)

  // console.log("Request Body:", requestBody)
  // console.log("Request URL:", url)

  const response = await fetch(url, {
    method: "POST",
    headers,
    body: requestBody,
  })
  const data = await response.json()
  // console.log("Response Data:", data)
  // console.log("Response Status:", response.status)
  if (response.ok) {
    return data
  }
  throw data
}

export const postFiles = async ({ url, body }: PostProps) => {
  for (const key of Object.keys(body)) {
    try {
      return await FileSystem.uploadAsync(url, body[key], {
        uploadType: FileSystem.FileSystemUploadType
          ? FileSystem.FileSystemUploadType.MULTIPART
          : undefined,
        fieldName: key,
        mimeType: getFileType(body[key]),
      })
    } catch (error) {
      console.error("Upload failed:", error)
    }
  }
}
