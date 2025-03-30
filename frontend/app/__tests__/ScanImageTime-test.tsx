import { render, userEvent, waitFor } from "@testing-library/react-native"
import * as ImagePicker from "expo-image-picker"
import ScanImage from "@/app/(tabs)/scan"
import { router } from "expo-router"

import fs from "fs"
import path from "path"

it("processes 95%+ of scanned images under 3 seconds", async () => {
  const folder = path.resolve(__dirname, "resources")
  const uris: string[] = []

  // locate test images and convert to local URIs
  fs.readdirSync(folder).forEach((file: string) => {
    const img = path.join(folder, file)
    const fileUri = "file:///" + path.resolve(img)
    uris.push(fileUri.replace(/\\/g, "/"))
  })

  // execute on each test image
  const limit = 3000
  let successes = 0

  for (const uri of uris) {
    const time = await executeScanImageTest(uri)
    if (time <= limit) successes += 1
    console.log(`Time taken for image ${uri}: ${time}ms`)
  }

  // calculate success rate
  const successRate = (successes / uris.length) * 100
  console.log(`Success rate: ${successRate}%`)

  expect(successRate).toBeGreaterThanOrEqual(95)
})

const executeScanImageTest = async (uri: string) => {
  // mock image upload content
  jest.spyOn(ImagePicker, "requestCameraPermissionsAsync").mockResolvedValue({
    canAskAgain: true,
    expires: "never",
    granted: true,
    status: ImagePicker.PermissionStatus.GRANTED,
  })

  const testImage: ImagePicker.ImagePickerAsset = {
    uri: uri,
    width: 350,
    height: 350,
    mimeType: "image/jpeg",
  }

  jest.spyOn(ImagePicker, "launchCameraAsync").mockResolvedValue({
    canceled: false,
    assets: [testImage],
  })

  // mock router reroute
  const routerSpy = jest.spyOn(router, "push").mockImplementation(jest.fn())

  // run UI interaction
  const user = userEvent.setup()
  const { getByTestId } = render(<ScanImage />)

  const takeButton = getByTestId("take-image-button")
  await user.press(takeButton)
  await waitFor(() => getByTestId("upload-image-button"))
  const uploadButton = getByTestId("upload-image-button")

  // this is the sequence we want to capture and calculate the time of
  const startTime: number = new Date().getTime()
  await user.press(uploadButton)
  await waitFor(() => expect(routerSpy).toHaveBeenCalled())
  const endTime: number = new Date().getTime()

  // calculate time difference
  const timeDifference: number = endTime - startTime
  return timeDifference
}
