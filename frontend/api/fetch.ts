import { isImageUri } from "@/utils/imageUtils"
import * as FileSystem from "expo-file-system"

interface GetProps {
  url: string
  params?: any
  token?: string
}

export const get = async ({ url, params }: GetProps) => {
  if (params) {
    url += "?" + new URLSearchParams(params).toString()
  }

  const response = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
    },
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

  const response = await fetch(url, {
    method: "POST",
    headers,
    body: requestBody,
  })

  const data = await response.json()

  if (response.ok) {
    return data
  }

  throw data
}

export const postFiles = async ({ url, body }: PostProps) => {
  for (const key of Object.keys(body)) {
    if (isImageUri(body[key])) {
      let filename = body[key].split("/").pop()
      let match = /\.(\w+)$/.exec(filename)
      let type = match ? `image/${match[1]}` : `image`

      return await FileSystem.uploadAsync(url, body[key], {
        uploadType: FileSystem.FileSystemUploadType.MULTIPART,
        fieldName: "files",
        mimeType: type,
      })
    }
  }
}
