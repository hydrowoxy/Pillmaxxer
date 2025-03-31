import fs from "fs"

export const isImageUri = (uri: string) => {
  return /\.(png|jpg|jpeg|gif|bmp|webp)$/i.test(uri)
}

export const base64ToBlob = (base64: string) => {
  var binary = atob(base64.split(",")[1])
  var mimeString = base64.split(",")[0].split(":")[1].split(";")[0]
  var array = []
  for (var i = 0; i < binary.length; i++) {
    array.push(binary.charCodeAt(i))
  }
  return new Blob([new Uint8Array(array)], { type: mimeString })
}

export const localPathToBase64 = (path: string) => {
  const data = fs.readFileSync(path, { encoding: "base64" })
  return `data:image/jpeg;base64,${data}`
}
