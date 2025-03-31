import { base64ToBlob, isImageUri, localPathToBase64 } from "@/utils/imageUtils"
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

      try {
        if (FileSystem.FileSystemUploadType) {
          // production version
          return await FileSystem.uploadAsync(url, body[key], {
            uploadType: FileSystem.FileSystemUploadType.MULTIPART,
            fieldName: key,
            mimeType: type,
          })
        } else {
          // testing version
          const formData = new FormData()
          formData.append(key, base64ToBlob(localPathToBase64(body[key])))

          const response = await fetch(url, {
            method: "POST",
            body: formData,
          })

          const data = await response.json()

          if (response.ok) {
            return data
          }

          throw data
        }
      } catch (error) {
        console.error("Upload failed:", error)
      }
    }
  }
}
