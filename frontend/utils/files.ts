export const getFileType = (uri: string) => {
  const filename = uri.split("/").pop() || ""
  const match = /\.(\w+)$/.exec(filename)
  const type = match ? `image/${match[1]}` : `image`
  return type
}

export const getLocalFileBlob = async (path: string) => {
  const fs = await import("fs")
  return base64ToBlob(localPathToBase64(path, fs))
}

const base64ToBlob = (base64: string) => {
  var binary = atob(base64.split(",")[1])
  var mimeString = base64.split(",")[0].split(":")[1].split(";")[0]
  var array = []
  for (var i = 0; i < binary.length; i++) {
    array.push(binary.charCodeAt(i))
  }
  return new Blob([new Uint8Array(array)], { type: mimeString })
}

const localPathToBase64 = (path: string, fs: typeof import("fs")) => {
  const data = fs.readFileSync(path, { encoding: "base64" })
  return `data:image/jpeg;base64,${data}`
}