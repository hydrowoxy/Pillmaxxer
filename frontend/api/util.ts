import { convertUriToBlob, isImageUri } from "@/utils/imageUtils"

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

export const post = async ({ url, body, formData }: PostProps) => {
  const headers: Record<string, string> = formData
    ? {}
    : { "Content-Type": "application/json" }

  let requestBody: any

  if (formData) {
    const form = new FormData()
    if (body) {
      for (const key of Object.keys(body)) {
        if (isImageUri(body[key])) {
          const fileBlob = await convertUriToBlob(body[key])
          form.append(key, fileBlob, key + ".jpg")
        } else {
          form.append(key, body[key])
        }
      }
    }
    requestBody = form
  } else {
    requestBody = JSON.stringify(body)
  }

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
