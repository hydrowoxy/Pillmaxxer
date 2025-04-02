import { render, userEvent, waitFor } from "@testing-library/react-native"
import * as ImagePicker from "expo-image-picker"
import ScanImage from "@/app/(tabs)/scan"
import { router } from "expo-router"

import fs from "fs"
import path from "path"

it("processes 95%+ of scanned images under 3 seconds", async () => {
  // locate local test images
  const folder = path.resolve(__dirname, "resources")
  const paths: string[] = []

  fs.readdirSync(folder).forEach((file: string) => {
    const img = path.join(folder, file)
    const relativePath = path.relative(process.cwd(), img).replace(/\\/g, "/")
    paths.push(relativePath)
  })

  // execute on each test image
  const limit = 3000
  let successes = 0

  for (const uri of paths) {
    const time = await executeScanImageTest(uri)
    if (time <= limit) successes += 1
    console.log(`Time taken for image ${uri}: ${time}ms`)
  }

  // calculate success rate
  const successRate = (successes / paths.length) * 100
  console.log(`Success rate: ${successRate}%`)

  expect(successRate).toBeGreaterThanOrEqual(95)
})

// helper
const executeScanImageTest = async (uri: string) => {
  // mock image upload content and router
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

  jest.spyOn(router, "setParams").mockImplementation()
  const routerSpy = jest.spyOn(router, "push").mockImplementation(jest.fn())

  // run UI interaction
  const user = userEvent.setup()
  const { getByTestId } = render(<ScanImage />)

  const takeButton = getByTestId("take-image-button")
  await user.press(takeButton)
  await waitFor(() => getByTestId("upload-image-button"))
  const uploadButton = getByTestId("upload-image-button")

  const startTime: number = new Date().getTime()
  await user.press(uploadButton)

  await waitFor(() => routerSpy.mock.calls.length > 0) // we know we're done when reroute happens
  const endTime: number = new Date().getTime()

  const timeDifference: number = endTime - startTime
  return timeDifference
}
